package src.ui.win;


import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.text.Document;

import src.data.MyJapanWord;
import src.data.MyTranslation;

import src.ui.button.*;
import src.ui.panel.*;

import src.jap.transhell.MyTranslateShell;
import src.jap.dict.jardic.MyJardic;



public class MyJapToRuJardicTranslateWin extends JFrame
{
	public MyJapToRuJardicTranslateWin( String s_text )
	{
		this.setTitle( "Jardic.ru - " + s_text );
		this.setMinimumSize( new Dimension( 100, 100 ) );
		
		MyJardic mj = new MyJardic();
		String s_transl = mj.getTranslation( s_text ).getRawData();
		
		JEditorPane jEditorPane = new JEditorPane();
		jEditorPane.setEditable(false);
		HTMLEditorKit kit = new HTMLEditorKit();
		jEditorPane.setEditorKit( kit );
		StyleSheet styleSheet = kit.getStyleSheet();
		
		styleSheet.addRule( ".kanji { font-weight: bold; }" );
		styleSheet.addRule( ".kana { font-weight: bold; }" );
		styleSheet.addRule( ".warodaientry { text-size: 300%; }" );
		
		Document doc = kit.createDefaultDocument();
		jEditorPane.setDocument(doc);
		jEditorPane.setText( s_transl );
		
		this.add( jEditorPane );
		this.pack();
	}
}
