package src.util;


import java.lang.reflect.Method;


public class MyObjectUtils
{
	//Печать всех методов Объекта
	public void printAllMethodsOfObject( Object obj )
	{
		Class c = obj.getClass();
		Method[] methods = c.getMethods();
		
		for( Method method : methods )
		{
			System.out.println( method.getName() );
		}
	}
}
