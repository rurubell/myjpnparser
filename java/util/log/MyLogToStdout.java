package src.util.log;


import src.util.time.MyTimeUtils;


public class MyLogToStdout
{
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_BLACK = "\u001B[30m";
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_BLUE = "\u001B[34m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";
	
	private static final String ANSI_BLACK_BRIGHT = "\033[0;90m";
	private static final String ANSI_RED_BRIGHT = "\033[0;91m";
	private static final String ANSI_GREEN_BRIGHT = "\033[0;92m";
	private static final String ANSI_YELLOW_BRIGHT = "\033[0;93m";
	private static final String ANSI_BLUE_BRIGHT = "\033[0;94m";
	private static final String ANSI_PURPLE_BRIGHT = "\033[0;95m";
	private static final String ANSI_CYAN_BRIGHT = "\033[0;96m";
	private static final String ANSI_WHITE_BRIGHT = "\033[0;97m";
	
	
	//Записать сообщение
	public void writeMess( String s_mess )
	{
		MyTimeUtils mtu = new MyTimeUtils();
		s_mess = mtu.getCurrentHumanizedTime() + " - " + s_mess;
		System.out.println( s_mess );
	}
	
	//Записать сообщение с типом
	public void writeMess( String s_mess, String s_type )
	{
		MyTimeUtils mtu = new MyTimeUtils();
		String s_time = mtu.getCurrentHumanizedTime();
		
		if( s_type.indexOf( "err" ) != -1 ) { System.out.println( ANSI_RED_BRIGHT + s_time + " - " + s_mess + ANSI_RESET ); }
		if( s_type.indexOf( "ok" ) != -1 ) { System.out.println( ANSI_GREEN_BRIGHT + s_time + " - " + s_mess + ANSI_RESET ); }
		if( s_type.indexOf( "warn" ) != -1 ) { System.out.println( ANSI_YELLOW_BRIGHT + s_time + " - " + s_mess + ANSI_RESET ); }
	}
	
	
	//Вставить отступ
	public void jump() { System.out.print( "\n\n" ); }
}
