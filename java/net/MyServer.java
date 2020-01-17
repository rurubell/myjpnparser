package src.net;


import java.net.*;
import java.io.*;

import src.data.MyJapanWord;
import src.ui.MyMainWin;
import src.util.MyExec;
import src.params.MyParams;
import src.util.log.MyLogToStdout;


public class MyServer 
{
	public static void startServer()
	{
		MyLogToStdout mlts = new MyLogToStdout();
		int n_port = MyParams.getIntValue( "net_port" );
		int n_iter = 0;
		
		try 
		{
			while( true )
			{
				mlts.writeMess( Integer.toString( n_iter ), "warn" );
				n_iter++;
				
				ServerSocket ss = new ServerSocket( n_port );
				mlts.writeMess( "Waiting for a new client..." );
				
				Socket socket = ss.accept();
				mlts.writeMess( "Got a client..." );
				
				InputStreamReader isr = new InputStreamReader( socket.getInputStream(), "UTF-8" );
				BufferedReader in = new BufferedReader( isr );
				
				String s_mess = "";
				String line = null;
				while( ( line = in.readLine() ) != null ) { s_mess += line + "_ENDLINE_"; }
				//Удаляем последний \n, зачем нам пустая строка?
				//if( s_mess.length() > 1 ) { s_mess = s_mess.substring( 0, s_mess.length() - 1 ); }
				
				mlts.writeMess( "Client say: " + getText( s_mess ) );
				MyMainWin mmw = new MyMainWin
					( 
						getText( s_mess ), 
						getWidth( s_mess ),
						getHeight( s_mess ),
						getVOffset( s_mess ),
						getHOffset( s_mess ),
						getOCRType( s_mess )
					);
				
				mmw.setVisible( true );
				
				ss.close();
				mlts.jump();
			}
		}
		catch( Exception e ) { e.printStackTrace(); }
	}
	
	
	//Получить текст из сообщения
	private static String getText( String s_mess )
	{
		String s_res = "";
		
		String s_start_tag = "<text>";
		String s_end_tag = "</text>";
		
		if( ( s_mess.indexOf( s_start_tag ) != -1 ) && ( s_mess.indexOf( s_end_tag ) != -1 ) )
		{
			s_res = s_mess.substring( s_mess.indexOf( s_start_tag ) + s_start_tag.length(), s_mess.indexOf( s_end_tag ) );
			s_res = s_res.replace( "_ENDLINE_", "\n" );
		}
		
		return s_res;
	}
	
	
	//Получить ширину выделенной области из сообщения
	private static int getWidth( String s_mess )
	{
		int n_res = 0;
		
		String s_start_tag = "<width>";
		String s_end_tag = "</width>";
		try
		{
			if( ( s_mess.indexOf( s_start_tag ) != -1 ) && ( s_mess.indexOf( s_end_tag ) != -1 ) )
			{
				String s_res = s_mess.substring( s_mess.indexOf( s_start_tag ) + s_start_tag.length(), s_mess.indexOf( s_end_tag ) );
				n_res = Integer.parseInt( s_res );
			}
		}
		catch ( Exception e ) {}
		
		return n_res;
	}
	
	
	//Получить высоту выделенной области из сообщения
	private static int getHeight( String s_mess )
	{
		int n_res = 0;
		
		String s_start_tag = "<height>";
		String s_end_tag = "</height>";
		try
		{
			if( ( s_mess.indexOf( s_start_tag ) != -1 ) && ( s_mess.indexOf( s_end_tag ) != -1 ) )
			{
				String s_res = s_mess.substring( s_mess.indexOf( s_start_tag ) + s_start_tag.length(), s_mess.indexOf( s_end_tag ) );
				n_res = Integer.parseInt( s_res );
			}
		}
		catch ( Exception e ) {}
		
		return n_res;
	}
	
	
	//Получить вертикальный оффсет из сообщения
	private static int getVOffset( String s_mess )
	{
		int n_res = 0;
		
		String s_start_tag = "<voffset>";
		String s_end_tag = "</voffset>";
		try
		{
			if( ( s_mess.indexOf( s_start_tag ) != -1 ) && ( s_mess.indexOf( s_end_tag ) != -1 ) )
			{
				String s_res = s_mess.substring( s_mess.indexOf( s_start_tag ) + s_start_tag.length(), s_mess.indexOf( s_end_tag ) );
				n_res = Integer.parseInt( s_res );
			}
		}
		catch ( Exception e ) {}
		
		return n_res;
	}
	
	
	//Получить горизонтальный оффсет из сообщения
	private static int getHOffset( String s_mess )
	{
		int n_res = 0;
		
		String s_start_tag = "<hoffset>";
		String s_end_tag = "</hoffset>";
		try
		{
			if( ( s_mess.indexOf( s_start_tag ) != -1 ) && ( s_mess.indexOf( s_end_tag ) != -1 ) )
			{
				String s_res = s_mess.substring( s_mess.indexOf( s_start_tag ) + s_start_tag.length(), s_mess.indexOf( s_end_tag ) );
				n_res = Integer.parseInt( s_res );
			}
		}
		catch ( Exception e ) {}
		
		return n_res;
	}
	
	
	//Получить тип OCR из сообщения
	private static String getOCRType( String s_mess )
	{
		String s_res = "";
		
		String s_start_tag = "<ocrtype>";
		String s_end_tag = "</ocrtype>";
		
		if( ( s_mess.indexOf( s_start_tag ) != -1 ) && ( s_mess.indexOf( s_end_tag ) != -1 ) )
		{
			s_res = s_mess.substring( s_mess.indexOf( s_start_tag ) + s_start_tag.length(), s_mess.indexOf( s_end_tag ) );
		}
		
		return s_res;
	}
}
