package publisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


import priceFactory.Price;
import client.User;

public final class TickerPublisher extends Publisher {
	private HashMap<String,Price> priceRecord;
	 private volatile static TickerPublisher instance;
	 private TickerPublisher(){
		 priceRecord=new  HashMap<String,Price>();
	 }
	 public static TickerPublisher getInstance()
	 {
		 if(instance==null)
		 {
			 synchronized(TickerPublisher.class)
			 {
				if(instance==null)
					instance=new TickerPublisher();
			 }
			 	
		 }
		 return instance;
	}
	 public synchronized void publishTicker(String product, Price p)
	 {
		 
		char tikerSymbol=' ';
		boolean found=false;
			    
		if(!priceRecord.entrySet().isEmpty())
		{ 
	
			Iterator<Entry<String, Price>> it = priceRecord.entrySet().iterator();
			while(it.hasNext()&&!found){
				
			Entry<String,Price> entry=it.next();
			   String productName = entry.getKey();
			   if(productName.equals(product)&&p!=null)
			   {
				    
				   if(entry.getValue().getValue()==p.getValue())
					   tikerSymbol='=';
				   else if (entry.getValue().getValue()<=p.getValue())
					   tikerSymbol= (char)8593;
				   else if (entry.getValue().getValue()>=p.getValue())
					   tikerSymbol=(char) 8595 ;
				   entry.setValue(p);
				   found=true;
			   } 
			  
			}
	 
			 if(!found)  priceRecord.put(product, p);	
			 }
		else if(priceRecord.entrySet().isEmpty())
			 priceRecord.put(product, p);	
			 
		ArrayList<User> userName=instance.getUserList(product);
		if(userName!=null){
				     for(int i=0;i<userName.size();i++)
				    {
				    	User user=userName.get(i);
				    	user.acceptTicker(product, p, tikerSymbol);
				    	}
				    }
				    
		 
	 }}
	 

