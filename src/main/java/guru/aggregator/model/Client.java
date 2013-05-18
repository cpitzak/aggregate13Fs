package guru.aggregator.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Client {

	public static final String hhr = "\n---------------\n"; // HALF HR
	public static final String hr = "\n------------------------------\n";
	private DefaultHttpClient client;

	/**
	 * Login to guru focus.
	 * 
	 * @param username
	 *            the user name to use
	 * @param password
	 *            the password to use
	 * @throws Exception
	 *             throws an exception while trying to login.
	 */
	public void login(String username, String password)
			throws ClientProtocolException, IOException {
		client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(
				"http://www.gurufocus.com/forum/login.php");

		EntityUtils.consume(client.execute(httpget).getEntity());

		HttpPost httpost = new HttpPost(
				"http://www.gurufocus.com/forum/login.php");

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("forum_id", "0"));
		nvps.add(new BasicNameValuePair("redir",
				"http://www.gurufocus.com/forum/index.php"));
		nvps.add(new BasicNameValuePair("username", username));
		nvps.add(new BasicNameValuePair("password", password));

		httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

		EntityUtils.consume(client.execute(httpost).getEntity());

	}

	/**
	 * Logout of guru focus.
	 */
	public void logout() {
		client.getConnectionManager().shutdown();
	}

	/**
	 * Get the web pages content while logged in.
	 * 
	 * @param url
	 *            the url to use
	 * @return the contents of the web page
	 * @throws IOException
	 *             an io exception
	 * @throws ClientProtocolException
	 *             an client protocol exception
	 */
	public String getWebPageContentsAsMember(String url)
			throws ClientProtocolException, IOException {
		return client.execute(new HttpGet(url), new BasicResponseHandler());
	}

	/**
	 * Get the web page contents from the given url as an anonymous user.
	 * 
	 * @param urlString
	 *            the url to use
	 * @return the web page contents as an anonymous user.
	 * @throws IOException
	 *             an io exception
	 */
	public String getWebPageContents(String urlString) throws IOException {
		URL url = new URL(urlString);

		URLConnection conn = url.openConnection();
		conn.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");

		// Get the response
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		StringBuffer sb = new StringBuffer();

		String line;
		while ((line = rd.readLine()) != null)
			sb.append(line);

		rd.close();
		return sb.toString();
	}

}
