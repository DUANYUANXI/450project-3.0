package Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import message.InvalidInputException;
import message.MarketMessage;
import publisher.MarketDataDTO;
import publisher.MessagePublisher;
import publisher.NoSubscribeException;
import tradable.InvalidValueException;
import tradable.InvalidVolumeException;
import tradable.Order;
import tradable.Quote;
import tradable.TradableDTO;



public final class ProductService {
	
	 private volatile static ProductService instance;
	 private HashMap<String, ProductBook> allBooks; 
	 private String currentMarketState; 
		
	 private ProductService(){
		 
		 currentMarketState="CLOSED";
		 allBooks = new HashMap< String, ProductBook >();

	 }
	 public static ProductService getInstance()
	 {
		 if(instance==null)
		 {
			 synchronized(ProductService.class)
			 {
				if(instance==null)
					instance=new ProductService();
			 }
			 	
		 }
		 return instance;
	}

	 
	 public synchronized String getMarketState()
	 {
	 return currentMarketState;
		 
	 }
	 
	 public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName, String product)
	 {
		 return allBooks.get(product).getOrdersWithRemainingQty(userName);
	 }
	 public synchronized MarketDataDTO getMarketData(String product)
	 {
		return  allBooks.get(product).getMarketData();
	 }
	 
	 public synchronized String[][] getBookDepth(String product) throws NoSuchProductException
	 {
		 
		 if(!allBooks.containsKey(product))
		throw new NoSuchProductException("There is no such a product");
		 else 
		 return  allBooks.get(product).getBookDepth(); 
	 }
	 public synchronized ArrayList<String> getProductList()
	 {
		 return new
				 ArrayList<String>(allBooks.keySet());
	 }
	 public synchronized void setMarketState(String ms) throws InvalidMarketStateTransition, InvalidInputException, NoSubscribeException, OrderNotFoundException, InvalidVolumeException
	 {
		 String preState=getMarketState();
		 if((preState.equals("CLOSED")&&ms.equals("PREOPEN"))||(preState.equals("PREOPEN")&&ms.equals("OPEN"))||
			(preState.equals("OPEN")&&ms.equals("CLOSED")))
		 {
			 currentMarketState=ms; 
			 MarketMessage msm=new MarketMessage(currentMarketState);
			 MessagePublisher.getInstance().publishMarketMessage(msm);
			 if(currentMarketState.equals("OPEN"))
			 {
				 for(Entry<String, ProductBook> ee:allBooks.entrySet())
					 ee.getValue().openMarket();
			 }
			 if(currentMarketState.equals("CLOSED"))
			 {
				 for(Entry<String, ProductBook> ee:allBooks.entrySet())
					 ee.getValue().closeMarket();
			 }
				 
		 }
		 else throw new InvalidMarketStateTransition("This new market state voilates Market state transition rule. ");
		
	 	}
	 public synchronized void createProduct(String product) throws DataValidationException, InvalidInputException, ProductAlreadyExistsException
	 {
		if(product==null||product.isEmpty())
			throw new DataValidationException("Product can't be null.");
		if(allBooks.containsKey(product))
			throw new ProductAlreadyExistsException("This product is already existed.");
		else{
			
			ProductBook pb=new ProductBook(product);
			allBooks.put(product, pb);
			
		}
	 }
	 public synchronized void submitQuote(Quote q) throws InvalidMarketStateException, NoSuchProductException, InvalidValueException, DataValidationException, NoSubscribeException, InvalidInputException, InvalidVolumeException
	 {
		 if(currentMarketState.equals("CLOSED"))
		 throw new InvalidMarketStateException("Market is closed.");
		 if(!allBooks.containsKey(q.getProduct()))
			 throw new NoSuchProductException("Product "+q.getProduct()+" does not exist.");
		 else
			 allBooks.get(q.getProduct()).addToBook(q);
		
		 
	 }
	 public synchronized String submitOrder(Order o) throws InvalidMarketStateException, NoSubscribeException, InvalidInputException, NoSuchProductException, InvalidVolumeException
	 {
		 if(currentMarketState.equals("CLOSED"))
			 throw new InvalidMarketStateException("Market is closed.");
		 if(currentMarketState.equals("REOPEN")&&o.getPrice().isMarket())
			 throw new InvalidMarketStateException("You cannot submit MKT orders during PREOPEN.");
		 if(!allBooks.containsKey(o.getProduct()))
			 throw new NoSuchProductException("Product " +o.getProduct()+" does not exist.");
		 else 
		  allBooks.get(o.getProduct()).addToBook(o); 
		 return o.getId();
			 
		 
		 
		 
	 }
	 public synchronized void submitOrderCancel(String product, String side, String orderId) throws InvalidMarketStateException, NoSuchProductException, InvalidInputException, NoSubscribeException, OrderNotFoundException, InvalidVolumeException
	 {
		 if(currentMarketState.equals("CLOSED"))
			 throw new InvalidMarketStateException("Market is closed.");
		 if(!allBooks.containsKey(product))
			 throw new NoSuchProductException("Product "+product+" does not exist.");
		 else 
			  allBooks.get(product).cancelOrder(side, orderId);
	 }
	 public synchronized void submitQuoteCancel(String userName, String product) throws InvalidInputException, NoSubscribeException, InvalidMarketStateException, NoSuchProductException
	 {
		 if(currentMarketState.equals("CLOSED"))
			 throw new InvalidMarketStateException("Market is closed.");
		 if(!allBooks.containsKey(product))
			 throw new NoSuchProductException("Product "+product+" does not exist.");
		 else  allBooks.get(product).cancelQuote(userName);	 	 
	 }
}
