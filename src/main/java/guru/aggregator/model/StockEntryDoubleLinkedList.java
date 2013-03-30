package guru.aggregator.model;

// TODO: replace this data structure with a more suitable data structure.
public class StockEntryDoubleLinkedList {
	private StockEntry latest;
	private StockEntry oldest;

	private int finalShares;

	public StockEntryDoubleLinkedList() {
	}

	public void addStockEntry(String date, String entryType, float avgPrice,
			float minPrice, int numShares) {
		StockEntry se = new StockEntry(date, entryType, avgPrice, minPrice,
				numShares);

		if (latest == null) {
			latest = se;
			oldest = se;
			finalShares = numShares;
		} else {
			se.setLater(oldest);
			oldest.setOlder(se);
			oldest = se;
		}
	}

	// adjusts the shares from total number of
	// shares to tx share change numbers; RUN
	// ONLY ONCE!
	public void adjustTxShares() {
		if (latest == null)
			return;
		StockEntry cur = latest;

		while (cur != oldest) {
			cur.setNumberOfShares(cur.getNumberOfShares()
					- cur.getOlder().getNumberOfShares());
			cur = cur.getOlder();
		}
	}

	public void calcPrintCostBases() {
		float avg = 0.0f, min = 0.0f;
		int numShares = 0;
		StockEntry se = latest;

		do {
			if (se.getEntryType().equals("Buy")
					|| se.getEntryType().equals("Add")) {
				avg += se.getAveragePrice() * se.getNumberOfShares();
				min += se.getMinimumPrice() * se.getNumberOfShares();
				numShares += se.getNumberOfShares();
			}
		} while ((se = se.getOlder()) != null);

		System.out.printf("Final shares: \t%,d\n\n", finalShares);
		System.out.printf("AVG: \t%.2f\n", avg / numShares);
		System.out.printf("MIN: \t%.2f\n", min / numShares);

	}

	public void printOldestToLatest() {
		System.out.println("Tx History:");

		StockEntry se = oldest;

		do {
			System.out.printf("%s\t %s\t %.2f\t %.2f\t %,d\n", se.getDate(),
					se.getEntryType(), se.getAveragePrice(),
					se.getMinimumPrice(), se.getNumberOfShares());
		} while ((se = se.getLater()) != null);

	}

	/**
	 * @return the latest
	 */
	public StockEntry getLatest() {
		return latest;
	}

	/**
	 * @param latest
	 *            the latest to set
	 */
	public void setLatest(StockEntry latest) {
		this.latest = latest;
	}

	/**
	 * @return the oldest
	 */
	public StockEntry getOldest() {
		return oldest;
	}

	/**
	 * @param oldest
	 *            the oldest to set
	 */
	public void setOldest(StockEntry oldest) {
		this.oldest = oldest;
	}

	/**
	 * @return the finalShares
	 */
	public int getFinalShares() {
		return finalShares;
	}

	/**
	 * @param finalShares
	 *            the finalShares to set
	 */
	public void setFinalShares(int finalShares) {
		this.finalShares = finalShares;
	}
}
