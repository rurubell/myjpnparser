package src.jap.simwords;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import src.util.MyExec;
import src.data.MyJapanWord;
import src.file.MyTextFileReader;
import src.params.MyParams;
import src.util.log.MyLogToStdout;


public class MySimWords
{
	//Получить ArrayList похожих слов
	public ArrayList< String > getSimilarWords( String s_word )
	{
		ArrayList< String > s_res_al = new ArrayList< String >();
		if( s_word == null ) { return null; }
		if( s_word.length() <= 0 ) { return null; }
		
		try
		{
			String s_script_answ = this.getScriptAnswer( s_word );
			if( s_script_answ.length() <= 0 ) { return null; }
			
			String[] s_sim_words_arr = s_script_answ.split( "::" );
			
			for( int i = 0; i < s_sim_words_arr.length; i++ )
			{
				s_res_al.add( s_sim_words_arr[i] );
			}
		}
		catch( Exception e ) { e.printStackTrace(); }
		
		return s_res_al;
	}
	
	
	//Получить результат работы скрипта
	private String getScriptAnswer( String s_word )
	{
		String s_res = "";
		MyExec me = new MyExec();
		String s_command = "perl " + MyParams.getStringValue( "get_simwords_path" ) + " " + s_word;
		s_res = me.executeCommand( s_command );
		s_res = s_res.replace( "\n", "" );
		
		return s_res;
	}
}
