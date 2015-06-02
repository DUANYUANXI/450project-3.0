package client;


import java.util.ArrayList;

import Book.DataValidationException;
import Book.InvalidMarketStateException;
import Book.NoSuchProductException;
import Book.OrderNotFoundException;
import message.*;
import priceFactory.InvalidPriceOperation;
import priceFactory.Price;
import publisher.AlreadySubscribedException;
import publisher.NoSubscribeException;
import tradable.InvalidValueException;
import tradable.InvalidVolumeException;
import tradable.TradableDTO;

public interface User {

	public String getUserName();
	public void acceptLastSale(String product, Price p, int v);
	public void acceptMessage(FillMessage fm);
	public void acceptMessage(CancelMessage cm);
	public void acceptMarketMessage(String message);
	public void acceptTicker(String product, Price p, char direction);
	public void acceptCurrentMarket(String product, Price bp, int bv, Price sp, int sv);
	public void connect() throws AlreadyConnectedException, UserNotConnectedException, InvalidConnectionIdExcpetion;
	public void disConnect() throws UserNotConnectedException, InvalidConnectionIdExcpetion;
	public void showMarketDisplay() throws UserNotConnectedException, Exception;
	public String submitOrder(String product, Price price, int volume, String side) throws InvalidValueException, UserNotConnectedException, InvalidConnectionIdExcpetion, InvalidMarketStateException, NoSubscribeException, InvalidInputException, NoSuchProductException, InvalidVolumeException;
	public void submitOrderCancel(String product, String side, String orderId) throws UserNotConnectedException, InvalidConnectionIdExcpetion, InvalidMarketStateException, NoSuchProductException, InvalidInputException, NoSubscribeException, OrderNotFoundException, InvalidVolumeException;
	public void submitQuote(String product, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws UserNotConnectedException, InvalidConnectionIdExcpetion, InvalidVolumeException, InvalidValueException, InvalidMarketStateException, NoSuchProductException, DataValidationException, NoSubscribeException, InvalidInputException;
	public void submitQuoteCancel(String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, InvalidInputException, NoSubscribeException, InvalidMarketStateException, NoSuchProductException;
	public void subscribeCurrentMarket(String product) throws AlreadySubscribedException, UserNotConnectedException, InvalidConnectionIdExcpetion;
	public void subscribeLastSale(String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, AlreadySubscribedException;
	public void subscribeMessages(String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, AlreadySubscribedException;
	public void subscribeTicker(String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion, AlreadySubscribedException;
	public Price getAllStockValue() throws InvalidPriceOperation;
	public Price getAccountCosts();
	public Price getNetAccountValue() throws InvalidPriceOperation;
	public String[][] getBookDepth(String product) throws NoSuchProductException, UserNotConnectedException, InvalidConnectionIdExcpetion;
	public String getMarketState() throws UserNotConnectedException, InvalidConnectionIdExcpetion;
	public ArrayList<TradableUserData> getOrderIds();
	public ArrayList<String> getProductList();
	public Price getStockPositionValue(String sym) throws InvalidPriceOperation;
	public int getStockPositionVolume(String product);
	public ArrayList<String> getHoldings();
	public ArrayList<TradableDTO> getOrdersWithRemainingQty(String product) throws UserNotConnectedException, InvalidConnectionIdExcpetion;
}
