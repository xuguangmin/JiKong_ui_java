package protocol;

public class RecvBuffer 
{
	public boolean Initial(IRecvBuffer ifRecvBuffer, int buffer_size)
	{
		if(ifRecvBuffer == null)
			return false;
		if(buffer_size <= 0)
			return false;

		m_buffer = new byte[buffer_size];
		if(m_buffer == null)
			return false;

		m_buffer_size  = buffer_size;
		m_ifRecvBuffer = ifRecvBuffer;
		return true;
	}
	
	public synchronized boolean AppendData(byte buffer[], int data_len)
	{
		if(buffer == null || data_len <= 0)
			return false;
		if(m_buffer == null || m_buffer_size <= 0 || m_data_len >= m_buffer_size)
			return false;

		int copy_len = (data_len > (m_buffer_size - m_data_len)) ? (m_buffer_size - m_data_len):data_len;
		System.arraycopy(buffer, 0, m_buffer, m_data_len, copy_len);
		m_data_len += copy_len;
		return true;
	}
	
	public synchronized void OutputBufferData()
	{
		if(m_data_len <= 0)
			return;

		m_data_len = m_ifRecvBuffer.RecvBufferData(m_buffer, m_data_len);
	}
	
	
	private byte               m_buffer[];
	private int                m_buffer_size;
	private int                m_data_len;	
	private IRecvBuffer        m_ifRecvBuffer;

}
