package message;

import tradable.InvalidValueException;

public class MarketMessage {
	
	
	private String state;
	
	public MarketMessage(String state) throws InvalidInputException{
		setState(state);
	}

	private void setState(String state) throws  InvalidInputException{
		String formated = state.toUpperCase().trim();
	
		if( formated.equals("CLOSED" ))
				{this.state = "CLOSED";}
		else if(formated.equals( "PREOPEN") )
				{this.state = "PREOPEN";}
		else if(formated.equals("OPEN")) 
		{ this.state = "OPEN";}
		else  throw new InvalidInputException("The state of Market stage is invalid");
		}
	
	
	public String getState() throws InvalidValueException{
		if(state.equals("CLOSED")) return "CLOSED";
		else if(state.equals("PREOPEN")) return "PREOPEN";
		else if(state.equals("OPEN")) return "OPEN";	
		else throw new InvalidValueException();
			
		}
	public String toString()
	{
		return state;
	}
	}

