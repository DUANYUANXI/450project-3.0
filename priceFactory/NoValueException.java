package priceFactory;

public class NoValueException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 public NoValueException()
	{
		super("Error: the price is invalid to operate.");
	}
	}
