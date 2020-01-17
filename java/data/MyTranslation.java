package src.data;


public class MyTranslation
{
	private String s_kanji = null;
	private String s_kana = null;
	private String s_en_translation = null;
	private String s_ru_translation = null;
	private String s_raw_data = null;
	
	
	public void setKanji( String s_kanji ) { this.s_kanji = s_kanji; }
	public void setKana( String s_kana ) { this.s_kana = s_kana; }
	public void setEnTranslation( String s_en_translation ) { this.s_en_translation = s_en_translation; }
	public void setRuTranslation( String s_ru_translation ) { this.s_ru_translation = s_ru_translation; }
	public void setRawData( String s_raw_data ) { this.s_raw_data = s_raw_data; }
	
	
	public String getKanji() { return this.s_kanji; }
	public String getKana() { return this.s_kana; }
	public String getEnTranslation() { return this.s_en_translation; }
	public String getRuTranslation() { return this.s_ru_translation; }
	public String getRawData() { return this.s_raw_data; }
	
	
	public MyTranslation() {}
	public MyTranslation( String s_kanji, String s_kana, String s_en_translation, String s_ru_translation, String s_raw_data )
	{
		this.s_kanji = s_kanji;
		this.s_kana = s_kana;
		this.s_en_translation = s_en_translation;
		this.s_ru_translation = s_ru_translation;
		this.s_raw_data = s_raw_data;
	}
}
