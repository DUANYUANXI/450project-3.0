package Book;

import java.util.HashMap;

import tradable.InvalidVolumeException;
import tradable.Tradable;
import message.FillMessage;
import message.InvalidInputException;

public interface TradeProcessor {
	
	
	public HashMap<String, FillMessage> doTrade(Tradable trd) throws InvalidInputException, InvalidVolumeException;

}
