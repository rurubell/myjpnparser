package src.net;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLDecoder;


public class MyGetSender
{
	private String USER_AGENT = "Mozilla/5.0";
	private int n_sec_timeout = 10 * 1000;
	
	
	//Послать GET запрос и получить ответ
	public synchronized String sendGET( String s_url )
	{
		String s_res = "";
		
		try
		{
			URL url = new URL( s_url );
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod( "GET" );
			con.setRequestProperty( "User-Agent", this.USER_AGENT );
			con.setConnectTimeout( this.n_sec_timeout );
			
			BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
			String s_buf = "";
			
			while ( ( s_buf = in.readLine() ) != null )
			{
				s_res += s_buf;
			}
			
			in.close();
		}
		catch( SocketTimeoutException ste ) { s_res = "<html><body><h1>TIMEOUT!!!</h1></body></html>"; }
		catch( Exception e ) { e.printStackTrace(); }
		
		return s_res;
	}
}
