package ccc_ui.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class DrawSlider 
{
	private Rectangle   m_rectSlipper     = new Rectangle();         // �������յĻ��Ƴߴ�
	private Rectangle   m_rectBar         = new Rectangle(0,0,0,0);  // ���������յĻ��Ƴߴ�
	private Color       m_colorSlipper    = new Color(205,225,120);  // �ޱ���ͼʱ��Ĭ�ϻ��鱳��ɫ
	private Color       m_colorBar        = new Color(148,126,79);   // �ޱ���ͼʱ��Ĭ�ϻ���������ɫ
		
	private int         m_edgeWidth              = 0;        // �߽��
	private int         m_edgeHeight             = 0;        // �߽��
	private int         m_barWidth               = 0;        // ��������
	private int         m_barHeight              = 0;        // ��������
	private int         m_slipperWidth           = 0;        // �����
	private int         m_slipperHeight          = 0;        // �����
	
	private ImageIcon   m_bkImageSlipper         = null;     // ���鱳��ͼ
	private Color       m_colorBackgroundSlipper = null;     // ���鱳��ɫ
	private ImageIcon   m_bkImageBar             = null;     // ����������ͼ
	private Color       m_colorBackgroundBar     = null;     // ����������ɫ
	private int         m_progress               = 0;        // ���������ٷֱ�
	private boolean     m_b_horizontal           = true;     // �Ƿ���򻬶�
	private boolean     m_b_prog_show            = true;               // �Ƿ񻭽�������	
	
	
	// �ڻ����ϻ���������
	private void DrawProgressText(Graphics g, int left, int top, int width, int height, String progress, boolean b_horizontal)
	{
		g.setColor(Color.black);
					
    	int txtWidth = g.getFontMetrics().stringWidth(progress);
		int txtHeight = g.getFontMetrics().getAscent();
		int x = left + (int)(width - txtWidth)/2;
		int y = top + txtHeight + (int)((height - txtHeight)/2 );
		g.drawString(progress, x, y);
	}
	// ������
	private void DrawSlipper(Graphics g, int left, int top, int width, int height)
	{
		if(m_bkImageSlipper != null)
		{
			g.drawImage(m_bkImageSlipper.getImage(), left, top, width, height, null);
		}
		else
		{
			if(null == m_colorBackgroundSlipper) g.setColor(m_colorSlipper);
			else{
				g.setColor(m_colorBackgroundSlipper);
			}
			g.fill3DRect(left, top, width, height, true);
		}
	}
	// ��������
	private void DrawBar(Graphics g, int left, int top, int width, int height)
	{
		if(m_bkImageBar != null)
		{
			g.drawImage(m_bkImageBar.getImage(), left, top, width, height, null);
		}
		else
		{
			if(null == m_colorBackgroundBar) g.setColor(m_colorBar);
			else{
				g.setColor(m_colorBackgroundBar);
			}
			g.fill3DRect(left, top, width, height, true);
		}
	}
	/*
	 *  ���㻬������λ��
	 *  �������������Ҿ���
	 */
	private int CalcuBarLocation(int edgeWidth, int barWidth)
	{
		return (int)((edgeWidth - barWidth)/2);
	}
	
	/*
	 *  ��������
	 *  �������ڿؼ����������Ҿ���
	 */
	private boolean StartDrawBar(Graphics g, int edgeWidth, int edgeHeight, 
			                              int barWidth, int barHeight)
	{	
		if(edgeWidth <= 0 || edgeHeight <= 0 || barWidth <= 0 || barHeight <= 0)
			return false;
		
		// ��黬�����ߴ磬���ܳ����ؼ��߽�
		if(barWidth > edgeWidth)   barWidth = edgeWidth;
		if(barHeight > edgeHeight) barHeight = edgeHeight;
				
		int left = CalcuBarLocation(edgeWidth, barWidth);
		int top  = CalcuBarLocation(edgeHeight, barHeight);
		
		DrawBar(g, left, top, barWidth, barHeight);
		m_rectBar.setBounds(left, top, barWidth, barHeight);
		return true;
	}
	
	/*
	 *  ������
	 *  ����
	 */
	private void StartDrawSlipper(Graphics g, int barLeft, int barTop, int barWidth, int barHeight, 
			                                  int slipperWidth, int slipperHeight, 
			                                  boolean b_horizontal, int progress)
	{
		if(slipperWidth <= 0 || slipperHeight <= 0 || progress < 0 || progress > 100)
			return;
		
		int x = 0, y = 0, increment = 0;
		if(b_horizontal)             // ����
		{
			// ˮƽ����ʱ������Ŀ�Ȳ������������Ŀ��
			if(slipperWidth > barWidth) slipperWidth = barWidth;         
			
			increment = ((barWidth - slipperWidth) * progress)/100;
			x = barLeft + increment;
			y = barTop + barHeight/2 - slipperHeight/2;
			
		}
		else
		{
			// ���򻬶�ʱ������ĸ߶Ȳ������������ĸ߶�
			if(slipperHeight > barHeight) slipperHeight = barHeight;   
						
			increment = ((barHeight - slipperHeight) * progress)/100;
			x = barLeft + barWidth/2 - slipperWidth/2;
			y = barTop + barHeight - slipperHeight - increment;
		}
		
		DrawSlipper(g, x, y, slipperWidth, slipperHeight);	
		m_rectSlipper.setBounds(x, y, slipperWidth, slipperHeight);
		if(m_b_prog_show) DrawProgressText(g, x, y, slipperWidth, slipperHeight, String.format("%d", m_progress), b_horizontal);
	}
	
	public void StartDraw(Graphics g)
	{
		//g.drawString("��20 50 ��ʼд", 20, 50);
		
		if(!StartDrawBar(g, m_edgeWidth, m_edgeHeight, m_barWidth, m_barHeight))
			return;
		
		// ��黬��ߴ�
		if(m_slipperWidth > m_edgeWidth)   m_slipperWidth = m_edgeWidth;
		if(m_slipperHeight > m_edgeHeight) m_slipperHeight = m_edgeHeight;
		StartDrawSlipper(g, m_rectBar.x, m_rectBar.y, m_rectBar.width, m_rectBar.height, 
				m_slipperWidth, m_slipperHeight, m_b_horizontal, m_progress);
		
	}
	
	/*	
	public void SetProgressBy(int x, int y)
	{
		System.out.printf("x = %d, y = %d\n", x, y);
		if(m_b_horizontal)
		{
			int left = CalcuBarLocation(m_edgeWidth, m_barWidth);
			
			
			int dis = (x - left - m_slipperWidth/2);
			if(dis < 0)
				return;
			
			int pro = (dis * 100)/(left - m_slipperWidth);
			SetProgress(pro);
		}
		else
		{
			
		}
	}*/
	
		
	
	public boolean PointInSlipperRect(int x, int y)
	{
		return m_rectSlipper.contains(x, y);
	}
	// ��ȥ����ߴ���ʣ�ಿ��
	public int GetBarLength()  
	{
		if(m_b_horizontal)
			return m_rectBar.width - m_rectSlipper.width;
		
		return m_rectBar.height - m_rectSlipper.height;
	}
	// �����ѻ����ĳ���
	public int GetSlideLength()  
	{
		if(m_b_horizontal)
			return m_rectSlipper.x - m_rectBar.x;
		
		return m_rectSlipper.y - m_rectBar.y;
	}
	
	public void SetSlipperImage(Color clBackground, ImageIcon bkImage)
	{
		m_colorBackgroundSlipper  = clBackground;
		m_bkImageSlipper = bkImage;
	}
	public void SetBarImage(Color clBackground, ImageIcon bkImage)
	{
		m_colorBackgroundBar = clBackground;
		m_bkImageBar = bkImage;
	}
	// ���ñ߽�ߴ�
	public void SetEdgeSize(int width, int height)
	{
		//System.out.printf("w = %d, h = %d\n", width, height);
		m_edgeWidth  = width;
		m_edgeHeight = height;
	}
	public void SetBarSize(int width, int height)
	{
		m_barWidth  = width;
		m_barHeight = height;
	}
	public void SetSlipperSize(int width, int height)
	{
		m_slipperWidth  = width;
		m_slipperHeight = height;
	}
	public void SetProgressShow(boolean b_show)
	{
		m_b_prog_show = b_show;
	}
	public void SetOrientation(boolean b_horizontal)
	{
		m_b_horizontal = b_horizontal;
	}
	public void SetProgress(int progress)
	{
		if(progress < 0 || progress > 100 || m_progress == progress)
			return;
		
		m_progress = progress; 
		//System.out.printf("m_progress = %d\n", m_progress);
	}
}