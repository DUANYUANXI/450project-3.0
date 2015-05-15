package publisher;

import java.util.ArrayList;


import client.User;

public final class CurrentMarketPublisher extends Publisher{
	
	
	 private volatile static CurrentMarketPublisher instance;
	
	 private CurrentMarketPublisher(){

	 }
	 public static CurrentMarketPublisher getInstance()
	 {
		 if(instance==null)
		 {
			 synchronized(CurrentMarketPublisher.class)
			 {
				if(instance==null)
					instance=new CurrentMarketPublisher();
			 }
			 	
		 }
		 return instance;
	}
	 
	 public synchronized void publishCurrentMarket(MarketDataDTO md) throws NoSubscribeException 
	 {
			    	ArrayList<User> userName=instance.getUserList(md.product);
			    	if(userName!=null){
			    	for(int i=0;i<userName.size();i++)
			    		{
			    		User user=userName.get(i);
			    		user.acceptCurrentMarket(md.product, md.buyPrice,md.buyVolume, md.sellPrice, md.sellVolume);
			    		}}
			    	else throw new NoSubscribeException("No user subscribed current market message for stock"+
			    		md.product);
			    	
			    
	 }
			    
	 }
