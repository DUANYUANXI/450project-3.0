package tradable;


/**
 * @author YU XI, XIAOYU YUAN, XINGYUE DUAN 
 * DATE: 04/20/2015
 * 
 */
public class InvalidVolumeException extends Exception {private static final long serialVersionUID = 1L;

public InvalidVolumeException()
{
	super("");
	}
public InvalidVolumeException(int o, int r, int c, int n)
{
		super("Request new Cancelled Volume (" +c+") plus the Remaining Volume ("+r+") exceeds the tradable's Original Volume ("+o+")");  
	
}

public InvalidVolumeException(int o,int r,int c)

{
	super("Request new Remaining Volume (" +r+") plus the Cancelled Volume ("+c+") exceeds the tradable's Original Volume ("+o+")");  
	}
	
public InvalidVolumeException(int price, String s)
{
	super("Invalid SELL-Side Volume: -"+price);
	
}
public InvalidVolumeException( String s,int price)
{
	super("Invalid BUY-Side Volume: -"+price);
	
}
}
