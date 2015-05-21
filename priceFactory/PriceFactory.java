package priceFactory;

import java.util.Hashtable;

/**
 * @author YU XI, XIAOYU YUAN, XINGYUE DUAN 
 * DATE: 04/20/2015
 * 
 */
public class PriceFactory {

	private static Hashtable<Long,Price> priceHashTable=new Hashtable<Long,Price>();
	private static Price mkt=new MarketPrice();
	public static Price makeLimitPrice(String value)
	{
		Double d=Double.parseDouble(value.replaceAll("[$,]",""))*100.00;
		long valueln=Math.round(d);
		
		//long valueln=(long)((Double.parseDouble(value.replaceAll("[$,]","")))*100.00);
		Price p=addToHashTable(valueln);
		return p;
	}
	public static Price makeLimitPrice(long value)
	{
		Price p=addToHashTable(value);
		return p;
	}
	public static Price makeMarketPrice()
	{
		return mkt;
	}
	private static Price addToHashTable(long value)
	{
		if(!priceHashTable.containsKey(value))
		{
			Price p=new LimitPrice(value);
			priceHashTable.put(value,p);
			return p;
		}
		else {
			return priceHashTable.get(value); 
			}
	}
}
