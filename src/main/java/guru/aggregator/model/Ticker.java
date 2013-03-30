package guru.aggregator.model;

/**
 * Defines a ticker.
 */
public class Ticker {

	private String ticker;
	private float percent;

	private boolean buyFound;
	private StockEntryDoubleLinkedList entryList = new StockEntryDoubleLinkedList();
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
	public String getTicker() {
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
	public StockEntryDoubleLinkedList getEntryList() {
		return entryList;
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
