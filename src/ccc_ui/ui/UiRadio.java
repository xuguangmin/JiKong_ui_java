package ccc_ui.ui;

import ccc_ui.component.IWndBase;
import ccc_ui.component.WndRadio;

public class UiRadio extends UiBase implements UiEventListener
{	
	public String   text;
	public UiStyle  styleNormal;
	public UiFont   fontNormal  = null;
	public boolean  checked = false;
	
	public UiRadio()
	{
		styleNormal = new UiStyle();
		fontNormal  = new UiFont();
		SetUiType(UiBase.XML_RADIO);
	}
	
	@Override
	public void UiEvent(int eventId, int value) 
	{
		this.XmlUiEvent(eventId, value);
	}
	
	public void SetProperty(int property, int value)
	{
		super.SetProperty(property, value);
		
		switch(property)
		{
		case UiPropertyEnum.CHECKED:
			this.checked = UiPropertyEnum.CHECKED_TRUE==value ? true:false;
			break;
		}
		
		WndRadio wndRadio = (WndRadio)GetWnd();
		if(wndRadio != null)
		{	
			switch(property)
			{
			case UiPropertyEnum.CHECKED:
				wndRadio.setSelected(this.checked);
				break;
			}
		}
	}
	
	// 设置对象的属性
	private void SetUiRadioAtrr(WndRadio wndRadio)
	{
		if(null == wndRadio)
			return;
		
		wndRadio.AddUiEventListener(this);	
		wndRadio.setSelected(this.checked);
		wndRadio.SetDrawText(this.text);	
		wndRadio.setStyleNormal(styleNormal.colorForeground, styleNormal.colorBackground);
		//wndRadio.SetImageNormal(SkinManager.GetBitmap(styleNormal.bkImage));
		wndRadio.setFontNormal(CreateFont(fontNormal, 1));
	}
	
	// 从外部传入一个对象
	public void SetUiAttr(IWndBase v)
	{
		super.SetUiAttr(v);
		SetUiRadioAtrr((WndRadio)v);
	}
	
	public void UpdateSize(double scaleX, double scaleY) 
	{
		super.UpdateSize(scaleX, scaleY);
		
		WndRadio wndRadio = (WndRadio)GetWnd();
		if(null == wndRadio)
			return;
		wndRadio.setFontNormal(CreateFont(fontNormal, scaleY));
	}
}
