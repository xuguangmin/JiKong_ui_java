package ccc_ui.ui;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

import ccc_ui.component.*;

public class UiPage implements IWndPage
{
	public int    id;
	public String title    = null;
	public Color  bkColor  = null;          // 背景颜色
	public Color  fwColor  = null;          // 前景颜色
	public String bkImage  = null;
	public int    imageMode;                // 拉伸、平铺、同比例缩放
	public int    jumpPage = -1;            // 单击时的弹出页
	public int    leftJumpPage = -1;        // 左滑动时的弹出页
	public int    rightJumpPage = -1;       // 右滑动时的弹出页
	
	public boolean AddXmlNone(UiBase xmlNone)
	{
		return m_childUiList.add(xmlNone);
	}
	
	// 根据z-index进行从大到小的排序
	// 好像和Android不同，java是先放上去的在先
	// 随后放上去的，会放到后边
	public void Reorder()
	{
		UiBase uib, uib2, temp;
		int total = m_childUiList.size();
		for(int k = 0; k < total; ++k)
		{
			uib = m_childUiList.get(k);
			for(int s = k+1; s < total; ++s)
			{
				uib2 = m_childUiList.get(s);
				if(uib.z_index < uib2.z_index)
				{
					temp = uib;
					uib = uib2;
					m_childUiList.set(s, temp);	
				}
			}
			m_childUiList.set(k, uib);	
		}
	}
	// 解除和wnd控件的关联
	public void DetachAllChild()
	{
		UiBase uib;
		int total = m_childUiList.size();
		for(int k = 0; k < total; ++k)
		{
			uib = m_childUiList.get(k);	
			uib.DetachWnd();
		}
		
	}
	// 得到页的标题
	public String GetTitle()
	{
		return title;
	}
		
	private int    m_uiWidth  = 0;           // 页宽，来自于根节点，目前并无页尺寸的设置
	private int    m_uiHeight = 0;           // 页高
	private int    m_wndWidth   = 0;         // 控件宽
	private int    m_wndHeight  = 0;         // 控件高
	private List<UiBase>  m_childUiList;
	private IUiListener m_iUiListener = null;
		
	public UiPage()
	{
		id      = -1;
		title   = "";
		bkImage = "";	
		m_childUiList = new ArrayList<UiBase>();
	}
	
	public void OnPageClick()
	{
		if(this.jumpPage > 0) m_iUiListener.PageEvent(this.jumpPage);
	}
	public void OnPageSlide(boolean b_left)
	{
		int idPage = b_left ? leftJumpPage:rightJumpPage;
		if(idPage > 0) 
		{
			m_iUiListener.PageEvent(idPage);		
		}		
	} 
	public void OnPageResized(int width, int height)
	{
		m_wndWidth = width;
		m_wndHeight = height;
		CalcuScale();
	}
	
	private void CalcuScale()
	{
		if(0 == this.m_uiWidth || 0 == this.m_uiHeight)
		{
			System.out.printf("CalcuScale() 0 == this.m_uiWidth || 0 == this.m_uiHeight \n");
			return;
		}	
		if(0 == this.m_wndWidth || 0 == this.m_wndHeight)
		{
			System.out.printf("CalcuScale() 0 == this.m_wndWidth || 0 == this.m_wndHeight \n");
			return;
		}
		
		double scaleX = (double)m_wndWidth/this.m_uiWidth;
		double scaleY = (double)m_wndHeight/this.m_uiHeight;
		UpdateSize(scaleX, scaleY);
	}
	
	private void UpdateSize(double scaleX, double scaleY)
	{
		UiBase uiCtrl = null;
		for(int k = 0; k < m_childUiList.size(); ++k)
		{
			uiCtrl = m_childUiList.get(k);
			uiCtrl.UpdateSize(scaleX, scaleY);
		}
	}
		
	private int UiToWndType(int uiType)
	{
		switch(uiType)
		{
		case UiBase.XML_BUTTON   :return WndType.WND_BUTTON;
		case UiBase.XML_IMAGE    :return WndType.WND_IMAGE;		
		case UiBase.XML_LABEL    :return WndType.WND_LABEL;
		case UiBase.XML_SLIDER   :return WndType.WND_SLIDER;
		case UiBase.XML_CHECKBOX :return WndType.WND_CHECKBOX;
		case UiBase.XML_RADIO    :return WndType.WND_RADIO;
		case UiBase.XML_PROGRESS :return WndType.WND_PROGRESS;
		}
		return WndType.WND_NONE;
	}
	
	private void CreatePageElements(WndPage wndPage)
	{
		if(null == wndPage)
			return;

		for(int k = 0; k < m_childUiList.size(); ++k)
		{
			UiBase uib = m_childUiList.get(k);
			uib.addUiListener(m_iUiListener);
			
			IWndBase v = wndPage.GetView(UiToWndType(uib.GetUiType()));
			if(null == v)
			{
				System.out.println("CreatePageElements IWndBase == NULL");
				continue;
			}
			
			uib.SetUiAttr(v);
			wndPage.Add(v);
		}
	}
			
	private void SetWndPageAtrr(WndPage wndPage)
	{
		wndPage.ClearAllView();
		wndPage.AddPageListener(this);
		wndPage.setBkColor(this.bkColor);		
		wndPage.setBkImage(SkinManager.GetBitmap(this.bkImage));
		//wndPage.SetShowHowMode(b_showHow);
		
		//System.out.printf("SetWndPageAtrr page image = %s, w=%d, h=%d\n", this.bkImage, wndPage.getWidth(), wndPage.getHeight());
	}
	
	public boolean SetVisible(int ctrlId, boolean b_visible)
	{
		return SetControlProperty(ctrlId, UiPropertyEnum.VISIBLE, 
				b_visible?UiPropertyEnum.VISIBLE_TRUE:UiPropertyEnum.VISIBLE_FALSE);
	}
	public boolean SetEnable(int ctrlId, boolean b_enable)
	{
		return SetControlProperty(ctrlId, UiPropertyEnum.ENABLE, 
				b_enable?UiPropertyEnum.ENABLE_TRUE:UiPropertyEnum.ENABLE_FALSE);
	}
	
	public boolean SetControlProperty(int ctrlId, int property, int value)
	{
		UiBase uib = null;
		for(int k = 0; k < m_childUiList.size(); ++k)
		{
			uib = m_childUiList.get(k);
			if(uib.id == ctrlId) 
			{
				uib.SetProperty(property, value);
				return true;
			}
		}
		return false;
		
	}

	/*
	 * width  界面设置的页宽
	 * height 界面设置的页高
	 */
	public boolean Show(WndPage wndPage, int width, int height, IUiListener uiListener)
	{
		if(null == wndPage)
			return false;
		
		m_iUiListener = uiListener; 
		m_uiWidth  = width;
		m_uiHeight = height;
		m_wndWidth   = wndPage.getWidth();
		m_wndHeight  = wndPage.getHeight();
				
		// 设置页控件属性
		SetWndPageAtrr(wndPage);	
		CreatePageElements(wndPage);
		CalcuScale();
		wndPage.repaint();
		m_iUiListener.UiEvent(this.id, UiEventEnum.PageShow, 0, false, null, -1, null);
		return true;
	}
}
