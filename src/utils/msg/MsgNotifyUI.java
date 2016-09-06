package utils.msg;

public class MsgNotifyUI extends MsgObject
{
	public static final int CONNECT_STATUS   = 0;
	public static final int UIFILE_UPDATE    = 1;
	public static final int MSG_VERSION_INFO = 2;
	
	public int    m_notifyType;
	public int    m_value;
	public String m_strValue = null;
	
	public MsgNotifyUI(int notifyType, int ivalue, String strValue)
	{
		SetMsgType(MsgType.MSG_NOTIFY_UI);
		m_notifyType = notifyType;
		m_value      = ivalue;
		m_strValue   = strValue;
	}
}
