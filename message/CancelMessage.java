package message;


import priceFactory.Price;



public class CancelMessage implements Message, Comparable<CancelMessage>  {
	private Message delegate;
	
	public CancelMessage(String user, String product,Price price,
			 int volume,String details,String side, String id) throws InvalidInputException{
		
		delegate=MessageImplFactory.makeMessageImpl("messageImpl", user, product, price, volume, details, side, id);
		
	}
	
	public String getUser() {
		return delegate.getUser();
	}

	@Override
	public String getProduct() { 
		return delegate.getProduct();
	}

	@Override
	public Price getPrice() {
		return delegate.getPrice();
	}

	@Override
	public int getVolume() {
		return delegate.getVolume();
	}

	@Override
	public String getDetails() {
		return delegate.getDetails();
	}

	@Override
	public String getSide() {
		return delegate.getSide();
	}


	public String getId() {
		return delegate.getId();
	}

	@Override
	public int compareTo(CancelMessage cm) {
		
		
			return delegate.getPrice().compareTo(cm.getPrice());
		
	}
	
	public String toString()
	{
		String output="User:"+getUser()+", Product:"+getProduct()+", price:"+getPrice().toString()+", Volume:"
				+getVolume()+", Details: Canclled By User, Side:"+getSide()+", Id:"+getId();
		return output;
	}

	@Override
	public void setVolume(int volume) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetail(String detail) {
		// TODO Auto-generated method stub
		
	}
	
}
