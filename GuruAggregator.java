import org.htmlcleaner.*;
import org.apache.http.impl.client.DefaultHttpClient;

public class GuruAggregator 
{
	private static final String hr=Utils.hr;
	private static final String hhr=Utils.hhr;
	
	public static void main(String[] args) throws Exception
	{
		DefaultHttpClient client = new DefaultHttpClient();
		
		Utils.guruFocusLogin(client);
		
		printGuruData("Mohnish Pabrai", 4.0f, client);
		
		client.getConnectionManager().shutdown();
	}
	
	public static void printGuruData(String guru, float minPercent, DefaultHttpClient client) throws Exception
	{
		System.out.println(hr + guru + hr);
						
		int pageNum=0;
		
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
								
				System.out.printf("\n"+hhr+ticker+hhr+"\n%.1f%%\n\n", percent);
				GuruStockAggregator.printGuruStockData(guru, ticker);
				
			}
		}
		
	}
	
	private static String getPortfolioPage(String guru, int pageNum, DefaultHttpClient client) throws Exception
	{
		return Utils.getWebPageContents(client, "http://www.gurufocus.com/modules/holdings/holdings_ajax.php?GuruName="+guru.replace(" ","+")+"&sort=position&order=desc&p="+pageNum);				
	}
}
