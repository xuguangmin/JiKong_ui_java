package device;

/* **************************************************************************

			��Ȩ���� (C), 2001-2020, ���������ſƼ��ɷ����޹�˾

*****************************************************************************
	�ļ����� : DownloadProgress.java
	����           : ���Ӹ�
	�������� : 2013-11-05
	
	�汾           : 1.0
	�������� : �������صĽ��ȼ�����ɰٷֱȡ��ٶȵ���Ϣ

******************************************************************************/

public class DownloadProgress 
{
	private int  m_fileTotalSize = 0;
	private int  m_fileSizeAccumulate = 0;
	private long m_startTime;                  // ���룬���صĿ�ʼʱ��
	private long m_elapse;                     // ���룬���غķѵ�ʱ��
	
	public DownloadProgress()
	{
		this.Clear();
	}
	

	// �Ѿ�ʹ�õ�ʱ��
	public long GetElapse()
	{
		return m_elapse;
	}
	// �õ�ƽ�������ٶ�
	public long GetSpeed()
	{
		m_elapse = (int)((System.nanoTime() - m_startTime)/1000000L);
		return (m_elapse > 0) ? m_fileSizeAccumulate/m_elapse:0;
	}
	// �õ���ǰ����
	public int GetPercent()
	{
		return m_fileSizeAccumulate/(m_fileTotalSize/100);
	}
	
	// �ۼ��ļ������ش�С
	public void SetAccumulateSize(int size) {m_fileSizeAccumulate += size;}
		
	public void Clear() 
	{
		m_fileTotalSize = 0;
		m_fileSizeAccumulate = 0;
		m_startTime = 0;
		m_elapse = 0;	
	}
	
	public void Initial(int fileSize) 
	{
		m_fileTotalSize = fileSize;
		m_startTime = System.nanoTime();
		
		m_fileSizeAccumulate = 0;
		m_elapse = 0;
	}
}