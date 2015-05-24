package Book;

/**
 * 
 * @author xiaoyu yuan, Xingyue Duan, Yu Xi
 * 05/21/2015
 */


public class NoSuchProductException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchProductException(String msg)
	{
		super(msg);
	}
}
