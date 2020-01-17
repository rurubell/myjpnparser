package src.params.conf;


import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

import src.file.MyTextFileReader;
import src.params.MyParams;


//Класс содержащий методы для инициализации конфиг-файла в MyConfValues
public class MyConfReader
{
	//Прочесть конфигурационный файл
	public HashMap< String, String > readConfigFile( String s_path )
	{
		HashMap< String, String > res_map = new HashMap< String, String >();
		
		MyTextFileReader mtfr = new MyTextFileReader();
		ArrayList< String > file_strings = mtfr.readFileAsStringAL( s_path );
		file_strings = this.deleteNotNeededStrings( file_strings );
		
		for( String s_pos : file_strings )
		{
			String[] s_pair = this.getPair( s_pos );
			res_map.put( s_pair[0], s_pair[1] );
		}
		
		return res_map;
	}
	
	
	//Записать конфигурационный файл
	public void writeConfigFile( String s_path, HashMap< String, String > params_map )
	{
		ArrayList< String > file_strings = new ArrayList< String >();
		
		for( Map.Entry entry : params_map.entrySet() )
		{
			file_strings.add( entry.getKey() + "=" + entry.getValue() + ";" );
		}
		
		MyTextFileReader mtfr = new MyTextFileReader();
		mtfr.writeALToFile( s_path, file_strings );
	}
	
	
	//Удаляем ненужные строки из коллекции строк
	private ArrayList< String > deleteNotNeededStrings( ArrayList< String > src_list )
	{
		ArrayList< String > res_list = new ArrayList< String >();
		
		for( int i = 0; i < src_list.size(); i++ )
		{
			if
			(
				( src_list.get(i).length() < 2 ) ||
				( src_list.get(i).indexOf("=") == -1 ) ||
				( src_list.get(i).indexOf(";") == -1 ) ||
				( src_list.get(i).indexOf("#") != -1 )
			)
			{ continue; }
			
			res_list.add( src_list.get(i) );
		}
		
		return res_list;
	}
	
	
	//Получаем пару параметр - значение ( String[2] )
	private String[] getPair( String s_src_string )
	{
		String[] s_res = new String[2];
		
		s_res[0] = s_src_string.substring( 0, s_src_string.indexOf( "=" ) );
		s_res[1] =  s_src_string.substring( s_src_string.indexOf( "=" ) + 1, s_src_string.indexOf( ";" ) );
		
		return s_res; 
	}
}
