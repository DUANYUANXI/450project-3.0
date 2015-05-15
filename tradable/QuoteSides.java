package tradable;
import priceFactory.Price;

/**
 * @author YU XI, XIAOYU YUAN, XINGYUE DUAN 
 * DATE: 04/20/2015
 * 
 */

/**
 * QuoteSides is a class implements Tradable Interface
 * 
 */
public class QuoteSides  implements Tradable{


		private String id;
		private Tradable delegate;
		private boolean isQuote;
		
		/**
		 * This is the 	QuoteSides constructor 
		 * @param userName 
		 * @param productSymbol
		 * @param price
		 * @param originalVolume
		 * @param side, "BUY" or "SELL"
		 * @throws InvalidValueException
		 */
		public QuoteSides(String userName, String productSymbol, Price price, int originalVolume,String
				side) throws InvalidValueException 
		{
			isQuote=true;
		this.id=userName+productSymbol+Long.toString(System.nanoTime());
		delegate=ImplFactory.makeTradableImpl("tradable",userName,productSymbol,price,originalVolume,
				side,isQuote,id);
	
		}
		
		public  QuoteSides (QuoteSides qs) throws InvalidValueException{
			isQuote=true;
			this.id=qs.getUser()+qs.getProduct()+Long.toString(System.nanoTime());
			delegate=ImplFactory.makeTradableImpl("tradable",qs.getUser(),qs.getProduct(),qs.getPrice(),qs.getOriginalVolume(),qs.getSide(),qs.isQuote(),id);
			
		}
		
		/**
		 * This is a function getting the product symbol 
		 * @return String product symbol
		 */
		public String getProduct()
		{
			return delegate.getProduct();
		}
		/**
		 * This is a function getting the quote price
		 * @return Price object
		 */
		public Price getPrice()
		{
			return delegate.getPrice();
		}
		/**
		 * This is the function getting the original volume
		 * @return String originalVolume
		 */
		public int getOriginalVolume()
		{
			return delegate.getOriginalVolume();
			
		}
		/**
		 * This is the function getting the remaining volume
		 * @return String remainingVolume
		 */
		public int getRemainingVolume()
		{
			return delegate.getRemainingVolume();
			
		}
		/**
		 * This is the function getting the cancelled volume
		 * @return String cancelledVolume
		 */
		public int getCancelledVolume()
		{
			return delegate.getCancelledVolume();
		}
		
		
		/**
		 * This is the method setting the cancelled volume 
		 * @exception InvalidVolumeException throw exception if the volume is invalid
		 * @param newCancelledVolume 
		 */
		public void  setCancelledVolume(int newCancelledVolume) throws InvalidVolumeException
		{
			delegate.setCancelledVolume(newCancelledVolume);
		}
		
		/**
		 * This is the method setting the remaining volume 
		 * @exception InvalidVolumeException throw exception if the volume is invalid
		 * @param newRemainingVolume 
		 * 
		 */
		public void setRemainingVolume(int newRemainingVolume) throws InvalidVolumeException
		{
			delegate.setRemainingVolume(newRemainingVolume);;
		}
		
		/**
		 * This is the method getting the user name
		 * @return String user name
		 * 
		 */
		public String getUser()
		{
			return delegate.getUser();
		}
		/**
		 * This is the method getting the side string 
		 * @return String side
		 */
		public String getSide()
		{
			return delegate.getSide();
		}
		/**
		 * This is the method getting a boolean of if is quote
		 * @return boolean
		 */
		public boolean isQuote()
		{
			return isQuote;
		}
		
		/**
		 *  This is a method to get user ID
		 *  @return id
		 */
		public String getId()
		{
			return id;
		}
		/**
		 * This is toString method
		 * @return String
		 */
		public String toString()
		{
			String outPut=delegate.toString();
			return outPut;
		}

		@Override
		public void setPrice(Price price) {
			delegate.setPrice(price);
			
		}
}
