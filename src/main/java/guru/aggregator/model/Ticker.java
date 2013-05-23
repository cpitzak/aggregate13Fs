package guru.aggregator.model;

import com.mongodb.BasicDBObject;

/**
 * Defines a ticker.
 */
public class Ticker {

	private String ticker;
	private float percent;
	private boolean buyFound;
	private TransactionHistory transactionHistory = new TransactionHistory();
	public static final String PERCENT = "percent";
	public static final String BUY_FOUND = "buyFound";
	public static final String TICKER = "ticker";
	public static final String TRANSACTION_HISTORY = "transactionHistory";


	/**
	 * Constructs a ticker.
	 * 
	 * @param ticker
	 *            the ticker to use
	 * @param percent
	 *            the percent impact on portfolio
	 */
	public Ticker(String ticker, float percent) {
		this.ticker = ticker;
		this.percent = percent;
	}
	
	public Ticker(String ticker, float percent, TransactionHistory transactionHistory) {
		this(ticker, percent);
		this.transactionHistory = transactionHistory;
	}

	/**
	 * @return the ticker
	 */
	public String getTickerSymbol() {
		return ticker;
	}

	/**
	 * @param ticker
	 *            the ticker to set
	 */
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	/**
	 * @return the percent
	 */
	public float getPercent() {
		return percent;
	}

	/**
	 * @param percent
	 *            the percent to set
	 */
	public void setPercent(float percent) {
		this.percent = percent;
	}

	/**
	 * @return the buyFound
	 */
	public boolean isBuyFound() {
		return buyFound;
	}

	/**
	 * @param buyFound
	 *            the buyFound to set
	 */
	public void setBuyFound(boolean buyFound) {
		this.buyFound = buyFound;
	}

	/**
	 * @return the entryList
	 */
	public TransactionHistory getTransactionHistory() {
		return transactionHistory;
	}

	/**
	 * @return the currentPrice
	 */
	public String getCurrentPrice() {
		return "$" + StockQuote.getQuote(ticker);
	}

	public BasicDBObject getDatabaseObject() {
		BasicDBObject document = new BasicDBObject(TICKER, ticker)
				.append(PERCENT, Float.toString(percent)).append(BUY_FOUND, buyFound)
				.append(TRANSACTION_HISTORY, transactionHistory.getDatabaseObject());
		return document;
	}

}
