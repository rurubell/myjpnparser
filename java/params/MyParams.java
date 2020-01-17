package src.params;


import java.util.HashMap;
import java.util.Map;

import src.util.log.MyLogToStdout;


//Класс для хранения параметров конфигурационного файла
public class MyParams
{
	//Хеш-мап в котором хранятся параметры конфига
	private static HashMap< String, String > hm_config = new HashMap< String, String >();
	
	
	//Получить строковое значение из хеш-мапа параметров
	public static synchronized String getStringValue( String s_key )
	{
		isKeyOK( s_key );
		return hm_config.get( s_key );
	}
	
	
	//Получить целое значение из хеш-мапа параметров
	public static synchronized int getIntValue( String s_key )
	{
		isKeyOK( s_key );
		int n_res = 0;
		MyLogToStdout mlts = new MyLogToStdout();
		
		try { n_res = Integer.parseInt( hm_config.get( s_key ) ); }
		catch( Exception e ) 
		{
			mlts.writeMess( "Недопустимое int значение \"" + hm_config.get( s_key ) + "\" для ключа \"" + s_key + "\"", "warn" );
			mlts.writeMess( "Внимание! Значение возврата остается равным 0 (нулю)!", "warn" );
		}
		
		return n_res;
	}
	
	
	//Получить значение истина, ложь из хеш-мапа параметров
	public static synchronized boolean getBoolValue( String s_key )
	{
		boolean b_res = false;
		MyLogToStdout mlts = new MyLogToStdout();
		
		try { b_res = Boolean.parseBoolean( hm_config.get( s_key ) ); }
		catch( Exception e ) 
		{
			mlts.writeMess( "Недопустимое boolean значение \"" + hm_config.get( s_key ) + "\" для ключа \"" + s_key + "\"", "warn" );
			mlts.writeMess( "Внимание! Значение поля остается равным false (ложь)!", "warn" );
		}
		
		return b_res;
	}
	
	
	//Занести новое значение в хеш-мап
	public static synchronized void setValue( String s_key, String s_value ) { hm_config.put( s_key, s_value ); }
	public static synchronized void setValue( HashMap< String, String > new_map ) { hm_config.putAll( new_map ); }
	
	
	//Проверка ключа на существование
	private static void isKeyOK( String s_key )
	{
		MyLogToStdout mlts = new MyLogToStdout();
		
		if( !hm_config.containsKey( s_key ) )
		{
			mlts.writeMess( "Несуществующий параметр конфигурации \"" + s_key + "\", завершаем работу...", "err" );
			System.exit( 0 );
		}
	}
	
	
	//Печать всех значений конфига
	public static synchronized void show()
	{
		System.out.println( "Текущие значения конфигурации:" );
		System.out.println( "\"Ключ\" = \"Значение\"" );
		
		for( Map.Entry entry : hm_config.entrySet() ) 
		{
			System.out.println( "\"" + entry.getKey() + "\" = \"" + entry.getValue() + "\"" );
		}
	}
}
