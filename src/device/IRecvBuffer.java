package device;

import utils.ByteArray;

public interface IRecvBuffer 
{
	/*
	 * ����ֵ��buffer��ʣ������ݳ���
	 */
	public abstract void RecvBufferData(ByteArray byteArray);
}
