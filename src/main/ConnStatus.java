package main;

import utils.lang.Language;
import device.net.ITcpClient;

public class ConnStatus 
{	
	private int m_connStatus = ITcpClient.CONN_STATUS_CLOSE;
	private boolean b_connJitter = false;
	
	public String GetConnectingNotify()
	{
		return String.format("%s %s", Language.Get(Language.LID_STRING_3), b_connJitter ? "......" : "...");
	}
	
	public boolean IsConnected()   
	{
		return (ITcpClient.CONN_STATUS_OPEN == m_connStatus) ? true:false;
	}
	
	public void SaveStatus(int connStatus)
	{
		m_connStatus = connStatus;
		if(ITcpClient.CONN_STATUS_CONNECTING == m_connStatus)
			b_connJitter = !b_connJitter;
	}
}
