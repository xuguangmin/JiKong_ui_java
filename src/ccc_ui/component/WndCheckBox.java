package ccc_ui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import ccc_ui.ui.UiEventEnum;
import ccc_ui.ui.UiEventListener;

public class WndCheckBox extends JCheckBox 
                implements IWndBase, ItemListener
{
	private static final long serialVersionUID = 1L;
	/*private String    m_caption   = null;
	
	private Font      m_font    = null;
	private Color     m_colorForeground = null;
	private Color     m_colorBackground = null;
	private ImageIcon m_bkImage         = null;*/
	private UiEventListener  m_eventListener = null;
	
	public WndCheckBox()
	{
		super();
		this.addItemListener(this);
		//this.setVisible(false);		
		//this.setBorder(null);
	}
/*
	@Override
	protected void paintComponent(Graphics g) 
	{
		
		
		// ±³¾°É«
		if(m_colorBackground != null) 
		{
			g.setColor(m_colorBackground);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		// ±³¾°Í¼
		//if(m_bkImage != null) g.drawImage(m_bkImage.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
				
		if(m_caption != null)  
		{
			g.setColor((m_colorForeground != null) ? m_colorForeground:Color.black);
			if(m_font != null) g.setFont(m_font);
				
	    	int txtWidth = g.getFontMetrics().stringWidth(m_caption);
			int txtHeight = g.getFontMetrics().getAscent(); //getHeight()	
			int x = (int)(this.getWidth() - txtWidth)/2;
			int y = txtHeight + (int)((this.getHeight() - txtHeight)/2 );
			g.drawString(m_caption, x, y);
		}
		super.paintComponent(g);
	}
*/	
	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		int state = (ItemEvent.SELECTED == e.getStateChange()) ? 1:0;
		m_eventListener.UiEvent(UiEventEnum.CheckBoxStateChanged, state);
	}
	
	
	public void SetDrawText(String text)
	{
		//m_caption = text;
		this.setText(text);
	}
	public void SetFontNormal(Font ft)
	{		
		//m_font = ft;  
		this.setFont(ft);
	}
	public void SetImageNormal(ImageIcon bkImage)
	{
		//m_bkImage = bkImage;
	}
	
	public void SetStyleNormal(Color clForeground, Color clBackground)
	{
		//m_colorForeground  = clForeground;
	    //m_colorBackground  = clBackground;
	    this.setBackground(clBackground);
	    this.setForeground(clForeground);
	}
	
	public void AddUiEventListener(UiEventListener l)
	{
		m_eventListener = l;
	}
	
	@Override
	public JComponent GetOwner() 
	{
		return this;
	}

	
}
