package guru.aggregator.model;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory {
	private List<Transaction> transactions = new ArrayList<Transaction>();
	private float avg;
	private float min;
	private int finalShares;

	public TransactionHistory() {
	}

	public void add(String date, String entryType, float avgPrice, float minPrice, int numShares) {
		Transaction transaction = new Transaction(date, entryType, avgPrice, minPrice, numShares);
		if (transactions.isEmpty()) {
			finalShares = numShares;
		}
		transactions.add(transaction);
	}

	public boolean isEmpty() {
		return transactions.isEmpty();
	}

	// adjusts the shares from total number of
	// shares to tx share change numbers; RUN
	// ONLY ONCE!
	// TODO: extract this out to another class
	public void adjustTxShares() {
		for (int i = 0; i < transactions.size() - 1; i++) {
			Transaction transaction = transactions.get(i);
			int numberOfShares = transaction.getNumberOfShares() - transactions.get(i + 1).getNumberOfShares();
			transaction.setNumberOfShares(numberOfShares);
		}
	}

	public void calcPrintCostBases() {
		float avg = 0.0f, min = 0.0f;
		int numShares = 0;
		for (Transaction transaction : transactions) {
			if (transaction.getEntryType().equals("Buy") || transaction.getEntryType().equals("Add")) {
				avg += transaction.getAveragePrice() * transaction.getNumberOfShares();
				min += transaction.getMinimumPrice() * transaction.getNumberOfShares();
				numShares += transaction.getNumberOfShares();
			}
		}

		System.out.printf("Final shares: \t%,d\n\n", finalShares);
		setAvg(avg / numShares);
		System.out.printf("AVG: \t%.2f\n", getAvg());
		setMin(min / numShares);
		System.out.printf("MIN: \t%.2f\n", getMin());

	}

	public float getAvg() {
		return avg;
	}

	public float getMin() {
		return min;
	}

	private void setAvg(float avg) {
		this.avg = avg;
	}

	private void setMin(float min) {
		this.min = min;
	}

	public void printOldestToLatest() {
		System.out.println("Tx History:");
		for (int i = transactions.size() - 1; i >= 0; i--) {
			Transaction transaction = transactions.get(i);
			System.out.printf("%s\t %s\t %.2f\t %.2f\t %,d\n", transaction.getDate(), transaction.getEntryType(),
					transaction.getAveragePrice(), transaction.getMinimumPrice(), transaction.getNumberOfShares());
		}
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
