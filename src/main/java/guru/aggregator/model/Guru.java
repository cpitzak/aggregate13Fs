package guru.aggregator.model;

import java.util.Collections;
import java.util.List;

public class Guru {

	private String name;
	private final List<Ticker> tickers;

	public Guru(String name, List<Ticker> tickers) {
		this.name = name;
		this.tickers = Collections.unmodifiableList(tickers);
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

}
