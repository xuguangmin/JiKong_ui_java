package ccc_ui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class WndLabel extends JLabel implements IWndBase
{
	private static final long serialVersionUID = -4896380422917785040L;
	private String    m_caption   = null;
	
	private Font      m_font    = null;
	private Color     m_colorForeground = null;
	private Color     m_colorBackground = null;
	private ImageIcon m_bkImage         = null;
	private int       m_textHAlign      = IWndBase.ALIGN_MIDDLE;
	private int       m_textVAlign      = IWndBase.ALIGN_MIDDLE;
	
	public WndLabel()
	{
		super();
		this.setVisible(false);		
		this.setBorder(null);
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
	
	public void SetTextAlignNormal(int hAlign, int vAlign) 
	{
		m_textHAlign = hAlign;
		m_textVAlign = vAlign;
	}
	public void SetDrawText(String text)
	{
		m_caption = text;
	}
	public void setFontNormal(Font ft)
	{		
		m_font = ft;    
	}
	public void setImageNormal(ImageIcon bkImage)
	{
		m_bkImage = bkImage;
	}
	
	public void setStyleNormal(Color clForeground, Color clBackground)
	{
		m_colorForeground  = clForeground;
	    m_colorBackground  = clBackground;
	}
	public JComponent GetOwner()
	{
		return this;
	}
}
