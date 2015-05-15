package priceFactory;



/**
 * @author YU XI, XIAOYU YUAN, XINGYUE DUAN 
 * DATE: 04/20/2015
 * 
 */
public class InvalidPriceOperation extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8753475802525319114L;
	/**
	 * 
	 */
	
	public InvalidPriceOperation()
	{
		super("Error: the price is invalid to operate.");
	}
	public InvalidPriceOperation(String message)
	{
		super(message);
	}
	
}
