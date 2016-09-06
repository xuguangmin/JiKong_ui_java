package utils.msg;

import java.util.Vector;

public class MsgQueue2 
{
	private Vector<Message> queue = null;

	public MsgQueue2() 
	{
		queue = new Vector<Message>();
	}

	public synchronized void send(Message o) 
	{
		queue.addElement(o);
		//System.out.printf("MsgQueue send, type %d\n", o.GetMsgType());
	}

	public synchronized Message recv() 
	{
		if (queue.size() == 0)
			return null;
		Message o = queue.firstElement();
		queue.removeElementAt(0);// or queue[0] = null can also work
		return o;
	}
	
	public synchronized Message Get() 
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

