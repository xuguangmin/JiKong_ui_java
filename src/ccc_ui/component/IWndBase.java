package ccc_ui.component;

import javax.swing.JComponent;

//该接口做为控件的基类
public interface IWndBase 
{
	public static final int ALIGN_UP     = 1; // 上
	public static final int ALIGN_DOWN   = 2; // 下
	public static final int ALIGN_LEFT   = 3; 
	public static final int ALIGN_RIGHT  = 4;
	public static final int ALIGN_MIDDLE = 5;
	
	
	public JComponent GetOwner();
	//public void Clear();
	//public void SetSize(int x, int y, int width, int height);
	
	//public void SetTextSize(int size);
	
	/*
	public void setStylePressed(int clForeground, int clBackground, Bitmap bkImage);	
	public void setStyleNormal(int clForeground, int clBackground, Bitmap bkImage);
	public void SetDrawText(String text);
	
	public void setSize(int x, int y, int width, int height);
	
	public void AddEventListener(IWndEvent l);
	
	
	
	public void SetFontSize(int size);
	public void setFontNormal(Typeface fontName);
	*/
}
