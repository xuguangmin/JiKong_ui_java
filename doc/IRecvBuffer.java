package protocol;

public interface IRecvBuffer 
{
	/*
	 * ����ֵ��buffer��ʣ������ݳ���
	 */
	public abstract int RecvBufferData(byte buffer[], int data_len);
}
