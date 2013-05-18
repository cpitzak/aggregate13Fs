package guru.aggregator.model;

/**
 * Defines a ticker.
 */
public class Ticker {

	private String ticker;
	private float percent;

	private boolean buyFound;
	private TransactionHistory transactionHistory = new TransactionHistory();
	private float currentPrice = -1.0f;

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
	public float getCurrentPrice() {
		return currentPrice;
	}

	/**
	 * @param currentPrice
	 *            the currentPrice to set
	 */
	public void setCurrentPrice(float currentPrice) {
		this.currentPrice = currentPrice;
	}

}
