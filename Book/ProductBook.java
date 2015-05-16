package Book;

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
		sellSide=new ProductBookSide(this,"SELL");//?
		buySide=new ProductBookSide(this,"BUY");//?
		
		
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
		
		 boolean find=false;
    	 Iterator<Entry<Price, ArrayList<Tradable>>> it = oldEntries.entrySet().iterator();
    	 while (!find&&it.hasNext())
    	 {
    		 Entry<Price,ArrayList<Tradable>> entry=it.next();
    		 ArrayList<Tradable> tradableList=entry.getValue();
    		 int tradableListSize=tradableList.size();
    		 int count=0;
    		 while(!find&&count<tradableListSize)
    		 {
    			 if(tradableList.get(count).getId().equals(orderId)&&!tradableList.get(count).isQuote())
    			 {
    				 String cancelDetail="Too Late to Cancel";
    				 CancelMessage cancelMessage=new CancelMessage(tradableList.get(count).getUser(),tradableList.get(count).getProduct(),tradableList.get(count).getPrice(),
    						 tradableList.get(count).getCancelledVolume(),cancelDetail,tradableList.get(count).getSide(),tradableList.get(count).getId());
    				 MessagePublisher.getInstance().publishCancel(cancelMessage); 
    			 }
    			 count++;
    		 }
    	 }
    	 
    	 if(!find) throw new OrderNotFoundException("Order not found.");
    	 
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
		if(tbp!=null&&tsp!=null)
		{
			//WHILE (the buyPrice is greater than or equal to the sell price OR the buyPrice is a MKT price OR the
					//sellPrice is MKT):
			while(tbp.getValue()>=tsp.getValue()||tbp.isMarket()||tsp.isMarket())
			{
				ArrayList<Tradable> topOfBuySide=buySide.getEntriesAtPrice(tbp);
				HashMap<String, FillMessage> allFills=null;
				ArrayList<Tradable> toRemove=new ArrayList<>();
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
				LastSalePublisher.getInstance().publishLastSale(topOfBuySide.get(0).getProduct(), lastSalePrice, lastSaleVolume);
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
	private synchronized void addToBook(String side, Tradable trd) throws NoSubscribeException, InvalidInputException, InvalidVolumeException {
		
			if(ProductService.getInstance().getMarketState().equals("PREOPEN"))
			{
				if(side.equals("BUY"))
					buySide.addToBook(trd);
				else if(side.equals("SELL"))
					sellSide.addToBook(trd);
			}
			
			HashMap<String, FillMessage> allFills = null;
			if(side.equals("BUY"))
				allFills=sellSide.tryTrade(trd);
			else if (side.equals("SELL"))
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
    						 trd.getCancelledVolume(),"CANCELLED",trd.getSide(),trd.getId());
    				 MessagePublisher.getInstance().publishCancel(cancelMessage);
				}
				else
				{
					if(side.equals("BUY"))
						buySide.addToBook(trd);
					else if(side.equals("SELL"))
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
		ArrayList<FillMessage> msgs = new ArrayList<>(fills.values());
		Collections.sort(msgs);
		return msgs.get(0).getPrice();
		
	}
	public synchronized void updateCurrentMarket() throws NoSubscribeException {
		MarketDataDTO mdto;
		String s=null;
		if(buySide.topOfBookPrice()==null)
		{
			if(lastMarketDataValue==s)
			{
				Price closedP=PriceFactory.makeLimitPrice(0);
				 mdto=new MarketDataDTO(productSymbol,closedP,buySide.topOfBookVolume(),closedP,sellSide.topOfBookVolume());
					CurrentMarketPublisher.getInstance().publishCurrentMarket(mdto);
					lastMarketDataValue=s;
			}//?????
		}
		
		 else 
		 {
			 s=buySide.topOfBookPrice().toString()+buySide.topOfBookVolume()+sellSide.topOfBookPrice().toString()+sellSide.topOfBookVolume();
			 if(lastMarketDataValue.equals(s))
			 {
		
		 mdto=new MarketDataDTO(productSymbol,buySide.topOfBookPrice(),buySide.topOfBookVolume(),sellSide.topOfBookPrice(),sellSide.topOfBookVolume());
			CurrentMarketPublisher.getInstance().publishCurrentMarket(mdto);
			lastMarketDataValue=s;
			 }
			 	} 
			
	}
	public void addOldEntry(Tradable t) throws InvalidVolumeException {
		if(!oldEntries.containsKey(t.getPrice()))
				{
			ArrayList<Tradable> tradableList=new ArrayList<>();
			oldEntries.put(t.getPrice(), tradableList);
 			t.setCancelledVolume(t.getRemainingVolume());
 			t.setRemainingVolume(0);
 		tradableList=oldEntries.get(t.getPrice());
 			tradableList.add(t);
 			
				}
		
	}
	
}
