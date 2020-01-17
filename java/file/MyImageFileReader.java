package src.file;


import java.io.*;
import java.awt.Image;
import java.awt.image.*;
import javax.imageio.ImageIO;

import java.util.ArrayList;

import src.util.log.MyLogToStdout;


public class MyImageFileReader
{
	//Прочесть файл как картинку
	public BufferedImage readFileAsBufferedImage( String s_path ) { return this.readFileAsBufferedImage( new File( s_path ) ); }
	public BufferedImage readFileAsBufferedImage( File file )
	{
		BufferedImage res_image = null;
		
		try { res_image = ImageIO.read( file ); } 
		catch ( IOException e ) { e.printStackTrace(); }
		
		return res_image;
	}
}
