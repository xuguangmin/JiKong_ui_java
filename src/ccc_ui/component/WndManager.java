package ccc_ui.component;

import java.util.ArrayList;
import java.util.List;

public class WndManager 
{
	private static List<WndItem>   m_wndItemList = new ArrayList<WndItem>();
	
	private static boolean AddWndItem(int vtype, IWndBase wnd, boolean b_using)
	{
		WndItem wi = new WndItem();
		wi.wnd     = wnd;
		wi.vtype   = vtype;
		wi.b_using = b_using;
		return m_wndItemList.add(wi);
	}
	
	private static IWndBase GetViewFromList(int wndType)
	{
		for(int k = 0; k < m_wndItemList.size(); ++k)
		{
			WndItem wi = m_wndItemList.get(k);
			if(wi.vtype == wndType && !wi.b_using)
			{
				wi.b_using = true;
				return wi.wnd;
			}
		}
		return null;	
	}
	
	private static IWndBase CreateNewView(int wndType)
	{		
		switch(wndType)
		{
		case WndType.WND_BUTTON    :return (new WndButton());
		case WndType.WND_IMAGE     :return (new WndImage());			
		case WndType.WND_LABEL     :return (new WndLabel());
		case WndType.WND_SLIDER    :return (new WndSlider());
		case WndType.WND_CHECKBOX  :return (new WndCheckBox());
		case WndType.WND_RADIO     :return (new WndRadio());
		case WndType.WND_PROGRESS  :return (new WndProgress());
		}
		return null;
	}
	
	public static void RestoreView(IWndBase wnd)
	{
		for(int k = 0; k < m_wndItemList.size(); ++k)
		{
			WndItem wi = m_wndItemList.get(k);
			if(wi.wnd == wnd)
			{
				wi.b_using = false;
				break;
			}
		}
	}
	
	public static IWndBase GetView(int wndType)
	{
		IWndBase v = GetViewFromList(wndType);
		if(v != null)
			return v;
		
		v = CreateNewView(wndType);
		if(null == v)
			return null;
		
		//System.out.printf("jia --- %d WndManager CreateNewView\n", ++m_count);
		
		AddWndItem(wndType, v, true);
		return v;
	}
	
	public static void Clear()
	{
		m_wndItemList.clear();
	}
}