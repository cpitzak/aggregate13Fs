package guru.aggregator.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TransactionHistoryTest {

	@Test
	public void Add() {
		TransactionHistory transactionHistory = new TransactionHistory();
		transactionHistory.add("2005-09-30", "Add", 88.0f, 82.0f, 10);

		transactionHistory.adjustTxShares();
		transactionHistory.printOldestToLatest();
		transactionHistory.calcPrintCostBases();

		assertEquals(88.0f, transactionHistory.getAvg(), 0);
		assertEquals(82.0f, transactionHistory.getMin(), 0);
		assertEquals(10f, transactionHistory.getFinalShares(), 0);
	}

	@Test
	public void Add2() {
		TransactionHistory transactionHistory = new TransactionHistory();
		transactionHistory.add("2012-12-31", "Add", 122.0f, 120.0f, 98);
		transactionHistory.add("2010-09-30", "Add", 120.0f, 120.0f, 55);
		transactionHistory.add("2007-12-31", "Add", 133.0f, 132.0f, 24);
		transactionHistory.add("2005-09-30", "Add", 122.0f, 118.0f, 12);

		transactionHistory.adjustTxShares();
		transactionHistory.printOldestToLatest();
		transactionHistory.calcPrintCostBases();

		assertEquals(122.71f, transactionHistory.getAvg(), 0.01);
		assertEquals(121.22f, transactionHistory.getMin(), 0.01);
		assertEquals(98f, transactionHistory.getFinalShares(), 0);
	}

	@Test
	public void Add3() {
		TransactionHistory transactionHistory = new TransactionHistory();
		transactionHistory.add("2013-03-31", "Add", 88.0f, 82.0f, 10);
		transactionHistory.add("2007-12-31", "Add", 190.0f, 188.0f, 5);
		transactionHistory.add("2005-03-31", "Add", 192f, 192f, 2);

		transactionHistory.adjustTxShares();
		transactionHistory.printOldestToLatest();
		transactionHistory.calcPrintCostBases();

		assertEquals(139.40f, transactionHistory.getAvg(), 0);
		assertEquals(135.80f, transactionHistory.getMin(), 0);
		assertEquals(10f, transactionHistory.getFinalShares(), 0);
	}

	@Test
	public void Buy() {
		TransactionHistory transactionHistory = new TransactionHistory();
		transactionHistory.add("2013-03-31", "Buy", 88.0f, 82.0f, 10);
		transactionHistory.add("2007-12-31", "Buy", 190.0f, 188.0f, 5);
		transactionHistory.add("2005-09-30", "Buy", 192f, 192f, 2);

		transactionHistory.adjustTxShares();
		transactionHistory.printOldestToLatest();
		transactionHistory.calcPrintCostBases();

		assertEquals(139.40f, transactionHistory.getAvg(), 0);
		assertEquals(135.80f, transactionHistory.getMin(), 0);
		assertEquals(10f, transactionHistory.getFinalShares(), 0);
	}

	@Test
	public void AddBuyAreSame() {
		TransactionHistory transactionHistory = new TransactionHistory();
		transactionHistory.add("2013-03-31", "Add", 88.0f, 82.0f, 10);
		transactionHistory.add("2007-12-31", "Add", 190.0f, 188.0f, 5);
		transactionHistory.add("2005-09-30", "Add", 192f, 192f, 2);

		transactionHistory.adjustTxShares();
		transactionHistory.printOldestToLatest();
		transactionHistory.calcPrintCostBases();

		assertEquals(139.40f, transactionHistory.getAvg(), 0);
		assertEquals(135.80f, transactionHistory.getMin(), 0);
		assertEquals(10f, transactionHistory.getFinalShares(), 0);

		TransactionHistory transactionHistory2 = new TransactionHistory();
		transactionHistory2.add("2013-03-31", "Buy", 88.0f, 82.0f, 10);
		transactionHistory2.add("2007-12-31", "Buy", 190.0f, 188.0f, 5);
		transactionHistory2.add("2005-09-30", "Buy", 192f, 192f, 2);

		transactionHistory2.adjustTxShares();
		transactionHistory2.printOldestToLatest();
		transactionHistory2.calcPrintCostBases();

		assertEquals(transactionHistory.getAvg(), transactionHistory2.getAvg(), 0);
		assertEquals(transactionHistory.getMin(), transactionHistory2.getMin(), 0);
		assertEquals(transactionHistory.getFinalShares(), transactionHistory2.getFinalShares(), 0);
	}

}
