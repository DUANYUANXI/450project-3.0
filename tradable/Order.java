package tradable;


import priceFactory.Price;

/**
 * @author YU XI, XIAOYU YUAN, XINGYUE DUAN 
 * DATE: 04/20/2015
 * 
 */
public class Order implements Tradable{


	private String id;
	//private TradableImpl traderableImp;
	//public enum BookSide{BUY,SIDE}
	private boolean isQuote;
	
	private Tradable delegate;
	
	public Order(String userName, String productSymbol, Price orderPrice, int originalVolume, String
			bookSide) throws InvalidValueException
	{
		
		isQuote=false;
		
		
		

	this.id=userName+productSymbol+orderPrice.toString()+Long.toString(System.nanoTime());
	delegate=ImplFactory.makeTradableImpl("tradable",userName,productSymbol,orderPrice,originalVolume,
			bookSide,isQuote,id);
	

	}
	
	
	public String getProduct()
	{
		return delegate.getProduct();
	}
	public Price getPrice()
	{
		return delegate.getPrice();
	}
	public int getOriginalVolume()
	{
		return delegate.getOriginalVolume();
		
	}
	public int getRemainingVolume()
	{
		return delegate.getRemainingVolume();
		
	}
	public int getCancelledVolume()
	{
		return delegate.getCancelledVolume();
	}
	public void  setCancelledVolume(int newCancelledVolume) throws InvalidVolumeException
	{
		delegate.setCancelledVolume(newCancelledVolume);
	}
	public void setRemainingVolume(int newRemainingVolume) throws InvalidVolumeException
	{
		delegate.setRemainingVolume(newRemainingVolume);;
	}
	public String getUser()
	{
		return delegate.getUser();
	}
	public String getSide()
	{
		return delegate.getSide();
	}
	public boolean isQuote()
	{
		return isQuote;
	}
	
	public String getId()
	{
		return id;
	}
	
	public String toString()
	{
		String outPut=delegate.toString();
		return outPut;
	}


	@Override
	public void setPrice(Price price) {
		
		delegate.setPrice(price);
	}
}
