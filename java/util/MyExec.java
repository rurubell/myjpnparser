package src.util;


import java.util.ArrayList;
import java.io.*;


public class MyExec
{
	//Выполнить команду и получить результат в виде ArrayList< String >
	public static ArrayList< String > executeCommandAndGetList( String s_command )
	{
		ArrayList< String > s_res_list = new ArrayList< String >();
		
		try
		{
			Runtime rt = Runtime.getRuntime();
			Process ps = rt.exec( s_command );
			BufferedReader br_input = new BufferedReader( new InputStreamReader( ps.getInputStream() ) );
			String s_line = null;
			
			while(( s_line = br_input.readLine()) != null ) 
			{
				s_res_list.add( s_line );
			}
		br_input.close();
		}
		catch( IOException ioe ) { ioe.printStackTrace(); }
		
		return s_res_list;
	}
	
	
	//Выполнить команду и получить результат в виде строки
	public String executeCommand( String s_command )
	{
		String s_res = "";
		
		try
		{
			Runtime rt = Runtime.getRuntime();
			Process ps = rt.exec( s_command );
			BufferedReader br_input = new BufferedReader( new InputStreamReader( ps.getInputStream() ) );
			String s_line = null;
			
			while(( s_line = br_input.readLine()) != null ) 
			{
				s_res += s_line + "\n";
			}
		br_input.close();
		}
		catch( IOException ioe ) { ioe.printStackTrace(); }
		
		return s_res;
	}
	
	
	//Выполнить команду и получить результат в виде строки
	public String executeCommand( String[] s_command )
	{
		String s_res = "";
		
		try
		{
			Process p = Runtime.getRuntime().exec( s_command );
			BufferedReader br_input = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
			String s_line = null;
			
			while(( s_line = br_input.readLine()) != null ) 
			{
				s_res += s_line + "\n";
			}
			br_input.close();
		}
		catch( IOException ioe ) { ioe.printStackTrace(); }
		
		return s_res;
	}
}
