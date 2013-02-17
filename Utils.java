import java.net.*;
import java.util.*;
import java.io.*;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Utils
{
	
	public static final String hhr="---------------";  //HALF HR
	public static final String hr=hhr+hhr;
	
	public static String getWebPageContents(String sURL) throws Exception
	{
		URL url = new URL(sURL);
		
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
		
		// Get the response
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb = new StringBuffer();
		
		String line;		
		while ((line = rd.readLine()) != null)
			sb.append(line);
		
		rd.close();
		return sb.toString();
	}
	
	public static String getWebPageContents(DefaultHttpClient httpclient, String URL) throws Exception
	{
        return httpclient.execute(new HttpGet(URL),  new BasicResponseHandler());
	}
	
	public static void guruFocusLogin(DefaultHttpClient httpclient) throws Exception
	{
		HttpGet httpget = new HttpGet("http://www.gurufocus.com/forum/login.php");
        
        EntityUtils.consume(httpclient.execute(httpget).getEntity());
        
        HttpPost httpost = new HttpPost("http://www.gurufocus.com/forum/login.php");

        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair("forum_id", "0"));
        nvps.add(new BasicNameValuePair("redir", "http://www.gurufocus.com/forum/index.php"));
        nvps.add(new BasicNameValuePair("username", Credentials.username));
        nvps.add(new BasicNameValuePair("password", Credentials.password));

        httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

        EntityUtils.consume(httpclient.execute(httpost).getEntity());
        
	}
}