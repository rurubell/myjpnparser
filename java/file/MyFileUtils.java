package src.file;

import java.io.*;
import java.util.ArrayList;

import src.util.log.MyLogToStdout;


public class MyFileUtils
{
	//Получить полный путь до файла
	public String getFullPath( String s_file_path )
	{
		String s_res = "";
		File file = new File( s_file_path );
		s_res = file.getAbsolutePath();
		
		return s_res;
	}
	
	
	//Получить все файлы в директории
	public ArrayList< File > getAllFilesInDir( String s_dir_path )
	{
		MyLogToStdout mlts = new MyLogToStdout();
		File f_dir = new File( s_dir_path );
		ArrayList< File > res_al = new ArrayList< File >();
		
		if( !f_dir.isDirectory() )
		{
			mlts.writeMess( s_dir_path + " - не является директорией!", "err" );
			System.exit( 0 );
		}
		else
		{
			for( File f_pos : f_dir.listFiles() ) { res_al.add( f_pos ); }
		}
		
		return res_al;
	}
}
