package src.util.time;


import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class MyTimeUtils
{
	//Получить текущую дату-время в удобочитаемом формате
	public String getCurrentHumanizedDateAndTime()
	{
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		
		return sdf.format( gc.getTime() );
	}
	
	
	//Получить текущуее время в удобочитаемом формате
	public String getCurrentHumanizedTime()
	{
		GregorianCalendar gc = new GregorianCalendar();
		//SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm:ss:SSS" );
		SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm:ss" );
		
		return sdf.format( gc.getTime() );
	}
}
