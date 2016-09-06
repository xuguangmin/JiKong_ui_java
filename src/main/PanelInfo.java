package main;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class PanelInfo extends JPanel
{
	private static final long serialVersionUID = 7903841962486830478L;
	private int     m_progress = 30;
	private String  m_strProgress = "Update ...   ";
	private String  m_strMessage  = "";
	private String  m_connInfo  = "";
	private boolean m_connStatus  = false;
	
	public PanelInfo()
	{
		m_progress = 0;
	}
	private void drawProgress(Graphics g, int progress, int x, int y, int width, int height)
	{
		int blank = x;
		if(width <= 0)
			return;
		g.setColor(Color.blue);
		g.fillRect(blank, y, width, height);
		
		int width2 = (width * progress)/100;
		g.setColor(Color.yellow);
		g.fillRect(blank, y, width2, height);
				
		g.setColor(Color.red);
		int strWidth = g.getFontMetrics().stringWidth(m_strProgress);
		int strHeight = g.getFontMetrics().getHeight();
		g.drawString(m_strProgress, blank +(width - strWidth)/2, y+ strHeight);		
	}
	
	private void drawConnInfo(Graphics g, String strInfo, boolean b_connected, int x, int y, int width, int height)
	{	
		g.setColor(b_connected ? Color.green:Color.red);
		g.fillOval(height/4, height/4, height/2, height/2);
		//int strWidth = g.getFontMetrics().stringWidth(strInfo);
		
		g.setColor(Color.black);
		int strHeight = g.getFontMetrics().getHeight();
		g.drawString(strInfo, height, y+ strHeight);		
	}
	
	private void drawMessage(Graphics g, String strMessage, int x, int y, int width, int height)
	{
		//int blank = 30;
		if(width <= 0)
			return;
						
		g.setColor(Color.red);
		//int strWidth = g.getFontMetrics().stringWidth(strMessage);
		int strHeight = g.getFontMetrics().getHeight();
		g.drawString(strMessage, x, y + strHeight);		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		int x = 0, y = 0;
		int width = 150;
		int height = this.getHeight();
		drawConnInfo(g, m_connInfo, m_connStatus, x, y, width, height);
		
		// 画进度条和提示信息
		x = x+width;
		width = this.getWidth() - width;
		if(m_progress > 0) drawProgress(g, m_progress, x, y, width, height);
		
		// 提示消息，如果有，则叠加在进度条上
		if(!m_strMessage.isEmpty()) drawMessage(g, m_strMessage, x, y, width, height);
	}

	public void SetMessage(String strMessage)
	{
		if(null == strMessage)
			return;
		
		m_strMessage = strMessage;
		this.repaint();
	}
	
	public void SetConnInfo(String strMessage)
	{
		m_connInfo = strMessage;
		this.repaint();
	}
	public void SetConnStatus(boolean b_connected)
	{
		m_connStatus = b_connected;
		this.repaint();
	}
	
	public void SetProgress(int progress, double speed)
	{
		m_progress = progress;
		m_strProgress = String.format("%d%%, speed %.1fKB/s", progress, speed);
		this.repaint();
	}
}