package ccc_ui.ui;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import ccc_ui.component.IWndBase;

public abstract class UiBase 
{
	// 解析来的属性
	public int      idPage;
	public int      id;
	public int      x;
	public int      y;
	public int      width;
	public int      height;
	public int      z_index;
	public boolean  visible;
	public boolean  enable;
	public int      jumpPage = -1;
	
	
	// 做为基类
	public static final int XML_NONE      = 0;
	public static final int XML_BUTTON    = 1;
	public static final int XML_IMAGE     = 2;
	public static final int XML_LABEL     = 3;
	public static final int XML_SLIDER    = 4;
	public static final int XML_CHECKBOX  = 5;
	public static final int XML_RADIO     = 6;
	public static final int XML_PROGRESS  = 7;
	
	private IUiListener m_iUiListener = null;
	protected IWndBase m_wndBase = null;
	private int m_uiType;
	
	public UiBase()
	{
		m_uiType = XML_NONE;
		m_wndBase = null;
		//scaleX   = 1;
		//scaleY   = 1;
	}
		
	protected void SetUiType(int uiType) 
	{
		m_uiType = uiType;
	}
	protected void AttachWnd(IWndBase uiNone) 
	{
		m_wndBase = uiNone;
	}
	protected void DetachWnd() 
	{
		m_wndBase = null;
	}
	protected IWndBase GetWnd()
	{
		return m_wndBase;
	}
		
	protected void PrintLog(String strText)
	{
		System.out.printf("%s :%s\n", this.getClass().getSimpleName(), strText);
	}
	
			
	protected void XmlUiEvent(int ctrlEvent)
	{
		XmlUiEvent(ctrlEvent, -1);	
	}
	/*
	 * value : 随事件附带的值
	 *      滑动条 百分比
	 *      checkbox 是否选中 1 0
	 */	
	protected void XmlUiEvent(int ctrlEvent, int value)
	{
		XmlUiEvent(ctrlEvent, value, false, null, -1, null);
	}
	protected void XmlUiEvent(int ctrlEvent, boolean bWarning,
			String strWarning, int jumpPage, UiActionGroup actionGroup) 
	{
		XmlUiEvent(ctrlEvent, -1, bWarning, strWarning, jumpPage, actionGroup);
	}
	protected void XmlUiEvent(int ctrlEvent, int value, boolean bWarning,
			                  String strWarning, int jumpPage, UiActionGroup actionGroup) 
	{
		if (null == m_iUiListener)
			return;

		m_iUiListener.UiEvent(id, ctrlEvent, value, bWarning, strWarning,
				jumpPage, actionGroup);
	}
	
	protected int AlignXmlToH(int align)
	{
		switch(align)
		{
		case UiFont.ALIGN_LEFT_TOP:
		case UiFont.ALIGN_LEFT_MIDDLE:
		case UiFont.ALIGN_LEFT_BOTTOM:
			return IWndBase.ALIGN_LEFT;
		case UiFont.ALIGN_RIGHT_TOP:
		case UiFont.ALIGN_RIGHT_MIDDLE:
		case UiFont.ALIGN_RIGHT_BOTTOM:
			return IWndBase.ALIGN_RIGHT;
		}
		return IWndBase.ALIGN_MIDDLE;     
	}
	protected int AlignXmlToV(int align)
	{
		switch(align)
		{
		case UiFont.ALIGN_LEFT_TOP:
		case UiFont.ALIGN_RIGHT_TOP:
		case UiFont.ALIGN_CENTER_TOP:
			return IWndBase.ALIGN_UP;
		
		case UiFont.ALIGN_LEFT_BOTTOM:
		case UiFont.ALIGN_RIGHT_BOTTOM:
		case UiFont.ALIGN_CENTER_BOTTOM:
			return IWndBase.ALIGN_DOWN;
		}
		return IWndBase.ALIGN_MIDDLE;    
	}
	
	/*
	protected Font CreateFont(UiFont xmlFont, double scale)
	{
		if(xmlFont == null || xmlFont.name == null)
			return null;
		
		int style = (1 == xmlFont.bold)?Font.BOLD:Font.PLAIN;
		style |= (1 == xmlFont.italic)?Font.ITALIC:0;
		style |= (1 == xmlFont.underLine)?Font.ROMAN_BASELINE:0;
		style |= (1 == xmlFont.strikeOut)?Font.CENTER_BASELINE:0;
		int size = (int)(xmlFont.size * scale);
		if(size <= 0) size = 1;
		//xmlFont.name = Font.SANS_SERIF;
		return (new Font(xmlFont.name, style, size));
	}
	*/
	protected Font CreateFont(UiFont xmlFont, double scale)
	{
		if(xmlFont == null || xmlFont.name == null)
			return null;
		
		HashMap<TextAttribute, Object> hm = new HashMap<TextAttribute, Object>(); 	
		// 字体名
		hm.put(TextAttribute.FAMILY, xmlFont.name); 
		// 加粗
		if(1 == xmlFont.bold)      hm.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD); 
		// 倾斜
		if(1 == xmlFont.italic)    hm.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
		// 下划线 
		if(1 == xmlFont.underLine) hm.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);  
		// 删除线
		if(1 == xmlFont.strikeOut) hm.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
				
		int size = (int)(xmlFont.size * scale);
		if(size <= 0) size = 1;
		hm.put(TextAttribute.SIZE, size);
		 
		return (new Font(hm));    
	}
	
	public void SetProperty(int property, int value)
	{
		switch(property)
		{
		case UiPropertyEnum.VISIBLE:
			this.visible = UiPropertyEnum.VISIBLE_FALSE==value ? false:true;
			break;
		case UiPropertyEnum.ENABLE:
			this.enable = UiPropertyEnum.ENABLE_FALSE==value ? false:true;
			break;
		}
		
		IWndBase wndBase = GetWnd();
		if(wndBase != null)
		{	
			switch(property)
			{
			case UiPropertyEnum.VISIBLE:
				wndBase.GetOwner().setVisible(this.visible);
				break;
			case UiPropertyEnum.ENABLE:
				wndBase.GetOwner().setEnabled(this.enable);
				break;
			}
		}
	}
	// 设置外观尺寸
	public void UpdateSize(double scaleX, double scaleY) 
	{
		IWndBase wndBase = GetWnd();
		if(wndBase != null)
		{	
			wndBase.GetOwner().setLocation((int)(scaleX * x), (int)(scaleY * y));
			wndBase.GetOwner().setSize((int)(scaleX * width), (int)(scaleY * height));
		}
	}
	
	/*
     * 子类需要实现这个函数，来为控件设置属性
     *
     * @param v 要被设置属性的控件
     */
	public void SetUiAttr(IWndBase v)
	{
		this.AttachWnd(v);	
		if(v != null)
		{
			v.GetOwner().setVisible(this.visible);
			v.GetOwner().setEnabled(this.enable);
		}
	}
	
	public int GetUiType()               
	{
		return m_uiType;
	}
	public void addUiListener(IUiListener l) {
		m_iUiListener = l;
    }
}
