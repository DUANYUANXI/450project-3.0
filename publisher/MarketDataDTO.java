package publisher;

import priceFactory.Price;

public class MarketDataDTO {

	public String product;
	public Price buyPrice; 
    public int buyVolume; 
	public Price sellPrice; 
	public int sellVolume; 
	 
	public MarketDataDTO(String product, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume)
	{
		this.product=product;
		this.buyPrice=buyPrice;
		this.buyVolume=buyVolume;
		this.sellPrice=sellPrice;
		this.sellVolume=sellVolume;
	}
	
	public String toString()
	{
		String outPut="Product: "+product+", Buy Price: "+buyPrice.toString()+",Buy Volume: "+buyVolume+", sellPrice"+
				sellPrice+", sellVolume: "+sellVolume;
		return outPut;
	}
}
