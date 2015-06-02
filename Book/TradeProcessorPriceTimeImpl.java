package Book;

import java.util.ArrayList;
import java.util.HashMap;

import priceFactory.Price;
import message.FillMessage;
import message.InvalidInputException;
import tradable.InvalidVolumeException;
import tradable.Tradable;

/**
 * 
 * @author xiaoyu yuan, Xingyue Duan, Yu Xi
 * 05/21/2015
 */



public class TradeProcessorPriceTimeImpl implements TradeProcessor{


	private HashMap<String, FillMessage> fillMessages; 
	private ProductBookSide productBookSide;
	
	public TradeProcessorPriceTimeImpl(ProductBookSide productBookSideIn)
	{
		productBookSide=productBookSideIn;
	}
	
	private String makeFillKey(FillMessage fm)
	{
		String key=fm.getUser()+fm.getId()+fm.getPrice().toString();
		return key;

	}	
	
	private boolean isNewFill(FillMessage fm)
	{
		 String key = makeFillKey(fm);
	        if (!fillMessages.containsKey(key)) {
	            return true;
	        }
	        FillMessage oldFill = fillMessages.get(key);
	        if (oldFill.getSide() != fm.getSide())  return true;
	        
	        if (!oldFill.getId().equals(fm.getId()))  return true;
	        
	        return false;
	    }
		
	
	private void addFillMessage(FillMessage fm) throws InvalidInputException
	{
		if(isNewFill(fm))
		{
			String key=makeFillKey(fm);
			fillMessages.put(key, fm);
		}
		else 
		{
			String key=makeFillKey(fm);
			FillMessage newFM=fillMessages.get(key);
			newFM.setVolume(newFM.getVolume()+fm.getVolume());
			newFM.setDetail(fm.getDetails());
			
			
		}
	}
	
	public HashMap<String, FillMessage> doTrade(Tradable trd) throws InvalidInputException, InvalidVolumeException
	{
		fillMessages= new HashMap<String, FillMessage>(); 
		ArrayList<Tradable> tradeOut=new ArrayList<Tradable>();
		ArrayList<Tradable> entriesAtPrice=productBookSide.getEntriesAtTopOfBook();
		Price tPrice;
		for(Tradable t : entriesAtPrice)
		{
			
			if(trd.getRemainingVolume()==0)
			{
				for(Tradable x :tradeOut)
				{
				
					entriesAtPrice.remove(x);	
				}
				if(entriesAtPrice.isEmpty())
				{
					productBookSide.clearIfEmpty(productBookSide.topOfBookPrice());
				}
				return fillMessages;
				
			}
			else 
			{
				if(trd.getRemainingVolume()>=t.getRemainingVolume())
				{
					tradeOut.add(t);
					if(t.getPrice().isMarket())
						tPrice=trd.getPrice();//????
					else tPrice=t.getPrice();//??
				
					
					
					FillMessage tfm=new FillMessage(t.getUser(),t.getProduct(),
							tPrice,t.getRemainingVolume()," Leaving 0",t.getSide(),t.getId());
					addFillMessage(tfm);
					
					FillMessage trdfm=new FillMessage(trd.getUser(),trd.getProduct(),
							tPrice,t.getRemainingVolume()," Leaving "+(trd.getRemainingVolume()-t.getRemainingVolume()),trd.
							getSide(),trd.getId());
					addFillMessage(trdfm);
				
					trd.setRemainingVolume(trd.getRemainingVolume()-t.getRemainingVolume());
					t.setRemainingVolume(0);
					
					
					productBookSide.addOldEntry(t);
					
				}
				else 
				{
					int remainder=t.getRemainingVolume()-trd.getRemainingVolume();
					if(t.getPrice().isMarket())
						tPrice=trd.getPrice();
					else
						tPrice=t.getPrice();
					FillMessage tfm=new FillMessage(t.getUser(),t.getProduct(),tPrice,trd.getRemainingVolume()," Leaving  "+remainder,
							t.getSide(),t.getId());
					addFillMessage(tfm);
					FillMessage trdfm=new FillMessage(trd.getUser(),trd.getProduct(),
					tPrice,trd.getRemainingVolume()," Leaving 0",trd.
							getSide(),trd.getId());
					addFillMessage(trdfm);
					trd.setRemainingVolume(0);
					t.setRemainingVolume(remainder);
					productBookSide.addOldEntry(trd);
						
					
				}
				
				
			}
		}
		
		
		for(Tradable j:tradeOut)
		{
			
			if(entriesAtPrice.contains(j));
				entriesAtPrice.remove(j);
		}
		if(entriesAtPrice.isEmpty())

		{
			productBookSide.clearIfEmpty(productBookSide.topOfBookPrice());	
		}
		return fillMessages;
	}

	
	
	
	
	
	
}
