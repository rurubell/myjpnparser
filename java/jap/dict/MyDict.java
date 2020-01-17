package src.jap.dict;


import java.util.ArrayList;

import src.data.MyTranslation;


public interface MyDict
{
	//перевод одного слова
	public MyTranslation getTranslation( String s_word );
	//перевод нескольких слов
	public ArrayList< MyTranslation > getTranslation( ArrayList< String > s_words_al );
}
