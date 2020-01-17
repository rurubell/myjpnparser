package src.jap.tokenizer.kuromoji;


import java.util.ArrayList;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import java.lang.reflect.Method;

import src.jap.tokenizer.MyTokenizer;
import src.data.MyJapanWord;
import src.util.log.MyLogToStdout;


public class MyKuromoji implements MyTokenizer
{
	private static Tokenizer tokenizer = null;
	private static boolean b_flag = true;
	
	public MyKuromoji()
	{
		if( this.b_flag )
		{
			MyLogToStdout mlts = new MyLogToStdout();
			mlts.writeMess( "Kuromoji init..." );
			this.tokenizer = new Tokenizer();
			this.b_flag = false;
		}
	}
	
	
	//Получить ArrayList слов и их типов
	public ArrayList< MyJapanWord > getWordsArrayList( String s_text )
	{
		ArrayList< MyJapanWord > res_list = new ArrayList< MyJapanWord >();
		
		for( Token token : tokenizer.tokenize( s_text ) )
		{
			String s_type = this.getWordType( token );
			MyJapanWord mjw = new MyJapanWord
			( 
				token.getSurface(), 
				s_type,
				token.getReading(),
				token.getBaseForm()
			);
			
			res_list.add( mjw );
		}
		
		return res_list;
	}
	
	
	//Определить тип слова
	private String getWordType( Token token )
	{
		String s_part_of_speech = token.getPartOfSpeechLevel1();
		
		if( s_part_of_speech.indexOf( "連体詞" ) != -1 ) { return "adnominal"; }
		if( s_part_of_speech.indexOf( "名詞" ) != -1 ) { return "noun"; }
		if( s_part_of_speech.indexOf( "動詞" ) != -1 ) { return "verb"; }
		if( s_part_of_speech.indexOf( "助動詞" ) != -1 ) { return "alverb"; }
		if( s_part_of_speech.indexOf( "副詞" ) != -1 ) { return "adverb"; }
		if( s_part_of_speech.indexOf( "形容詞" ) != -1 ) { return "adjective"; }
		if( s_part_of_speech.indexOf( "助詞" ) != -1 ) { return "particle"; }
		if( s_part_of_speech.indexOf( "記号" ) != -1 ) { return "symbol"; }
		
		return "none";
	}
}
