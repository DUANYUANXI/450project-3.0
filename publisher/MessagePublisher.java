package publisher;

import java.util.ArrayList;


import client.User;
import message.CancelMessage;
import message.FillMessage;
import message.MarketMessage;

public final class MessagePublisher extends Publisher {
	 private volatile static MessagePublisher instance;
	 private  ArrayList<User> userName=new ArrayList<User>();
	 private MessagePublisher(){
		 
	 }
	 public static MessagePublisher getInstance()
	 {
		 if(instance==null)
		 {
			 synchronized(MessagePublisher.class)
			 {
				if(instance==null)
					instance=new MessagePublisher();
			 }
			 	
		 }
		 return instance;
	}

	 public synchronized void publishCancel(CancelMessage cm) throws NoSubscribeException{
		 userName=instance.getUserList(cm.getProduct());
		 if(userName!=null){
	 			    
			    	
			    for(int i=0;i<userName.size();i++)
			    {
			    	if(userName.get(i).getUserName().equals(cm.getUser()))
			    		userName.get(i).acceptMessage(cm);
			    	//else if(!cm.getUser().equals(userName.get(i).getUserName()))
			    		//throw new NoSubscribeException(cm.getUser()+" doesn't subscribed the cancell message for "+cm.getProduct());
			    }
			    
			   }
		 else throw new NoSubscribeException(cm.getUser()+" doesn't subscribe the cancell message for "+cm.getProduct());
	    }
		 
	 public synchronized void publishFill(FillMessage fm) throws NoSubscribeException{
		 userName=instance.getUserList(fm.getProduct());
		 if(userName!=null){
	 
			    for(int i=0;i<userName.size();i++)
			    {
			    	if(userName.get(i).getUserName().equals(fm.getUser()))
			    		userName.get(i).acceptMessage(fm);
			    	//else throw new NoSubscribeException(fm.getUser()+" doesn't subscribed the cancell message for "+fm.getProduct());
			    }	    
		 }
		 else throw new NoSubscribeException(fm.getUser()+" doesn't subscribe the cancell message for "+fm.getProduct());
		 
	 }
	 public synchronized void publishMarketMessage(MarketMessage marketMessage) throws NoSubscribeException
	 {
		 
		 ArrayList<User> allUser=instance.getAllUser();
		 if(allUser!=null){
			    for(int i=0;i<allUser.size();i++)
			    {
			    	User user=allUser.get(i);
			    	user.acceptMarketMessage(marketMessage.toString());
			    	}  
			    }
		 else throw new NoSubscribeException("No user subscribed market message");
			    
	 }
		 
}

