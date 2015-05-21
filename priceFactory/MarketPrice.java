package priceFactory;


/**
 * @author YU XI, XIAOYU YUAN, XINGYUE DUAN 
 * DATE: 04/20/2015
 * 
 */
public class MarketPrice extends Price{
	

     public MarketPrice()
	{
		super();
	}	 
	 
   	public Price subtract(Price p)throws InvalidPriceOperation 
		{
			throw new InvalidPriceOperation("Cannot subtract a LIMIT price from  MARKET price.");
			
		}
		
		public Price multiply(int p) throws InvalidPriceOperation 
		{
			
				throw new InvalidPriceOperation("Cannot multiply a MARKET price.");
				
			
		}
		public int compareTo(Price p) 

		{
			
				throw new ClassCastException("Cannot compare to a MARKET price.");
			
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
			return true;
		}
		public boolean isNegative()
		{
			 return false;
			
		}
		
		public String toString(){
			
				return "MKT";
		}
	}

