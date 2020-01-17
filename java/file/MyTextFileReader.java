package src.file;


import java.io.*;

import java.util.ArrayList;

import src.util.log.MyLogToStdout;


public class MyTextFileReader
{
	//Прочесть текстовый файл как коллекцию строк
	public ArrayList< String > readFileAsStringAL( String s_path )
	{
		ArrayList< String > al_res = new ArrayList< String >();
		MyLogToStdout mlts = new MyLogToStdout();
		
		try
		{
			FileInputStream fis = new FileInputStream( s_path );
			BufferedReader reader = new BufferedReader( new InputStreamReader( fis ) );
			String s_line = "";
			while( ( s_line = reader.readLine() ) != null ) { al_res.add( s_line ); }
		}
		catch( FileNotFoundException ffe ) { mlts.writeMess( "File " + s_path + " is not exists..." ); }
		catch( IOException ffe ) { mlts.writeMess( "Some IO error, while reading file - " + s_path + "..." ); }
		catch( Exception e ) { e.printStackTrace(); }
		
		return al_res;
	}
	
	
	//Записать коллекцию строк в файл
	public void writeALToFile( String s_path, ArrayList< String > str_list )
	{
		try
		{
			FileWriter writer = new FileWriter( s_path );
			for( String s_pos : str_list ) { writer.write( s_pos + "\n" ); }
			writer.close();
		}
		catch( Exception e ) { e.printStackTrace(); }
	}
}
