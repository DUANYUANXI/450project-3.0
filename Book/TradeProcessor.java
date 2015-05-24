package Book;

import java.util.HashMap;

import tradable.InvalidVolumeException;
import tradable.Tradable;
import message.FillMessage;
import message.InvalidInputException;

/**
 * 
 * @author xiaoyu yuan, Xingyue Duan, Yu Xi
 * 05/21/2015
 */


public interface TradeProcessor {
	
	
	public HashMap<String, FillMessage> doTrade(Tradable trd) throws InvalidInputException, InvalidVolumeException;

}
