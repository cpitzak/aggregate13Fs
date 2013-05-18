package guru.aggregator.model;

import java.util.HashSet;
import java.util.Set;

public class Filters {
	
	private Set<String> tickerSymbols = new HashSet<String>();
	
	public boolean isTickerSymbolFound(String tickerSymbol) {
		return tickerSymbols.contains(tickerSymbol.toUpperCase());
	}
	
	public void addTickerSymbol(String tickerSymbol) {
		tickerSymbols.add(tickerSymbol.toUpperCase());
	}
	

}
