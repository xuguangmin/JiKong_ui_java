package ccc_ui.ui;

import ccc_ui.component.IWndBase;
import ccc_ui.component.WndProgress;

public class UiProgress extends UiBase
{	
	public int     orientation     = 0;         // ��������0 ����1 ����
	public int     value           = 0;
	
	public UiProgress()
	{
		SetUiType(UiBase.XML_PROGRESS);		
	}
		
	public void SetProperty(int property, int value)
	{
		super.SetProperty(property, value);
		
		switch(property)
		{
		case UiPropertyEnum.CHECKED:
			this.value = value;
			break;
		}
		
		WndProgress wndProgress = (WndProgress)GetWnd();
		if(wndProgress != null)
		{
			switch(property)
			{
			case UiPropertyEnum.CHANGED:
				wndProgress.SetProgress(value);
				break;
			}
		}
	}
	
	// ���ö��������
	private void SetUiProgressAtrr(WndProgress wndProgress)
	{
		if(null == wndProgress)
			return;
		
		wndProgress.SetOrientation((0==orientation)?true:false);
		wndProgress.SetProgress(this.value);
	}
		
	// ���ⲿ����һ������
	public void SetUiAttr(IWndBase v)
	{
		super.SetUiAttr(v);
		SetUiProgressAtrr((WndProgress)v);
	}
		
	public void UpdateSize(double scaleX, double scaleY) 
	{
		super.UpdateSize(scaleX, scaleY);
	}
}
