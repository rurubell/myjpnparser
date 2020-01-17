package src.file;

import java.io.*;
import java.util.ArrayList;

import src.util.log.MyLogToStdout;


public class MyFolderViewer
{
	//Получить список всех файлов/директорий в директории
	public ArrayList< String > dir( String s_path )
	{
		ArrayList< String > res_list = new ArrayList< String >();
		MyLogToStdout mlts = new MyLogToStdout();
		File folder = new File( s_path );
		
		if( folder.isDirectory() )
		{
			for( File f_pos : folder.listFiles() ) { res_list.add( f_pos.getName() ); }
		}
		else { mlts.writeMess( "Запрос на список файлов в директории - " + s_path + ", который директорией не является..." ); }
		
		return res_list;
	}
	
	
	//Получить список всех файлов с поределенным расширением в директории
	public ArrayList< String > dir( String s_path, String s_fname_ext )
	{
		ArrayList< String > res_list = new ArrayList< String >();
		ArrayList< String > all_files_list = this.dir( s_path );
		
		for( String s_pos : all_files_list )
		{
			if( s_pos.indexOf( s_fname_ext ) != -1 ) { res_list.add( s_pos ); }
		}
		
		return res_list;
	}
}
