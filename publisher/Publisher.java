package publisher;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map.Entry;

import client.User;

public class Publisher {
	 private HashMap<String,ArrayList<User>> subscribedHashMap;
	 
	 
	 public Publisher(){
		 subscribedHashMap=new HashMap<String,ArrayList<User>>();
	 }
	 public synchronized void subscribe(User user, String stockNames) throws AlreadySubscribedException
		{
			
				if(subscribedHashMap.containsKey(stockNames))
				{
							ArrayList<User> userList=subscribedHashMap.get(stockNames);
							boolean found=false;
							int i = 0;
							while(i<userList.size()&&!found)
							{
								if(userList.get(i).getUserName().equals(user.getUserName()))
								found=true;
								i++;
								}
							if(found)
								throw new AlreadySubscribedException();
							else subscribedHashMap.get(stockNames).add(user);
							}
				else  
					{
					ArrayList<User> list=new ArrayList<User>();
					list.add(user);
					subscribedHashMap.put(stockNames,list);
					}
						
						
			}
	 public synchronized void unSubscribe(User user, String stockName) throws UnableUnSubscribeException 
		{
			
			
			
				if(subscribedHashMap.containsKey(stockName))
				{
							ArrayList<User> userList=subscribedHashMap.get(stockName);
							boolean found=false; 
							int j=0;
							while(j<userList.size()&&!found)
							{
								if(userList.get(j).getUserName().equals(user.getUserName()))
								found=true;
								j++;
							}
									
							if (found) subscribedHashMap.get(stockName).remove(j-1);
							else throw new UnableUnSubscribeException();
				}
							
					else
					
					throw new UnableUnSubscribeException();
					
						
						
			}
			
	public ArrayList<User> getUserList(String product)
	{
		ArrayList<User> userName=new ArrayList<User>();
		 for(Entry<String, ArrayList<User>> ee : subscribedHashMap.entrySet()) {
			    String p = ee.getKey();
			    if(p.equals(product))
			    	for(int i=0;i<ee.getValue().size();i++)
			    		{userName.add(ee.getValue().get(i));}
		
			    
			    
		 }
			    return userName;
			    }
	
	public ArrayList<User> getAllUser()
	{
		ArrayList<User> allUserName = new ArrayList<User>();
		ArrayList<User> userName;
		 for(Entry<String, ArrayList<User>> ee : subscribedHashMap.entrySet()) {
			    userName = ee.getValue();
			    for(int i=0;i<userName.size();i++)
			    {	if(!allUserName.contains(userName.get(i)))
			    	allUserName.add(userName.get(i));
			    }
			    userName=null;
	}
		 return allUserName;
	
	}}

