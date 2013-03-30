package guru.aggregator;

import guru.aggregator.model.Client;
import guru.aggregator.model.Ticker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

/**
 * Defines an aggregator to aggregate over the guru's 13F forms.
 */
public class Aggregator {
	private Client client;
	private static final String hr = Client.hr;

	/**
	 * Connect the aggregator.
	 * 
	 * @param username
	 *            the username to use
	 * @param password
	 *            the password to use
	 */
	public void connect(String username, String password) {
		client = new Client();
		try {
			client.login(username, password);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close the connection.
	 */
	public void close() {
		client.logout();
	}

	/**
	 * Aggregator over the guru's 13F form data.
	 * 
	 * @param minimumPercent
	 *            the minimum percent to use
	 */
	public void aggregate(float minimumPercent) {
		System.err.printf("Minimum Percent: %.2f\n", minimumPercent);
		HtmlCleaner hc = new HtmlCleaner();
		TagNode root = null;
		try {
			root = hc
					.clean(client
							.getWebPageContentsAsMember("http://www.gurufocus.com/ListGuru.php"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Object[] gurus = null;
		try {
			gurus = root.evaluateXPath("//a[@class='gurunames']");
		} catch (XPatherException e) {
			e.printStackTrace();
		}

		for (Object tmp : gurus) {
			TagNode tagNode = (TagNode) tmp;
			String guru = tagNode.getText().toString().trim();

			System.err.println(guru);

			if (tagNode.getParent().getAttributeByName("class")
					.contains("premium")) {
				continue;
			}

			try {
				aggregateGuru(guru, minimumPercent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void aggregateGuru(String guru, float minPercent) throws Exception {
		System.out.println(hr + guru + hr);

		List<Ticker> tickers = new ArrayList<Ticker>();

		int pageNumber = 0;
		for (Object[] rows = getGuruTableRows(guru, pageNumber); rows.length > 0; rows = getGuruTableRows(
				guru, pageNumber)) {
			if (!addTicker(minPercent, tickers, rows)) {
				break;
			}
		}

		StockAggregator stockAggregator = new StockAggregator(client);
		stockAggregator.printGuruStockData(guru, tickers);

	}

	private Object[] getGuruTableRows(String guru, int pageNumber)
			throws Exception, XPatherException {
		String html = getPortfolioPage(guru, pageNumber);

		HtmlCleaner hc = new HtmlCleaner();
		TagNode root = hc.clean(html);

		Object[] rows = root
				.evaluateXPath("//table[@id='Rf1']/tbody/tr[@title]");
		return rows;
	}

	private boolean addTicker(float minPercent, List<Ticker> tickers,
			Object[] rows) {
		for (Object row : rows) {
			TagNode[] td = ((TagNode) row).getElementsByName("td", false);

			String ticker = td[0].getText().toString().trim();

			float percent = Float.parseFloat(td[5].getText().toString().trim()
					.replace("%", ""));

			if (percent < minPercent) {
				return false;
			}

			tickers.add(new Ticker(ticker, percent));
		}
		return true;
	}

	private String getPortfolioPage(String guru, int pageNum) throws Exception {
		return client
				.getWebPageContentsAsMember("http://www.gurufocus.com/modules/holdings/holdings_ajax.php?GuruName="
						+ guru.replace(" ", "+")
						+ "&sort=position&order=desc&p=" + pageNum);
	}
}
