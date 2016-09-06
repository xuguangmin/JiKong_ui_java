package utils.msg;

/*
 * ��Ϊjava����locked by object���������synchronized �Ϳ��������߳�ͬ����������
 * ������Ϊ���̴߳��������Ĵ��task�Ķ��С�����client������װ�õ�task���Լ�thread�� 
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
