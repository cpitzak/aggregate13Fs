package guru.aggregator;

import guru.aggregator.model.Client;
import guru.aggregator.model.Ticker;

import java.text.NumberFormat;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 * Defines an stock aggregator to aggregate over the stocks data from the guru's
 * 13F forms.
 */
public class StockAggregator {
	private static final String hhr = Client.hhr;
	private Client client;

	/**
	 * Creates an stock aggregator with the given client.
	 * 
	 * @param client
	 *            the client to use
	 */
	public StockAggregator(Client client) {
		this.client = client;
	}

	// TODO: clean up complexity of this method
	public void printGuruStockData(String guru, List<Ticker> tickers)
			throws Exception {
		int pageNum = 0;
		int numDone = 0;

		String html = getFirstTxPage(guru);

		do {
			System.err.println("Page " + (pageNum++));

			if (numDone == tickers.size())
				break;

			HtmlCleaner hc = new HtmlCleaner();
			TagNode root = hc.clean(html);

			Object[] test = root.evaluateXPath("//body/table");
			if (test.length == 0)
				break;

			for (Ticker ticker : tickers) {
				if (ticker.isBuyFound())
					continue;

				Object[] nodes = root.evaluateXPath("//td[text()='"
						+ ticker.getTicker() + "']");

				for (Object obj : nodes) {
					if (ticker.isBuyFound())
						break;

					TagNode node = (TagNode) obj;
					TagNode[] td = node.getParent().getElementsByName("td",
							false);

					String date = td[2].getText().toString().trim();

					String entryType = td[3].getText().toString().trim();
					ticker.setBuyFound(entryType.equals("Buy"));
					if (ticker.isBuyFound())
						numDone++;

					String price = td[5].getText().toString();

					String avgPrice = price;
					if (avgPrice.contains("("))
						avgPrice = avgPrice.substring(
								avgPrice.indexOf("(") + 1,
								avgPrice.indexOf(")"));
					avgPrice = avgPrice.trim();
					if (avgPrice.charAt(0) == '$')
						avgPrice = avgPrice.substring(1);
					float fAvgPrice = Float.parseFloat(avgPrice);

					String minPrice = price;
					float fMinPrice = -1.0f;
					if (minPrice.contains("-")) // Otherwise no minPrice entry
					{
						minPrice = minPrice.substring(0, minPrice.indexOf("-"));
						minPrice = minPrice.trim();
						if (minPrice.charAt(0) == '$')
							minPrice = minPrice.substring(1);
						fMinPrice = Float.parseFloat(minPrice);
					}

					if (ticker.getCurrentPrice() == -1.0f) {
						String sCurPrice = td[6].getText().toString().trim();
						if (sCurPrice.charAt(0) == '$')
							sCurPrice = sCurPrice.substring(1);
						ticker.setCurrentPrice(Float.parseFloat(sCurPrice));
					}

					String sNumShares = td[9].getText().toString().trim();
					int numShares = NumberFormat
							.getInstance()
							.parse(sNumShares.length() == 0 ? "-1" : sNumShares)
							.intValue();

					ticker.getTransactionHistory().add(date, entryType,
							fAvgPrice, fMinPrice, numShares);
				}
			}

			test = root
					.evaluateXPath("//img[@src='http://static.gurufocus.com/images/icons/gray/png/playback_play_icon&16.png']");
			if (test.length == 0)
				break;

			html = getNextTxPage(((TagNode) test[0]).getParent()
					.getAttributeByName("href"));
		} while (true);

		for (Ticker ticker : tickers) {
			System.out.printf("\n" + hhr + ticker.getTicker() + hhr
					+ "\n%.1f%%\n\n", ticker.getPercent());

			System.err.println(ticker.getTicker());

			if (ticker.getTransactionHistory().isEmpty()) {
				System.out.println("No data");
				continue;
			}

			ticker.getTransactionHistory().adjustTxShares();

			System.out.printf("CUR: \t%.2f", ticker.getCurrentPrice());
			System.out.println("\n");

			ticker.getTransactionHistory().printOldestToLatest();

			ticker.getTransactionHistory().calcPrintCostBases();

			if (!ticker.isBuyFound())
				System.out.println("BUY NOT FOUND");
		}
	}

	private String getFirstTxPage(String guru) throws Exception {
		return client
				.getWebPageContents("http://www.gurufocus.com/modules/stock/StockBuy_ajax.php?GuruName="
						+ guru.replace(" ", "+"));
	}

	private String getNextTxPage(String relativeURL) throws Exception {
		return client.getWebPageContents("http://www.gurufocus.com"
				+ relativeURL);
	}

}
