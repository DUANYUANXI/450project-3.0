package message;


import priceFactory.Price;


public class FillMessage implements Message,Comparable<FillMessage> {
	
		private Message delegate;
		public FillMessage(String user, String product,Price price,
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

		@Override
		public String getId() {

			return delegate.getId();
		}

		@Override
		public int compareTo(FillMessage cm) {
	
		
				return delegate.getPrice().compareTo(cm.getPrice());
			
		}
		public String toString()
		{
			String output="User:"+getUser()+", Product:"+getProduct()+", price:"+getPrice().toString()+", Volume:"
					+getVolume()+", Details: Filled By User, Side:"+getSide();
			return output;
		}
public void setVolume(int volume) throws InvalidInputException
{
	delegate.setVolume(volume);
	}

@Override
public void setDetail(String detail) {
	delegate.setDetail(detail);
	
}



		
	}

