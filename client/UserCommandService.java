package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import message.InvalidInputException;
import priceFactory.Price;
import publisher.AlreadySubscribedException;
import publisher.CurrentMarketPublisher;
import publisher.LastSalePublisher;
import publisher.MessagePublisher;
import publisher.NoSubscribeException;
import publisher.TickerPublisher;
import publisher.UnableUnSubscribeException;
import tradable.*;
import Book.DataValidationException;
import Book.InvalidMarketStateException;
import Book.NoSuchProductException;
import Book.OrderNotFoundException;
import Book.ProductService;

public class UserCommandService {
	private volatile static UserCommandService instance;
	private HashMap<String,Long> connectedUserIds;
	private HashMap<String, User> connectedUsers;
	private HashMap<String, Long> connectedTime;
	
	private UserCommandService(){
		this.connectedUserIds = new HashMap<String,Long>();
		this.connectedUsers = new HashMap<String,User>();
		this.connectedTime = new HashMap<String, Long>();
	}
	
	public static UserCommandService getInstance(){
		if(instance == null)
			synchronized (UserCommandService.class){
				if(instance == null)
					instance= new UserCommandService();
			}
		return instance;
	}
	
	private void verifyUser(String userName,Long connld) throws UserNotConnectedException, InvalidConnectionIdExcpetion{
		if(!connectedUserIds.containsKey(userName))
			throw new UserNotConnectedException();
		else if(this.connectedUserIds.get(userName)!=connld)
			throw new InvalidConnectionIdExcpetion();
	}
	
	public synchronized long connect(User user) throws AlreadyConnectedException{
		if(this.connectedUserIds.containsKey(user.getUserName()))
			throw new AlreadyConnectedException();
		this.connectedUserIds.put(user.getUserName(), System.nanoTime());
		this.connectedUsers.put(user.getUserName(),user);
		this.connectedTime.put(user.getUserName(), System.currentTimeMillis());
		return this.connectedUserIds.get(user.getUserName());
	}
	
	public synchronized void disConnect(String userName,long connld) throws UserNotConnectedException, InvalidConnectionIdExcpetion{
		this.verifyUser(userName, connld);
		this.connectedUserIds.remove(userName);
		this.connectedUsers.remove(userName);
		this.connectedTime.remove(userName);
	}
	
	public synchronized String[][] getBookDepth(String userName,long connld,String product) throws NoSuchProductException, UserNotConnectedException, InvalidConnectionIdExcpetion{
		this.verifyUser(userName, connld);
		return ProductService.getInstance().getBookDepth(product);
	}
	public synchronized String  getMarketState(String userName,long connld) throws UserNotConnectedException, InvalidConnectionIdExcpetion{
		this.verifyUser(userName, connld);
		return ProductService.getInstance().getMarketState().toLowerCase();
	}
	
	public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName,long connld, String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion{
		this.verifyUser(userName, connld);
		return ProductService.getInstance().getOrdersWithRemainingQty(userName, product);
	}
	
	public ArrayList<String> getProducts(String userName,long connld) throws UserNotConnectedException, InvalidConnectionIdExcpetion{
		this.verifyUser(userName, connld);
		ArrayList<String> str = ProductService.getInstance().getProductList();
		Collections.sort(str);
		return str;
	}
	/**
	 * 
	 * @param userName
	 * @param connld
	 * @param product
	 * @param price
	 * @param volume
	 * @param side
	 * @return
	 * @throws InvalidValueException
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdExcpetion
	 * @throws InvalidMarketStateException
	 * @throws NoSubscribeException
	 * @throws InvalidInputException
	 * @throws NoSuchProductException
	 * @throws InvalidVolumeException
	 */
	public String submitOrder(String userName,long connld,String product,Price price,int volume,String side) throws InvalidValueException, UserNotConnectedException, InvalidConnectionIdExcpetion, InvalidMarketStateException, NoSubscribeException, InvalidInputException, NoSuchProductException, InvalidVolumeException{
		this.verifyUser(userName, connld);
		Order newOrder = new Order(userName,product,price,volume,side);
		return ProductService.getInstance().submitOrder(newOrder);
	}
	
	/**
	 * 
	 * @param userName
	 * @param connld
	 * @param product
	 * @param side
	 * @param orderId
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdExcpetion
	 * @throws InvalidMarketStateException
	 * @throws NoSuchProductException
	 * @throws InvalidInputException
	 * @throws NoSubscribeException
	 * @throws OrderNotFoundException
	 * @throws InvalidVolumeException
	 */
	
	public void submitOrderCancel(String userName,long connld,String product,String side,String orderId) throws UserNotConnectedException, InvalidConnectionIdExcpetion, InvalidMarketStateException, NoSuchProductException, InvalidInputException, NoSubscribeException, OrderNotFoundException, InvalidVolumeException{
		this.verifyUser(userName, connld);
		ProductService.getInstance().submitOrderCancel(product, side, orderId);
	}
	
	
	/**
	 * 
	 * @param userName
	 * @param connld
	 * @param product
	 * @param bPrice
	 * @param bVolume
	 * @param sPrice
	 * @param sVolume
	 * @throws UserNotConnectedException
	 * @throws InvalidConnectionIdExcpetion
	 * @throws InvalidVolumeException
	 * @throws InvalidValueException
	 * @throws InvalidMarketStateException
	 * @throws NoSuchProductException
	 * @throws DataValidationException
	 * @throws NoSubscribeException
	 * @throws InvalidInputException
	 */
	public void submitQuote(String userName,long connld, String product,Price bPrice,int bVolume, Price sPrice,int sVolume) throws UserNotConnectedException, InvalidConnectionIdExcpetion, InvalidVolumeException, InvalidValueException, InvalidMarketStateException, NoSuchProductException, DataValidationException, NoSubscribeException, InvalidInputException{
		this.verifyUser(userName, connld);
		Quote newQuote = new Quote(userName,product, bPrice, bVolume, sPrice, sVolume);
		ProductService.getInstance().submitQuote(newQuote);
	}
	
	public void submitQuoteCancel(String userName,long connld,String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, InvalidInputException, NoSubscribeException, InvalidMarketStateException, NoSuchProductException{
		this.verifyUser(userName, connld);
		ProductService.getInstance().submitQuoteCancel(userName, product);
	}
	
	public void subscribeCurrentMarket(String userName,long connld,String product) throws AlreadySubscribedException, UserNotConnectedException, InvalidConnectionIdExcpetion{
		this.verifyUser(userName, connld);
		CurrentMarketPublisher.getInstance().subscribe(connectedUsers.get(userName), product);
	}
	
	public void subscribeLastSale(String userName,long connld,String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, AlreadySubscribedException{
		this.verifyUser(userName, connld);
		LastSalePublisher.getInstance().subscribe(connectedUsers.get(userName), product);
	}
	
	public void subscribeMessages (String userName,long conn,String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, AlreadySubscribedException{
		this.verifyUser(userName, conn);
		MessagePublisher.getInstance().subscribe(connectedUsers.get(userName), product);
	}
	
	public void subscribeTicker(String userName,long connld,String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, AlreadySubscribedException{
		this.verifyUser(userName, connld);
		TickerPublisher.getInstance().subscribe(connectedUsers.get(userName), product);
	}
	
	public void unSubscribeCurrentMarket(String userName,long conn,String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, UnableUnSubscribeException{
		this.verifyUser(userName, conn);
		CurrentMarketPublisher.getInstance().unSubscribe(connectedUsers.get(userName), product);
	}
	
	public void unSubscribeLastSale(String userName,long connld,String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, UnableUnSubscribeException{
		this.verifyUser(userName, connld);
		LastSalePublisher.getInstance().unSubscribe(connectedUsers.get(userName), product);
	}
	
	public void unSubscribeMessages (String userName,long conn,String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, UnableUnSubscribeException{
		this.verifyUser(userName, conn);
		MessagePublisher.getInstance().unSubscribe(connectedUsers.get(userName), product);
	}
	
	public void unSubscribeTicker(String userName,long connld,String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, UnableUnSubscribeException{
		this.verifyUser(userName, connld);
		TickerPublisher.getInstance().unSubscribe(connectedUsers.get(userName), product);
	}
	
	
}
