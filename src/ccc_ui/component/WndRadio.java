package ccc_ui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JRadioButton;

import ccc_ui.ui.UiEventEnum;
import ccc_ui.ui.UiEventListener;

public class WndRadio extends JRadioButton 
                      implements IWndBase, ItemListener
{
	private static final long serialVersionUID = 2940610551736007508L;	
	private UiEventListener  m_eventListener = null;
	public WndRadio()
	{
		super();
		this.addItemListener(this);
	}
		
	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		int state = (ItemEvent.SELECTED == e.getStateChange()) ? 1:0;
		m_eventListener.UiEvent(UiEventEnum.RadioBoxStateChanged, state);
	}
	
		
	public void SetDrawText(String text)
	{
		this.setText(text);
		this.setText(text);
	}
	public void setFontNormal(Font ft)
	{		
		this.setFont(ft);
		this.setFont(ft);
	}
	public void setImageNormal(ImageIcon bkImage)
	{
		//this.setIcon(bkImage);
	}
	
	public void setStyleNormal(Color clForeground, Color clBackground)
	{	    
	    this.setBackground(clBackground);
	    this.setForeground(clForeground);
	}
	public void AddUiEventListener(UiEventListener l)
	{
		m_eventListener = l;
	}
	public JComponent GetOwner()
	{
		return this;
	}
}
