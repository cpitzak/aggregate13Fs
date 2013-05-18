package guru.aggregator.aggregators;

import guru.aggregator.interfaces.HoldingsTable;
import guru.aggregator.model.Client;
import guru.aggregator.model.Filters;
import guru.aggregator.model.Guru;
import guru.aggregator.model.Ticker;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aggregates over the stocks data from the guru's 13F forms.
 */
public class GuruDataAggregator {
	private static final Logger logger = LoggerFactory
			.getLogger(GuruDataAggregator.class);
	private static final String hr = Client.hr;
	private Client client;

	/**
	 * Creates an aggregator with the given client.
	 * 
	 * @param client
	 *            the client to use
	 */
	public GuruDataAggregator(Client client) {
		this.client = client;
	}

	public Guru getGuruWithFilters(String guruName, Filters filters) {
		List<Ticker> tickers = getTickers(guruName, filters);
		Guru guru = null;
		if (!tickers.isEmpty()) {
			System.out.println(hr + guruName + hr);
			guru = buildGuruStockData(guruName, tickers);
		} else {
			System.out.println("Searching...");
		}
		return guru;
	}

	public Guru getGuru(String guruName) {
		System.out.println(hr + guruName + hr);
		List<Ticker> tickers = getTickers(guruName, null);
		return buildGuruStockData(guruName, tickers);
	}

	private List<Ticker> getTickers(String guruName, Filters filters) {
		List<Ticker> tickers = new ArrayList<Ticker>();
		int pageNumber = 0;
		Object[] rows = null;
		rows = getGuruTableRows(guruName, pageNumber);
		while (rows.length > 0) {
			addTickers(tickers, rows, filters);
			pageNumber++;
			rows = getGuruTableRows(guruName, pageNumber);
		}
		return tickers;
	}

	private void addTickers(List<Ticker> tickers, Object[] rows, Filters filters) {
		for (Object row : rows) {
			Ticker ticker = getTicker(row);
			if (filters != null) {
				if (filters.isTickerSymbolFound(ticker.getTickerSymbol())) {
					tickers.add(ticker);
				}
			} else {
				tickers.add(ticker);
			}
		}
	}

	private Ticker getTicker(Object row) {
		TagNode[] td = ((TagNode) row).getElementsByName("td", false);
		String tickerSymbol = td[HoldingsTable.TICKER_SYMBOL_COLUMN].getText()
				.toString().trim();
		float percent = 0;
		try {
			String percentStr = td[HoldingsTable.PERCENT_WEIGHT_AS_OF_LATEST_QUARTER_COLUMN]
					.getText().toString().trim().replace("%", "");
			percent = Float.parseFloat(percentStr);
		} catch (NumberFormatException e) {
			logger.debug(tickerSymbol + " ticker sold out. 0% of portfolio.");
		}
		return new Ticker(tickerSymbol, percent);
	}

	private Object[] getGuruTableRows(String guru, int pageNumber) {
		String html = getPortfolioPage(guru, pageNumber);

		HtmlCleaner hc = new HtmlCleaner();
		TagNode root = hc.clean(html);

		Object[] rows = null;
		try {
			rows = root.evaluateXPath("//table[@id='Rf1']/tbody/tr[@title]");
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		return rows;
	}

	// TODO: clean up complexity of this method
	private Guru buildGuruStockData(String guruName, List<Ticker> tickers) {
		int pageNum = 0;
		int numDone = 0;
		final int MAX_SCREEN_LINE = 24;
		String html = getFirstTxPage(guruName);
		System.out.print("Page(s) [ ");
		do {
			System.out.print(pageNum++ + " ");
			if (pageNum % MAX_SCREEN_LINE == 0) {
				System.out.println();
			}

			if (numDone == tickers.size())
				break;

			HtmlCleaner hc = new HtmlCleaner();
			TagNode root = hc.clean(html);

			Object[] test = null;
			try {
				test = root.evaluateXPath("//body/table");
			} catch (XPatherException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (test == null || test.length == 0)
				break;

			for (Ticker ticker : tickers) {
				if (ticker.isBuyFound())
					continue;

				Object[] nodes = null;
				try {
					nodes = root.evaluateXPath("//td[text()='"
							+ ticker.getTickerSymbol() + "']");
				} catch (XPatherException e) {
					e.printStackTrace();
				}

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
					int numShares = -1;
					try {
						numShares = NumberFormat
								.getInstance()
								.parse(sNumShares.length() == 0 ? "-1"
										: sNumShares).intValue();
					} catch (ParseException e) {
						e.printStackTrace();
					}

					ticker.getTransactionHistory().add(date, entryType,
							fAvgPrice, fMinPrice, numShares);
				}
			}

			try {
				test = root
						.evaluateXPath("//img[@src='http://static.gurufocus.com/images/icons/gray/png/playback_play_icon&16.png']");
			} catch (XPatherException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (test.length == 0)
				break;

			html = getNextTxPage(((TagNode) test[0]).getParent()
					.getAttributeByName("href"));
		} while (true);
		System.out.println("]");
		return new Guru(guruName, tickers);
	}

	private String getPortfolioPage(String guru, int pageNum) {
		String portfolioPage = null;
		try {
			portfolioPage = client
					.getWebPageContentsAsMember("http://www.gurufocus.com/modules/holdings/holdings_ajax.php?GuruName="
							+ guru.replace(" ", "+")
							+ "&sort=position&order=desc&p=" + pageNum);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return portfolioPage;
	}

	private String getFirstTxPage(String guru) {
		String firstTxPage = null;
		try {
			firstTxPage = client
					.getWebPageContents("http://www.gurufocus.com/modules/stock/StockBuy_ajax.php?GuruName="
							+ guru.replace(" ", "+"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return firstTxPage;
	}

	private String getNextTxPage(String relativeURL) {
		String nextTxPage = null;
		try {
			nextTxPage = client.getWebPageContents("http://www.gurufocus.com"
					+ relativeURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nextTxPage;
	}

}
