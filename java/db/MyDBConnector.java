package src.db;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import src.data.MyJapanWord;
import src.file.MyTextFileReader;
import src.params.MyParams;
import src.util.log.MyLogToStdout;


public class MyDBConnector
{
	//Получить Connection объект
	public Connection connect( String s_db_path )
	{
		String s_url = "jdbc:sqlite:" + s_db_path;
		Connection conn = null;
		
		try { conn = DriverManager.getConnection( s_url ); }
		catch (SQLException e) { System.out.println( e.getMessage() ); }
		
		return conn;
	}
	
	
	//Выполнить SQL запрос и получить ResultSet
	public ResultSet executeSQLAndGetResultSet( Connection conn, String s_sql )
	{
		ResultSet rs = null;
		
		try
		{
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery( s_sql );
		}
		catch( SQLException sql_e ) { sql_e.printStackTrace(); }
		
		return rs;
	}
	
	
	//Выполнить SQL запрос
	public void executeSQL( Connection conn, String s_sql )
	{
		try
		{
			Statement stmt = conn.createStatement();
			stmt.executeQuery( s_sql );
		}
		catch( SQLException sql_e ) { sql_e.printStackTrace(); }
	}
}
