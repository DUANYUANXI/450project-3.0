package Book;

import message.InvalidInputException;
import message.Message;
import message.MessageImpl;
import priceFactory.Price;

public class TradeProcessorPriceTimeImplFactory {

	public static TradeProcessorPriceTimeImpl MakeTradeProcessorPriceTimeImpl(String algorithms,ProductBookSide productBookSideIn)
	{
		if(algorithms.equals("time"))
		return new TradeProcessorPriceTimeImpl(productBookSideIn);
		else return null;
		
		
	}
}
