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
	public Connection makeConnection( String s_db_path ) throws SQLException
	{
		String s_url = "jdbc:sqlite:" + s_db_path;
		Connection conn = DriverManager.getConnection( s_url );
		
		return conn;
	}
	
	
	//Выполнить SQL запрос и получить ResultSet
	public ResultSet executeSQLAndGetResultSet( Connection conn, String s_sql ) throws SQLException
	{
		ResultSet rs = null;
		
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery( s_sql );
		
		return rs;
	}
	
	
	//Выполнить SQL запрос
	public void executeSQL( Connection conn, String s_sql ) throws SQLException
	{
			Statement stmt = conn.createStatement();
			stmt.executeUpdate( s_sql );
	}
}
