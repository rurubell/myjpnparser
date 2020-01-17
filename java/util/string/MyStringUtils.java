package src.util.string;


public class MyStringUtils
{
	//Удалить все shell escape символы
	public String deleteBashEsc( String s_src )
	{
		String s_res = s_src;
		
		s_res = s_res.replace( "[1m", "" );
		s_res = s_res.replace( "[4m", "" );
		s_res = s_res.replace( "[22m", "" );
		s_res = s_res.replace( "[24m", "" );
		s_res = s_res.replace( "", "" );
		s_res = s_res.replace( "", "" );
		return s_res;
	}
	
	
	//Разбить строку на две, по символу, близжайшему от позиции
	public String splitOnTwo( String s_src, String s_symb, int n_pos )
	{
		String s_res = s_src;
		
		if( s_src.indexOf( s_symb ) == -1 ) { return s_src; }
		if( n_pos >= s_src.length() ) { return s_src; };
		
		//Определяем близжайший символ от позиции
		int n_index = 0;
		int n_lindex = s_src.indexOf( s_symb, n_pos );
		int n_rindex = s_src.lastIndexOf( s_symb, n_pos );
		
		if( n_lindex <= 0 ) { n_index = n_rindex; }
		else if( n_rindex <= 0 ) { n_index = n_lindex; }
		else if( ( n_lindex - n_pos ) <= ( n_pos - n_rindex ) ) { n_index = n_lindex; }
		else { n_index = n_rindex; }
		n_index += s_symb.length();
		
		try
		{
			String str_1 = s_src.substring( 0, n_index );
			String str_2 = s_src.substring( n_index );
			s_res = str_1 + "\n" + str_2;
		} 
		catch ( Exception e ) {}
		s_res = s_res.replace( "\n ", "\n" );
		
		return s_res;
	}
}
