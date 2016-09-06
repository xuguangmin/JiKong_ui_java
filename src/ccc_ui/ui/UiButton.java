package ccc_ui.ui;

import ccc_ui.component.IWndBase;
import ccc_ui.component.WndButton;

public class UiButton extends UiBase implements UiEventListener
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
	
	public UiButton()
	{
		styleNormal  = new UiStyle();
		stylePressed = new UiStyle();
		styleFocused = new UiStyle();
		
		fontNormal  = new UiFont();
		fontFocused = new UiFont();
		fontPressed = new UiFont();
		SetUiType(UiBase.XML_BUTTON);
	}
	
	@Override
	public void UiEvent(int eventId, int value) 
	{
		switch(eventId)
		{
		case UiEventEnum.ButtonControlPressed:
		case UiEventEnum.ButtonControlReleased:
			XmlUiEvent(eventId);
			break;
			
		case UiEventEnum.ButtonControlClick:
			XmlUiEvent(eventId, warning > 0 ? true:false, warningText, jumpPage, agClick);
			break;
		}	
	}
				
	// 设置Image对象的属性
	private void SetUiImageAtrr(WndButton wndButton)
	{
		if(null == wndButton)
			return;
	
		wndButton.AddEventListener(this);
		wndButton.SetDrawText(this.text);
		
		wndButton.setStyleNormal(styleNormal.colorForeground,   styleNormal.colorBackground);
		wndButton.setStylePressed(stylePressed.colorForeground, stylePressed.colorBackground);
		wndButton.setStyleFocused(styleFocused.colorForeground, styleFocused.colorBackground);
		
		wndButton.setImageNormal(SkinManager.GetBitmap(styleNormal.bkImage));
		wndButton.setImagePressed(SkinManager.GetBitmap(stylePressed.bkImage));
		
		wndButton.setFontNormal(CreateFont(fontNormal,   1));
		wndButton.setFontPressed(CreateFont(fontPressed, 1));
		wndButton.setFontFocused(CreateFont(fontFocused, 1));
		
		wndButton.SetTextAlignNormal(AlignXmlToH(fontNormal.textAlign), AlignXmlToV(fontNormal.textAlign));
		wndButton.SetTextAlignPressed(AlignXmlToH(fontPressed.textAlign), AlignXmlToV(fontPressed.textAlign));
	}
		
	public void SetUiAttr(IWndBase v)
	{
		super.SetUiAttr(v);
		SetUiImageAtrr((WndButton)v);
	}
			
	public void UpdateSize(double scaleX, double scaleY) 
	{
		super.UpdateSize(scaleX, scaleY);
		
		WndButton wndButton = (WndButton)GetWnd();
		if(null == wndButton)
			return;
				
		wndButton.setFontNormal(CreateFont(fontNormal,   scaleY));
		wndButton.setFontPressed(CreateFont(fontPressed, scaleY));
		wndButton.setFontFocused(CreateFont(fontFocused, scaleY));
	}
}

