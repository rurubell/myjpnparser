package src.jap.transhell;


import java.util.ArrayList;
import java.util.Random;

import src.data.MyJapanWord;
import src.util.MyExec;
import src.params.MyParams;
import src.util.string.MyStringUtils;


public class MyTranslateShell
{
	//перевести с английского на русский
	public String translateEnToRu( String s_text )
	{
		String s_res = "";
		MyExec me = new MyExec();
		MyStringUtils msu = new MyStringUtils();
		
		String s_command = MyParams.getStringValue( "trans_path" ) + " -t ru -s en \"" + s_text + "\"";
		ArrayList< String > s_transl_list = me.executeCommandAndGetList( s_command );
		ArrayList< String > s_clear_list = this.deleteUnnecessaryStrings( s_transl_list );
		
		for( String s_pos : s_clear_list ) { s_res += s_pos + "\n"; }
		s_res = s_res.replace( "\n\n\n", "\n\n" ); //Удаляем двойные пустые строки
		s_res = s_res.replace( "\", \"", "\",\n    \"" );
		
		return s_res;
	}
	
	
	//перевести с японского на русский
	public String translateJapToRu( String s_text )
	{
		String s_res = "";
		MyExec me = new MyExec();
		MyStringUtils msu = new MyStringUtils();
		
		String s_command = MyParams.getStringValue( "trans_path" ) + " -t ru -s jp \"" + s_text + "\"";
		ArrayList< String > s_transl_list = me.executeCommandAndGetList( s_command );
		ArrayList< String > s_clear_list = this.deleteUnnecessaryStrings( s_transl_list );
		
		for( String s_pos : s_clear_list ) { s_res += s_pos + "\n"; }
		s_res = s_res.replace( "\n\n\n", "\n\n" ); //Удаляем двойные пустые строки
		s_res = s_res.replace( "\", \"", "\",\n    \"" );
		
		return s_res;
	}
	
	
	//перевести с японского на английский
	public String translateJapToEng( String s_text )
	{
		String s_res = "";
		MyExec me = new MyExec();
		MyStringUtils msu = new MyStringUtils();
		
		String s_command = MyParams.getStringValue( "trans_path" ) + " -t en -s jp \"" + s_text + "\"";
		ArrayList< String > s_transl_list = me.executeCommandAndGetList( s_command );
		ArrayList< String > s_clear_list = this.deleteUnnecessaryStrings( s_transl_list );
		
		for( String s_pos : s_clear_list ) { s_res += s_pos + "\n"; }
		s_res = s_res.replace( "\n\n\n", "\n\n" ); //Удаляем двойные пустые строки
		s_res = s_res.replace( "\", \"", "\",\n    \"" );
		
		return s_res;
	}
	
	
	//Удалить лишние строки из перевода
	private ArrayList< String > deleteUnnecessaryStrings( ArrayList< String > s_strings )
	{
		ArrayList< String > s_res_list = new ArrayList< String >();
		MyStringUtils msu = new MyStringUtils();
		
		for( String s_pos : s_strings )
		{
			if( s_pos.indexOf( "->" ) != -1 ) { continue; }
			if( s_pos.indexOf( "Translations of" ) != -1 ) { continue; }
			
			s_res_list.add( msu.deleteBashEsc( s_pos ) );
		}
		
		return s_res_list;
	}
}
