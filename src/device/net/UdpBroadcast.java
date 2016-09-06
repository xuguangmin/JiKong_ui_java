/* ***************************************************************************

                  		版权所有 (C), 2013-2100

 *****************************************************************************
	  文件名称 : UdpBroadcase.java
	  作者           : 贾延刚
	  生成日期 : 2014-05-12
	
	  版本           : 1.0
	  功能描述 : UDP客户端
                                       发送广播，并接收返回	
                                       
	  修改历史 :

******************************************************************************/
package device.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpBroadcast implements Runnable
{
	private static final int   BUFFER_SIZE = 4096;
	private Thread             m_thread = null;
	private IUdpBroadcast      m_interface = null;
	private byte               m_data[] = null;
	private DatagramSocket     m_client = null;
	
	
	public UdpBroadcast()
	{	
		m_data = new byte[BUFFER_SIZE];
		m_thread = new Thread(this);
	}
								
	public void run()
	{
		DatagramPacket packet = new DatagramPacket(m_data, BUFFER_SIZE);
		while(true)
		{
			try {
				m_client.receive(packet);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			if(m_interface != null) m_interface.UdpRecvData(m_data, packet.getLength());
		}
	}	
		
	public boolean Broadcast(int port, byte b[], int len)
	{			
		try {
			DatagramPacket packet = new DatagramPacket(b, len, InetAddress.getByName("255.255.255.255"), port);
			m_client.send(packet);
		} 
		catch (UnknownHostException e1) 
		{
			e1.printStackTrace();
			return false;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean Initial(IUdpBroadcast interf)
	{
		try 
		{
			m_client = new DatagramSocket();
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
			return false;
		}
		m_interface = interf;
		m_thread.start();	
		return true;
	}
}