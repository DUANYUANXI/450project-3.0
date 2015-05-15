package tradable;

/**
 * @author YU XI, XIAOYU YUAN, XINGYUE DUAN 
 * DATE: 04/20/2015
 * 
 */
public class InvalidValueException extends Exception{

	private static final long serialVersionUID = 1L;


	public InvalidValueException()
	{
		
			super("Invalid Order Volume: 0");
		
	}
}
