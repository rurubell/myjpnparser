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
import java.util.TimerTask;
import java.util.Timer;

import static java.awt.GraphicsDevice.WindowTranslucency.*;

 
public class MyUndecoratedFullscreenWindow extends JFrame
{
	JFrame self_link = this;
	MyUndecoratedSelectionWindow musw = null;
	
	//Положение селектового окна
	int n_select_win_x = 0;
	int n_select_win_y = 0;
	
	//Ширина и высота селектового окна
	int n_select_win_width = 0;
	int n_select_win_height = 0;
	
	//Мы нажали левую кнопку мыши, и начали рисовать окно?
	boolean b_lmb_is_pressed = false;
	
	
	public MyUndecoratedFullscreenWindow()
	{
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.setLocation( 0, 0 );
		this.setSize( (int) dim.getWidth(), (int) dim.getHeight() );
		this.setUndecorated( true );
		this.setOpacity( 0.0f );
		//this.setType( Type.POPUP );
		this.setCursor( new Cursor( Cursor.CROSSHAIR_CURSOR ) );
		
		this.setVisible( true );
		this.addMouseListener( new MyMouseListener() );
		
		Timer timer = new Timer( "Timer" );
		timer.schedule( new MyTimerTask(), 0, 50 );
	}
	
	
	class MyTimerTask extends TimerTask
	{
		public void run()
		{
			if( b_lmb_is_pressed )
			{
				Point p = MouseInfo.getPointerInfo().getLocation();
				
				int n_width = 0;
				int n_height = 0;
				int n_x_location = n_select_win_x;
				int n_y_location = n_select_win_y;
				
				if( p.x < n_select_win_x ) 
				{ 
					n_width = n_select_win_x - p.x;
					n_x_location = p.x;
				}
				else { n_width = p.x - n_select_win_x; }
				
				if( p.y < n_select_win_y ) 
				{ 
					n_height = n_select_win_y - p.y;
					n_y_location = p.y;
				}
				else { n_height = p.y - n_select_win_y; }
				
				musw.setLocation( n_x_location, n_y_location );
				musw.setSize( n_width, n_height );
			}
		}
	}
	
	
	class MyMouseListener implements MouseListener
	{
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		
		public void mousePressed(MouseEvent e) 
		{
			n_select_win_x = e.getX();
			n_select_win_y = e.getY();
			
			if( musw != null ) 
			{
				musw.setVisible(false);
				musw.dispose();
			}
			
			musw = new MyUndecoratedSelectionWindow( n_select_win_y, n_select_win_x, 0, 0, Color.decode( "#FF0000" ) );
			
			b_lmb_is_pressed = true;
		}
		
		public void mouseReleased(MouseEvent e) 
		{  
			b_lmb_is_pressed = false;
		}
	}
}
