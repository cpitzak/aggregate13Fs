import java.util.LinkedList;

import org.htmlcleaner.*;
import org.apache.http.impl.client.DefaultHttpClient;

public class GuruAggregator 
{
	private static final String hr=Utils.hr;
		
	public static void main(String[] args) throws Exception
	{
		//Initialize
		DefaultHttpClient client = new DefaultHttpClient();
		Utils.guruFocusLogin(client);
		
		int GURUS=19;
		String[] gurus= new String[GURUS];
		gurus[0]="David Einhorn";
		gurus[1]="Mohnish Pabrai";
		gurus[2]="Bill Ackman";
		gurus[3]="Bruce Berkowitz";
		gurus[4]="Carl Icahn"; 
		gurus[5]="Chuck Akre"; 
		gurus[6]="Daniel Loeb";
		gurus[7]="Wilbur Ross";
		gurus[8]="Prem Watsa"; 
		gurus[9]="John Paulson";
		gurus[10]="Edward Lampert";
		gurus[11]="Glenn Greenberg";
		gurus[12]="Ian Cumming";
		gurus[13]="Lou Simpson";
		gurus[14]="Robert Rodriguez";
		gurus[15]="David Swensen";
		gurus[16]="T Boone Pickens";
		gurus[17]="Robert Karr";
		gurus[18]="Seth Klarman";
		
		//MAIN CALL
		for(int i=0; i<GURUS; i++)
			printGuruData(gurus[i], 4.0f, client);		
		
		//Close
		client.getConnectionManager().shutdown();
	}
	
	public static void printGuruData(String guru, float minPercent, DefaultHttpClient client) throws Exception
	{
		System.out.println(hr + guru + hr);
						
		int pageNum=0;
		
		LinkedList<GuruTicker> tickers = new LinkedList<>();
		
top:	while(true)
		{
			String html = getPortfolioPage(guru, pageNum++, client);
						
			HtmlCleaner hc=new HtmlCleaner();
			TagNode root=hc.clean(html);
			
			Object[] rows=root.evaluateXPath("//table[@id='Rf1']/tbody/tr[@title]");
			if(rows.length==0) break;
			
			for(Object row : rows)
			{				
				TagNode[] td = ((TagNode) row).getElementsByName("td", false);
				
				String ticker=td[0].getText().toString().trim();
				
				float percent=Float.parseFloat(td[5].getText().toString().trim().replace("%",""));
				
				if (percent < minPercent) break top;
				
				tickers.add(new GuruTicker(ticker, percent));
			}
		}
				
		GuruStockAggregator.printGuruStockData(guru, tickers);
		
	}
	
	private static String getPortfolioPage(String guru, int pageNum, DefaultHttpClient client) throws Exception
	{
		return Utils.getWebPageContents(client, "http://www.gurufocus.com/modules/holdings/holdings_ajax.php?GuruName="+guru.replace(" ","+")+"&sort=position&order=desc&p="+pageNum);				
	}
}

class GuruTicker
{
	public String ticker;
	public float percent;
	
	public boolean buyFound=false; 
	public StockEntryDoubleLinkedList entryList = new StockEntryDoubleLinkedList();
	public float curPrice=-1.0f;
	
	public GuruTicker(String ticker, float percent)
	{
		this.ticker=ticker;
		this.percent=percent;
	}
}
