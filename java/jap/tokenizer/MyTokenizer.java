package src.jap.tokenizer;


import java.util.ArrayList;
import src.data.MyJapanWord;



public interface MyTokenizer
{
	public ArrayList< MyJapanWord > getWordsArrayList( String s_text );
}
