package src.jap.dict.jardic;


import java.util.ArrayList;
import java.net.URLEncoder;

import src.net.MyGetSender;
import src.jap.dict.MyDict;
import src.data.MyTranslation;


//Класс для работы с интернет словарем http://www.jardic.ru/
public class MyJardic implements MyDict
{
	private String s_jardic_url = "http://www.jardic.ru/search/search_r.php?q=";
	
	
	//перевод одного слова
	@Override
	public MyTranslation getTranslation( String s_word )
	{
		try { s_word = URLEncoder.encode( s_word, "UTF-8" ); }
		catch( Exception e ) { e.printStackTrace(); }
		
		MyGetSender mgs = new MyGetSender();
		String s_resp = mgs.sendGET( this.s_jardic_url + s_word );
		String s_data = this.deleteTrashDivs( s_resp );
		
		return new MyTranslation( "", "", "", "", s_data );
	}
	
	
	//перевод нескольких слов
	@Override
	public ArrayList< MyTranslation > getTranslation( ArrayList< String > s_words_al )
	{
		return null;
	}
	
	
	//Удалить лишние блоки из html блока
	private String deleteTrashDivs( String s_html_response )
	{
		String s_res = "";
		
		try
		{
			int n_start_index = s_html_response.indexOf( "Найдено статей:" );
			s_html_response = s_html_response.substring( n_start_index );
		} catch ( Exception e ) {}
		
		try
		{
			int n_start_index = s_html_response.indexOf( "<table width=\"100%\" style=\"padding-top: 4pt;\">" );
			int n_end_index = s_html_response.indexOf( "</table>", n_start_index ) + new String( "</table>" ).length();
			String s_useless_div = s_html_response.substring( n_start_index, n_end_index );
			s_html_response = s_html_response.replace( s_useless_div, "" );
		} catch ( Exception e ) {}
		
		s_res = s_html_response;
		
		return s_res;
	}
}
