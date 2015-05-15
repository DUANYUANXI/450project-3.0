package tradable;

import priceFactory.Price;

public class ImplFactory {

	public static Tradable makeTradableImpl(String implName,String userNameln, String productSymbolln, Price priceln, int originalVolumeln,  String
			bookSideln,boolean isQuoteln,String idln) throws InvalidValueException
	{
		String instructor=implName.toLowerCase().trim();
		if(instructor.equals("tradable"))
			return new TradableImpl(userNameln, productSymbolln,priceln,  originalVolumeln,  
					bookSideln,isQuoteln,idln);
		else return null;
		
	}
}
 