package src.jap.dict.jmdict;


import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

import src.jap.dict.MyDict;
import src.data.MyTranslation;
import src.file.MyTextFileReader;
import src.params.MyParams;
import src.util.log.MyLogToStdout;


public class MyJMDICT implements MyDict
{
	private static HashMap< String, MyTranslation > res_map = new HashMap< String, MyTranslation >();
	private static boolean b_flag = true;
	
	
	public MyJMDICT()
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
		mlts.writeMess( "Read MYJMDICT file: \"" + MyParams.getStringValue( "myjmdic_path" ) + "\"..." );
		
		for( String s_pos : mtfr.readFileAsStringAL( MyParams.getStringValue( "myjmdic_path" ) ) )
		{
			if( this.isCorrectString( s_pos ) ) 
			{
				MyTranslation mt = new MyTranslation();
				
				mt.setKanji( this.parseKanji( s_pos ) );
				mt.setKana( this.parseKana( s_pos ) );
				mt.setEnTranslation( this.parseEnTranslation( s_pos ) );
				mt.setRuTranslation( this.parseRuTranslation( s_pos ) );
				
				this.res_map.put( mt.getKanji(), mt ); 
			}
		}
	}
	
	
	//перевод одного слова
	@Override
	public MyTranslation getTranslation( String s_word )
	{
		MyTranslation mt_res = this.res_map.get( s_word );
		if( mt_res == null ) { mt_res = new MyTranslation( s_word, "", "", "", "" ); }
		
		return mt_res;
	}
	
	
	//перевод нескольких слов
	@Override
	public ArrayList< MyTranslation > getTranslation( ArrayList< String > s_words_al )
	{
		ArrayList< MyTranslation > mt_res_al = new ArrayList< MyTranslation >();
		
		return mt_res_al;
	}
	
	
	//Проверка строки на корректность
	public static boolean isCorrectString( String s_string )
	{
		if
		(
			( s_string.indexOf( "<knj>" ) != -1 ) && 
			( s_string.indexOf( "</knj>" ) != -1 ) && 
			( s_string.indexOf( "<kan>" ) != -1 ) && 
			( s_string.indexOf( "</kan>" ) != -1 ) &&
			( s_string.indexOf( "<en>" ) != -1 ) && 
			( s_string.indexOf( "</en>" ) != -1 ) &&
			( s_string.indexOf( "<ru>" ) != -1 ) && 
			( s_string.indexOf( "</ru>" ) != -1 )
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
	
	
	//Распарсить кану
	private String parseKana( String s_string )
	{
		MyLogToStdout mlts = new MyLogToStdout();
		String s_res = "";
		String s_start_tag = "<kan>";
		String s_end_tag = "</kan>";
		
		try
		{
			int n_start = s_string.indexOf( s_start_tag ) + s_start_tag.length();
			int n_end = s_string.indexOf( s_end_tag );
			s_res = s_string.substring( n_start, n_end );
		}
		catch( Exception e ) { mlts.writeMess( "Не удалось прочесть кану из строки - \"" + s_string + "\"" ); }
		
		return s_res;
	}
	
	
	//Распарсить английский перевод
	private String parseEnTranslation( String s_string )
	{
		MyLogToStdout mlts = new MyLogToStdout();
		String s_res = "";
		String s_start_tag = "<en>";
		String s_end_tag = "</en>";
		
		try
		{
			int n_start = s_string.indexOf( s_start_tag ) + s_start_tag.length();
			int n_end = s_string.indexOf( s_end_tag );
			s_res = s_string.substring( n_start, n_end );
		}
		catch( Exception e ) { mlts.writeMess( "Не удалось прочесть англ. перевод из строки - \"" + s_string + "\"" ); }
		
		return s_res;
	}
	
	
	//Распарсить русский перевод
	private String parseRuTranslation( String s_string )
	{
		MyLogToStdout mlts = new MyLogToStdout();
		String s_res = "";
		String s_start_tag = "<ru>";
		String s_end_tag = "</ru>";
		
		try
		{
			int n_start = s_string.indexOf( s_start_tag ) + s_start_tag.length();
			int n_end = s_string.indexOf( s_end_tag );
			s_res = s_string.substring( n_start, n_end );
		}
		catch( Exception e ) { mlts.writeMess( "Не удалось прочесть русск. перевод из строки - \"" + s_string + "\"" ); }
		
		return s_res;
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
