package tradable;



import priceFactory.Price;
import priceFactory.PriceFactory;

/**
 * @author YU XI, XIAOYU YUAN, XINGYUE DUAN 
 * DATE: 04/20/2015
 * 
 */

/**
 * This is a class delegate part of function of Order and QuoteSides class
 * 
 *
 */
public class TradableImpl implements Tradable {
	
	private String userName;
		private String productSymbol;
	private String id;
		private String side;
		private Price price;
		private String originalVolume;
		private String remainingVolume;
		private String cancelledVolume;
		@SuppressWarnings("unused")
		private TradableDTO dto;

		/**
		 * This is the constructor 
		 * @param userNameln
		 * @param productSymbolln
		 * @param priceln
		 * @param originalVolumeln
		 * @param bookSideln
		 * @param isQuoteln
		 * @param idln
		 * @throws InvalidValueException If originalVolume is 0, throw an exception.
		 */
		public TradableImpl(String userNameln, String productSymbolln, Price priceln, int originalVolumeln,  String
				bookSideln,boolean isQuoteln,String idln) throws InvalidValueException
		{
		
		if(originalVolumeln==0)throw new InvalidValueException();
		
		else if(originalVolumeln>0) this.originalVolume=Integer.toString(originalVolumeln);
		this.userName=userNameln;
		this.productSymbol=productSymbolln;
		this.id=idln;
		
		if(priceln.isMarket())
		this.price=PriceFactory.makeMarketPrice();	
		else if(!priceln.isMarket())
		this.price=PriceFactory.makeLimitPrice(priceln.getValue());	
		this.remainingVolume=this.originalVolume;
		
		this.side=bookSideln;
		this.cancelledVolume=Integer.toString(0);
		
		dto=new TradableDTO ( productSymbolln, priceln,originalVolumeln,Integer.parseInt(remainingVolume),Integer.parseInt(cancelledVolume),userNameln,bookSideln, isQuoteln, idln );
		}
		
		/**
		 * This is the method of getting product symbol
		 * @return String productSymbol
		 */
		public String getProduct()
		{
			return productSymbol;
		}
		/**
		 * This is the method of getting Price object
		 * @return Price object
		 */
		public Price getPrice()
		{
			return price;
		}
		/**
		 * This is the method of getting originalVolume
		 * @return int originalVolume
		 */
		public int getOriginalVolume()
		{
			return Integer.parseInt(originalVolume);
			
		}
		/**
		 * This is the method of getting remainingVolume 
		 * @return int remaining volume
		 */
		public int getRemainingVolume()
		{
			return Integer.parseInt(remainingVolume);
			
		}
		/**
		 * This is the method of getting cancelledVolume
		 * @return int cancelled volume
		 */
		public int getCancelledVolume()
		{
			return Integer.parseInt(cancelledVolume);
		}
		/**
		 * This is the method of setting new cancelled volume 
		 * @param newCancelledVolume
		 * @throws InvalidVolumeException 
		 */
		public void  setCancelledVolume(int newCancelledVolume) throws  InvalidVolumeException
		{
			if(newCancelledVolume<0||newCancelledVolume+Integer.parseInt(remainingVolume)>Integer.parseInt(originalVolume))
			throw new InvalidVolumeException(Integer.parseInt(originalVolume),Integer.parseInt(remainingVolume),newCancelledVolume,1);
			this.cancelledVolume=Integer.toString(newCancelledVolume);
		}
		/**
		 * This is the method of setting new remaining volume 
		 * @param newRemainingVolume
		 * @throws InvalidVolumeException
		 */
		public void setRemainingVolume(int newRemainingVolume) throws InvalidVolumeException
		{
			if(newRemainingVolume<0||newRemainingVolume+Integer.parseInt(cancelledVolume)>Integer.parseInt(originalVolume))
				throw new InvalidVolumeException(Integer.parseInt(originalVolume),newRemainingVolume,Integer.parseInt(cancelledVolume));
			this.remainingVolume=Integer.toString(newRemainingVolume);
		}
		/**
		 * This is the method of getting user name
		 * @return String user name 
		 */
		public String getUser()
		{
			return userName;
		}
		/**
		 * This is the method of getting side 
		 * @return String side 
		 */
		public String getSide()
		{
			return side;
		}
		
		/**
		 * This is the method of toString 
		 * @return String 
		 */
		public String toString()
		{
			String outPut=userName+" order: "+side+" "+remainingVolume+" "+productSymbol+" at "+price.toString()+
			" (Original Vol: "+originalVolume+", CXL'd Vol: "+cancelledVolume+
			" ), ID: "+id;
			return outPut;
		}

		@Override
		public boolean isQuote() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String getId() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setPrice(Price priceIn) {
		
			price=priceIn;
		}

}