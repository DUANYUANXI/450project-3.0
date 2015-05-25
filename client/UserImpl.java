package client;

import java.sql.Timestamp;
import java.util.ArrayList;

import Book.DataValidationException;
import Book.InvalidMarketStateException;
import Book.NoSuchProductException;
import Book.OrderNotFoundException;
import message.CancelMessage;
import message.FillMessage;
import message.InvalidInputException;
import priceFactory.InvalidPriceOperation;
import priceFactory.Price;
import publisher.AlreadySubscribedException;
import publisher.NoSubscribeException;
import tradable.InvalidValueException;
import tradable.InvalidVolumeException;
import tradable.TradableDTO;
import gui.UserDisplayManager;

public class UserImpl implements User {
	private String userName;
	private long connectedId;
	private ArrayList<String> stockList;
	private ArrayList<TradableUserData> submittedOrders;
	private Position userValue;
	private UserDisplayManager display;
	
	public UserImpl(String userName){
		this.userName = userName;
		userValue = new Position();
	}
	
	@Override
	public String getUserName(){
		return this.userName;
	}
	
	@Override
	public void acceptMarketMessage(String message){
		try{
		display.updateMarketState(message);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void acceptTicker(String product, Price price, char direction){
		try{
		display.updateTicker(product, price, direction);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void acceptCurrentMarket(String product, Price bprice, int bVolume,Price sPrice, int sVolume){
		try{
			display.updateMarketData(product, bprice, bVolume, sPrice, sVolume);
			} catch(Exception e){
				e.printStackTrace();
			}
	}
	
	
	public static void main(String[] args){
		Timestamp timeSt = new Timestamp(System.currentTimeMillis());
		System.out.println(timeSt.toString());
	}

	@Override
	public void acceptLastSale(String product, Price p, int v) {
		try{
			display.updateLastSale(product, p, v);
			userValue.updateLastSale(product, p);
			}
			catch( Exception e){
				System.err.println(e.getMessage());
			}
	}

	@Override
	public void acceptMessage(FillMessage fm) {
		try {
			Timestamp timeSt = new Timestamp(System.currentTimeMillis());
			String summary = " { " + timeSt + " } " + " Fill Message: " + fm.getSide()+ " " + fm.getVolume() + " " + fm.getProduct() + " at " + fm.getProduct() + " " + fm.getPrice().toString()
			+ " leaving " + " [ " +  fm.getId() + " ]";
			display.updateMarketActivity(summary);
			userValue.updatePositon(fm.getProduct(), fm.getPrice(), fm.getSide(), fm.getVolume());
		} catch (InvalidPriceOperation e) {
			e.printStackTrace();
		}
	}

	@Override
	public void acceptMessage(CancelMessage cm) {
		try{
			Timestamp timeSt = new Timestamp(System.currentTimeMillis());
			String summary = " { " + timeSt + " } " + " Fill Message: " + cm.getSide()+ " " + cm.getVolume() + " " + cm.getProduct() + " at " + cm.getProduct() + " " + cm.getPrice().toString()
					+ " leaving " + " [ " +  cm.getId() + " ]";
			display.updateMarketActivity(summary);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void connect() throws AlreadyConnectedException, UserNotConnectedException, InvalidConnectionIdExcpetion{
		UserCommandService.getInstance().connect(this);
		//stockList = UserCommandService.getInstance().getProducts(userName, connectedId);
	}
	
	@Override
	public void disConnect() throws UserNotConnectedException, InvalidConnectionIdExcpetion {
		UserCommandService.getInstance().disConnect(userName, connectedId);
	}

	@Override
	public void showMarketDisplay() throws Exception {
		if(stockList == null)
			throw new UserNotConnectedException();
		else if(display ==null)
			display = new UserDisplayManager(this);
		display.showMarketDisplay();
	}
   @override
	public String submitOrder(String Product, Price price, int volume,
			String side) throws InvalidValueException, UserNotConnectedException, InvalidConnectionIdExcpetion, InvalidMarketStateException, NoSubscribeException, InvalidInputException, NoSuchProductException, InvalidVolumeException  {
		String id =UserCommandService.getInstance().submitOrder(userName, connectedId, Product, price, volume, side);
		TradableUserData newData = new TradableUserData(userName, Product, side, id);
		submittedOrders.add(newData);
		return id;
	}

	@Override
	public void submitOrderCancel(String product, String side, String orderId) throws UserNotConnectedException, InvalidConnectionIdExcpetion, InvalidMarketStateException, NoSuchProductException, InvalidInputException, NoSubscribeException, OrderNotFoundException, InvalidVolumeException {
		UserCommandService.getInstance().submitOrderCancel(userName, connectedId, product, side, orderId);
		
	}

	@Override
	public void submitQuote(String product, Price buyPrice, int buyVolume,
			Price sellPrice, int sellVolume) throws UserNotConnectedException, InvalidConnectionIdExcpetion, InvalidVolumeException, InvalidValueException, InvalidMarketStateException, NoSuchProductException, DataValidationException, NoSubscribeException, InvalidInputException {
		UserCommandService.getInstance().submitQuote(userName, connectedId, product, buyPrice, buyVolume, sellPrice, sellVolume);
		
		
	}

	@Override
	public void submitQuoteCancel(String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, InvalidInputException, NoSubscribeException, InvalidMarketStateException, NoSuchProductException {
		
		UserCommandService.getInstance().submitQuoteCancel(userName, connectedId, product);
	}

	@Override
	public void subscribeCurrentMarket(String product) throws AlreadySubscribedException, UserNotConnectedException, InvalidConnectionIdExcpetion {
		
		UserCommandService.getInstance().subscribeCurrentMarket(userName, connectedId, product);
	}

	@Override
	public void subscribeLastSale(String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, AlreadySubscribedException {
		
		UserCommandService.getInstance().subscribeLastSale(userName, connectedId, product);
	}

	@Override
	public void subscribeMessages(String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, AlreadySubscribedException {
		UserCommandService.getInstance().subscribeMessages(userName, connectedId, product);
		
	}

	@Override
	public void subscribeTicker(String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, AlreadySubscribedException {
		
		UserCommandService.getInstance().subscribeTicker(userName, connectedId, product);
	}

	@Override
	public Price getAllStockValue() {
		return userValue.getAllStockValues();
		
	}

	@Override
	public Price getAccountCosts() {
		
		return userValue.getAccountCosts();
	}

	@Override
	public Price getNetAccountValue() throws InvalidPriceOperation {
		
		return userValue.getNetAccountValue();
	}

	@Override
	public String[][] getBookDepth(String product) throws NoSuchProductException, UserNotConnectedException, InvalidConnectionIdExcpetion {
	return	UserCommandService.getInstance().getBookDepth(userName, connectedId, product);
		
	}

	@Override
	public String getMarketState() throws UserNotConnectedException, InvalidConnectionIdExcpetion {
		 return UserCommandService.getInstance().getMarketState(userName, connectedId);
		
	}

	@Override
	public ArrayList<TradableUserData> getOrderIds() {
		
	
		return submittedOrders;
	}

	@Override
	public ArrayList<String> getProductList() {
		
		
		return stockList;
	}

	@Override
	public Price getStockPositionValue(String product) throws InvalidPriceOperation {
		
		return userValue.getStockPositionValue(product);
	}

	@Override
	public int getStockPositionVolume(String product) {
	
		return userValue.getStockPositionVolume(product);
	}

	@Override
	public ArrayList<String> getHoldings() {
		
		return userValue.getHoldings();
	}

	@Override
	public ArrayList<TradableDTO> getOrdersWithRemainingQty(String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion {
		
		return UserCommandService.getInstance().getOrdersWithRemainingQty(userName, connectedId, product);
	}
}
