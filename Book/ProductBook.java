package Book;

/**
 * 
 * @author xiaoyu yuan, Xingyue Duan, Yu Xi
 * 05/21/2015
 */


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import message.CancelMessage;
import message.FillMessage;
import message.InvalidInputException;
import priceFactory.Price;
import priceFactory.PriceFactory;
import publisher.CurrentMarketPublisher;
import publisher.LastSalePublisher;
import publisher.MarketDataDTO;
import publisher.MessagePublisher;
import publisher.NoSubscribeException;
import tradable.InvalidValueException;
import tradable.InvalidVolumeException;
import tradable.Order;
import tradable.Quote;
import tradable.Tradable;
import tradable.TradableDTO;

public class ProductBook {

	private String productSymbol;
	private ProductBookSide sellSide;
	private ProductBookSide buySide;
	private String lastMarketDataValue;
	private HashSet<String> userQuotes; 
	private HashMap<Price, ArrayList<Tradable>> oldEntries; 
	
	public ProductBook(String productSymbolIn) throws InvalidInputException
	{
		userQuotes = new HashSet<>();
		oldEntries = new HashMap< Price, ArrayList<Tradable>>();
		setProductSymbol(productSymbolIn);
		sellSide=new ProductBookSide(this,"SELL");
		buySide=new ProductBookSide(this,"BUY");
		
		
	}
	public String getProductSymbol()
	{
		return productSymbol;
	}
	public ProductBook(ProductBook pb) throws  InvalidInputException
	{
		if(pb==null) throw new InvalidInputException("ProductBook object cannot be null");
		else
			{
		userQuotes = new HashSet<>();
		oldEntries = new HashMap< Price, ArrayList<Tradable>>();
		setProductSymbol(pb.getProductSymbol());
		sellSide=pb.getBuySide();
		buySide=pb.getSellSide();
		}
	}
	
	private ProductBookSide getBuySide() throws InvalidInputException
	{
		return buySide;
	}
   private ProductBookSide getSellSide() throws InvalidInputException
	{
		return sellSide;
	}
	private void setProductSymbol(String productSymbolIn) throws InvalidInputException {

		if(productSymbolIn==null||productSymbolIn.isEmpty())
			throw new InvalidInputException("Product symbol can't be null or empty");
		else productSymbol=productSymbolIn;
		
	}
	
	public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName)
	{
		ArrayList<TradableDTO> dtoList=new ArrayList<>();
		ArrayList<TradableDTO> dtoSList=new ArrayList<>();
		ArrayList<TradableDTO> dtoBList=new ArrayList<>();
		dtoBList=buySide.getOrdersWithRemainingQty(userName);
		for(int i=0;i<dtoBList.size();i++)
		{
			dtoList.add(dtoBList.get(i));
		}
		dtoSList=sellSide.getOrdersWithRemainingQty(userName);
		for(int i=0;i<dtoSList.size();i++)
		{
			dtoList.add(dtoSList.get(i));
		}
		return dtoList;
	}
	
	public synchronized void checkTooLateToCancel(String orderId) throws InvalidInputException, NoSubscribeException, OrderNotFoundException
	{
		String cancelDetail = "Too late to Cancel";
		for (Price p : oldEntries.keySet()) {
            ArrayList<Tradable> tradablelist = oldEntries.get(p);
           for (int i = 0; i < tradablelist.size(); i++) {
               if (tradablelist.get(i).getId().equals(orderId)) {
                 CancelMessage cm = new CancelMessage(tradablelist.get(i).getUser(), tradablelist.get(i).getProduct(),tradablelist.get(i).getPrice(), tradablelist.get(i).getCancelledVolume(), cancelDetail, tradablelist.get(i).getSide(), tradablelist.get(i).getId());
                   MessagePublisher.getInstance().publishCancel(cm);
                  return;
                }
            }
        }
        throw new OrderNotFoundException("Cannot find order");
	}
	
	
	
	public synchronized String[ ][ ] getBookDepth()
	{
		String[][] bd = new String[2][];
		bd[0]=buySide.getBookDepth();
		bd[1]=sellSide.getBookDepth();
		return bd;
	}
	public synchronized MarketDataDTO getMarketData()
	{
		Price bestBuySidePrice=buySide.topOfBookPrice();
		Price bestSellSidePrice=sellSide.topOfBookPrice();
		if(bestBuySidePrice==null)
			bestBuySidePrice=PriceFactory.makeLimitPrice("0.00");
		if(bestSellSidePrice==null)
			bestSellSidePrice=PriceFactory.makeLimitPrice("0.00");
		int bestBuySideVolume=buySide.topOfBookVolume();
		int bestSellSideVolume=sellSide.topOfBookVolume();
		 MarketDataDTO mddto=new MarketDataDTO(productSymbol,bestBuySidePrice,bestBuySideVolume,bestSellSidePrice,bestSellSideVolume);
		return mddto;
	}
	public synchronized void openMarket() throws NoSubscribeException, InvalidInputException, InvalidVolumeException
	{
		Price tbp=buySide.topOfBookPrice();
		Price tsp=sellSide.topOfBookPrice();
		if(tbp==null||tsp==null) return;
		else if(tbp!=null&&tsp!=null)
		{
			//WHILE (the buyPrice is greater than or equal to the sell price OR the buyPrice is a MKT price OR the
					//sellPrice is MKT):
			ArrayList<Tradable> topOfBuySide=buySide.getEntriesAtPrice(tbp);
			while((tbp.getValue()>=tsp.getValue()||tbp.isMarket()||tsp.isMarket())&&topOfBuySide!=null)
			{
				 topOfBuySide=buySide.getEntriesAtPrice(tbp);
				HashMap<String, FillMessage> allFills=null;
				ArrayList<Tradable> toRemove=new ArrayList<>();
				String s=topOfBuySide.get(0).getProduct();
				for(int i=0;i<topOfBuySide.size();i++)
				{
					allFills=sellSide.tryTrade(topOfBuySide.get(i));
					if(topOfBuySide.get(i).getRemainingVolume()==0)
						toRemove.add(topOfBuySide.get(i));
						
				}
				for(int j=0;j<toRemove.size();j++)
				{
					buySide.removeTradable(toRemove.get(j));
				}
				updateCurrentMarket();
			
			
				Price lastSalePrice=determineLastSalePrice(allFills);
				int lastSaleVolume=determineLastSaleQuantity(allFills);	
				LastSalePublisher.getInstance().publishLastSale(s, lastSalePrice, lastSaleVolume);
				//topOfBuySide=buySide.getEntriesAtPrice(tbp);
				topOfBuySide=buySide.getEntriesAtPrice(tbp);
				Price buyPrice=buySide.topOfBookPrice();
				Price sellPrice=sellSide.topOfBookPrice();
				if(buyPrice==null||sellPrice==null)
					break;
				
				}
				
			}
		}
	
	public synchronized void closeMarket() throws InvalidInputException, NoSubscribeException, OrderNotFoundException, InvalidVolumeException
	{
		sellSide.cancelAll();
		buySide.cancelAll();
		updateCurrentMarket();
	}

	public synchronized void cancelOrder(String side, String orderId) throws InvalidInputException, NoSubscribeException, OrderNotFoundException, InvalidVolumeException
	{
		if(side.equals("BUY"))
			buySide.submitOrderCancel(orderId);
		else if(side.equals("SELL"))
			sellSide.submitOrderCancel(orderId);
		updateCurrentMarket();
		
	}
	public synchronized void cancelQuote(String userName) throws InvalidInputException, NoSubscribeException
	{
		
			buySide.submitQuoteCancel(userName);
			sellSide.submitQuoteCancel(userName);
		updateCurrentMarket();
	}
	public synchronized void addToBook(Quote q) throws InvalidValueException, DataValidationException, NoSubscribeException, InvalidInputException, InvalidVolumeException
	{
	
		if(q.getQuoteSide("SELL").getPrice().getValue()<=q.getQuoteSide("BUY").getPrice().getValue())
			throw new DataValidationException("This is an illegal quote because that the sell price is less than or equal to the buy price");
		if(q.getQuoteSide("SELL").getPrice().getValue()<=0||q.getQuoteSide("BUY").getPrice().getValue()<=0)	
			throw new DataValidationException("This is an illegal quote because that the sell price or the buy price is less than or equal to zero");
		if(q.getQuoteSide("SELL").getOriginalVolume()<=0||q.getQuoteSide("BUY").getOriginalVolume()<=0)
			throw new DataValidationException("This is an illegal quote because that the sell side or buy side original volume is less than or equal to zero");
		if (userQuotes.contains(q.getUserName()))
		{
			buySide.removeQuote(q.getUserName());
			sellSide.removeQuote(q.getUserName());
			updateCurrentMarket();
		}
		addToBook("BUY",q.getQuoteSide("BUY"));//????
		addToBook("SELL",q.getQuoteSide("SELL"));//????
		userQuotes.add(q.getUserName());
		updateCurrentMarket();
	}
	private synchronized void addToBook(String sideIn, Tradable trd) throws NoSubscribeException, InvalidInputException, InvalidVolumeException {
		
			if(ProductService.getInstance().getMarketState().equals("PREOPEN"))
			{
				if(sideIn.equals("BUY"))
					buySide.addToBook(trd);
				else if(sideIn.equals("SELL"))
					sellSide.addToBook(trd);
				return;
			}
			
			HashMap<String, FillMessage> allFills = null;
			if(sideIn.equals("BUY"))
				allFills=sellSide.tryTrade(trd);
			else if (sideIn.equals("SELL"))
				allFills=buySide.tryTrade(trd);
			if(!allFills.isEmpty()&&allFills!=null)
			{
				updateCurrentMarket();
				int tradedVolume=trd.getOriginalVolume()-trd.getRemainingVolume();
				Price  lastSalePrice=determineLastSalePrice(allFills);
				LastSalePublisher.getInstance().publishLastSale(productSymbol, lastSalePrice, tradedVolume);
			}
			if(trd.getRemainingVolume()>0)
			{
			
				if(trd.getPrice().isMarket())
				{
					 
    				 CancelMessage cancelMessage=new CancelMessage(trd.getUser(),trd.getProduct(),trd.getPrice(),
    						 trd.getRemainingVolume(),"CANCELLED",trd.getSide(),trd.getId());
    				 MessagePublisher.getInstance().publishCancel(cancelMessage);
				}
				else
				{
					if(sideIn.equals("BUY"))
						buySide.addToBook(trd);
					else if(sideIn.equals("SELL"))
						sellSide.addToBook(trd);
				}
			}
			
		
	}
	public synchronized void addToBook(Order o) throws NoSubscribeException, InvalidInputException, InvalidVolumeException
	{
		addToBook(o.getSide(),o);
		updateCurrentMarket();
	}
	private synchronized int determineLastSaleQuantity(HashMap<String, FillMessage> fills) {
		ArrayList<FillMessage> msgs = new ArrayList<>(fills.values());
		Collections.sort(msgs);
		return msgs.get(0).getVolume();
		
	}
	private synchronized Price determineLastSalePrice(HashMap<String, FillMessage> fills){
		Price p=null;
		ArrayList<FillMessage> msgs = new ArrayList<>(fills.values());
		Collections.sort(msgs);
		if(msgs.get(0).getPrice().isMarket())
			p= PriceFactory.makeMarketPrice();
		else if(!msgs.get(0).getPrice().isMarket())
		p=PriceFactory.makeLimitPrice(msgs.get(0).getPrice().getValue());
		return p;
		
	}
	public synchronized void updateCurrentMarket() throws NoSubscribeException {
		Price buyPrice = this.buySide.topOfBookPrice();
		if(buyPrice == null){
			buyPrice = PriceFactory.makeLimitPrice(0);
		}
		Price sellPrice = this.sellSide.topOfBookPrice();
		if(sellPrice == null){
			sellPrice = PriceFactory.makeLimitPrice(0);
		}
		String s= buyPrice.toString()+buySide.topOfBookVolume()+sellPrice.toString()+sellSide.topOfBookVolume();
		if(!s.equals(lastMarketDataValue)){
			MarketDataDTO mdto = new MarketDataDTO(productSymbol,buyPrice,buySide.topOfBookVolume(),sellPrice,sellSide.topOfBookVolume());
		 CurrentMarketPublisher.getInstance().publishCurrentMarket(mdto);
		 lastMarketDataValue=s;
		}
	}
	public void addOldEntry(Tradable t) throws InvalidVolumeException {
		if(!oldEntries.containsKey(t.getPrice()))
				{
			ArrayList<Tradable> tradableList=new ArrayList<>();
			oldEntries.put(t.getPrice(), tradableList);
			}
			int newCancelledVolume=t.getRemainingVolume();
			t.setRemainingVolume(0);
 			t.setCancelledVolume(newCancelledVolume);
 			
 			ArrayList<Tradable> tradableList=oldEntries.get(t.getPrice());
 			tradableList.add(t);
 			oldEntries.put(t.getPrice(), tradableList);
 			
				}
		
	}
	

