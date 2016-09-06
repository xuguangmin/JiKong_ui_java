package ccc_ui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class WndPage extends JPanel
{
	private static final long serialVersionUID = 1L;
	private boolean    m_expand = true;
	private ImageIcon  m_bkImage = null;
	private Font       m_font = new Font("楷体_GB2312", Font.BOLD, 22);
	private String     m_caption = "科技创造美好生活！";
	private boolean    m_default = true;	
	private IWndPage   m_pageListener = null;
	private boolean    m_b_slide = false;
	private Point      m_startPoint = new Point();
	private Point      m_newPoint = new Point();
	private List<IWndBase>    m_childWndList;
	
	public WndPage()
	{
		super();
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e)  {OnMousePressed(e);}
		    public void mouseReleased(MouseEvent e) {OnMouseReleased(e);}
		    public void mouseMoved(MouseEvent e)    {OnMouseMoved(e);}
		    public void mouseClicked(MouseEvent e)  {OnMouseClicked(e);}
		});
		addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e) {OnComponentResized(e);}
		});
		
		m_childWndList = new ArrayList<IWndBase>();
		showDefault();
	}
	
	private void OnComponentResized(ComponentEvent e)
	{
		if(m_pageListener == null)
			return;
		
		m_pageListener.OnPageResized(this.getWidth(), this.getHeight());
	}
	private void OnMouseClicked(MouseEvent e)
    {
		if(m_pageListener == null)
			return;
		
		m_pageListener.OnPageClick();
    }
	
	private void OnMousePressed(MouseEvent e)
    {/*
		m_startPoint.x = e.getX();
		//m_startPoint.y = e.getY();
		m_b_slide = true;
		*/
    }
	private void OnMouseMoved(MouseEvent e)
    {
    }
    private void OnMouseReleased(MouseEvent e)
    {	/*
    	m_newPoint.x = e.getX();
    	//m_newPoint.y = e.getY();
    	int len = m_newPoint.x - m_startPoint.x;
    	boolean b_left = (len > 0) ? false:true;    	
    	if(Math.abs(len) > 5) m_pageListener.OnPageSlide(b_left);
    	*/
    }
    
	private void DrawLogoText(Graphics g, String strText)
	{
		g.setFont(m_font);
		g.setColor(Color.white);
		int strWidth = g.getFontMetrics().stringWidth(strText);
		int strHeight = g.getFontMetrics().getHeight();
		g.drawString(strText, (this.getWidth()-strWidth)/2, (this.getHeight()-strHeight)/2);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(m_default)
		{
			DrawLogoText(g, m_caption);
			return;
		}
		
		if(m_bkImage != null)
		{
			if(!m_expand)
			{
				int x = (this.getWidth() - m_bkImage.getIconWidth())/2;
				int y = (this.getHeight() - m_bkImage.getIconHeight())/2;
				g.drawImage(m_bkImage.getImage(), x, y, this);
			}
			else g.drawImage(m_bkImage.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);//0, 0, m_bkImage.getIconWidth(), m_bkImage.getIconHeight(), null);
		}
	}
	
	public void setBkColor(Color bkColor)
	{
		m_default = false;
		this.setBackground(bkColor);
	}
	public void setBkImage(ImageIcon bkImage)
	{
		m_default = false;				
		m_bkImage = bkImage;
	}
	public void showDefault()
	{
		m_default = true;
		this.setBackground(Color.black);
	}
	public void AddPageListener(IWndPage l) {
		m_pageListener = l;
    }
	
	public IWndBase GetView(int wndType)
	{
		return WndManager.GetView(wndType);
	}
	
	public void ClearAllView()
	{
		IWndBase wnd = null;
		for(int k = 0; k < m_childWndList.size(); ++k)
		{
			wnd = m_childWndList.get(k);
			// TODO: 20131109 wnd.Clear();
			
			WndManager.RestoreView(wnd);
		}
		m_childWndList.clear();
		
		setBkImage(null);
		this.removeAll();
		System.gc();
	}
	
	public boolean AddWndBase(IWndBase wndBase)
	{
		return m_childWndList.add(wndBase);
	}
	public void Add(IWndBase child)
	{
		this.add(child.GetOwner());
		AddWndBase(child);
	}
}
