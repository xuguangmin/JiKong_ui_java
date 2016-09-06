package ccc_ui.ui;

import ccc_ui.component.IWndBase;
import ccc_ui.component.WndImage;

public class UiImage extends UiBase implements UiEventListener
{	
	public String  text;	
	public UiStyle styleNormal;
	public UiStyle stylePressed;
	public UiStyle styleFocused;
	
	public UiFont  fontNormal  = null;
	public UiFont  fontFocused = null;
	public UiFont  fontPressed = null;
	public int     warning     = 0;
	public String  warningText = "";
	public UiActionGroup  agClick = null;
	
	public UiImage()
	{
		styleNormal  = new UiStyle();
		stylePressed = new UiStyle();
		styleFocused = new UiStyle();
		
		fontNormal  = new UiFont();
		fontFocused = new UiFont();
		fontPressed = new UiFont();
		SetUiType(UiBase.XML_IMAGE);
	}

	@Override
	public void UiEvent(int eventId, int value) 
	{
		switch(eventId)
		{
		case UiEventEnum.IamgeControlPressed:
		case UiEventEnum.IamgeControlReleased:
			XmlUiEvent(eventId);
			break;
			
		case UiEventEnum.IamgeControlClick:
			XmlUiEvent(eventId, warning > 0 ? true:false, warningText, jumpPage, agClick);
			break;
		}
	}
	
	// 设置Image对象的属性
	private void SetUiImageAtrr(WndImage wndImage)
	{
		if(null == wndImage)
			return;
		
		wndImage.AddUiEventListener(this);
		wndImage.SetDrawText(this.text);			
		
		wndImage.setStyleNormal(styleNormal.colorForeground,   styleNormal.colorBackground);
		wndImage.setStylePressed(stylePressed.colorForeground, stylePressed.colorBackground);
		wndImage.setStyleFocused(styleFocused.colorForeground, styleFocused.colorBackground);
		
		wndImage.setImageNormal(SkinManager.GetBitmap(styleNormal.bkImage));
		wndImage.setImagePressed(SkinManager.GetBitmap(stylePressed.bkImage));
		
		wndImage.setFontNormal(CreateFont(fontNormal,   1));
		wndImage.setFontPressed(CreateFont(fontPressed, 1));
		wndImage.setFontFocused(CreateFont(fontFocused, 1));
		
		wndImage.SetTextAlignNormal(AlignXmlToH(fontNormal.textAlign), AlignXmlToV(fontNormal.textAlign));
		wndImage.SetTextAlignPressed(AlignXmlToH(fontPressed.textAlign), AlignXmlToV(fontPressed.textAlign));
	}
	
	// 从外部传入一个Image对象
	public void SetUiAttr(IWndBase v)
	{
		super.SetUiAttr(v);
		SetUiImageAtrr((WndImage)v);
	}
	
	public void UpdateSize(double scaleX, double scaleY) 
	{
		super.UpdateSize(scaleX, scaleY);
		
		WndImage wndImage = (WndImage)GetWnd();
		if(null == wndImage)
			return;
				
		wndImage.setFontNormal(CreateFont(fontNormal,   scaleY));
		wndImage.setFontPressed(CreateFont(fontPressed, scaleY));
		wndImage.setFontFocused(CreateFont(fontFocused, scaleY));
	}
}
