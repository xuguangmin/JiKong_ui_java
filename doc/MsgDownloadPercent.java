package utils.msg;


public class MsgDownloadPercent extends MsgObject 
{
	public int m_percent = 0;
	public long m_speed = 0;
	public MsgDownloadPercent(int percent, long speed)
	{
		SetMsgType(MsgType.MSG_DL_PERCENT);
		m_percent = percent;
		m_speed   = speed;
	}
}
