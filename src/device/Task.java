/* ***************************************************************************

                  		版权所有 (C), 2013-2100

 *****************************************************************************
	  文件名称 : Task.java
	  作者           : 贾延刚
	  生成日期 : 2013年
	
	  版本           : 1.0
	  功能描述 : 保存当前正在处理的指令的信息
	
	  修改历史 :

******************************************************************************/
package device;

import utils.msg.MsgTask;

public class Task 
{
	private int              m_task;
	private long             m_pcsStart;                 // 开始时间，毫秒
	private int              m_delay;                    // 最多等待时间，毫秒
		
	public Task()
	{
		m_task     = MsgTask.TASK_INVALID;
		m_pcsStart = 0;
		m_delay    = 0;
	}
	
	public boolean IsRunning()
	{
		return m_task != MsgTask.TASK_INVALID;	
	}
	public boolean IsTimeout()
	{
		return ((System.currentTimeMillis() - m_pcsStart) >= m_delay)? true:false;		
	}
	public int Current()  {return m_task;}
	
	public void Clear()
	{
		m_task = MsgTask.TASK_INVALID;
	}
	
	// 符合条件才清除
	public boolean Clear(int task)
	{
		if(task == m_task)
		{
			m_task = MsgTask.TASK_INVALID;
			//System.out.printf("CTask::Clear pcs %d\n", pcs);
			return true;
		}
		return false;
	}
	
	// delay 延时，单位毫秒
	public void NewTask(int task, int delay)
	{
		m_task     = task;
		m_pcsStart = System.currentTimeMillis();
		m_delay    = delay;
	}
}
