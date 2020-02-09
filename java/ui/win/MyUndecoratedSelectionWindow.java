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
import static java.awt.GraphicsDevice.WindowTranslucency.*;


public class MyUndecoratedSelectionWindow extends JFrame
{
	private int n_bg_alpha = 10;
	private int n_border_alpha = 255;
	
	
	public MyUndecoratedSelectionWindow( int n_v_offset, int n_h_offset, int n_width, int n_height, Color main_color )
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		
		if ( !gd.isWindowTranslucencySupported( TRANSLUCENT ) ) {}
		else
		{
			Color bg_color = new Color( main_color.getRed(), main_color.getGreen(), main_color.getBlue(), this.n_bg_alpha );
			Color border_color = new Color( main_color.getRed(), main_color.getGreen(), main_color.getBlue(), this.n_border_alpha );
			
			this.setLocation( n_h_offset, n_v_offset );
			this.setSize( n_width, n_height );
			this.setUndecorated( true );
			
			this.setType( Type.POPUP );
			this.setOpacity( 0.35f );
			this.setBackground( bg_color );
			
			JPanel main_panel = new JPanel();
			main_panel.setBackground( bg_color );
			main_panel.setBorder( BorderFactory.createLineBorder( border_color, 3 ) );
			
			this.add( main_panel );
			
			this.setVisible( true );
		}
	}
}
