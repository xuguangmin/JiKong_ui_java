package device;

import utils.ByteArray;

public interface IRecvBuffer 
{
	/*
	 * 返回值是buffer中剩余的数据长度
	 */
	public abstract void RecvBufferData(ByteArray byteArray);
}
