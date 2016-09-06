package utils.msg;

public class MsgTask extends MsgObject
{
	public static final int TASK_INVALID        = -1;
	public static final int TASK_LOGIN_TO       = 0;
	public static final int TASK_CHECK_VERSION  = 1;
	public static final int TASK_DOWNLOAD       = 2;
	public static final int TASK_SEND_EVENT     = 3;
	public static final int TASK_SEARCH_DEV     = 4;
	
	public int m_task;
	public int m_ctrlId;
	public int m_eventId;
	public int m_percent;
	
	public MsgTask(int task)
	{
		this(task, 0, 0, 0);
	}
	public MsgTask(int task, int ctrlId, int eventId, int percent)
	{
		SetMsgType(MsgType.MSG_TASK);
		m_task     = task;
		m_ctrlId  = ctrlId;
		m_eventId = eventId;
		m_percent = percent;
	}		
}
