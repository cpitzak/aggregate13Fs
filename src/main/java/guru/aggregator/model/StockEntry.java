package guru.aggregator.model;

/**
 * Defines a stock entry.
 */
public class StockEntry {

	private String date;
	private String entryType;
	private float averagePrice;
	private float minimumPrice;
	private int numberOfShares;
	private StockEntry older;
	private StockEntry later;

	/**
	 * Constructs a stock entry.
	 * 
	 * @param date
	 *            the date to use
	 * @param entryType
	 *            the entry type to use
	 * @param averagePrice
	 *            the average price to use
	 * @param minimumPrice
	 *            the minimum price to use
	 * @param numberOfShares
	 *            the number of shares to use
	 */
	public StockEntry(String date, String entryType, float averagePrice,
			float minimumPrice, int numberOfShares) {
		this.date = date;
		this.entryType = entryType;
		this.averagePrice = averagePrice;
		this.minimumPrice = minimumPrice;
		this.numberOfShares = numberOfShares;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the entryType
	 */
	public String getEntryType() {
		return entryType;
	}

	/**
	 * @param entryType
	 *            the entryType to set
	 */
	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	/**
	 * @return the avgPrice
	 */
	public float getAveragePrice() {
		return averagePrice;
	}

	/**
	 * @param averagePrice
	 *            the avgPrice to set
	 */
	public void setAveragePrice(float averagePrice) {
		this.averagePrice = averagePrice;
	}

	/**
	 * @return the minPrice
	 */
	public float getMinimumPrice() {
		return minimumPrice;
	}

	/**
	 * @param minimumPrice
	 *            the minPrice to set
	 */
	public void setMinimumPrice(float minimumPrice) {
		this.minimumPrice = minimumPrice;
	}

	/**
	 * @return the numShares
	 */
	public int getNumberOfShares() {
		return numberOfShares;
	}

	/**
	 * @param numberOfShares
	 *            the numShares to set
	 */
	public void setNumberOfShares(int numberOfShares) {
		this.numberOfShares = numberOfShares;
	}

	/**
	 * @return the older
	 */
	public StockEntry getOlder() {
		return older;
	}

	/**
	 * @param older
	 *            the older to set
	 */
	public void setOlder(StockEntry older) {
		this.older = older;
	}

	/**
	 * @return the later
	 */
	public StockEntry getLater() {
		return later;
	}

	/**
	 * @param later
	 *            the later to set
	 */
	public void setLater(StockEntry later) {
		this.later = later;
	}

}
