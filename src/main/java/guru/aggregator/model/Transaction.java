package guru.aggregator.model;

import com.mongodb.BasicDBObject;

/**
 * Defines a transaction.
 */
public class Transaction {

	private String date;
	private String entryType;
	private Money averagePrice = new Money("0.00");
	private Money minimumPrice = new Money("0.00");
	private int numberOfShares;
	public static final String MIN = "min";
	public static final String AVG = "avg";
	public static final String DATE = "date";
	public static final String ENTRY_TYPE = "entryType";
	public static final String NUMBER_OF_SHARES = "numberOfShares";

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
	public Transaction(String date, String entryType, Money averagePrice,
			Money minimumPrice, int numberOfShares) {
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
	public Money getAveragePrice() {
		return averagePrice;
	}

	/**
	 * @param averagePrice
	 *            the avgPrice to set
	 */
	public void setAveragePrice(Money averagePrice) {
		this.averagePrice = averagePrice;
	}

	/**
	 * @return the minPrice
	 */
	public Money getMinimumPrice() {
		return minimumPrice;
	}

	/**
	 * @param minimumPrice
	 *            the minPrice to set
	 */
	public void setMinimumPrice(Money minimumPrice) {
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

	public BasicDBObject getDatabaseObject() {
		BasicDBObject document = new BasicDBObject(DATE, date)
				.append(ENTRY_TYPE, entryType).append(AVG, averagePrice.inCents())
				.append(MIN, minimumPrice.inCents())
				.append(NUMBER_OF_SHARES, numberOfShares);
		return document;
	}

}
