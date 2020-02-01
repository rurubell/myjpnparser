package src.jap.simkanji;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import src.data.MyJapanWord;
import src.file.MyTextFileReader;
import src.params.MyParams;
import src.util.log.MyLogToStdout;
import src.db.MyDBConnector;


public class MySimKanji
{
	//Карта которая хранит список канзи (ключ) - строка похожих канзи
	private static HashMap< String, ArrayList< String > > res_map = new HashMap< String, ArrayList< String > >();
	private static boolean b_flag = true;
	
	
	public MySimKanji()
	{
		if( this.b_flag )
		{
			this.b_flag = false;
			this.res_map = this.readToHashMap();
		}
	}
	
	
	//Читаем все списки похожих канзи в карту
	private HashMap< String, ArrayList< String > > readToHashMap()
	{
		MyDBConnector mdc = new MyDBConnector();
		MyTextFileReader mtfr = new MyTextFileReader();
		MyLogToStdout mlts = new MyLogToStdout();
		mlts.writeMess( "Read simkanji from DB: \"" + MyParams.getStringValue( "db_path" ) + "\"..." );
		HashMap< String, ArrayList< String > > res_map = new HashMap< String, ArrayList< String > >();
		
		try
		{
			Connection conn = mdc.makeConnection( MyParams.getStringValue( "db_path" ) );
			ResultSet rs = mdc.executeSQLAndGetResultSet( conn, "SELECT word, similar FROM similar_words" );
			
			while( rs.next() )
			{
				String s_word = rs.getString( "word" );
				String s_similar = rs.getString( "similar" );
				ArrayList< String > s_al_similar = this.parseSimKanji( s_similar );
				res_map.put( s_word, s_al_similar );
			}
			
			conn.close();
		}
		catch( SQLException sql_e ) { sql_e.printStackTrace(); }
		
		return res_map;
	}
	
	
	//Получить похожие кандзи
	public ArrayList< String > getSimilar( String s_kanji )
	{
		ArrayList< String > s_res_al = this.res_map.get( s_kanji );
		
		return s_res_al;
	}
	
	
	//Добавить/Изменить замены для символа/строки
	public void changeOrAddSimilar( String s_str, ArrayList< String > s_similar_al ) 
	{
		this.res_map.put( s_str, s_similar_al );
	}
	
	
	//Сохранить список в базу
	public void saveToBase()
	{
		String s_sql = 
			"DELETE FROM similar_words;\n" +
			"VACUUM;\n" +
			"BEGIN TRANSACTION;\n";
		
		for( Entry entry : this.res_map.entrySet() ) 
		{
			String s_word = (String) entry.getKey();
			String s_sim = "";
			
			ArrayList< String > s_sim_al = ( ArrayList< String > ) entry.getValue();
			for( int i = 0; i < s_sim_al.size(); i++ )
			{
				if( ( i + 1 ) < s_sim_al.size() ) { s_sim += s_sim_al.get(i) + "<::>"; }
				else { s_sim += s_sim_al.get(i); }
			}
			
			s_sql += "INSERT INTO similar_words (word, similar) VALUES( \'" + s_word + "\', \'" + s_sim + "\' );\n";
		}
		
		s_sql += "COMMIT;\n";
		
		try
		{
			MyDBConnector mdc = new MyDBConnector();
			Connection conn = mdc.makeConnection( MyParams.getStringValue( "db_path" ) );
			mdc.executeSQL( conn, s_sql );
			conn.close();
		}
		catch( SQLException sql_e ) { sql_e.printStackTrace(); }
	}
	
	
	//Распарсить похожие канзи
	private ArrayList< String > parseSimKanji( String s_string )
	{
		ArrayList< String > s_res_al = new ArrayList< String >();
		
		//Разбить строку на подстроки по "<::>"
		String[] s_parts = s_string.split( "<::>" );
		for( int i = 0; i < s_parts.length; i++ ) { s_res_al.add( s_parts[i] ); }
		
		return s_res_al;
	}
	
	
	//Напечатать весь список
	public void show()
	{
		for( Entry entry : this.res_map.entrySet() ) 
		{
			System.out.println();
			System.out.println( "Symbol: " + entry.getKey() );
			
			ArrayList< String > s_sim_al = ( ArrayList< String > ) entry.getValue();
			
			System.out.print( "Similar: " );
			for( int i = 0; i < s_sim_al.size(); i++ ) { System.out.print( s_sim_al.get(i) + " " ); }
			System.out.println( "" );
		}
	}
}
