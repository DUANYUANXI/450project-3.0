package tradable;

import priceFactory.Price;

/**
 * @author YU XI, XIAOYU YUAN, XINGYUE DUAN 
 * DATE: 04/20/2015
 * 
 */	

/**
 * This is an interface class
 *
 */
public interface Tradable  {

	/**
	 * This is get product symbol method declaration 
	 * @return product symbol
	 */
	public String getProduct();
	
	/**
	 * This is get Price object method declaration
	 * @return Price Object
	 */
	public Price getPrice();
	
	/**
	 * This is get OriginalVolume method declaration
	 * @return int origianlVolume
	 */
	public int getOriginalVolume();
	/**
	 * This is a get remainingVolume method declaration
	 * @return int remainingVolume
	 */
	public int getRemainingVolume();
	
	/**
	 * This is a get cancelledVolume method declaration
	 * @return int cancelledVolume
	 */
	public int getCancelledVolume();
	
	/**
	 * This is a set cancelledVolume method declaration
	 * @param newCancelledVolume 
	 * @throws InvalidVolumeException
	 */
	public void  setCancelledVolume(int newCancelledVolume) throws InvalidVolumeException;
	/**
	 * This is a set remainingVolume method declaration
	 * @param newRemainingVolume
	 * @throws InvalidVolumeException
	 */
	public void setRemainingVolume(int newRemainingVolume) throws  InvalidVolumeException;
	/**
	 * This is a method declaration of getting user name 
	 * @return String username
	 */
	public String getUser();
	/**
	 *  This is a method declaration of getting side 
	 * @return String side
	 */
	public String getSide();
	/**
	 * This is a method declaration of getting isQuote boolean
	 * @return boolean isQuote
	 */
	public boolean isQuote();
	/**
	 * This  is a method declaration of getting id
	 * @return String id
	 */
	public String getId();

	public void setPrice(Price price);

}