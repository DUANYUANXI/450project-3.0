package publisher;

import java.util.ArrayList;


import client.User;
import priceFactory.Price;
import priceFactory.PriceFactory;

public final class LastSalePublisher extends Publisher {
	
	 private volatile static LastSalePublisher instance;
	 private LastSalePublisher(){
		 
	 }
	 public static LastSalePublisher getInstance()
	 {
		 if(instance==null)
		 {
			 synchronized(LastSalePublisher.class)
			 {
				if(instance==null)
					instance=new LastSalePublisher();
			 }
			 	
		 }
		 return instance;
	}
	 public synchronized void publishLastSale(String product, Price p, int v) throws NoSubscribeException
	 {
		 
		 ArrayList<User> userName=instance.getUserList(product);
		 if(userName!=null){
			    		for(int i=0;i<userName.size();i++)
			    	{
			    			User user=userName.get(i);
			    			if(p!=null)
			    				user.acceptLastSale(product, p, v);
			    			else if(p==null)
			    			{
				    		Price passedPrice=PriceFactory.makeLimitPrice("0");	
				    		user.acceptLastSale(product, passedPrice, v);
				    		}	
			    	}
			   TickerPublisher.getInstance().publishTicker(product, p); 		
			   
	 }
		 else throw new NoSubscribeException("No user subscried last sale message for "+
	 product);
	 }
			 
			 
		
			 }
			    
		 
			 
			    
	 

