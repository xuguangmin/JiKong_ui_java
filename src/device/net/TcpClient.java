/* ***************************************************************************

                  		版权所有 (C), 2013-2100

 *****************************************************************************
	  文件名称 : TcpClient.java
	  作者           : 贾延刚
	  生成日期 : 2013年
	
	  版本           : 1.0
	  功能描述 : TCP客户端
	
	  修改历史 :

******************************************************************************/
package device.net;

import java.net.*;
import java.io.*;

public class TcpClient implements Runnable
{
	private static final int CONNECTING_NOTIFY = 2000;
	private static final int BUFFER_SIZE = 4096;
	
	private Object             m_lockThread = new Object();
	private Object             m_lockParams = new Object();	
	private Thread             m_thread = null;
	private ITcpClient         m_interface = null;
	private boolean            m_req_close = false;      // 主动关闭
	private long               m_connNotify = 0;         // 连接时，记录每次连接的起始时间，毫秒
	private byte               m_data[] = null;
	
	private OutputStream       m_outputStream = null;
	private Socket             m_client = null;
	private InetSocketAddress  m_sockAddr = null;
	
	
	public TcpClient()
	{	
		m_data = new byte[BUFFER_SIZE];
		m_thread = new Thread(this);
	}
		
	private void Reset()
	{
		m_outputStream = null;
		m_client = null;
	}
	
	private void SetCloseStatus(boolean b_close)
	{
		synchronized(m_lockParams){
			m_req_close = b_close;
		}
	}
	private boolean IsCloseStatus()
	{
		synchronized(m_lockParams){
			return m_req_close ? true:false;
		}
	}
	
	private boolean ModifyIpAddr(String strIpAddr, int ipPort)
	{
		synchronized(m_lockParams){
			try
			{
				m_sockAddr = new InetSocketAddress(strIpAddr, ipPort);
			}
			catch(IllegalArgumentException e)
			{
				return false;
			}
			catch(SecurityException e)
			{
				return false;
			}
			catch(Exception e)
			{}
			return true;
		}
	}
	private InetSocketAddress GetIpAddr()
	{
		synchronized(m_lockParams){
			return m_sockAddr;
		}
	}
	
	private void BlockThread()
	{
		synchronized(m_lockThread)
		{
			try {
				m_lockThread.wait();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	private void UnblockThread()
	{
		synchronized(m_lockThread)
		{
			m_lockThread.notify();
		}
	}
	
		
	private boolean EstablishConnect()
	{
		m_connNotify = System.currentTimeMillis();
		while(true)
		{
			if(IsCloseStatus())
				return false;
			
			if((System.currentTimeMillis() - m_connNotify) >= CONNECTING_NOTIFY)
			{
				m_connNotify = System.currentTimeMillis();
				m_interface.ConnectStatus(ITcpClient.CONN_STATUS_CONNECTING);
				m_client = new Socket();
			}
				
			try 
			{
				m_client.connect(GetIpAddr(), CONNECTING_NOTIFY);
				break;
			} 
			catch (IOException e) 
			{
				continue;
			}	
		}
		return true;
	}
	private void ExecuteTask()
	{
		try
		{	
			m_client = new Socket();
			if(!EstablishConnect())
				return;
			
			m_outputStream = m_client.getOutputStream();
			InputStream m_ins = m_client.getInputStream();
			m_interface.ConnectStatus(ITcpClient.CONN_STATUS_OPEN);
			
			while(true)
			{
				int ret = m_ins.read(m_data, 0, BUFFER_SIZE);
				if(ret <= 0)
					break;
				
				m_interface.RecvDataFromComm(m_data, ret);
			}
		}
		catch(IOException e)
		{
			//e.printStackTrace();
		}
		return;
		
	}
	
	public void run()
	{
		while(true)
		{
			BlockThread();
			
			while(true)
			{
				ExecuteTask();
				m_interface.ConnectStatus(ITcpClient.CONN_STATUS_CLOSE);
				Reset();
				
				if(IsCloseStatus())
					break;
			}
		}
	}	
	
	public boolean Send(byte b[], int len)
	{
		try
		{
			if(null == m_outputStream)
				return false;
			m_outputStream.write(b, 0, len);
		}
		catch(IOException e)
		{
			//e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void Close()
	{
		SetCloseStatus(true);
		
		try
		{
			if(m_client != null) m_client.close();
		}
		catch(IOException e)
		{
			//e.printStackTrace();	
		}
	}
	public void Open()
	{
		SetCloseStatus(false);
		UnblockThread();
	}
	public boolean SetIpAddr(String strIpAddr, int ipPort)
	{
		return ModifyIpAddr(strIpAddr, ipPort);
	}
	public boolean Initial(ITcpClient interf)
	{
		m_interface = interf;
		m_thread.start();	
		return true;
	}
}