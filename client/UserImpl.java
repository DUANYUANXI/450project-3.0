package client;

import gui.UserDisplayManager;

public class UserImpl {
	private String userName;
	private long connectedId;
	private String[] stockList;
	private TradableUserData[] submittedOrders;
	private Position userValue;
	private UserDisplayManager display;
	
	public UserImpl(String userName){
		this.userName = userName;
		userValue = new Position();
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public void 
}
