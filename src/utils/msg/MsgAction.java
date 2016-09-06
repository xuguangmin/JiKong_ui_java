package utils.msg;

public class MsgAction extends MsgObject
{
	public static final int ACTION_SET_COMM     = 0;
	public static final int ACTION_COMM_OPEN    = 1;
	public static final int ACTION_COMM_CLOSE   = 2;
	//public static final int ACTION_CLEAR_COMMAND= 3;
	
	public int    m_action = -1;
	public String m_strValue;
	public int    m_ivalue;
	public MsgAction(int action, String strValue, int ivalue)
	{
		SetMsgType(MsgType.MSG_ACTION);
		m_action   = action;
		m_strValue = strValue;
		m_ivalue   = ivalue;
	}
}
