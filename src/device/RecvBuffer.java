package device;

import utils.ByteArray;

public class RecvBuffer 
{
	public boolean Initial(IRecvBuffer ifRecvBuffer, int buffer_size)
	{
		if(ifRecvBuffer == null)
			return false;
		if(buffer_size <= 0)
			return false;

		m_buffer = new ByteArray(buffer_size);
		if(m_buffer == null)
			return false;

		m_ifRecvBuffer = ifRecvBuffer;
		return true;
	}
	
	public synchronized boolean AppendData(byte buffer[], int data_len)
	{
		if(buffer == null || data_len <= 0)
			return false;
		if(m_buffer == null || m_buffer.array_len <= 0 || m_buffer.data_len >= m_buffer.array_len)
			return false;

		int copy_len = (data_len > (m_buffer.array_len - m_buffer.data_len)) ? (m_buffer.array_len - m_buffer.data_len):data_len;
		System.arraycopy(buffer, 0, m_buffer.data, m_buffer.data_len, copy_len);
		m_buffer.data_len += copy_len;
		return true;
	}
	
	public synchronized boolean OutputBufferData()
	{
		if(m_buffer.data_len <= 0)
			return false;
		
		m_ifRecvBuffer.RecvBufferData(m_buffer);
		return true;
	}
	
	
	private ByteArray          m_buffer;	
	private IRecvBuffer        m_ifRecvBuffer;
}
