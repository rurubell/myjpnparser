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
import src.jap.dict.elib.MyELIB;


public class MyJapToRuELIBTranslateWin extends JFrame
{
	public MyJapToRuELIBTranslateWin( String s_text )
	{
		this.setTitle( "e-lib.ua - " + s_text );
		this.setMinimumSize( new Dimension( 100, 100 ) );
		
		MyELIB me = new MyELIB();
		String s_transl = me.getTranslation( s_text ).getRawData();
		
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
