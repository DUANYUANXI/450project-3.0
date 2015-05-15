package message;

import priceFactory.Price;



public class MessageImplFactory {
	
	public static Message makeMessageImpl(String implName,String user, String product,Price price,
			 int volume,String details,String side, String id) throws InvalidInputException
	{

		
		if(implName.equals("messageImpl"))
			
			
			return new MessageImpl(user, product,price,volume, details, side,  id);
			
			
		
		else return null;
		
	}

}
