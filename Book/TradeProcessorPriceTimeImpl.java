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
		String key=makeFillKey(fm);	
		if(!fillMessages.containsKey(key)) return true;
		FillMessage oldFill=fillMessages.get(key);
		if(!oldFill.equals(fm)) return true;
		if(!oldFill.getId().equals(fm.getId())) return true;
		else return false;
		
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
		for(int i=0;i<entriesAtPrice.size();i++)
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
			else if(trd.getRemainingVolume()!=0)
			{
				if(trd.getRemainingVolume()>=entriesAtPrice.get(i).getRemainingVolume())
				{
					tradeOut.add(entriesAtPrice.get(i));
					if(entriesAtPrice.get(i).getPrice().isMarket())
						tPrice=trd.getPrice();//????
					else tPrice=entriesAtPrice.get(i).getPrice();//??
				
					FillMessage tfm=new FillMessage(entriesAtPrice.get(i).getUser(),entriesAtPrice.get(i).getProduct(),
							tPrice,entriesAtPrice.get(i).getRemainingVolume(),"Leaving 0",entriesAtPrice.get(i).
							getSide(),entriesAtPrice.get(i).getId());
					addFillMessage(tfm);
					FillMessage trdfm=new FillMessage(trd.getUser(),trd.getProduct(),
							tPrice,entriesAtPrice.get(i).getRemainingVolume(),"Leaving"+(trd.getRemainingVolume()-entriesAtPrice.get(i).getRemainingVolume()),trd.
							getSide(),trd.getId());
					addFillMessage(trdfm);
					trd.setRemainingVolume(trd.getRemainingVolume()-entriesAtPrice.get(i).getRemainingVolume());
					entriesAtPrice.get(i).setRemainingVolume(0);
					//productBookSide.removeTradable(entriesAtPrice.get(i));
					//entriesAtPrice.remove(i);
					
					productBookSide.addOldEntry(entriesAtPrice.get(i));
					
				}
				else 
				{
					int remainder=entriesAtPrice.get(i).getRemainingVolume()-trd.getRemainingVolume();
					if(entriesAtPrice.get(i).getPrice().isMarket())
						tPrice=entriesAtPrice.get(i).getPrice();//???
					else tPrice=trd.getPrice();//??
					FillMessage tfm=new FillMessage(entriesAtPrice.get(i).getUser(),entriesAtPrice.get(i).getProduct(),
							tPrice,trd.getRemainingVolume(),"Leaving"+remainder,entriesAtPrice.get(i).
							getSide(),entriesAtPrice.get(i).getId());
					addFillMessage(tfm);
					FillMessage trdfm=new FillMessage(trd.getUser(),trd.getProduct(),
					tPrice,trd.getRemainingVolume(),"Leaving 0",trd.
							getSide(),trd.getId());
					addFillMessage(trdfm);
					trd.setRemainingVolume(0);
					entriesAtPrice.get(i).setRemainingVolume(remainder);
					productBookSide.addOldEntry(trd);
						
					
				}
				
				
			}
		}
		
		
		for(int j=0;j<tradeOut.size();j++)
		{
			
			if(entriesAtPrice.contains(tradeOut.get(j)))
				entriesAtPrice.remove(tradeOut.get(j));
		}
		if(entriesAtPrice.isEmpty())

		{
			productBookSide.clearIfEmpty(productBookSide.topOfBookPrice());	
		}
		return fillMessages;
	}

	
	
	
	
	
	
}
