package priceFactory;


/**
 * @author YU XI, XIAOYU YUAN, XINGYUE DUAN 
 * DATE: 04/20/2015
 * 
 */
public class LimitPrice extends Price {

	private long value;
	
	LimitPrice(long valueln) {
	
	this.value=valueln;
	}

	
	

	public long getValue() 
	{
			return value;

	}

	public Price add(Price p) throws InvalidPriceOperation 
	{
	
		if(p!=null)
			{
				if(!p.isMarket())
					return PriceFactory.makeLimitPrice(value+p.getValue());
				
				else if(p.isMarket())
						throw new InvalidPriceOperation("Cannot add a MARKET price to a LIMIT Price: ");	
					
			} 
		
	else 
	{
		
		throw new InvalidPriceOperation("Cannot add a NULL price to a LIMIT Price: ");
		
	}
		return null;
	}
	
	
	public Price subtract(Price p) throws InvalidPriceOperation 
	{
	
		if(p!=null)
			{
				if(!p.isMarket())
					return PriceFactory.makeLimitPrice(value-p.getValue());
				else{
					if(p.isMarket())
						throw new InvalidPriceOperation("Cannot subtract a MARKET price from a LIMIT Price: ");	
					
					
				
					} 
			} 
		
	else 
	{
		
		throw new InvalidPriceOperation("Cannot subtract a NULL price from a LIMIT Price: ");
		
	}
		return null;
	}
	
	public Price multiply(int p) throws InvalidPriceOperation 
	{
		return PriceFactory.makeLimitPrice(value*p);
	}
	
	public int compareTo(Price p) 

	{
		
		if(p!=null&&(p.isMarket()))
			throw new ClassCastException("Cannot compare a MARKET price to a LIMIT Price: ");
		else if(p==null)
			throw new NullPointerException("Cannot compare a NULL price to a LIMIT Price: ");
		
		else {
			
		if(p.getValue()<value)
			return-1;
		else if(p.getValue()==value)
			return 0;
		else if(p.getValue()>value)
			return 1;
		}
		return 0;
	}
	public boolean greaterOrEqual(Price p)
	{
		
		if(p==null||p.isMarket())
			return false;
		if(value>=p.getValue()) return true;
		else  return false;
			
	}
	public boolean greaterThan(Price p) 
	{

	
		if(p==null||p.isMarket())
			return false;
		 if(value>p.getValue()) return true;
		else  return false;
	}
	
	public boolean lessOrEqual(Price p)  
	{

		if(p==null||p.isMarket())
		return false;
	 if(value<=p.getValue()) return true;

		else return false;
	}
	public boolean lessThan(Price p)
	{
	
		if(p==null||p.isMarket())
			return false;
		 if(value<p.getValue()) return true;
		
		else return false;	
}
	public boolean equals(Price p)
	{
		
		if(p==null||p.isMarket())
			return false;
		 if(value==p.getValue()) return true;
			else return false;		
	}
	public boolean isMarket()
	{
		
		return false;
	}
	public boolean isNegative()
	{
		
		if(value<0)return true;
		else return false;
	}
	
	public String toString(){
		
			Long payment = this.value;
			String output=String.format("$%,.2f",payment/100.0);
			return output;

		}
	}
 