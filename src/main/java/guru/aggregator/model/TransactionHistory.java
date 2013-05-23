package guru.aggregator.model;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class TransactionHistory {
	private Money avg = new Money("0.00");
	private Money min = new Money("0.00");
	private int finalShares;
	private List<Transaction> transactions = new ArrayList<Transaction>();
	public static final String MIN = "min";
	public static final String AVG = "avg";
	public static final String TRANSACTIONS = "transactions";
	public static final String FINAL_SHARES = "finalShares";

	public TransactionHistory() {
	}
	
	public TransactionHistory(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	public void add(String date, String entryType, Money avgPrice,
			Money minPrice, int numShares) {
		Transaction transaction = new Transaction(date, entryType, avgPrice,
				minPrice, numShares);
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
			int numberOfShares = transaction.getNumberOfShares()
					- transactions.get(i + 1).getNumberOfShares();
			transaction.setNumberOfShares(numberOfShares);
		}
	}

	public void calcPrintCostBases() {
		Money avg = new Money("0.00");
		Money min = new Money("0.00");
		int numShares = 0;
		boolean increase = false;
		for (Transaction transaction : transactions) {
			if (transaction.getEntryType().equals("Buy")
					|| transaction.getEntryType().equals("Add")) {
				avg = avg.plus(transaction.getAveragePrice().times(transaction.getNumberOfShares()));
				min = min.plus(transaction.getMinimumPrice().times(transaction.getNumberOfShares()));
				numShares += transaction.getNumberOfShares();
				increase = true;
			}
		}

		if (increase && numShares != 0) {
			setAvg(avg.div(numShares));
			setMin(min.div(numShares));
		}
		System.out.printf("Final shares: \t%,d\n\n", finalShares);
		System.out.printf("AVG: \t%s\n", getAvg());
		System.out.printf("MIN: \t%s\n", getMin());
		
	}

	public Money getAvg() {
		return avg;
	}

	public Money getMin() {
		return min;
	}

	public void setAvg(Money avg) {
		this.avg = avg;
	}

	public void setMin(Money min) {
		this.min = min;
	}

	public void printOldestToLatest() {
		System.out.println("\nTx History:");
		System.out.println("Date\t\tAction\t Average Minimum Volume");
		for (int i = transactions.size() - 1; i >= 0; i--) {
			Transaction transaction = transactions.get(i);
			int entryTypeLength = transaction.getEntryType().length();
			int secondLongestEntryType = "Reduce".length();
			System.out.println(transaction.getDate() + "\t" + transaction.getEntryType()
					+ (entryTypeLength > secondLongestEntryType ? " " : "\t ") + transaction.getAveragePrice() + "  "
					+ transaction.getMinimumPrice() + "  " + transaction.getNumberOfShares());
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

	public BasicDBObject getDatabaseObject() {
		List<DBObject> transactionDBObjects = new ArrayList<DBObject>();
		for (Transaction transaction : transactions) {
			transactionDBObjects.add(transaction.getDatabaseObject());
		}
		BasicDBObject document = new BasicDBObject(AVG, avg.inCents())
				.append(MIN, min.inCents()).append(FINAL_SHARES, finalShares)
				.append(TRANSACTIONS, transactionDBObjects);
		return document;
	}
}
