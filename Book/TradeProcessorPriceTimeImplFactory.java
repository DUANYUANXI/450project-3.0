package Book;


/**
 * 
 * @author xiaoyu yuan, Xingyue Duan, Yu Xi
 * 05/21/2015
 */



public class TradeProcessorPriceTimeImplFactory {

	public static TradeProcessorPriceTimeImpl MakeTradeProcessorPriceTimeImpl(String algorithms,ProductBookSide productBookSideIn)
	{
		if(algorithms.equals("time"))
		return new TradeProcessorPriceTimeImpl(productBookSideIn);
		else return null;
		
		
	}
}
