package utils.msg;

/*
 * 因为java中是locked by object的所以添加synchronized 就可以用于线程同步锁定对象
 * 可以作为多线程处理多任务的存放task的队列。他的client包括封装好的task类以及thread类 
 */


import java.util.*;

public class MsgQueue
{
	private Vector<MsgObject> queue = null;

	public MsgQueue() {
		queue = new Vector<MsgObject>();
	}

	public synchronized void send(MsgObject o) {
		queue.addElement(o);
		//System.out.printf("MsgQueue send, type %d\n", o.GetMsgType());
	}

	public synchronized MsgObject recv() 
	{
		if (queue.size() == 0)
			return null;
		MsgObject o = queue.firstElement();
		queue.removeElementAt(0);// or queue[0] = null can also work
		return o;
	}
	
	public synchronized MsgObject Get() 
	{
		if (queue.size() == 0)
			return null;
		
		return queue.firstElement();
	}
	
	public synchronized void Delete() 
	{
		if (queue.size() == 0)
			return;
		
		queue.removeElementAt(0);// or queue[0] = null can also work
	}
	public synchronized void Clear() 
	{
		if (queue.size() == 0)
			return;
		
		queue.clear();
	}
}
