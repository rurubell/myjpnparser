package src;


import java.util.ArrayList;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

import java.net.URLEncoder;

import src.jap.tokenizer.MyTokenizer;
import src.jap.tokenizer.kuromoji.MyKuromoji;
import src.data.MyJapanWord;
import src.ui.MyMainWin;
import src.util.MyExec;
import src.params.MyParams;
import src.params.arg.MyArgReader;
import src.params.conf.MyConfReader;
import src.net.MyServer;
import src.util.log.MyLogToStdout;
import src.file.MyTextFileReader;
import src.jap.simkanji.MySimKanji;
import src.jap.dict.jmdict.MyJMDICT;
import src.jap.dict.edict2.MyEdict2;
import src.jap.autorepl.*;
import src.jap.wordstat.MyWordStat;
import src.ui.win.*;
import src.ui.icon.MyIconLoader;
import src.net.MyGetSender;

import src.MyThread;


public class MyMain
{
	public static void main( String[] args )
	{
		MyUndecoratedFullscreenWindow mufw = new MyUndecoratedFullscreenWindow();
		mufw.setVisible( true );
		
		//MyMain.init( args );
		
		//MyServer ms = new MyServer();
		//ms.startServer();
	}
	
	
	private static void init( String[] args )
	{
		MyLogToStdout mlts = new MyLogToStdout();
		mlts.writeMess( "Main init..." );
		
		MyArgReader mar = new MyArgReader( args );
		MyConfReader mcr = new MyConfReader();
		
		mlts.writeMess( "Read config file: \"" + MyParams.getStringValue( "conf_path" ) + "\"..." );
		MyParams.setValue( mcr.readConfigFile( MyParams.getStringValue( "conf_path" ) ) );
		
		MyIconLoader mil = new MyIconLoader();
		MySimKanji msk = new MySimKanji();
		MyWordStat mws = new MyWordStat();
		MyTesseractAutoRepl mtar = new MyTesseractAutoRepl();
		MyOCRSpaceAutoRepl mosar = new MyOCRSpaceAutoRepl();
		MyTokenizer mjta = new MyKuromoji();
		
		System.gc();
		
		mlts.writeMess( "Ready!", "ok" );
		mlts.jump();
	}
}
