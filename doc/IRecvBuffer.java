package protocol;

public interface IRecvBuffer 
{
	/*
	 * 返回值是buffer中剩余的数据长度
	 */
	public abstract int RecvBufferData(byte buffer[], int data_len);
}
