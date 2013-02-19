import java.text.NumberFormat;
import java.util.LinkedList;
import java.io.*;

import org.htmlcleaner.*;

public class GuruStockAggregator 
{
	private static final String hhr=Utils.hhr;
	
	//TEST
	public static void main(String[] args) throws Exception
	{	
		int guruNum,GURUS=18;
		String guru,ticker; 
		
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
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for(int i=0; i<GURUS; i++)
			System.out.println((i+1) + ") " + gurus[i]);		
		System.out.println("\nPick a guru (by number): ");
		guruNum=Integer.parseInt(br.readLine())-1;
		if (guruNum<0 || guruNum>=GURUS)
		{
			System.out.println("Wrong Entry");
			return;
		}
		
		guru=gurus[guruNum];
		
		System.out.println("\nEnter Ticker: ");
		ticker=br.readLine();
		ticker=ticker.toUpperCase();
		
		System.out.println("Guru: \t\t"+guru);		
		System.out.println("Ticker: \t"+ticker);
						
		LinkedList<GuruTicker> tickers=new LinkedList<>();
		tickers.add(new GuruTicker(ticker, -1.0f));
		
		printGuruStockData(guru, tickers);
		
		System.out.println("\nPress <ENTER> to exit...");
		br.readLine();
	}
		
	//MAIN FUNCTION
	public static void printGuruStockData(String guru, LinkedList<GuruTicker> tickers) throws Exception
	{
		int pageNum=0;
		int numDone=0;
		
		String html=getFirstTxPage(guru);
				
		do
		{
			System.err.println("Page "+(pageNum++));
			
			if(numDone==tickers.size()) break;			
				
			HtmlCleaner hc=new HtmlCleaner();
			TagNode root=hc.clean(html);
			
			Object[] test=root.evaluateXPath("//body/table");
			if(test.length==0) break;

			for(GuruTicker t : tickers)
			{
				if(t.buyFound) continue;
				
				Object[] nodes=root.evaluateXPath("//td[text()='"+t.ticker+"']");
				
				for(Object obj : nodes)
				{
					if(t.buyFound) break;
					
					TagNode node=(TagNode) obj;
					TagNode[] td=node.getParent().getElementsByName("td", false);
					
					String date=td[2].getText().toString().trim();
					
					String entryType=td[3].getText().toString().trim();
					t.buyFound=entryType.equals("Buy");
					if(t.buyFound) numDone++;
					
					String price=td[5].getText().toString();
					
					String avgPrice=price;
					if(avgPrice.contains("("))
						avgPrice=avgPrice.substring(avgPrice.indexOf("(")+1,avgPrice.indexOf(")"));
					avgPrice=avgPrice.trim();
					if (avgPrice.charAt(0)=='$') avgPrice=avgPrice.substring(1);
					float fAvgPrice=Float.parseFloat(avgPrice);
					
					String minPrice=price;
					float fMinPrice=-1.0f;
					if(minPrice.contains("-"))  //Otherwise no minPrice entry
					{
						minPrice=minPrice.substring(0,minPrice.indexOf("-"));
						minPrice=minPrice.trim();
						if (minPrice.charAt(0)=='$') minPrice=minPrice.substring(1);
						fMinPrice=Float.parseFloat(minPrice);
					}
							
					if (t.curPrice==-1.0f)
					{
						String sCurPrice=td[6].getText().toString().trim();
						if(sCurPrice.charAt(0)=='$') sCurPrice=sCurPrice.substring(1);
						t.curPrice=Float.parseFloat(sCurPrice);					
					}
					
					String sNumShares=td[9].getText().toString().trim();
					int numShares=NumberFormat.getInstance().parse(sNumShares.length()==0?"-1":sNumShares).intValue();
						
					t.entryList.addStockEntry(date, entryType, fAvgPrice, fMinPrice, numShares);
				}
			}
			
			
			test=root.evaluateXPath("//img[@src='http://static.gurufocus.com/images/icons/gray/png/playback_play_icon&16.png']");
			if(test.length==0) break;
			
			html=getNextTxPage(((TagNode)test[0]).getParent().getAttributeByName("href"));
		}
		while(true);
				
		for (GuruTicker t : tickers)
		{			
			System.out.printf("\n"+hhr+t.ticker+hhr+"\n%.1f%%\n\n", t.percent);
			
			System.err.println(t.ticker);
			
			if (t.entryList.latest==null)
			{
				System.out.println("No data");
				continue;
			}
			
			t.entryList.adjustTxShares();
			
			System.out.printf("CUR: \t%.2f",t.curPrice);
			System.out.println("\n");
			
			t.entryList.printOldestToLatest();
			
			t.entryList.calcPrintCostBases();	
						
			if (!t.buyFound) System.out.println("BUY NOT FOUND");
		}
	}
	
	private static String getFirstTxPage(String guru) throws Exception
	{
		return Utils.getWebPageContents("http://www.gurufocus.com/modules/stock/StockBuy_ajax.php?GuruName="+guru.replace(" ", "+"));				
	}
	
	private static String getNextTxPage(String relativeURL) throws Exception
	{
		return Utils.getWebPageContents("http://www.gurufocus.com" + relativeURL);
	}
	
	
}

//Private Classes	
class StockEntryDoubleLinkedList
{
	public StockEntry latest;
	public StockEntry oldest;
	
	public int finalShares;
	
	public StockEntryDoubleLinkedList(){}
	
	public void addStockEntry(String date, String entryType, float avgPrice, float minPrice, int numShares)
	{
		StockEntry se=new StockEntry(date,entryType,avgPrice,minPrice,numShares);
		
		if (latest==null)
		{
			latest=se;
			oldest=se;
			finalShares=numShares;
		}
		else
		{
			se.later=oldest;
			oldest.older=se;
			oldest=se;
		}
	}
	
	public void adjustTxShares() //adjusts the shares from total number of shares to tx share change numbers; RUN ONLY ONCE!
	{
		if (latest==null) return;
		StockEntry cur=latest;
		
		while(cur!=oldest)
		{
			cur.numShares-=cur.older.numShares;
			cur=cur.older;
		}
	}
	
	public void calcPrintCostBases()
	{		
		float avg=0.0f, min=0.0f;
		int numShares=0;
		StockEntry se=latest;
		
		do
		{
			if(se.entryType.equals("Buy") || se.entryType.equals("Add"))
			{
				avg+=se.avgPrice*se.numShares;
				min+=se.minPrice*se.numShares;
				numShares+=se.numShares;
			}
		}
		while((se=se.older)!=null);
		
		System.out.printf("Final shares: \t%,d\n\n",finalShares);
		System.out.printf("AVG: \t%.2f\n", avg/numShares);
		System.out.printf("MIN: \t%.2f\n", min/numShares);
		
	}
	
	public void printOldestToLatest()
	{
		System.out.println("Tx History:");
		
		StockEntry se=oldest;
		
		do
		{
			System.out.printf("%s\t %s\t %.2f\t %.2f\t %,d\n",se.date,se.entryType,se.avgPrice,se.minPrice,se.numShares);
		}
		while((se=se.later)!=null);
		
		
	}
	
}

class StockEntry
{
	public String date;
	public String entryType;
	public float avgPrice;
	public float minPrice;
	public int numShares;
	public StockEntry older;
	public StockEntry later;
	
	public StockEntry(String aDate, String aEntryType, float aAvgPrice, float aMinPrice, int aNumShares)
	{
		date=aDate;
		entryType=aEntryType;
		avgPrice=aAvgPrice;
		minPrice=aMinPrice;
		numShares=aNumShares;
	}
}
