package client;

public class TradableUserData {
	private String userName;
	private String symbol;
	private String side;
	private String orderId;
	
	public TradableUserData(String userName, String symbol,
			String side, String orderId) {
		setUserName(userName);
		this.setSide(side);
		this.setSymbol(symbol);
		this.setOrderId(orderId);
	}
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	private void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}
	/**
	 * @param symbol the symbol to set
	 */
	private void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	/**
	 * @return the side
	 */
	public String getSide() {
		return side;
	}
	/**
	 * @param side the side to set
	 */
	private void setSide(String side) {
		this.side = side;
	}
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	private void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String toString(){
		return "User " + userName + "," + side + " " + symbol + "(" + this.orderId + ")";
		
	}
}
