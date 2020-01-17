package src.jap.dict.edict2;


import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

import src.jap.dict.MyDict;
import src.data.MyTranslation;
import src.file.MyTextFileReader;
import src.params.MyParams;
import src.util.log.MyLogToStdout;


public class MyEdict2 implements MyDict
{
	private static HashMap< String, String > res_map = new HashMap< String, String >();
	private static boolean b_flag = true;
	
	
	public MyEdict2()
	{
		if( this.b_flag )
		{
			this.b_flag = false;
			this.init();
		}
	}
	
	
	//Инициализация словаря
	private void init()
	{
		MyTextFileReader mtfr = new MyTextFileReader();
		MyLogToStdout mlts = new MyLogToStdout();
		mlts.writeMess( "Read myedict2 file: \"" + MyParams.getStringValue( "myedict2_path" ) + "\"..." );
		
		for( String s_pos : mtfr.readFileAsStringAL( MyParams.getStringValue( "myedict2_path" ) ) )
		{
			if( this.isCorrectString( s_pos ) ) 
			{
				ArrayList< String > s_keys_al = this.parseKeys( s_pos );
				String s_translation = this.parseTranslation( s_pos );
				
				for( String s_pos_key : s_keys_al )
				{
					this.res_map.put( s_pos_key, s_translation );
				}
			}
		}
	}
	
	
	//перевод одного слова
	@Override
	public MyTranslation getTranslation( String s_word )
	{
		String s_translation = this.res_map.get( s_word );
		
		if( s_translation == null ) { return new MyTranslation( s_word, "", "", "", "" ); }
		else { return new MyTranslation( s_word, "", s_translation, "", "" ); }
	}
	
	
	//перевод нескольких слов
	@Override
	public ArrayList< MyTranslation > getTranslation( ArrayList< String > s_words_al )
	{
		return null;
	}
	
	
	//Проверка строки на корректность
	public static boolean isCorrectString( String s_string )
	{
		if
		(
			( s_string.indexOf( "<key>" ) != -1 ) && 
			( s_string.indexOf( "</key>" ) != -1 ) && 
			( s_string.indexOf( "<transl>" ) != -1 ) && 
			( s_string.indexOf( "</transl>" ) != -1 )
		)
		{ return true; }
		return false;
	}
	
	
	//Распарсить ключи
	private ArrayList< String > parseKeys( String s_string )
	{
		ArrayList< String > s_res_al = new ArrayList< String >();
		
		int n_start_pos = 0;
		int n_end_pos = 0;
		String s_start_tag = "<key>";
		String s_end_tag = "</key>";
		
		while( s_string.indexOf( s_start_tag, n_start_pos ) != -1 )
		{
			n_start_pos = s_string.indexOf( s_start_tag, n_end_pos ) + s_start_tag.length();
			n_end_pos = s_string.indexOf( s_end_tag, n_start_pos );
			s_res_al.add( s_string.substring( n_start_pos, n_end_pos ) );
		}
		
		return s_res_al;
	}
	
	
	//Распарсить перевод
	private String parseTranslation( String s_string )
	{
		MyLogToStdout mlts = new MyLogToStdout();
		String s_res = "";
		String s_start_tag = "<transl>";
		String s_end_tag = "</transl>";
		
		try
		{
			int n_start = s_string.indexOf( s_start_tag ) + s_start_tag.length();
			int n_end = s_string.indexOf( s_end_tag );
			s_res = s_string.substring( n_start, n_end );
		}
		catch( Exception e ) { mlts.writeMess( "Не удалось прочесть перевод из строки - \"" + s_string + "\"" ); }
		
		return s_res;
	}
}
