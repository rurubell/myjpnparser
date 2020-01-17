package src.params.arg;


import java.util.ArrayList;

import src.params.MyParams;


public class MyArgReader
{
	public MyArgReader( String[] args )
	{
		MyParams.setValue( "conf_path", this.getValueOfArg( "--conf_path", args ) );
	}
	
	
	//Получить значение параметра
	public String getValueOfArg( String s_arg, String[] args )
	{
		String s_res = "";
		int n_arg_pos = this.isArgExists( s_arg, args );
		
		if( n_arg_pos >= 0 )
		{
			try { s_res = args[ n_arg_pos + 1 ]; }
			catch( Exception e )
			{
				System.out.println( "Не удалось получить значение параметра " + s_arg + ", завершаем работу..." );
				System.exit( 0 );
			}
		}
		else
		{
			System.out.println( "Не указан параметр " + s_arg + ", завершаем работу..." );
			System.exit( 0 );
		}
		
		return s_res;
	}
	
	
	//Проверка параметра на существование
	private int isArgExists( String s_arg, String[] args )
	{
		for( int i = 0; i < args.length; i++ )
		{
			if( args[i].contains( s_arg ) ) { return i; }
		}
		
		return -1;
	}
}
