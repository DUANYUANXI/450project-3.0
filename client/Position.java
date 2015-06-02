package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import priceFactory.InvalidPriceOperation;
import priceFactory.Price;
import priceFactory.PriceFactory;

public class Position {
	private HashMap<String,Integer> holdings;
	private Price accountCosts;
	private HashMap<String, Price> lastSale;
	
	public Position(){
		holdings = new HashMap<String,Integer>();
		accountCosts = PriceFactory.makeLimitPrice(0);
		lastSale = new HashMap<String,Price>();
	}
	
	public void updatePositon(String Product,Price price, String side, int volume) throws InvalidPriceOperation{
		int adjustedVolume = 0;
		if(side.equals("BUY")){
			adjustedVolume = volume;
		}
		else if (side.equals("SELL")){
			adjustedVolume = -volume;
		}
		
		if(!holdings.containsValue(Product)){
			holdings.put(Product, adjustedVolume);
		}
		else{
				int newVolume = holdings.get(Product) + adjustedVolume;
				if(newVolume == 0)
					holdings.remove(Product);
				else
					holdings.replace(Product, newVolume);
		}
		Price totalPrice = PriceFactory.makeLimitPrice(volume*price.getValue());
		if(side.equals("BUY")){
			accountCosts = accountCosts.subtract(totalPrice);
		}
		else
			accountCosts = accountCosts.add(totalPrice);
	}
	
	public void updateLastSale(String product, Price price){
		lastSale.put(product, price);
	}
	
	public int getStockPositionVolume(String product){
		if(!holdings.containsKey(product))
			return 0;
		else
			return holdings.get(product);
	}
	
	public ArrayList<String> getHoldings(){
		ArrayList<String> h = new ArrayList<String>(holdings.keySet());
		Collections.sort(h);
		return h;
	}
	
	public Price getStockPositionValue(String product) throws InvalidPriceOperation{
		if(!holdings.containsKey(product))
			return PriceFactory.makeLimitPrice("$0.00");
		Price temp = lastSale.get(product);
		if(temp == null)
			temp = PriceFactory.makeLimitPrice("$0.00");
		return PriceFactory.makeLimitPrice(temp.multiply(holdings.get(product)).getValue());
	}
	
	public Price getAccountCosts(){
		return PriceFactory.makeLimitPrice(accountCosts.getValue());
	}
	
	public Price getAllStockValues() throws InvalidPriceOperation{
		Price totalvalue = PriceFactory.makeLimitPrice(0);
		for (String s :holdings.keySet()){
			totalvalue = totalvalue.add(getStockPositionValue(s));
		}
		return totalvalue;
	}
	
	public Price getNetAccountValue() throws InvalidPriceOperation{
		return PriceFactory.makeLimitPrice(this.getAllStockValues().add(accountCosts).getValue());
	}
	
	
}
