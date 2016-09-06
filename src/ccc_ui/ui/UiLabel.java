package ccc_ui.ui;

import ccc_ui.component.IWndBase;
import ccc_ui.component.WndLabel;

public class UiLabel extends UiBase 
{
	public String   text;
	public UiStyle styleNormal;
	public UiFont  fontNormal  = null;
	
	public UiLabel()
	{
		styleNormal = new UiStyle();
		fontNormal  = new UiFont();
		SetUiType(UiBase.XML_LABEL);
	}
			
	// 设置对象的属性
	private void SetUiLabelAtrr(WndLabel wndLabel)
	{
		if(null == wndLabel)
			return;
		
		wndLabel.SetDrawText(this.text);
		wndLabel.setStyleNormal(styleNormal.colorForeground, styleNormal.colorBackground);
		wndLabel.setImageNormal(SkinManager.GetBitmap(styleNormal.bkImage));
		wndLabel.setFontNormal(CreateFont(fontNormal, 1));
		wndLabel.SetTextAlignNormal(AlignXmlToH(fontNormal.textAlign), AlignXmlToV(fontNormal.textAlign));
	}
	
	// 从外部传入一个对象
	public void SetUiAttr(IWndBase v)
	{
		super.SetUiAttr(v);
		SetUiLabelAtrr((WndLabel)v);
	}
		
	public void UpdateSize(double scaleX, double scaleY) 
	{
		super.UpdateSize(scaleX, scaleY);
		
		WndLabel wndLabel = (WndLabel)GetWnd();
		if(null == wndLabel)
			return;
		
		wndLabel.setFontNormal(CreateFont(fontNormal, scaleY));
	}
}
