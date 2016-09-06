package ccc_ui.component;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import ccc_ui.ui.UiEventEnum;
import ccc_ui.ui.UiEventListener;

public class WndImage extends JButton implements IWndBase
{
	private static final long serialVersionUID = 1L;
	private String    m_caption     = null;
	private boolean   m_b_enable = false;
	//private int       m_capX = 0;
	//private int       m_capY = 0;
	
	private Font      m_fontNormal  = null;
	private Font      m_fontPressed = null;
	private Font      m_fontFocused = null;
	
	private Color     m_colorForegroundNormal    = null;
	private Color     m_colorForegroundPressed   = null;
	private Color     m_colorForegroundFocused   = null;
	private Color     m_colorBackgroundNormal    = null;
	private Color     m_colorBackgroundPressed   = null;
	private Color     m_colorBackgroundFocused   = null;
		
	private ImageIcon m_bkImageNormal   = null;
	private ImageIcon m_bkImagePressed  = null;
	private ImageIcon m_bkImageFocused  = null;
	
	private int       m_textHAlignNormal  = IWndBase.ALIGN_MIDDLE;
	private int       m_textVAlignNormal  = IWndBase.ALIGN_MIDDLE;
	private int       m_textHAlignPressed = IWndBase.ALIGN_MIDDLE;
	private int       m_textVAlignPressed = IWndBase.ALIGN_MIDDLE;
	private int       m_textHAlign        = IWndBase.ALIGN_MIDDLE;
	private int       m_textVAlign        = IWndBase.ALIGN_MIDDLE;
	
	private Font      m_font    = null;
	private Color     m_colorForeground = null;
	private Color     m_colorBackground = null;
	private ImageIcon m_bkImage     = null;
	private Point     m_textPos     = new Point();
	
	private UiEventListener  m_eventListener = null;
	
	public WndImage()
	{
		super();
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e)  {OnMousePressed(e);}
		    public void mouseReleased(MouseEvent e) {OnMouseReleased(e);}
		});
		/*
		this.addFocusListener(new FocusAdapter(){
			 public void focusGained(FocusEvent e) 
			 {
				 if(m_bkImageFocused != null) m_bkImage = m_bkImageFocused;
				 System.out.println("WndImage focusGained");
			 }
			 public void focusLost(FocusEvent e)   
			 {
				 m_bkImage = m_bkImageNormal;
				 System.out.println("WndImage focusLost");
			 }
			
		});*/
		this.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				m_eventListener.UiEvent(UiEventEnum.IamgeControlClick, 0);
			}
		});	
		//this.setFocusable(true);
		this.setVisible(false);
		this.setContentAreaFilled(false);
		this.setBorder(null);
		this.setFocusPainted(false);
		//this.setComponentZOrder(comp, index)
		//this.setOpaque(false);	
	}
	    
    private void OnMousePressed(MouseEvent e)
    {
    	if(!m_b_enable) return;
    	if(m_bkImagePressed != null) m_bkImage = m_bkImagePressed;
    	m_font = m_fontPressed;
    	
    	m_colorForeground = m_colorForegroundPressed;
    	m_colorBackground = m_colorBackgroundPressed;
    	m_textHAlign = m_textHAlignPressed;
    	m_textVAlign = m_textVAlignPressed;
    	m_eventListener.UiEvent(UiEventEnum.IamgeControlPressed, 0);
    }
    private void OnMouseReleased(MouseEvent e)
    {
    	if(!m_b_enable) return;
    	m_font = m_fontNormal;
    	m_bkImage = m_bkImageNormal;
    	m_colorForeground = m_colorForegroundNormal;
    	m_colorBackground = m_colorBackgroundNormal;
    	m_textHAlign = m_textHAlignNormal;
    	m_textVAlign = m_textVAlignNormal;
    	m_eventListener.UiEvent(UiEventEnum.IamgeControlReleased, 0);
    }
        
	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		// 背景色
		if(m_colorBackground != null) 
		{
			g.setColor(m_colorBackground);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		// 背景图
		if(m_bkImage != null) g.drawImage(m_bkImage.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
		
		if(m_caption != null)  
		{			
			g.setColor((m_colorForeground != null) ? m_colorForeground:Color.black);
			if(m_font != null) g.setFont(m_font);
			
			FontMetrics fm = g.getFontMetrics();
			int txtWidth = fm.stringWidth(m_caption);
			int txtHeight = fm.getAscent();
			
			// 横向向坐标
			int x = 0;
			switch(m_textHAlign)
			{
			case IWndBase.ALIGN_MIDDLE:
				x = (int)(this.getWidth() - txtWidth)/2;
				break;
			case IWndBase.ALIGN_RIGHT:
				x = (int)(this.getWidth() - txtWidth);
				break;
			}
						
			// 纵向坐标
			int y;	
			switch(m_textVAlign)
			{
			case IWndBase.ALIGN_UP:
				y = Math.abs(fm.getAscent());
				break;
			case IWndBase.ALIGN_DOWN:
				y = this.getHeight()-fm.getDescent();
				break;
			default:
				//y = this.getHeight()/2 - (fm.getDescent() + fm.getAscent())/2;
				y = txtHeight + (int)((this.getHeight() - txtHeight)/2 );
			}			
			g.drawString(m_caption, x, y);
		}
	}
	
	public void SetTextAlignPressed(int hAlign, int vAlign) 
	{		
		m_textHAlignPressed = hAlign;
		m_textVAlignPressed = vAlign;
	}
	public void SetTextAlignNormal(int hAlign, int vAlign) 
	{
		m_textHAlignNormal = hAlign;
		m_textVAlignNormal = vAlign;
		m_textHAlign = hAlign;
		m_textVAlign = vAlign;
	}
	
	public void setFontFocused(Font ft)
	{
		m_fontFocused = ft;	    
	}
	public void setFontPressed(Font ft)
	{
		m_fontPressed = ft;	    
	}
	public void setFontNormal(Font ft)
	{
		m_font = ft;
		m_fontNormal = ft;
	}
	
	
	public void setImageFocused(ImageIcon bkImage)
	{
		m_bkImageFocused = bkImage;
	}
	public void setStyleFocused(Color clForeground, Color clBackground)
	{
		m_colorForegroundFocused = clForeground;
		m_colorBackgroundFocused = clBackground;	    
	}
	public void setImagePressed(ImageIcon bkImage)
	{
		m_bkImagePressed = bkImage;
	}
	public void setStylePressed(Color clForeground, Color clBackground)
	{
		m_colorForegroundPressed = clForeground;	
		m_colorBackgroundPressed = clBackground;
	}
	public void setImageNormal(ImageIcon bkImage)
	{
		m_bkImage   = bkImage;
		m_bkImageNormal = bkImage;
	}
	public void setStyleNormal(Color clForeground, Color clBackground)
	{
		m_colorForegroundNormal = clForeground;
	    m_colorBackgroundNormal = clBackground;
	    
	    m_colorForeground  = clForeground;
		m_colorBackground  = clBackground;
	}
	
	public void SetDrawText(String text)
	{
		m_caption = text;
	}
	public void setEnabled(boolean b_enable)
	{
		super.setEnabled(b_enable);
		m_b_enable = b_enable;
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