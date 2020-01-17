package src.jap.dict.elib;


import java.util.ArrayList;
import java.net.URLEncoder;

import src.net.MyGetSender;
import src.jap.dict.MyDict;
import src.data.MyTranslation;


//Класс для работы с интернет словарем http://e-lib.ua
public class MyELIB implements MyDict
{
	private String s_elib_url_1 = "http://e-lib.ua/dic/results?w=";
	private String s_elib_url_2 = "&m=0";
	
	
	//перевод одного слова
	@Override
	public MyTranslation getTranslation( String s_word )
	{
		try { s_word = URLEncoder.encode( s_word, "UTF-8" ); }
		catch( Exception e ) { e.printStackTrace(); }
		
		MyGetSender mgs = new MyGetSender();
		String s_resp = mgs.sendGET( this.s_elib_url_1 + s_word + this.s_elib_url_2 );
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
			int n_start_index = s_html_response.indexOf( "<div class=\"kanjipic\">" );
			int n_end_index = s_html_response.indexOf( "</div>", n_start_index ) + new String( "</div>" ).length();
			String s_div_block = s_html_response.substring( n_start_index, n_end_index );
			s_html_response = s_html_response.replace( s_div_block, "" );
		} catch ( Exception e ) {}
		
		try
		{
			int n_start_index = s_html_response.indexOf( "<div class=\"akusentoblock\">" );
			int n_end_index = s_html_response.indexOf( "</div>", n_start_index )  + new String( "</div>" ).length();
			String s_div_block = s_html_response.substring( n_start_index, n_end_index );
			s_html_response = s_html_response.replace( s_div_block, "" );
		} catch ( Exception e ) {}
		
		try
		{
			s_html_response = s_html_response.replace( "<!--|-->", "" );
		} catch ( Exception e ) {}
		
		s_res = s_html_response + "<br/><br/>";
		return s_res;
	}
}
