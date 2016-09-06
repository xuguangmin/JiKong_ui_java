/* *****************************************************************************

                  版权所有 (C), 2013-2100

 ******************************************************************************
	  文件名称 : CCC_UI.java
	  作者           : 贾延刚
	  生成日期 : 2012年
	
	  版本           : 1.0
	  功能描述 : 
	                           管理用来控制集控主机的界面页
	                          上层调用需要提供：界面描述文件、界面图片目录
	                          通过GetPageObject获取页控件
	          
	                          返回：                
	                                      本类通过接口IPageManager返回数据
	                                      
******************************************************************************/
package ccc_ui;

import java.util.*;

import ccc_ui.component.WndManager;
import ccc_ui.component.WndPage;
import ccc_ui.xml.XmlParser;
import ccc_ui.xml.IXmlParser;
import ccc_ui.ui.*;

public class CCC_UI implements IXmlParser, IUiListener                                    
{
	public static final int   LOAD_UI_FAILED   = 1;
	public static final int   PAGE_TITLE       = 2;
	private UiRoot            m_uiRoot         = null;
	private List<UiPage>      m_uiPageList     = null;
	
	private ICCC_UI           m_iCCC_UI        = null;        // 对外接口	
	private WndPage           m_wndPage        = null;        // 页控件
	private UiPage            m_uiPage         = null;
	private int               m_curPage        = -1;
	
	public CCC_UI()
	{
		m_uiPageList = new ArrayList<UiPage>();
	}	
	private void PrintLog(String strLog)
	{
		System.out.printf("%s :%s\n", this.getClass().getSimpleName(), strLog);	
	}
	private void ReorderUi()
	{
		for(int k = 0; k < m_uiPageList.size(); ++k)
		{
			UiPage uip = m_uiPageList.get(k);
			uip.Reorder();
		}
	}
	// 解除UI和Wnd控件的关联
	private void DetachWnd()
	{
		for(int k = 0; k < m_uiPageList.size(); ++k)
		{
			UiPage uip = m_uiPageList.get(k);
			uip.DetachAllChild();
		}
	}
		
	private void NotifyParent(int type, String strNotify)
	{
		if(m_iCCC_UI == null)
			return;
		m_iCCC_UI.ccc_ui_notify(type, strNotify);
	}
	
	private UiPage GetUiPage(int idPage)
	{
		UiPage result = null;
		for(int k = 0; k < m_uiPageList.size(); ++k)
		{
			result = m_uiPageList.get(k);
			if(result.id == idPage)
				return result;
		}		
		return result;
	}
	// 选择要显示的页界面
	private boolean LoadUiPage(int idPage)
	{
		if(null == m_uiRoot)   
			return false;
		
		DetachWnd();
		m_uiPage = GetUiPage(idPage);
		if(null == m_uiPage)
		{
			PrintLog("LoadUiPage m_uiPage == null");
			return false;
		}
		m_uiPage.Show(m_wndPage, m_uiRoot.width, m_uiRoot.height, this);
		NotifyParent(PAGE_TITLE, m_uiPage.GetTitle());			
		return true;
	}
	
	// 显示或者隐藏弹出页
	public boolean InvokePage(int idPage, boolean b_show)
	{
		if(!b_show) idPage = m_curPage;
		
		System.out.printf("InvokePage idPage = %d\n", idPage);
		return LoadUiPage(idPage);	
	}
	
	private void ExecuteAction(UiActionGroup actionGroup)
	{
		if(actionGroup == null)
			return;
		
		UiAction uiAction;
		for(int k = 0; k < actionGroup.m_xmlActionList.size(); ++k)
		{
			uiAction = actionGroup.m_xmlActionList.get(k);
			switch(uiAction.action)
			{
			case UiAction.ACTION_HIDE:
				m_uiPage.SetVisible(uiAction.targetId, false);
				break;
			case UiAction.ACTION_SHOW:
				m_uiPage.SetVisible(uiAction.targetId, true);
				break;
			case UiAction.ACTION_ENABLE:
				m_uiPage.SetEnable(uiAction.targetId, true);
				break;
			case UiAction.ACTION_DISABLE:
				m_uiPage.SetEnable(uiAction.targetId, false);
				break;
			case UiAction.ACTION_INVOKE_APP:
				//m_iCCC_UI.ccc_ui_invoke_app(uiAction.parameter);
				break;
			}
		}
	}
	
	private void ExecuteJumpPage(int idPage)
	{	
		if(idPage <= 0)
			return;
		
		m_curPage = idPage;
		this.LoadUiPage(idPage);
	}
	
	private void ProcessUiEvent(int jumpPage, UiActionGroup actionGroup)
	{
		if(jumpPage > 0)        this.ExecuteJumpPage(jumpPage);
		if(actionGroup != null) this.ExecuteAction(actionGroup);
	}

	/* ******************  接口 IXmlParser  ****************** */
	public void ParseResult(int type, int idPage, Object xmlCtrl)
    {
		switch(type)
    	{
    	case XmlParser.XML_ROOT:
	    	{
	    		UiRoot uiRoot = (UiRoot)xmlCtrl;
	    		m_uiRoot = uiRoot;
	    		
	    		//System.out.println("\n-----------  XML  -----------");
	    		//System.out.printf("XML root : %d %d, %d\n", uiRoot.width, uiRoot.height, uiRoot.startPage);
	    	}
    		break;
    	case XmlParser.XML_PAGE:
    	case XmlParser.XML_POPUP_PAGE:
	    	{
	    		UiPage uiPage = (UiPage)xmlCtrl;
	    		m_uiPageList.add(uiPage);
	    		
	    		//System.out.println("");
	    		//System.out.printf("Page : %d %s, %s, %s\n", uiPage.id, uiPage.title, uiPage.bkImage, uiPage.bkColor);	
	    	}
	    	break;
    	}
    }
	/* ******************  接口 IUiListener  ****************** */
	@Override
	public void UiEvent(int ctrlId, int ctrlEvent, int value, boolean bWarning,
			            String strWarning, int jumpPage, UiActionGroup actionGroup) 
	{
		if(m_iCCC_UI.ccc_ui_event(ctrlId, ctrlEvent, value, bWarning, strWarning))
			ProcessUiEvent(jumpPage, actionGroup);
		
	}
	@Override
	public void PageEvent(int jumpPage) 
	{
		ExecuteJumpPage(jumpPage);
	}
	
		
	// 从外部设置控件的属性
	public void SetControlProperty(int ctrlId, int property, int value)
	{
		for(int k = 0; k < m_uiPageList.size(); ++k)
		{
			UiPage uip = m_uiPageList.get(k);
			if(uip.SetControlProperty(ctrlId, property, value))
				return;
		}
	}
			
	private WndPage InitialPage()
	{
		WndPage uiPage = new WndPage();
		if(uiPage != null)
		{
			uiPage.setLayout(null);
		}
		return uiPage;
	}
				
	/*
	 * 装载默认界面
     */
	public void LoadDefaultUI()
	{
		m_wndPage.removeAll();
		m_wndPage.showDefault();
		m_wndPage.repaint();
	}
	
	private void Clear()
	{
		m_wndPage.ClearAllView();
		m_uiPageList.clear();
		WndManager.Clear();
		SkinManager.ClearAll();
	}
	
	/*
	 * 装载界面
	 * 
	 * 参数说明：
	 *          strZipFileName 界面zip文件
     */
	public boolean LoadUI(String strZipFileName)
	{
		Clear();
		if(null == strZipFileName)
			return false;
		
		// 打开zip文件
		if(!SkinManager.OpenZipFile(strZipFileName))
		{
			PrintLog(String.format("Open zip failed :%s\n", strZipFileName));
			return false;
		}
		
		m_uiPageList.clear();
		if(!(new XmlParser().OpenFile(SkinManager.GetXmlInputStream(), this)))
		{
			PrintLog("Open xml failed");
			return false;
		}
		
		if(null == m_uiRoot)
		{
			/* 
			 * 如果连根节点都没有，
			 * 说明不是一个合法的配置文件
			 */
			PrintLog("LoadUI m_uiRoot == null");
			return false;
		}
		
		// 如果解析成功，则装载首页
		ReorderUi();
		
		m_curPage = m_uiRoot.startPage;
		return LoadUiPage(m_uiRoot.startPage);
	}
	
	public WndPage GetPanel()  {return m_wndPage;}
	public boolean Initial(ICCC_UI ifCM)
	{
		m_wndPage = InitialPage();
		if(m_wndPage == null)
			return false;
		
		m_iCCC_UI = ifCM;
		return true;
	}
}
