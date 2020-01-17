package src.jap.wordstat;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import src.data.MyJapanWord;
import src.file.MyTextFileReader;
import src.params.MyParams;
import src.util.log.MyLogToStdout;


//Класс для сбора и хранения статистики по словам
public class MyWordStat
{
	//Карта которая хранит список слов и число их просмотров
	private static HashMap< String, Integer > res_map = new HashMap< String, Integer >();
	private static boolean b_flag = true;
	
	
	public MyWordStat()
	{
		if( this.b_flag )
		{
			this.b_flag = false;
			this.res_map = this.readToHashMap();
		}
	}
	
	
	//Читаем файл статистики в хешмап
	private HashMap< String, Integer > readToHashMap()
	{
		MyTextFileReader mtfr = new MyTextFileReader();
		MyLogToStdout mlts = new MyLogToStdout();
		mlts.writeMess( "Read wordstat file: \"" + MyParams.getStringValue( "wordstat_path" ) + "\"..." );
		HashMap< String, Integer > res_map = new HashMap< String, Integer >();
		
		for( String s_pos : mtfr.readFileAsStringAL( MyParams.getStringValue( "wordstat_path" ) ) )
		{
			if( this.isCorrectString( s_pos ) ) 
			{
				res_map.put( this.parseWord( s_pos ), this.parseValue( s_pos ) ); 
			}
		}
		
		return res_map;
	}
	
	
	//Добавить слова в файл со статистикой
	public void putWordsToStaticFile( ArrayList< MyJapanWord > mjw_list )
	{
		for( MyJapanWord mjw_pos : mjw_list )
		{
			if( !this.isCorrectWord( mjw_pos ) ) { continue; }
			Integer n_val = this.res_map.get( mjw_pos.getBaseForm() );
			
			if( n_val == null ) 
			{
				res_map.put( mjw_pos.getBaseForm(), 1 );
			}
			else
			{
				res_map.put( mjw_pos.getBaseForm(), n_val + 1 );
			}
		}
		
		this.saveToFile();
	}
	
	
	//Это корректное слово, для записи в статистику?
	private boolean isCorrectWord( MyJapanWord mjw )
	{
		//Если все слово - один знак каны, не переводим
		if( mjw.isOnlyHiragana() && ( mjw.getWord().length() <= 1 ) ) { return false; }
		if
		( 
			( mjw.getType().indexOf( "none" ) != -1 ) || //А шо бы нет?
			( mjw.getType().indexOf( "noun" ) != -1 ) ||
			( mjw.getType().indexOf( "verb" ) != -1 ) ||
			( mjw.getType().indexOf( "adnominal" ) != -1 ) ||
			( mjw.getType().indexOf( "particle" ) != -1 ) ||
			( mjw.getType().indexOf( "adjective" ) != -1 )
		)
		{
			return true;
		}
		
		return false;
	}
	
	
	//Сохранить список в файл
	public void saveToFile() { this.saveToFile( MyParams.getStringValue( "wordstat_path" ) ); }
	public void saveToFile( String s_path )
	{
		ArrayList< String > res_list = new ArrayList< String >();
		MyTextFileReader mtfr = new MyTextFileReader();
		
		for( Entry entry : this.res_map.entrySet() ) 
		{
			res_list.add( "<word>" + entry.getKey() + "</word><val>" + entry.getValue() + "</val>" );
		}
		
		mtfr.writeALToFile( s_path, res_list );
	}
	
	
	//Проверка строки на корректность
	public static boolean isCorrectString( String s_string )
	{
		if
		(
			( s_string.indexOf( "<word>" ) != -1 ) && 
			( s_string.indexOf( "</word>" ) != -1 ) && 
			( s_string.indexOf( "<val>" ) != -1 ) && 
			( s_string.indexOf( "</val>" ) != -1 )
		)
		{ return true; }
		return false;
	}
	
	
	//Распарсить слово
	private String parseWord( String s_string )
	{
		MyLogToStdout mlts = new MyLogToStdout();
		String s_res = "";
		String s_start_tag = "<word>";
		String s_end_tag = "</word>";
		
		try
		{
			int n_start = s_string.indexOf( s_start_tag ) + s_start_tag.length();
			int n_end = s_string.indexOf( s_end_tag );
			s_res = s_string.substring( n_start, n_end );
		}
		catch( Exception e ) { mlts.writeMess( "Не удалось прочесть слово из строки - \"" + s_string + "\"" ); }
		
		return s_res;
	}
	
	
	//Распарсить 
	private Integer parseValue( String s_string )
	{
		MyLogToStdout mlts = new MyLogToStdout();
		Integer n_res = 0;
		String s_start_tag = "<val>";
		String s_end_tag = "</val>";
		
		try
		{
			int n_start = s_string.indexOf( s_start_tag ) + s_start_tag.length();
			int n_end = s_string.indexOf( s_end_tag );
			n_res = Integer.parseInt( s_string.substring( n_start, n_end ) );
		}
		catch( Exception e ) { mlts.writeMess( "Не удалось прочесть число повторов слова из строки - \"" + s_string + "\"" ); }
		
		return n_res;
	}
	
	
	//Напечатать весь список
	public void show()
	{
		for( Entry entry : this.res_map.entrySet() ) 
		{
			System.out.println();
			System.out.println( "Word: " + entry.getKey() );
			System.out.println( "Value: " + entry.getValue() );
		}
	}
}
