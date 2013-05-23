package guru.aggregator.model;

import static org.junit.Assert.assertEquals;

import java.math.RoundingMode;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

public class TransactionHistoryTest {
	
	@Before
	public void setup() {
		Money.init(Currency.getInstance("USD"), RoundingMode.HALF_EVEN);
	}

	@Test
	public void Add() {
		TransactionHistory transactionHistory = new TransactionHistory();
		transactionHistory.add("2005-09-30", "Add", new Money("88.0"), new Money("82.0"), 10);

		transactionHistory.adjustTxShares();
		transactionHistory.printOldestToLatest();
		transactionHistory.calcPrintCostBases();

		assertEquals(new Money("88.00"), transactionHistory.getAvg());
		assertEquals(new Money("82.00"), transactionHistory.getMin());
		assertEquals(10, transactionHistory.getFinalShares(), 0);
	}

	@Test
	public void Add2() {
		TransactionHistory transactionHistory = new TransactionHistory();
		transactionHistory.add("2012-12-31", "Add", new Money("122.0"), new Money("120.0"), 98);
		transactionHistory.add("2010-09-30", "Add", new Money("120.0"), new Money("120.0"), 55);
		transactionHistory.add("2007-12-31", "Add", new Money("133.0"), new Money("132.0"), 24);
		transactionHistory.add("2005-09-30", "Add", new Money("122.0"), new Money("118.0"), 12);

		transactionHistory.adjustTxShares();
		transactionHistory.printOldestToLatest();
		transactionHistory.calcPrintCostBases();

		assertEquals(new Money("122.71"), transactionHistory.getAvg());
		assertEquals(new Money("121.22"), transactionHistory.getMin());
		assertEquals(98, transactionHistory.getFinalShares());
	}

	@Test
	public void Add3() {
		TransactionHistory transactionHistory = new TransactionHistory();
		transactionHistory.add("2013-03-31", "Add", new Money("88.0"), new Money("82.0"), 10);
		transactionHistory.add("2007-12-31", "Add", new Money("190.0"), new Money("188.0"), 5);
		transactionHistory.add("2005-03-31", "Add", new Money("192"), new Money("192"), 2);

		transactionHistory.adjustTxShares();
		transactionHistory.printOldestToLatest();
		transactionHistory.calcPrintCostBases();

		assertEquals(new Money("139.40"), transactionHistory.getAvg());
		assertEquals(new Money("135.80"), transactionHistory.getMin());
		assertEquals(10, transactionHistory.getFinalShares(), 0);
	}

	@Test
	public void Buy() {
		TransactionHistory transactionHistory = new TransactionHistory();
		transactionHistory.add("2013-03-31", "Buy", new Money("88.0"), new Money("82.0"), 10);
		transactionHistory.add("2007-12-31", "Buy", new Money("190.0"), new Money("188.0"), 5);
		transactionHistory.add("2005-09-30", "Buy", new Money("192"), new Money("192"), 2);

		transactionHistory.adjustTxShares();
		transactionHistory.printOldestToLatest();
		transactionHistory.calcPrintCostBases();

		assertEquals(new Money("139.40"), transactionHistory.getAvg());
		assertEquals(new Money("135.80"), transactionHistory.getMin());
		assertEquals(10, transactionHistory.getFinalShares(), 0);
	}

	@Test
	public void AddBuyAreSame() {
		TransactionHistory transactionHistory = new TransactionHistory();
		transactionHistory.add("2013-03-31", "Add", new Money("88.0"), new Money("82.0"), 10);
		transactionHistory.add("2007-12-31", "Add", new Money("190.0"), new Money("188.0"), 5);
		transactionHistory.add("2005-09-30", "Add", new Money("192"), new Money("192"), 2);

		transactionHistory.adjustTxShares();
		transactionHistory.printOldestToLatest();
		transactionHistory.calcPrintCostBases();

		assertEquals(new Money("139.40"), transactionHistory.getAvg());
		assertEquals(new Money("135.80"), transactionHistory.getMin());
		assertEquals(10, transactionHistory.getFinalShares(), 0);

		TransactionHistory transactionHistory2 = new TransactionHistory();
		transactionHistory2.add("2013-03-31", "Buy", new Money("88.0"), new Money("82.0"), 10);
		transactionHistory2.add("2007-12-31", "Buy", new Money("190.0"), new Money("188.0"), 5);
		transactionHistory2.add("2005-09-30", "Buy", new Money("192"), new Money("192"), 2);

		transactionHistory2.adjustTxShares();
		transactionHistory2.printOldestToLatest();
		transactionHistory2.calcPrintCostBases();

		assertEquals(transactionHistory.getAvg(), transactionHistory2.getAvg());
		assertEquals(transactionHistory.getMin(), transactionHistory2.getMin());
		assertEquals(transactionHistory.getFinalShares(), transactionHistory2.getFinalShares(), 0);
	}

}
