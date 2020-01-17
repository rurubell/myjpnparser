package src.ui.button;


import javax.swing.*;
import java.awt.*;


public class MySimKanjiButton extends JButton
{
	private String s_kanji = null;
	
	
	public String getKanji() { return this.s_kanji; }
	
	
	public MySimKanjiButton( String s_label )
	{
		super( s_label );
		this.s_kanji = s_label;
		this.setFont( new Font( "Sans", Font.PLAIN, 30 ) );
		//this.setAlignmentX( Component.CENTER_ALIGNMENT );
	}
}
