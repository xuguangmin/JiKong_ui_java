package ccc_ui.ui;

import ccc_ui.component.IWndBase;
import ccc_ui.component.WndSlider;

public class UiSlider extends UiBase implements UiEventListener
{
	public String  bkImage;
	public UiStyle styleSlipper;
	public UiStyle styleBar;
	
	public int     percent         = 0;
	public int     percentShow     = 0;	        // 0 隐藏，1 显示
	public int     orientation     = 0;         // 滑动方向，0 横向，1 纵向
	public int     barWidth        = 10;        // 滑动条宽
	public int     barHeight       = 300;       // 滑动条高
	public int     slipperWidth    = 60;        // 滑块宽
	public int     slipperHeight   = 60;        // 滑块高
	
	public UiSlider()
	{
		styleBar = new UiStyle();
		styleSlipper = new UiStyle();
		SetUiType(UiBase.XML_SLIDER);		
	}
	
	@Override
	public void UiEvent(int eventId, int value) 
	{
		// value :slider percent
		this.XmlUiEvent(UiEventEnum.SliderValueChange, value);
		this.percent = value;
		//this.PrintLog(String.format("value = %d", value));	
	}
		
	public void SetProperty(int property, int value)
	{
		super.SetProperty(property, value);
		
		switch(property)
		{
		case UiPropertyEnum.CHANGED:
			this.percent = value;
			break;
		}
		
		
		WndSlider wndSlider = (WndSlider)GetWnd();
		if(wndSlider != null)
		{
			switch(property)
			{
			case UiPropertyEnum.CHANGED:
				wndSlider.SetProgress(value);
				break;
			}
		}
	}
	
	// 设置Image对象的属性
	private void SetUiSliderAtrr(WndSlider wndSlider)
	{
		if(null == wndSlider)
			return;
				
		wndSlider.AddUiEventListener(this);
		wndSlider.SetProgress(this.percent);
		wndSlider.SetProgressShow((0 == this.percentShow)?false:true);
		wndSlider.SetBarSize(barWidth, barHeight);
		wndSlider.SetSlipperSize(slipperWidth, slipperHeight);
		wndSlider.SetOrientation((0==orientation)?true:false);
		
		wndSlider.SetStyleSlipper(styleSlipper.colorBackground, SkinManager.GetBitmap(styleSlipper.bkImage));
		wndSlider.SetStyleBar(styleBar.colorBackground, SkinManager.GetBitmap(styleBar.bkImage));
	}
		
	// 从外部传入一个对象
	public void SetUiAttr(IWndBase v)
	{
		super.SetUiAttr(v);
		SetUiSliderAtrr((WndSlider)v);
	}

	public void UpdateSize(double scaleX, double scaleY) 
	{
		super.UpdateSize(scaleX, scaleY);
		
		WndSlider wndSlider = (WndSlider)GetWnd();
		if(null == wndSlider)
			return;
		
		wndSlider.SetBarSize((int)(scaleX * barWidth), (int)(scaleY * barHeight));
		wndSlider.SetSlipperSize((int)(scaleX * slipperWidth), (int)(scaleY * slipperHeight));
	}
}
