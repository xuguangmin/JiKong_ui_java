package ccc_ui.component;

import java.awt.Color;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import utils.FileUtil;

public class WndSlider extends JComponent
{	
	private static final long serialVersionUID = -2103335763969277480L;
	private IWndSlider  m_iUiListener = null;
	private int         m_progOld  = -1;
	private int         m_progress = 0;
	private boolean     m_bMousePressed = false;
	
	private ImageIcon   m_imgProgress   = null;
	private ImageIcon   m_imgBackground = null;
	private Color       m_bkColor       = new Color(148,126,79);
	private Color       m_pgColor       = new Color(205,225,120);
	private int         m_orientation   = 0;                              // 0 ºáÏò£¬1 ×ÝÏò
	private int         m_barWidth= 0;
	private int         m_barHeight= 0;
	private Rectangle   m_chunk     = new Rectangle();                   // »¬¿é³ß´ç
	private Rectangle   m_rectCalcu = new Rectangle(0,0,0,0);
	private Point       m_ptOffset  = new Point();
	private DrawSlider  m_DrawSlider = new DrawSlider();
	
	public WndSlider()
	{
		super();
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e)  {OnMouseClicked(e);}
			public void mousePressed(MouseEvent e)  {OnMousePressed(e);}
		    public void mouseReleased(MouseEvent e) {OnMouseReleased(e);}
		    public void mouseExited(MouseEvent e)   {OnMouseReleased(e);}
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e)  {OnMouseDragged(e);}
		});
	}

	public void addUiListener(IWndSlider l) {
		m_iUiListener = l;
    }
	private int calcuProgress(int y, int height)
	{
		return (int)(100 * (height - y)/height);
	}
	private void OnMouseClicked(MouseEvent e)
	{
		if(m_orientation > 0)
		{
			m_progress = calcuProgress(e.getY(), this.getHeight());
			m_chunk.y = (int)((100 - m_progress) * m_rectCalcu.getHeight()/100);
		}
		else
		{
			int calcu_width = (int)m_rectCalcu.getWidth();
			int walking = e.getX() - m_rectCalcu.x;
			if(walking > calcu_width) m_progress = 100;
			else if(walking <= 0)     m_progress = 0;
			else
			{
				m_progress = (int)(100 * ((double)walking/m_rectCalcu.getWidth()));
			}
			m_chunk.x = (int)(m_progress * m_rectCalcu.getWidth()/100);
		}
		this.repaint();
		NotifyPercent(m_progress);
	}

	private void OnMouseReleased(MouseEvent e)
	{
		if(m_bMousePressed)
		{
			m_bMousePressed = false;
			if(m_orientation > 0) 
			{
				m_chunk.y = (int)((100 - m_progress) * m_rectCalcu.getHeight()/100);
			}
			else
			{
				m_chunk.x = (int)(m_progress * m_rectCalcu.getWidth()/100);
			}
		}
	}
	private void OnMousePressed(MouseEvent e)
	{
		if(m_chunk.contains(e.getX(), e.getY()))
		{
			m_bMousePressed = true;
			if(m_orientation > 0)
			{
				m_ptOffset.y = e.getY() - m_chunk.y;
			}
			else
			{
				m_ptOffset.x = e.getX() - m_chunk.x;
			}
		}
	}
	private void OnMouseDragged(MouseEvent e)
	{
		if(!m_bMousePressed) 
			return;
		
		if(m_orientation > 0)
		{
			int calcu_height = m_rectCalcu.height;
			int walking = e.getY() - m_ptOffset.y;
			if(walking >= calcu_height) m_progress = 0;
			else if(walking <= 0) m_progress = 100;
			else
			{
				m_progress = 100- (int)(100 * ((double)walking/calcu_height));
			}
		}
		else
		{
			int calcu_width = m_rectCalcu.width;
			int walking = e.getX() - m_ptOffset.x;
			if(walking >= calcu_width) m_progress = 100;
			else if(walking <= 0) m_progress = 0;
			else
			{
				m_progress = (int)(100 * ((double)walking/calcu_width));
			}
		}  
		this.repaint();
		NotifyPercent(m_progress);
	}

	private void NotifyPercent(int progress)
	{
		if(null == m_iUiListener || progress == m_progOld) 
			return;
		
		m_iUiListener.SliderPercent(this, progress);
		m_progOld = progress;
	}
	private void drawProgressNormal(Graphics g, int progress, int width, int height)
	{
		if(progress < 0)   progress = 0;
		if(progress > 100) progress = 100;
		int x = 0; 
		int y = 0;
		if(m_orientation > 0)
		{
			y = (int)(m_chunk.height/2 +((100- progress)* (height - m_chunk.height)/100));
			x = width/2;
		}
		else
		{
			x = (int)(m_chunk.width/2 +(progress* (width - m_chunk.width)/100));
			y = height/2;
		}		
		m_DrawSlider.drawProgressNormalChunk(g, x, y, m_chunk.width, m_chunk.height);
	}
	private void drawProgressNormalChunk(Graphics g, int x, int y, int width, int height)
	{
		if(m_imgProgress != null)
		{
			g.drawImage(m_imgProgress.getImage(), x-width/2, y-height/2, width, height, this);
		}
		else
		{
			g.setColor(m_pgColor);
			//g.fillOval(x-width/2, y-height/2, width, height);
			g.fill3DRect(x-width/2, y-height/2, width, height, true);
		}
	}
	private void drawBackgroundNormal(Graphics g, int x, int y, int width, int height)
	{
		if(m_imgBackground != null)
		{
			g.drawImage(m_imgBackground.getImage(), x, y, width, height, this);
		}
		else
		{
			g.setColor(m_bkColor);
			g.fillRect(x, y, width, height);
		}
	}
	private void drawSliderNormal(Graphics g, int progress)
	{		
		if(m_orientation > 0) // V
		{
			int x = (this.getWidth() - m_barWidth)/2;
			int y = 0;
			drawBackgroundNormal(g, x,y, m_barWidth, m_barHeight);//m_barWidth, m_barHeight);
		}
		else
		{
			int x = 0;
			int y = (this.getHeight() - m_barHeight)/2;
			drawBackgroundNormal(g, x,y, m_barWidth, m_barHeight);//m_barWidth, m_barHeight);	
		}
		//drawProgress(g, m_progress, width, height);
		
		drawProgressNormal(g, m_progress, getWidth(), getHeight());	
	}
	
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		drawSliderNormal(g, m_progress);
	}
		
	private void CalcuElementsSizeNormal(int width, int height)
	{
		if(m_orientation > 0) // V
		{
			m_barWidth  = (int)(width * 0.3);;
			m_barHeight = height;
			m_chunk.width  = width;
			m_chunk.height = width;
			
			m_rectCalcu.setBounds(0, m_chunk.height/2, width, height - m_chunk.height);
			m_chunk.x      = 0;
			m_chunk.y      = (int)((100 - m_progress) * m_rectCalcu.getHeight()/100);
		}
		else   // H
		{
			m_barWidth  = width;
			m_barHeight = (int)(height * 0.3);
			m_chunk.width  = height;
			m_chunk.height = height;	
			
			m_rectCalcu.setBounds(m_chunk.width/2, 0, width-m_chunk.width, height);
			m_chunk.x      = (int)(m_progress * m_rectCalcu.getWidth()/100);
			m_chunk.y      = 0;
		}
	}
	
	public void setSliderSize(int width, int height)
	{
		CalcuElementsSizeNormal(width, height);
		this.setSize(width, height);
	}
	public void setOrientation(int orientation)
	{
		m_orientation = orientation;
	}
	
	public void setImageProgress(String normalIcon)
	{
		if(!FileUtil.Exists(normalIcon))
			return;
		//m_imgProgress = new ImageIcon(normalIcon);
		m_DrawSlider.SetSlipperImage(new ImageIcon(normalIcon));
	}
	public void setImageBackground(String normalIcon)
	{
		if(!FileUtil.Exists(normalIcon))
			return;
		m_imgBackground = new ImageIcon(normalIcon);
	}
	public void setProgress(int prog)
	{
		if(prog >= 0 && prog <= 100)
		{
			m_progress = prog; 
			this.invalidate();
		}
	}
}


