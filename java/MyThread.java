package src;


import java.util.ArrayList;
import java.awt.MouseInfo;

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
import src.ui.win.MyJapToRuJardicTranslateWin;
import src.ui.win.MyUndecoratedSelectionWindow;
import src.ui.icon.MyIconLoader;
import src.net.MyGetSender;


public class MyThread extends Thread
{
	MyUndecoratedSelectionWindow musl = null;
	
	public void run()
	{
		for(;;)
		{
			System.out.println( MouseInfo.getPointerInfo().getLocation() );
			try
			{
				Thread.sleep( 50 );
			}
			catch( Exception e ) { e.printStackTrace(); }
		}
	}
}
