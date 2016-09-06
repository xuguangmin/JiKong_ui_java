package ccc_ui.ui;

import java.awt.Color;

public class UiStyle 
{
	public String bkImage = null;
	public int    imageStyle = IMAGE_STYLE_STRETCH;
	public Color  colorForeground = null;
	public Color  colorBackground = null;
	
	public static final int IMAGE_STYLE_STRETCH  = 0;           // 拉伸
	public static final int IMAGE_STYLE_PINGPU   = 1;           // 平铺
	public static final int IMAGE_STYLE_SUOFANG  = 2;           // 按比例缩放
}
