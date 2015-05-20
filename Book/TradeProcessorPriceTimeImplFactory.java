package Book;



public class TradeProcessorPriceTimeImplFactory {

	public static TradeProcessorPriceTimeImpl MakeTradeProcessorPriceTimeImpl(String algorithms,ProductBookSide productBookSideIn)
	{
		if(algorithms.equals("time"))
		return new TradeProcessorPriceTimeImpl(productBookSideIn);
		else return null;
		
		
	}
}
