package guru.aggregator.view;

import guru.aggregator.model.Client;
import guru.aggregator.model.Guru;
import guru.aggregator.model.Ticker;

public class PrintResults {

	private static final String hhr = Client.hhr;

	public static void print(Guru guru) {
		if (guru != null) {
			for (Ticker ticker : guru.getTickers()) {
				System.out.println(hhr + ticker.getTickerSymbol() + hhr);
				System.out.println("Portfolio Impact: " + ticker.getPercent()
						+ "%");
				if (ticker.getTransactionHistory().isEmpty()) {
					System.out.println("No data");
					continue;
				}
//				ticker.getTransactionHistory().adjustTxShares();
				System.out.println("Current Share Price: "
						+ ticker.getCurrentPrice());
				ticker.getTransactionHistory().printOldestToLatest();
				ticker.getTransactionHistory().calcPrintCostBases();
				if (!ticker.isBuyFound()) {
					System.out.println("BUY NOT FOUND");
				}
				System.out.println();
			}
		} else {
			System.out.println("No data for guru");
		}
	}
}
