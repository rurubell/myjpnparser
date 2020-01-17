package src.jap.simkanji;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import src.data.MyJapanWord;
import src.file.MyTextFileReader;
import src.params.MyParams;
import src.util.log.MyLogToStdout;


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
		MyTextFileReader mtfr = new MyTextFileReader();
		MyLogToStdout mlts = new MyLogToStdout();
		mlts.writeMess( "Read simkanji file: \"" + MyParams.getStringValue( "simkanji_path" ) + "\"..." );
		HashMap< String, ArrayList< String > > res_map = new HashMap< String, ArrayList< String > >();
		
		for( String s_pos : mtfr.readFileAsStringAL( MyParams.getStringValue( "simkanji_path" ) ) )
		{
			if( this.isCorrectString( s_pos ) ) 
			{
				res_map.put( this.parseKanji( s_pos ), this.parseSimKanji( s_pos ) ); 
			}
		}
		
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
	
	
	//Сохранить список в файл
	public void saveToFile() { this.saveToFile( MyParams.getStringValue( "simkanji_path" ) ); }
	public void saveToFile( String s_path )
	{
		ArrayList< String > res_list = new ArrayList< String >();
		MyTextFileReader mtfr = new MyTextFileReader();
		
		for( Entry entry : this.res_map.entrySet() ) 
		{
			String s_str = "<knj>" + entry.getKey() + "</knj><sim>";
			ArrayList< String > s_sim_al = ( ArrayList< String > ) entry.getValue();
			
			for( int i = 0; i < s_sim_al.size(); i++ )
			{
				if( ( i + 1 ) < s_sim_al.size() ) { s_str += s_sim_al.get(i) + "<::>"; }
				else { s_str += s_sim_al.get(i); }
			}
			
			s_str += "</sim>";
			res_list.add( s_str );
		}
		
		mtfr.writeALToFile( s_path, res_list );
	}
	
	
	//Проверка строки на корректность
	public static boolean isCorrectString( String s_string )
	{
		if
		(
			( s_string.indexOf( "<knj>" ) != -1 ) && 
			( s_string.indexOf( "</knj>" ) != -1 ) && 
			( s_string.indexOf( "<sim>" ) != -1 ) && 
			( s_string.indexOf( "</sim>" ) != -1 )
		)
		{ return true; }
		return false;
	}
	
	
	//Распарсить кандзи
	private String parseKanji( String s_string )
	{
		MyLogToStdout mlts = new MyLogToStdout();
		String s_res = "";
		String s_start_tag = "<knj>";
		String s_end_tag = "</knj>";
		
		try
		{
			int n_start = s_string.indexOf( s_start_tag ) + s_start_tag.length();
			int n_end = s_string.indexOf( s_end_tag );
			s_res = s_string.substring( n_start, n_end );
		}
		catch( Exception e ) { mlts.writeMess( "Не удалось прочесть кандзи из строки - \"" + s_string + "\"" ); }
		
		return s_res;
	}
	
	
	//Распарсить похожие канзи
	private ArrayList< String > parseSimKanji( String s_string )
	{
		MyLogToStdout mlts = new MyLogToStdout();
		ArrayList< String > s_res_al = new ArrayList< String >();
		String s_res = "";
		String s_start_tag = "<sim>";
		String s_end_tag = "</sim>";
		
		try
		{
			int n_start = s_string.indexOf( s_start_tag ) + s_start_tag.length();
			int n_end = s_string.indexOf( s_end_tag );
			s_res = s_string.substring( n_start, n_end );
		}
		catch( Exception e ) { mlts.writeMess( "Не удалось прочесть похожие канзи из строки - \"" + s_string + "\"" ); }
		
		//Разбить строку на подстроки по "<::>"
		String[] s_parts = s_res.split( "<::>" );
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
