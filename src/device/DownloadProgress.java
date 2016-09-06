package device;

/* **************************************************************************

			版权所有 (C), 2001-2020, 北京飞利信科技股份有限公司

*****************************************************************************
	文件名称 : DownloadProgress.java
	作者           : 贾延刚
	生成日期 : 2013-11-05
	
	版本           : 1.0
	功能描述 : 根据下载的进度计算完成百分比、速度等信息

******************************************************************************/

public class DownloadProgress 
{
	private int  m_fileTotalSize = 0;
	private int  m_fileSizeAccumulate = 0;
	private long m_startTime;                  // 纳秒，下载的开始时间
	private long m_elapse;                     // 毫秒，下载耗费的时间
	
	public DownloadProgress()
	{
		this.Clear();
	}
	

	// 已经使用的时间
	public long GetElapse()
	{
		return m_elapse;
	}
	// 得到平均下载速度
	public long GetSpeed()
	{
		m_elapse = (int)((System.nanoTime() - m_startTime)/1000000L);
		return (m_elapse > 0) ? m_fileSizeAccumulate/m_elapse:0;
	}
	// 得到当前进度
	public int GetPercent()
	{
		return m_fileSizeAccumulate/(m_fileTotalSize/100);
	}
	
	// 累计文件已下载大小
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