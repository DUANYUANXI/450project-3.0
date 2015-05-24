package Book;

/**
 * 
 * @author xiaoyu yuan, Xingyue Duan, Yu Xi
 * 05/21/2015
 */



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import message.CancelMessage;
import message.FillMessage;
import message.InvalidInputException;
import priceFactory.Price;
import priceFactory.PriceFactory;
import publisher.MessagePublisher;
import publisher.NoSubscribeException;
import tradable.InvalidVolumeException;
import tradable.Tradable;
import tradable.TradableDTO;

public class ProductBookSide {
	
	private String side;
	private HashMap<Price, ArrayList<Tradable>> bookEntries;
	private TradeProcessor tradeProcessor;
	private ProductBook productBookParent;
	
	
	public ProductBookSide(ProductBook object, String sideIn) throws InvalidInputException
	{
		bookEntries = new HashMap< Price, ArrayList<Tradable>>();
		setSide(sideIn);
		setProductBookSideParent(object);
		tradeProcessor=TradeProcessorPriceTimeImplFactory.MakeTradeProcessorPriceTimeImpl("time", this);
	}
	public ProductBookSide(ProductBookSide pdb) throws InvalidInputException
	{
		bookEntries = new HashMap< Price, ArrayList<Tradable>>();
		setSide(pdb.getSide());
		setProductBookSideParent(pdb.getParent());
		tradeProcessor=pdb.getTradeProcessor();
	}
	private TradeProcessor getTradeProcessor() throws InvalidInputException
	{
		return tradeProcessor;
	}
	private ProductBook getParent() throws InvalidInputException
	{
		return new ProductBook(productBookParent);
	}
	private String getSide()
	{
		return side;
	}
	private void setProductBookSideParent(ProductBook object) throws InvalidInputException {
		if(object==null) throw new InvalidInputException("The productBook object couldn't be null.");
		else productBookParent= new ProductBook(object);
		
	}

	private void setSide(String sideIn) throws InvalidInputException {
		
		if(sideIn==null||sideIn.isEmpty())
			throw new InvalidInputException("The side can't be null or empty.");
		else
			side=sideIn;
	}
	public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName)
	{
		ArrayList<TradableDTO> listOfTradableDTO=new ArrayList<>();
		 for(Entry<Price, ArrayList<Tradable>> ee : bookEntries.entrySet()) {
			    ArrayList<Tradable> tradableList = ee.getValue();
			    for(int i=0;i<tradableList.size();i++)
			    {
			    	Tradable tradableObject=tradableList.get(i);
			    
			    	if(tradableObject.getUser().equals(userName)&&tradableObject.getRemainingVolume()>0)
			    	{
			    		
			    	//String productln,Price priceln,int originalVolumeln,int remainingVolumeln,int cancelledVolumeln, String userln,String sideln,boolean isQuoteln, String idln
			    		TradableDTO tradableDTO=new TradableDTO(tradableObject.getProduct(), tradableObject.getPrice(),
			    				tradableObject.getOriginalVolume(), tradableObject.getRemainingVolume(),
			    				tradableObject.getCancelledVolume(),tradableObject.getUser(),tradableObject.getSide(),tradableObject.isQuote(),tradableObject.getId());
			    		listOfTradableDTO.add(tradableDTO);
			    	}
			    }
		
	}
		 return listOfTradableDTO;
	}
	
     synchronized ArrayList<Tradable> getEntriesAtTopOfBook()
	{
		if(bookEntries.isEmpty())
			return null;
		else {
			 ArrayList<Price> sorted = new ArrayList<Price>(bookEntries.keySet()); // Get prices
			Collections.sort(sorted); // Sort them
			if (side.equals("SELL")) 
				Collections.reverse(sorted); // Reverse them
			return bookEntries.get(sorted.get(0));
		/*	ArrayList<Tradable> list= bookEntries.get(sorted.get(0));
			ArrayList<Tradable> returnList=new ArrayList<Tradable>();
			
			for(int i=0;i<list.size();i++)
				returnList.add(list.get(i));
			return returnList;*/
			
		}
			
		}
     public synchronized String[] getBookDepth()
     {String[] stringArray;
    	 
    	 if(bookEntries.isEmpty())
    		return new String[]{"<Empty>"};
    	 else 
    	 {
    	stringArray=new String[bookEntries.size()];
    		 int stringArraySize=0;
    		 ArrayList<Price> sorted = new ArrayList<Price>(bookEntries.keySet()); // Get prices
 			Collections.sort(sorted); // Sort them
 			if (side.equals("SELL")) 
 				Collections.reverse(sorted); // Reverse them
 			for(int i=0;i<sorted.size();i++){
 				int sum=0;
 				ArrayList<Tradable> tradableList=bookEntries.get(sorted.get(i));
 				for(int j=0;j<tradableList.size();j++)
 						{
 							sum=sum+tradableList.get(j).getRemainingVolume();
 							String priceVolume=sorted.get(i).toString()+"*"+sum;
 							if(stringArraySize<bookEntries.size())
 							
 								stringArray[stringArraySize]=priceVolume;
 								
 							}stringArraySize++;
 						}
 						
 				}
 			return stringArray;
 		
 			
 					}
    		 
    	 
     
    	
     
     synchronized ArrayList<Tradable> getEntriesAtPrice(Price price)
     {
    	 boolean find=false;
    	 Iterator<Entry<Price, ArrayList<Tradable>>> it = bookEntries.entrySet().iterator();
    	 ArrayList<Tradable> returnArray=null;
    	 while (!find&&it.hasNext())
    	 {
    		 Entry<Price,ArrayList<Tradable>> entry=it.next();
    		Price priceInBookEntries =entry.getKey();
    		if(priceInBookEntries.equals(price))
    			{
    			find=true;
    			returnArray= entry.getValue();
    			}	 
    	 }
    	 
    	 if(find==false)
    		 returnArray=null;
    	 return returnArray;
     }
     
     public synchronized boolean hasMarketPrice()
     {
    	 boolean find=false;
    	 boolean ifHasMarketPrice=false;
    	 Iterator<Entry<Price, ArrayList<Tradable>>> it = bookEntries.entrySet().iterator();
    	 while (!find&&it.hasNext())
    	 {
    		 Entry<Price,ArrayList<Tradable>> entry=it.next();
    		Price priceInBookEntries =entry.getKey();
    		if(priceInBookEntries.isMarket())
    			{
    			find=true;
    			ifHasMarketPrice=true;
    			}		 
    	 }
    	 return ifHasMarketPrice;
     }
     
     public synchronized boolean hasOnlyMarketPrice()
     {
    	boolean onlyMarket=false;
    	if(bookEntries.size()==1)
    	{
    		for(Entry<Price, ArrayList<Tradable>> ee: bookEntries.entrySet())
    		if(ee.getKey().isMarket())
    			onlyMarket=true;
    	}
    	return onlyMarket;	
     }
     public synchronized Price topOfBookPrice()
     {
    	
    	 if(bookEntries==null||bookEntries.isEmpty())//
    		 return null;
    	 else 
    	 {
    		 Price p=null;
    		 ArrayList<Price> sorted = new ArrayList<Price>(bookEntries.keySet()); // Get prices
 			Collections.sort(sorted); // Sort them
 			if (side.equals("SELL")) 
 				Collections.reverse(sorted); // Reverse them
 			if(sorted.get(0).isMarket())
 				p=PriceFactory.makeMarketPrice();
 			else if(!sorted.get(0).isMarket())
 				p=PriceFactory.makeLimitPrice(sorted.get(0).getValue());
 			return p;
 					
    	 }
    	
     }
     public synchronized int topOfBookVolume()
     {
    	 int sum=0;
    	 if(bookEntries==null||bookEntries.isEmpty())
    		 return 0;
    	 else 
    	 {
    		 
    		 ArrayList<Price> sorted = new ArrayList<Price>(bookEntries.keySet()); // Get prices
 			Collections.sort(sorted); // Sort them
 			if (side.equals("SELL")) 
 				Collections.reverse(sorted); // Reverse them
 			ArrayList<Tradable> tradableList=bookEntries.get(sorted.get(0));
 			for(int i=0;i<tradableList.size();i++)
 			{
 				sum=sum+tradableList.get(i).getRemainingVolume();
 			}
 					
    	 }
    	 return sum;
     }
     public synchronized boolean isEmpty()
     {
    	 if(bookEntries==null)
    		 return true;
    	 else return false;
     }
     
     
     public synchronized void cancelAll() throws InvalidInputException, NoSubscribeException, OrderNotFoundException, InvalidVolumeException
     {
    	
    	 while(bookEntries.size()!=0)
    		 //for(Iterator<Entry<Price, ArrayList<Tradable>>> it = bookEntries.entrySet().iterator(); it.hasNext(); )
    	 {
    			 Iterator<Entry<Price, ArrayList<Tradable>>> it = bookEntries.entrySet().iterator();
    		 Entry<Price,ArrayList<Tradable>> entry=it.next();
    		 ArrayList<Tradable> tradableList=entry.getValue();
 
    		 while(tradableList.size()!=0)
    		 { 
    			 if(!tradableList.get(0).isQuote())
    				 submitOrderCancel(tradableList.get(0).getId());	
    			 else if(tradableList.get(0).isQuote())
    			 submitQuoteCancel(tradableList.get(0).getUser());
    			
    			 }
    			
    		 }
    	
    	 }
    	 
     
     
     
     public synchronized TradableDTO removeQuote(String user){
    	 
    	 boolean find=false;
    	 Iterator<Entry<Price, ArrayList<Tradable>>> it = bookEntries.entrySet().iterator();
    	 TradableDTO dto=null;
    	 while (!find&&it.hasNext())
    	 {
    		 Entry<Price,ArrayList<Tradable>> entry=it.next();
    		 
    		 int countTradableList=0;
    		 ArrayList<Tradable> tradableList=entry.getValue();
    		 int tradableListOriginialSize=tradableList.size();
    		 while(!find&&countTradableList<tradableListOriginialSize)
    		 {
    			 if(tradableList.get(countTradableList).isQuote()&&tradableList.get(countTradableList).getUser().equals(user))
    			 {		
    				 find=true;
    				 Tradable tradableObject=tradableList.get(countTradableList);
    				//String productln,Price priceln,int originalVolumeln,int remainingVolumeln,int cancelledVolumeln, String userln,String sideln,boolean isQuoteln, String idln
			    		dto=new TradableDTO(tradableObject.getProduct(), tradableObject.getPrice(),
			    				tradableObject.getOriginalVolume(), tradableObject.getRemainingVolume(),
			    				tradableObject.getCancelledVolume(),tradableObject.getUser(),tradableObject.getSide(),tradableObject.isQuote(),tradableObject.getId());
			 
    				 tradableList.remove(countTradableList);
    				 if(tradableList.size()==0)
    					bookEntries.remove(entry.getKey());
    			
    			 }
    			 countTradableList++; 
    		 }
    	 }
    	 return dto;
    	 
     }
     public synchronized void submitOrderCancel(String orderId) throws InvalidInputException, NoSubscribeException, OrderNotFoundException, InvalidVolumeException
     {
    	 
    	 boolean find=false;
    	 Iterator<Entry<Price, ArrayList<Tradable>>> it = bookEntries.entrySet().iterator();
    	 //TradableDTO dto=null;
    	 while (!find&&it.hasNext())
    	 {
    		 Entry<Price,ArrayList<Tradable>> entry=it.next();
    		 int countTradableList=0;
    		 ArrayList<Tradable> tradableList=entry.getValue();
    		 int tradableListOriginialSize=tradableList.size();
    		 while(!find&&countTradableList<tradableListOriginialSize)
    		 {
    			 Tradable tradableObject=tradableList.get(countTradableList);
    			 if(!tradableObject.isQuote()&&tradableObject.getId().equals(orderId))
    			 {	
    				 find=true;
    				 tradableList.remove(countTradableList); 
    				 String cancelDetail=tradableObject.getSide()+" SIDE ORDER CANCELLED";
    				 CancelMessage cancelMessage=new CancelMessage(tradableObject.getUser(),tradableObject.getProduct(),tradableObject.getPrice(),
    						 tradableObject.getRemainingVolume(),cancelDetail,tradableObject.getSide(),tradableObject.getId());
    				 MessagePublisher.getInstance().publishCancel(cancelMessage);
    				 addOldEntry(tradableObject);
    				 //????volume which volume?????
    				if(tradableList.size()==0)
    					
    				 bookEntries.remove(entry.getKey());
    				// CancelMessage(String user, String product,Price price,
    						 //int volume,String details,String side, String id)
    				 
    			 }
    				 countTradableList++; 
    				 
    		}}
    		// if(tradableList.isEmpty())
    		// bookEntries.remove(entry.getKey());
    		 if(!find)
    		 {
    			 productBookParent.checkTooLateToCancel(orderId); 
    		 }
    		
    		
    		 
     }
     
     public synchronized void submitQuoteCancel(String userName) throws InvalidInputException, NoSubscribeException
     {
    	TradableDTO tradableObject= removeQuote(userName);
    	if(tradableObject!=null)
    	{
    		String cancelDetail=side+" SIDE QUOTE CANCELLED";
    		 CancelMessage cancelMessage=new CancelMessage(tradableObject.user,tradableObject.product,tradableObject.price,
					 tradableObject.remainingVolume,cancelDetail,tradableObject.side,tradableObject.id);
			 MessagePublisher.getInstance().publishCancel(cancelMessage);
    	}
  
    
     }
     public void addOldEntry(Tradable t) throws InvalidVolumeException

     {
    	 productBookParent.addOldEntry(t);
     }
     
     
     public synchronized void addToBook(Tradable trd)

     
     { 
    	 if(!bookEntries.containsKey(trd.getPrice()))
    	 {
    		 ArrayList<Tradable> newTradableList=new ArrayList<>();
    		 bookEntries.put(trd.getPrice(), newTradableList);
    	 }
    	 	ArrayList<Tradable> tradableList=bookEntries.get(trd.getPrice());
    	 	tradableList.add(trd);
   		 bookEntries.put(trd.getPrice(), tradableList);
    	/*
    		 boolean find=false;
        	 Iterator<Entry<Price, ArrayList<Tradable>>> it = bookEntries.entrySet().iterator();
        	 ArrayList<Tradable> tradableList=null;
        	 while (!find&&it.hasNext())
        	 {
        		Entry<Price,ArrayList<Tradable>> entry=it.next();
        		Price priceInBookEntries =entry.getKey();
        		if(priceInBookEntries.equals(trd.getPrice()))
        			{
        			find=true;
        			tradableList= entry.getValue();
        			tradableList.add(trd);
        			
        			}	 
        	 }
        	 */
        	
    	 }
    	 
     
    
     public HashMap<String, FillMessage> tryTrade(Tradable trd) throws NoSubscribeException, InvalidInputException, InvalidVolumeException
     {
    	 HashMap<String, FillMessage> allFills=new HashMap<String, FillMessage>();
    	 if(side.equals("BUY"))
    		allFills=trySellAgainstBuySideTrade(trd);
    	 else if(side.equals("SELL"))
    		allFills=tryBuyAgainstSellSideTrade(trd);
    		 
    	 for(Entry<String, FillMessage> ee:allFills.entrySet())
    		 MessagePublisher.getInstance().publishFill(ee.getValue());
    	 return allFills;
     }

     public synchronized HashMap<String, FillMessage> tryBuyAgainstSellSideTrade(Tradable trd) throws InvalidInputException, InvalidVolumeException {
    	 HashMap<String, FillMessage> allFills = new HashMap<String, FillMessage>();
    	 HashMap<String, FillMessage> fillMsgs = new HashMap<String, FillMessage>();
 
    	 while((trd.getRemainingVolume()>0&&!bookEntries.isEmpty()&&trd.getPrice().getValue()<=topOfBookPrice().getValue())||
    			 (trd.getRemainingVolume()>0&&!bookEntries.isEmpty()&&trd.getPrice().isMarket()))
    	 {
    		 HashMap<String, FillMessage> someMsgs=tradeProcessor.doTrade(trd);
    		 fillMsgs = mergeFills(fillMsgs, someMsgs);
    		 
    	 }
    	 
    	 //allFills.putAll(fillMsgs);
    	 for(Entry<String, FillMessage> ee:fillMsgs.entrySet())
    	 {
    		 allFills.put(ee.getKey(), ee.getValue());
    	 }
    	 return allFills;
	}

     
     private HashMap<String, FillMessage> mergeFills(HashMap<String, FillMessage> existing, HashMap<String,
    		 FillMessage> newOnes) throws InvalidInputException
    		 {
    	 		HashMap<String,FillMessage> mergedFills=new HashMap<>(); 
    	 		if(existing.isEmpty())
    	 		{
    	 			for(Entry<String,FillMessage > ee:newOnes.entrySet())
    	 			{
    	 				mergedFills.put(ee.getKey(), ee.getValue());
    	 			}
    	 			return mergedFills;
    	 		}
    	 		
    	 		
    	 	
    	 			//return new HashMap<String, FillMessage>(newOnes);
    	 		else
    	 		{
    	 			HashMap<String, FillMessage> results = new HashMap<>(existing);
    	 			for (String key : newOnes.keySet()) {
    	 				if (!existing.containsKey(key))
    	 					results.put(key, newOnes.get(key));
    	 				else 
    	 					{
    	 					FillMessage fm = results.get(key);
    	 					fm.setVolume(newOnes.get(key).getVolume());
    	 					fm.setDetail(newOnes.get(key).getDetails());
    	 					}
    	 				
    	 				
    	 			}
    	 			 return results;}
    	 		
    
    		 }
  

	public  synchronized HashMap<String, FillMessage> trySellAgainstBuySideTrade(Tradable trd) throws InvalidInputException, InvalidVolumeException {
		HashMap<String, FillMessage> allFills = new HashMap<String, FillMessage>();
		HashMap<String, FillMessage> fillMsgs = new HashMap<String, FillMessage>();
		 while((trd.getRemainingVolume()>0&&!bookEntries.isEmpty()&&trd.getPrice().getValue()<=topOfBookPrice().getValue())||
    			 (trd.getRemainingVolume()>0&&!bookEntries.isEmpty()&&trd.getPrice().isMarket()))
			 
		 {
			 HashMap<String, FillMessage> someMsgs=tradeProcessor.doTrade(trd);
			 fillMsgs = mergeFills(fillMsgs, someMsgs);
		 }
		 for(Entry<String, FillMessage> ee:fillMsgs.entrySet())
    	 {
    		 allFills.put(ee.getKey(), ee.getValue());
    	 }
		 //allFills.putAll(fillMsgs);
    	 return allFills;

		
	}
	
	
	public synchronized void clearIfEmpty(Price p)
	{
		if(bookEntries.containsKey(p))
		{	if(bookEntries.get(p).isEmpty())
			bookEntries.remove(p);
		}
				
	}
	
	public synchronized void removeTradable(Tradable t)

	{
		boolean ifRemoved=false;
		ArrayList<Tradable> entries = bookEntries.get(t.getPrice());
		if(!entries.isEmpty())
			ifRemoved=entries.remove(t);
		if(entries.isEmpty()&&ifRemoved)
			clearIfEmpty(t.getPrice());
	}
	

}
	
	
	
	

	
	
	
	


