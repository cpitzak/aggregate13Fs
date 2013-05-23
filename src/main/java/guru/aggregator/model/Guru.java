package guru.aggregator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Guru {

	private String name;
	private final List<Ticker> tickers;
	public static final String NAME = "name";
	public static final String TICKERS = "tickers";

	public Guru(String name, List<Ticker> tickers) {
		this.name = name;
		this.tickers = Collections.unmodifiableList(tickers);
	}
	
	// TODO: REMOVE, hack to provide functionality
	public Guru getGuruWith(String tickerSymbol) {
		Guru guru = null;
		for (Ticker ticker : tickers) {
			if (ticker.getTickerSymbol().equalsIgnoreCase(tickerSymbol)) {
				List<Ticker> tmp = new ArrayList<Ticker>();
				tmp.add(ticker);
				guru = new Guru(name, tmp);
			}
		}
		return guru;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the tickers
	 */
	public List<Ticker> getTickers() {
		return tickers;
	}

	@Override
	public String toString() {
		return name;
	}

	public BasicDBObject getDatabaseObject() {
		List<DBObject> tickerDBObjects = new ArrayList<DBObject>();
		for (Ticker ticker : tickers) {
			tickerDBObjects.add(ticker.getDatabaseObject());
		}
		BasicDBObject document = new BasicDBObject(NAME, name).append(
				TICKERS, tickerDBObjects);
		return document;
	}

}
