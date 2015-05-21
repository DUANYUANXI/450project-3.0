package message;

import priceFactory.Price;


public interface Message {

	public String getUser();
	
	public String getProduct();
	
	public Price getPrice();
	
	public int getVolume();
	
	public String getDetails();
	
	public String getSide();
	
	public String getId();
	
	public String toString();
	public void setVolume(int volume) throws InvalidInputException;
	public void setDetail(String detail);
	
	
	
	
	
	
}
