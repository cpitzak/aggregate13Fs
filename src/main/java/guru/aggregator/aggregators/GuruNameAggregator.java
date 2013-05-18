package guru.aggregator.aggregators;

import guru.aggregator.model.Client;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

/**
 * Aggregates over guru names.
 *
 */
public class GuruNameAggregator {
	private Client client;
	
	public GuruNameAggregator(Client client) {
		this.client = client;
	}

	/**
	 * @return the guru names
	 */
	public List<String> getGuruNames() {
		List<String> guruNames = new ArrayList<String>();
		HtmlCleaner hc = new HtmlCleaner();
		TagNode root = null;
		try {
			root = hc.clean(client.getWebPageContentsAsMember("http://www.gurufocus.com/ListGuru.php"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Object[] guruNamesTagNodes = null;
		try {
			guruNamesTagNodes = root.evaluateXPath("//a[@class='gurunames']");
		} catch (XPatherException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < guruNamesTagNodes.length; i++) {
			TagNode tagNode = (TagNode) guruNamesTagNodes[i];
			String guruName = tagNode.getText().toString().trim();
			if (!isDate(guruName)) {
				if (tagNode.getParent().getAttributeByName("class").contains("premium")) {
				} else {
					guruNames.add(guruName);
				}
			}
		}
		return guruNames;
	}
	
	private boolean isDate(String inDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

}
