package src.jap.dict.jisho;


import java.util.ArrayList;
import java.net.URLEncoder;

import src.jap.dict.MyDict;
import src.jap.dict.jisho.*;
import src.data.MyJapanWord;
import src.data.MyTranslation;
import src.net.MyGetSender;


public class MyJisho implements MyDict
{
	private String s_jisho_api_link = "https://jisho.org/api/v1/search/words?keyword=";
	
	
	//перевод нескольких слов
	public ArrayList< MyTranslation > getTranslation( ArrayList< String > s_words )
	{
		ArrayList< MyTranslation > mt_res_list = new ArrayList< MyTranslation >();
		
		/* ... */
		
		return mt_res_list;
	}
	
	
	//Перевод одного слова
	public MyTranslation getTranslation( String s_word )
	{
		MyTranslation mt_res = new MyTranslation();
		
		MyGetSender mgs = new MyGetSender();
		String s_resp = "";
		String s_parameter = "";
		
		try{ s_parameter = URLEncoder.encode( s_word, "UTF-8" ); }
		catch( Exception e ) { e.printStackTrace(); }
		
		s_resp = mgs.sendGET( this.s_jisho_api_link + s_parameter );
		
		mt_res.setEnTranslation( this.getEnglishTranslations( s_resp ) );
		mt_res.setKana( this.getReading( s_resp ) );
		mt_res.setKanji( s_word );
		
		return mt_res;
	}
	
	
	//Получить английские переводы для слова
	private String getEnglishTranslations( String s_resp )
	{
		String s_res = "";
		
		try
		{
			String s_start_tag = "\"english_definitions\":[";
			String s_end_tag = "]";
			
			int n_start = s_resp.indexOf( s_start_tag ) + s_start_tag.length();
			int n_end = s_resp.indexOf( s_end_tag, n_start );
			
			s_res += s_resp.substring( n_start, n_end );
			s_res = s_res.replace( "\"", "" );
			s_res = s_res.replace( ",", ", " );
		}
		catch( Exception e ) { e.printStackTrace(); }
		
		return s_res;
	}
	
	
	//Получить чтение для слова
	private String getReading( String s_resp )
	{
		String s_res = "";
		
		try
		{
			String s_start_tag = "\"reading\":";
			String s_end_tag = "}";
			
			int n_start = s_resp.indexOf( s_start_tag ) + s_start_tag.length();
			int n_end = s_resp.indexOf( s_end_tag, n_start );
			
			s_res += s_resp.substring( n_start, n_end );
			s_res = s_res.replace( "\"", "" );
		}
		catch( Exception e ) { e.printStackTrace(); }
		
		return s_res;
	}
	
	
	//Это common word?
	private boolean isCommon( String s_resp )
	{
		if( s_resp.indexOf( "\"is_common\":true" ) != -1 ) { return true; }
		return false;
	}
}
