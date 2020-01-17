package src.ui.icon;


import java.awt.Image;
import java.awt.image.*;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.swing.ImageIcon;

import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import src.params.MyParams;
import src.util.log.MyLogToStdout;
import src.file.*;


public class MyIconLoader
{
	//Ширина и высота иконки
	private int N_ICON_WIDTH = 24;
	private int N_ICON_HEIGHT = 24;
	
	private static HashMap< String, ImageIcon > res_map = new HashMap< String, ImageIcon >();
	private static ImageIcon blank_icon = null;
	private static boolean b_flag = true;
	
	
	public MyIconLoader()
	{
		if( this.b_flag )
		{
			this.b_flag = false;
			this.res_map = this.readToHashMap( MyParams.getStringValue( "icon_path" ) );
			
			BufferedImage blank_image = new BufferedImage( N_ICON_WIDTH, N_ICON_HEIGHT, BufferedImage.TYPE_INT_ARGB );
			Graphics2D g2d = blank_image.createGraphics();
			g2d.setPaint( Color.decode( "#FF00FF" ) );
			g2d.fillRect ( 0, 0, blank_image.getWidth(), blank_image.getHeight() );
			blank_icon = new ImageIcon( blank_image ); 
		}
	}
	
	
	//Читаем все файлы-иконки в карту
	private HashMap< String, ImageIcon > readToHashMap( String s_path )
	{
		MyLogToStdout mlts = new MyLogToStdout();
		mlts.writeMess( "Read icon dir: \"" + s_path + "\"..." );
		
		MyFileUtils mfu = new MyFileUtils();
		MyImageFileReader mifr = new MyImageFileReader();
		HashMap< String, ImageIcon > res_img_map = new HashMap< String, ImageIcon >();
		
		ArrayList< File > image_files = mfu.getAllFilesInDir( s_path );
		
		for( File f_pos : image_files )
		{
			String s_name = f_pos.getName();
			BufferedImage bi_pos = mifr.readFileAsBufferedImage( f_pos );
			ImageIcon icon = new ImageIcon( bi_pos );
			icon = this.resize( icon, this.N_ICON_WIDTH, this.N_ICON_HEIGHT );
			res_img_map.put( s_name, icon );
		}
		
		return res_img_map;
	}
	
	
	private ImageIcon resize( ImageIcon src_icon, int n_new_width, int n_new_height )
	{
		Image image = src_icon.getImage();
		Image resized_image = image.getScaledInstance( n_new_width, n_new_height,  java.awt.Image.SCALE_SMOOTH );
		ImageIcon res_icon = new ImageIcon( resized_image );
		
		return res_icon;
	}
	
	
	
	//Получить картинку по имени
	public ImageIcon getIcon( String s_name )
	{
		ImageIcon res_icon = this.res_map.get( s_name );
		
		if( res_icon == null ) { return this.blank_icon; }
		return res_icon;
	}
}
