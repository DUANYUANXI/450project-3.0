package priceFactory;




/**
 * @author YU XI, XIAOYU YUAN, XINGYUE DUAN 
 * DATE: 04/20/2015
 * 
 */
abstract public class Price implements Comparable<Price> {
	private long value;

	Price ()
	{

	
	}
	
	public long getValue() 
	{
		
			return value;

		
	}
	
	public Price add(Price p) throws InvalidPriceOperation 
	{
	
		
	return null;	
	}
	
	public Price subtract(Price p)throws InvalidPriceOperation 
	{
	
		
		return null;
		
	}
	
	public Price multiply(int p) throws InvalidPriceOperation 
	{
		return null;
	}
	
	
	
	public int compareTo(Price p) 

	{
		return 0;
	}
	
	public boolean greaterOrEqual(Price p)
	{
		
		return false;	
	}
	public boolean greaterThan(Price p) 
	{

		
		return false;	
	}
	public boolean lessOrEqual(Price p)  
	{
		return false;
	}
	public boolean lessThan(Price p)
	{
		return false;
}
	public boolean equals(Price p)
			{
		return false;
		}
	public boolean isMarket()
	{
		return false;
	}
	public boolean isNegative()
	{
		
		 return false;
	}
	
	public String toString(){
		return null;
	}



}