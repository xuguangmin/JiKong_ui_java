package utils.msg;

public class MsgObject 
{
	private int m_msgType;
	public MsgObject()
	{
		m_msgType = MsgType.MSG_OBJECT;
	}
	protected void SetMsgType(int msgType)
	{
		m_msgType = msgType;
	}
	
	public int GetMsgType()               
	{
		return m_msgType;
	}
}
