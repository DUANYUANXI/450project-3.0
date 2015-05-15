package publisher;

public class AlreadySubscribedException extends Exception{
	
	private static final long serialVersionUID = 1L;


	public AlreadySubscribedException()
	{
		
			super("is already be subscribe");
		
	}

}
