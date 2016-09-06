package ccc_ui.component;

import java.awt.Color;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import ccc_ui.ui.UiEventEnum;
import ccc_ui.ui.UiEventListener;

public class WndSlider extends JComponent implements IWndBase
{	
	private static final long serialVersionUID = -2103335763969277480L;
	private boolean     m_b_enable = true;
	private UiEventListener  m_eventListener = null;
	private int         m_progOld  = -1;
	private boolean     m_b_horizontal = true;
		
	private boolean     m_bMousePressed = false;
	private Point       m_ptOld     = new Point();
	private int         m_slide_length = 0;      // 当前滑块已滑动的长度
	private int         m_bar_length = 0;        // 当前滑动条的长度（除去滑块）
	private DrawSlider  m_drawSlider = new DrawSlider();
		
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
	
	private void OnMouseClicked(MouseEvent e)
	{/*
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
		NotifyPercent(m_progress);*/
	}

	private void OnMouseReleased(MouseEvent e)
	{
		if(m_bMousePressed)
		{
			m_bMousePressed = false;
		}
	}
	private void OnMousePressed(MouseEvent e)
	{
		if(m_drawSlider.PointInSlipperRect(e.getX(), e.getY()))
		{
			m_bMousePressed = true;
			m_slide_length = m_drawSlider.GetSlideLength();
			m_bar_length   = m_drawSlider.GetBarLength();
			
			if(m_b_horizontal) m_ptOld.x = e.getX();
			else               m_ptOld.y = e.getY();
		}	
	}
	private void OnMouseDragged(MouseEvent e)
	{
		if(!m_b_enable)
			return;
		if(!m_bMousePressed) 
			return;
		
		int progress = 0;
		if(m_b_horizontal)
		{
			int walking = e.getX() - m_ptOld.x;
			progress = (int)(100 * ((double)(m_slide_length + walking)/m_bar_length));
		}
		else
		{
			int walking = e.getY() - m_ptOld.y;	
			progress = (int)(100 * ((double)(m_slide_length + walking)/m_bar_length));
			progress = 100 - progress;			
		}	
		m_drawSlider.SetProgress(progress);
		this.repaint();
		NotifyPercent(progress);
	}

	// TODO:progress在滑动条比较短的时候，步进不是1，这个需要优化
	private void NotifyPercent(int progress)
	{
		if(!m_b_enable) return;
		
		if(progress < 0 || progress > 100)
			return;
		if(null == m_eventListener || progress == m_progOld) 
			return;
		
		m_eventListener.UiEvent(UiEventEnum.SliderValueChange, progress);
		m_progOld = progress;
	}
		
	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);		
		m_drawSlider.StartDraw(g);
	}
	
	public void setEnabled(boolean enabled) 
	{
		super.setEnabled(enabled);
		m_b_enable = enabled;
	}
	// 重载父函数		
	public void setSize(int width, int height)
	{
		super.setSize(width, height);
		m_drawSlider.SetEdgeSize(width, height);
	}
	
	public void SetBarSize(int width, int height)
	{
		m_drawSlider.SetBarSize(width, height);
	}
	public void SetSlipperSize(int width, int height)
	{
		m_drawSlider.SetSlipperSize(width, height);
	}
		
	public void SetStyleSlipper(Color clBackground, ImageIcon bkImage)
	{
	    m_drawSlider.SetSlipperImage(clBackground, bkImage);
	}
	public void SetStyleBar(Color clBackground, ImageIcon bkImage)
	{
	    m_drawSlider.SetBarImage(clBackground, bkImage);
	}
	
	public void SetProgressShow(boolean b_show)
	{
		m_drawSlider.SetProgressShow(b_show);
	}
	public void SetOrientation(boolean b_horizontal)
	{
		m_drawSlider.SetOrientation(b_horizontal);
		m_b_horizontal = b_horizontal;
	}
	// TODO: 这儿设置值时，是否需要激活事件？
	public void SetProgress(int progress)
	{
		m_drawSlider.SetProgress(progress);
		this.repaint();
	}
	public void AddUiEventListener(UiEventListener l) {
		m_eventListener = l;
    }
	public JComponent GetOwner()
	{
		return this;
	}
}


