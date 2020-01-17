package src.data;


import com.mariten.kanatools.*;
import src.jap.tokenizer.kuromoji.*;


public class MyJapanWord
{
	private static String S_HIRAGANA = "まあいうえおかがきぎくぐけげこごさざしじすずせぜそぞただちぢつづてでとどなにぬねのはばぱひびぴふぶぷへべぺほぼぽまみむめもやゆよらりるれろわをん";
	private static String S_KATAKANA = "マアイウエオカガキギクグケゲコゴサザシジスズセゼソゾタダチヂツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモヤユヨラリルレロワヲン";
	
	
	private String s_word = "";
	private String s_type = "";
	private String s_reading = "";
	private String s_hiragana = "";
	private String s_base_form = "";
	private boolean b_only_kana = false; //Указатель, в слове только кана или нет
	
	public MyJapanWord() {}
	public MyJapanWord( String s_word, String s_type, String s_kana, String s_base_form )
	{
		this.s_word = s_word;
		this.s_type = s_type;
		this.s_reading = s_kana;
		this.s_base_form = s_base_form;
		this.b_only_kana = this.isOnlyHiragana( s_word );
		
		try
		{
			this.s_hiragana = KanaConverter.convertKana( s_kana, KanaConverter.OP_ZEN_KATA_TO_ZEN_HIRA );
		} 
		catch ( Exception e ) {}
	}
	
	
	public void setWord( String s_word ) 
	{ 
		this.s_word = s_word; 
		this.b_only_kana = this.isOnlyHiragana( s_word );
	}
	
	public void setType( String s_type ) { this.s_type = s_type; }
	public void setReading( String s_reading ) { this.s_reading = s_reading; }
	public void setHiragana( String s_hiragana ) { this.s_hiragana = s_hiragana; }
	public void setBaseForm( String s_base_form ) { this.s_base_form = s_base_form; }
	
	public String getWord() { return this.s_word; }
	public String getType() { return this.s_type; }
	public String getReading() { return this.s_reading; }
	public String getHiragana() { return this.s_hiragana; }
	public String getBaseForm() { return this.s_base_form; }
	public boolean isOnlyHiragana() { return this.b_only_kana; }
	
	
	//Это слово состоит только их хираганы?
	public boolean isOnlyHiragana( String s_word )
	{
		for( int i = 0; i < s_word.length(); i++ )
		{
			if( this.S_HIRAGANA.indexOf( s_word.charAt(i) ) == -1 ) { return false; }
		}
		
		return true;
	}
	
	
	//Это простое слово?
	public boolean isSimple()
	{
		if( ( this.getBaseForm() == null ) || ( this.getWord().equals( this.getBaseForm() ) ) )
		{
			return true;
		}
		return false;
	}
	
	
	public void show() 
	{ 
		System.out.println( this.s_word + " " + this.s_type + " " + this.s_reading ); 
	}
}
