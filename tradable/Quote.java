package tradable;


import priceFactory.Price;

/**
 * @author YU XI, XIAOYU YUAN, XINGYUE DUAN 
 * DATE: 04/20/2015
 * 
 */

public class Quote {

	private String userName;
	private String productSymbol;
	private QuoteSides buySide;
	private QuoteSides sellSide;
	
	public Quote(String userNameln, String productSymbolln, Price buyPrice, int buyVolume, Price sellPrice, int
			sellVolume) throws InvalidVolumeException, InvalidValueException
	{
		this.userName=userNameln;
		this.productSymbol=productSymbolln;
		if(buyVolume<=0)
			throw new InvalidVolumeException("Buy",buyVolume);
		else 
			buySide=new QuoteSides(userNameln,productSymbolln, buyPrice, buyVolume,"BUY");
		if(sellVolume<=0)
			throw new InvalidVolumeException(sellVolume,"SELL");
		else 
			sellSide=new QuoteSides(userNameln,productSymbolln, sellPrice, sellVolume,"SELL");
	}
	public String getUserName()
	{
		return userName;
	}
	public String getProduct ()
	{
		return productSymbol;
	}
	// This method is modified to fit the information hiding principle. The previous version returns a reference to 
	//quotesides which is dangerous
	public QuoteSides getQuoteSide (String bs) throws InvalidValueException 
	{
		String sideString=bs.toUpperCase().trim();
		if(sideString.equals("BUY"))
		return new QuoteSides(buySide);
		else  if(sideString.equals("SELL")) return new QuoteSides(sellSide); 
		return null;
	}
	public String toString()
	{
		String id=buySide.getUser()+buySide.getProduct()+Long.toString(System.nanoTime());
		String outPut=userName+" quote: "+productSymbol+" "+buySide.getPrice().toString()+" * "+buySide.getOriginalVolume()+
			"(Original Vol:" +	buySide.getOriginalVolume()+" , CXL'd Vol: "+buySide.getCancelledVolume()+" ) "+"["+id+"]-"+sellSide.getPrice().toString()+" * "+buySide.getOriginalVolume()+
			"(Original Vol:" +	sellSide.getOriginalVolume()+" , CXL'd Vol: "+sellSide.getCancelledVolume()+" ) "+"["+id+"]";
		
		return outPut;
	}
	
}
