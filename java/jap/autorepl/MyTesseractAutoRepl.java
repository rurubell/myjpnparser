package src.jap.autorepl;


import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Comparator;

import src.data.MyJapanWord;
import src.file.MyTextFileReader;
import src.params.MyParams;
import src.util.log.MyLogToStdout;


public class MyTesseractAutoRepl implements MyAutoReplacement
{
	//Карта которая хранит список канзи (ключ) - строка похожих канзи
	private static Map<String, String> res_map = null;
	private static boolean b_flag = true;
	
	
	public MyTesseractAutoRepl()
	{
		if( this.b_flag )
		{
			this.b_flag = false;
			this.res_map = this.readToTreeMap();
		}
	}
	
	
	//Читаем все списки сырых строк и ответов OCR в карту
	private Map< String, String > readToTreeMap()
	{
		MyTextFileReader mtfr = new MyTextFileReader();
		MyLogToStdout mlts = new MyLogToStdout();
		mlts.writeMess( "Read auto replaces file for Tesserract: \"" + MyParams.getStringValue( "autorepl_tesseract_path" ) + "\"..." );
		
		Map<String, String> res_map = new TreeMap<String, String>
		(
			new Comparator<String>() 
			{
				@Override
				public int compare( String s1, String s2 )
				{
					if ( s1.length() > s2.length() ) { return -1; } 
					else if ( s1.length() < s2.length() ) { return 1; } 
					else { return s1.compareTo( s2 ); }
				}
			}
		);
		
		for( String s_pos : mtfr.readFileAsStringAL( MyParams.getStringValue( "autorepl_tesseract_path" ) ) )
		{
			if( this.isCorrectString( s_pos ) ) 
			{
				res_map.put( this.parseSrc( s_pos ), this.parseReplacement( s_pos ) ); 
			}
		}
		
		return res_map;
	}
	
	
	//Удалить замену для строки
	public void deleteAutoReplacement( String s_key_string )
	{
		this.res_map.remove( s_key_string );
		this.saveToFile();
	}
	
	
	//Узнать ключ для автозамены
	public String getKeyForAutoReplacementValue( String s_ar_value )
	{
		for( Entry entry : this.res_map.entrySet() ) 
		{
			if( entry.getValue().equals( s_ar_value ) ) { return ( String ) entry.getKey(); }
		}
		
		return null;
	}
	
	
	//Обработать текст
	public String handleText( String s_text )
	{
		for( Entry<String, String> entry : this.res_map.entrySet() )
		{
			s_text = s_text.replace( entry.getKey(), "ЁЁЁ" + entry.getValue() + "ЙЙЙ" );
		}
		
		s_text = s_text.replace( "ЁЁЁ", "<font color=\"#FF0000\">" );
		s_text = s_text.replace( "ЙЙЙ", "</font>" );
		
		return s_text;
	}
	
	
	//Добавить/Изменить замены для символа/строки
	public void changeOrAddSimilar( String s_str, String s_similar_str ) { this.res_map.put( s_str, s_similar_str ); }
	
	
	//Сохранить список в файл
	public void saveToFile() { this.saveToFile( MyParams.getStringValue( "autorepl_tesseract_path" ) ); }
	public void saveToFile( String s_path )
	{
		ArrayList< String > res_list = new ArrayList< String >();
		MyTextFileReader mtfr = new MyTextFileReader();
		
		for( Entry entry : this.res_map.entrySet() ) 
		{
			res_list.add( "<src>" + entry.getKey() + "</src><repl>" + entry.getValue() + "</repl>" );
		}
		
		mtfr.writeALToFile( s_path, res_list );
	}
	
	
	//Напечатать весь список
	public void show()
	{
		for( Entry entry : this.res_map.entrySet() ) 
		{
			System.out.println();
			System.out.println( "Src: " + entry.getKey() );
			System.out.println( "Replacement: " + entry.getValue() );
		}
	}
	
	
	//Распарсить замену для строки
	private String parseReplacement( String s_string )
	{
		MyLogToStdout mlts = new MyLogToStdout();
		String s_res = "";
		String s_start_tag = "<repl>";
		String s_end_tag = "</repl>";
		
		try
		{
			int n_start = s_string.indexOf( s_start_tag ) + s_start_tag.length();
			int n_end = s_string.indexOf( s_end_tag );
			s_res = s_string.substring( n_start, n_end );
		}
		catch( Exception e ) { mlts.writeMess( "Не удалось прочесть замену из строки - \"" + s_string + "\"", "warn" ); }
		
		return s_res;
	}
	
	
	//Распарсить сырую строку (которую дал OCR)
	private String parseSrc( String s_string )
	{
		MyLogToStdout mlts = new MyLogToStdout();
		String s_res = "";
		String s_start_tag = "<src>";
		String s_end_tag = "</src>";
		
		try
		{
			int n_start = s_string.indexOf( s_start_tag ) + s_start_tag.length();
			int n_end = s_string.indexOf( s_end_tag );
			s_res = s_string.substring( n_start, n_end );
		}
		catch( Exception e ) { mlts.writeMess( "Не удалось прочесть шаблон OCR из строки - \"" + s_string + "\"", "warn" ); }
		
		return s_res;
	}
	
	
	//Проверка строки на корректность
	public boolean isCorrectString( String s_string )
	{
		if
		(
			( s_string.indexOf( "<src>" ) != -1 ) && 
			( s_string.indexOf( "</src>" ) != -1 ) && 
			( s_string.indexOf( "<repl>" ) != -1 ) && 
			( s_string.indexOf( "</repl>" ) != -1 )
		)
		{ return true; }
		return false;
	} 
}
