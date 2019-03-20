package main.java.utils;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class ScreenUtils
{
	public static Rectangle getCenteredShape(double width, double height)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return new Rectangle(	(int) (screenSize.getWidth()/2 - width/2),
								(int) (screenSize.getHeight()/2 - height/2),
								(int) width, (int) height);
	}
}
