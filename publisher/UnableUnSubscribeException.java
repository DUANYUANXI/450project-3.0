package publisher;

public class UnableUnSubscribeException extends Exception {
private static final long serialVersionUID = 1L;
	
	public UnableUnSubscribeException()
	{
		
			super("It hasn't been subscribed. So it's unable to unsubscribe it");
}

}
