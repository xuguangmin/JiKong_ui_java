package ccc_ui.ui;

public class UiFont 
{
	public String name = null; 
	public int    size; 
	public int    bold; 
	public int    italic;
	public int    underLine; 
	public int    strikeOut;
	public int    textAlign;
	
	public static final int FONT_NAME_SONG   = 1;     // 宋体
	public static final int FONT_NAME_HEI    = 2;     // 黑体
	public static final int FONT_NAME_KAISHU = 3;     // 楷体
	
	public static final int ALIGN_LEFT_MIDDLE   = 129; // 左对齐
	public static final int ALIGN_CENTER_MIDDLE = 132; // 居中对齐
	public static final int ALIGN_RIGHT_MIDDLE  = 130; // 右对齐
	public static final int ALIGN_LEFT_TOP      = 33;  // 左上
	public static final int ALIGN_CENTER_TOP    = 36;  // 中上
	public static final int ALIGN_RIGHT_TOP     = 34;  // 右上
	public static final int ALIGN_LEFT_BOTTOM   = 65;  // 左下
	public static final int ALIGN_CENTER_BOTTOM = 68;  // 中下
	public static final int ALIGN_RIGHT_BOTTOM  = 66;  // 右下
}
