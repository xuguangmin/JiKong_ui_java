package ccc_ui.ui;

import ccc_ui.component.IWndBase;
import ccc_ui.component.WndCheckBox;

public class UiCheckBox extends UiBase implements UiEventListener
{
	public String   text;
	public UiStyle  styleNormal;
	public UiFont   fontNormal  = null;
	public boolean  checked = false;
	
	public UiCheckBox()
	{
		styleNormal = new UiStyle();
		fontNormal  = new UiFont();
		SetUiType(UiBase.XML_CHECKBOX);
	}
	
	@Override
	public void UiEvent(int eventId, int value) 
	{
		this.XmlUiEvent(eventId, value);
		this.PrintLog(String.format("value = %d", value));	
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
		
		
		WndCheckBox wndCheckBox = (WndCheckBox)GetWnd();
		if(wndCheckBox != null)
		{		
			switch(property)
			{
			case UiPropertyEnum.CHECKED:
				wndCheckBox.setSelected(this.checked);
				break;
			}
		}
	}
	
	// 设置对象的属性
	private void SetUiCheckBoxAtrr(WndCheckBox wndCheckBox)
	{
		if(null == wndCheckBox)
			return;
		
		wndCheckBox.AddUiEventListener(this);
		wndCheckBox.setSelected(this.checked);
		wndCheckBox.SetDrawText(this.text);
		
		wndCheckBox.SetStyleNormal(styleNormal.colorForeground, styleNormal.colorBackground);
		wndCheckBox.SetImageNormal(SkinManager.GetBitmap(styleNormal.bkImage));
		wndCheckBox.SetFontNormal(CreateFont(fontNormal, 1));
	}
		
	// 从外部传入一个对象
	public void SetUiAttr(IWndBase v)
	{
		super.SetUiAttr(v);
		SetUiCheckBoxAtrr((WndCheckBox)v);
	}
		
	public void UpdateSize(double scaleX, double scaleY) 
	{
		super.UpdateSize(scaleX, scaleY);
				
		WndCheckBox wndCheckBox = (WndCheckBox)GetWnd();
		if(null == wndCheckBox)
			return;
		
		wndCheckBox.SetFontNormal(CreateFont(fontNormal, scaleY));
	}
}
