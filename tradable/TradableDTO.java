package tradable;
import priceFactory.Price;

/**
 * @author YU XI, XIAOYU YUAN, XINGYUE DUAN 
 * DATE: 04/20/2015
 * 
 */


/**
 * This is a tradable data transfer object class

 */
public class TradableDTO {	
	
	public String product;
	
	public Price price;
	
	public int originalVolume;
	
	
	public int  remainingVolume;
	
	public int  cancelledVolume;
	
	
	public String user;
	
	public String side;
	
	public boolean isQuote;
	
	
	public String id;

	/**
	 * Constructor
	 * @param product
	 * @param price
	 * @param ov originalVolume
	 * @param rv remaingingVolume
	 * @param cv cancelledVolume
	 * @param user user name 
	 * @param side
	 * @param isQuote boolean
	 * @param id
	 */
public  TradableDTO ( String productln,Price priceln,int originalVolumeln,int remainingVolumeln,int cancelledVolumeln, String userln,String sideln,boolean isQuoteln, String idln )
	
	{
		this.product=productln;
		this.price=priceln;
		this.originalVolume= originalVolumeln;
		this.remainingVolume=remainingVolumeln;
		this.cancelledVolume=cancelledVolumeln;
		this.user=userln;
		this.side= sideln;
		this.isQuote=isQuoteln;
		this.id=idln;
	}
	
/**
 * This is a toString method
 * @return String
 */
	public String toString()
	{
		String outPut="Product: "+product+", Price: "+price.toString()+", OriginalVolume: "+originalVolume+", RemainingVolume "+
	remainingVolume+", CancelledVolume: "+cancelledVolume+", User: "+user+", Side: "+side+", IsQuote: "+isQuote+", id: "+id;
		return outPut;
	}
}

