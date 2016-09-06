package ccc_ui.component;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class WndProgress extends JProgressBar implements IWndBase
{
	private static final long serialVersionUID = 1L;
	
	public void SetOrientation(boolean b_horizontal)
	{
		this.setOrientation(b_horizontal ? SwingConstants.HORIZONTAL:SwingConstants.VERTICAL);
	}
	public void SetProgress(int progress)
	{
		//System.out.printf("%s progress = %d\n", this.getClass().toString(), progress);
		this.setValue(progress);
	}
		
	@Override
	public JComponent GetOwner() 
	{
		return this;
	}
}

/*
public class WndProgress extends JComponent
{
	private static final long serialVersionUID = 3894756208887325131L;
	public static final int STYLE_NORMAL  = 0; 
	public static final int STYLE_2       = 1;
	private int       m_orientation   = 0;                              // 0 ºáÏò£¬1 ×ÝÏò
	private Rectangle m_chunk     = new Rectangle();   // »¬¿é³ß´ç
	private Color     m_bkColor       = new Color(148,126,79);
	private Color     m_pgColor       = new Color(205,225,120);
	private ImageIcon m_imgProgress   = null;
	private ImageIcon m_imgBackground = null;
	
	private void drawProgress(Graphics g, int progress, int width, int height)
	{
		if(progress < 0)    progress = 0;
		if(progress > 100) progress = 100;
		
		if(m_orientation > 0)
		{
			int top = height - (int)(progress * height/100);
			if(m_imgProgress != null)
			{
				g.drawImage(m_imgProgress.getImage(), 0, top, width, height, this);
			}
			else
			{
			    g.setColor(m_pgColor);
				g.fillRect(0, top, width, height);
			}
			//g.fillOval(0, top, width, width);
			if(progress > 0)
			{
				g.setColor(Color.black);
				g.drawString(String.format("%d%%", progress), 0, height/2);
			}
		}
		else
		{
			int right = (int)(progress * width/100);
			if(m_imgProgress != null)
			{
				g.drawImage(m_imgProgress.getImage(), 0, 0, right, height, this);
			}
			else
			{
				g.setColor(m_pgColor);
				g.fillRect(0, 0, right, height);
			}
			
			if(progress > 0)
			{
				g.setColor(Color.black);
				g.drawString(String.format("%d%%", progress), width/2, height/2);
			}
		}
	}
	
	private void drawProgressRect2_1(Graphics g, int x, int y, int width, int height)
	{
		g.setColor(m_pgColor);
		//g.fillRect(x-width/2, y-height/2, width, height);
		g.fillOval(x-width/2, y-height/2, width, height);
		//g.fill3DRect(x-width/2, y-height/2, width, height, true);
	}
	
	private void drawProgressRect(Graphics g, int progress, int width, int height)
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
			//x = (int)(progress * width/100);
			x = (int)(m_chunk.width/2 +(progress* (width - m_chunk.width)/100));
			y = height/2;
		}
		
		//System.out.printf("x %d y %d %d %d\n", x, y, m_chunk.width, m_chunk.height);
		
		drawProgress(g, progress, width, height);
		//drawProgressRect2_1(g, x, y, m_chunk.width, m_chunk.height);
	}
	private void drawBackgroundRect(Graphics g, int width, int height)
	{
		if(m_imgBackground != null)
		{
			g.drawImage(m_imgBackground.getImage(), 0, 0, width, height, this);
		}
		else
		{
			g.setColor(m_bkColor);
			g.fillRect(0, 0, width, height);
		}
	}
	private void drawSliderRect(Graphics g, int progress, int width, int height)
	{
		drawBackgroundRect(g, width, height);
		//drawProgress(g, m_progress, width, height);
		
		drawProgressRect(g, progress, width, height);
		
	}
}
*/
