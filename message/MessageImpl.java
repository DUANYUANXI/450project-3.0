package message;

import priceFactory.Price;
import priceFactory.PriceFactory;




public class MessageImpl implements Message {
	private String user;
	private String product;
	private Price price;
	private int volume;
	private String details;
	private String side;
	public String id;
	
 public MessageImpl(String user, String product,Price price,
		 int volume,String details,String side, String id) throws InvalidInputException{
	
	   this.setUser(user);
	   this.setProduct(product);
	   this.setPrice(price);
	   this.setVolume(volume);
	   this.setDetails(details);
	   this.setSide(side);
	   this.setId(id);
	 
 }
 
 public String getUser(){
	 return user;
	 }
 	public String getProduct(){return product;}
	
	public Price getPrice(){return price;}
	
	public int getVolume(){return volume;}
	
	public String getDetails(){return details;}
	
	public String getSide(){return side;}
	
	public String getId(){return id;}

	private void setUser(String user) throws InvalidInputException  {
		if(user!=null&&!user.equals(""))
		this.user = user;
		else throw new InvalidInputException("User name can't be null or empty String");
	}

	private void setProduct(String product) throws InvalidInputException {
		if(product!=null&&!product.equals(""))
		this.product = product;
		else throw new  InvalidInputException("Product Symbol can't be null or empty String");
	}

	private void setPrice(Price price) throws InvalidInputException {
		if(price!=null){
			if(!price.isMarket())
				this.price = PriceFactory.makeLimitPrice(price.getValue());
			else
				this.price=PriceFactory.makeMarketPrice();}
		else throw new InvalidInputException("Price object can't be null");
		
		
	}
	//??????
	public void setVolume(int volume) throws InvalidInputException {
		if(volume>=0)
		this.volume = volume;
		else throw new InvalidInputException("Volume can't be negative");
	}

	public void setDetails(String details) throws InvalidInputException {
		if(details!=null)
		this.details = details;
		else throw new InvalidInputException("Detail can't be null");
	}

	private  void setSide(String side) throws InvalidInputException {
		side=side.toUpperCase().trim();
		if(side.equals("BUY")||side.equals("SELL"))
		this.side = side;
		else throw new InvalidInputException("Side doesn't provide a valid string");
	} 

	private void setId(String id) throws InvalidInputException {
		if(id!=null)
		this.id = id;
		else throw new InvalidInputException("Id can't be null");
		
	}


	public String toString(){
		
		String output="User:"+user+" Product:"+product+" price:"+price.toString()+" Volume:"
				+volume+",Details:" +details+", Side:"+side+"Id:"+id;
		return output;
				
	}

	@Override
	public void setDetail(String detail) {
		this.details=detail;
		
	}

	
	}
	
	

