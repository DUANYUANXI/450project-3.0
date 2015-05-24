package client;

import java.sql.Timestamp;
import java.util.ArrayList;

import Book.InvalidMarketStateException;
import Book.NoSuchProductException;
import Book.OrderNotFoundException;
import message.CancelMessage;
import message.FillMessage;
import message.InvalidInputException;
import priceFactory.InvalidPriceOperation;
import priceFactory.Price;
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
		stockList = UserCommandService.getInstance().getProducts(userName, connectedId);
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

	@Override
	public String submitOrderCancel(String Product, Price price, int volume,
			String side) throws InvalidValueException, UserNotConnectedException, InvalidConnectionIdExcpetion, InvalidMarketStateException, NoSubscribeException, InvalidInputException, NoSuchProductException, InvalidVolumeException {
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
			Price sellPrice, int sellVolume) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitQuoteCancel(String product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribeCurrentMarket(String product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribeLastSale(String product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribeMessages(String product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribeTicker(String product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Price getAllStockValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Price getAccountCosts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Price getNetAccountValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[][] getBookDepth(String product) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMarketState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<TradableUserData> getOrderIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getProductList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Price getStockPositionValue(String sym) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStockPositionVolume(String product) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<String> getHoldings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<TradableDTO> getOrdersWithRemainingQty(String product) {
		// TODO Auto-generated method stub
		return null;
	}
}
