package src.jap.dict.edict2_light;


import java.util.ArrayList;
import java.util.Map;
import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;

import org.unix4j.Unix4j;
import org.unix4j.builder.Unix4jCommandBuilder;
import org.unix4j.io.Output;
import org.unix4j.io.StreamOutput;
import org.unix4j.unix.Cut;
import org.unix4j.unix.Grep;
import org.unix4j.unix.Ls;
import org.unix4j.unix.Sort;
import org.unix4j.unix.grep.GrepOption;
import org.unix4j.util.Range;
import org.unix4j.variable.Arg;

import src.util.MyExec;
import src.jap.dict.MyDict;
import src.data.MyTranslation;
import src.file.MyTextFileReader;
import src.params.MyParams;
import src.util.log.MyLogToStdout;
import src.util.string.MyStringUtils;


public class MyEdict2_Light implements MyDict
{
	int N_MAX_TRANSLATION_LENGTH = 30;
	
	
	//перевод одного слова
	@Override
	public MyTranslation getTranslation( String s_word )
	{
		if( s_word.indexOf( "*" ) != -1 ) { return new MyTranslation( s_word, "", "", "", "" ); }
		
		MyExec me = new MyExec();
		String s_command = "perl " + MyParams.getStringValue( "get_word_translation_by_myedict2_path" ) + " " + s_word;
		String s_str = me.executeCommand( s_command );
		if( s_str.length() <= 1 ) { return new MyTranslation( s_word, "", "...", "", "" ); }
		
		String s_transl = this.parseTranslation( s_str );
		String s_read = this.parseReading( s_str );
		
		if( s_transl.length() > this.N_MAX_TRANSLATION_LENGTH ) { s_transl = this.makeTranslationShorten( s_transl ); }
		
		return new MyTranslation( s_word, s_read, s_transl, "", "" );
	}
	
	
	//перевод нескольких слов
	@Override
	public ArrayList< MyTranslation > getTranslation( ArrayList< String > s_words_al )
	{
		return null;
	}
	
	
	//Укоротить перевод
	private String makeTranslationShorten( String s_res )
	{
		MyStringUtils msu = new MyStringUtils();
		
		//Удаляем все что после (3), tckb tcnm (1) и (2)
		if( s_res.indexOf( "(2)" ) != -1 )
		{
			int n_index = s_res.indexOf( "(3)" );
			if( n_index != -1 ) { s_res = s_res.substring( 0, n_index ); }
			
			n_index = s_res.indexOf( "(2)" );
			if( n_index != -1 ) { s_res = s_res.replace( "(2)", "\n(2)" ); }
		}
		else if( s_res.indexOf( ", " ) != -1 ) { s_res = msu.splitOnTwo( s_res, ",", N_MAX_TRANSLATION_LENGTH ); }
		else if( s_res.indexOf( "; " ) != -1 ) { s_res = msu.splitOnTwo( s_res, ";", N_MAX_TRANSLATION_LENGTH ); }
		else if( s_res.indexOf( ". " ) != -1 ) { s_res = msu.splitOnTwo( s_res, ".", N_MAX_TRANSLATION_LENGTH ); }
		else if( s_res.indexOf( ") " ) != -1 ) { s_res = msu.splitOnTwo( s_res, ")", N_MAX_TRANSLATION_LENGTH ); }
		
		return s_res;
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
		catch( Exception e ) {} //mlts.writeMess( "Не удалось прочесть перевод из строки - \"" + s_string + "\"" ); }
		
		return s_res;
	}
	
	
	//Распарсить чтение
	private String parseReading( String s_string )
	{
		MyLogToStdout mlts = new MyLogToStdout();
		String s_res = "";
		String s_start_tag = "<read>";
		String s_end_tag = "</read>";
		
		try
		{
			int n_start = s_string.indexOf( s_start_tag ) + s_start_tag.length();
			int n_end = s_string.indexOf( s_end_tag );
			s_res = s_string.substring( n_start, n_end );
		}
		catch( Exception e ) {} //mlts.writeMess( "Не удалось прочесть чтение из строки - \"" + s_string + "\"" ); }
		
		return s_res;
	}
}
