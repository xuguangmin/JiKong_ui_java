/* ***************************************************************************

                  		��Ȩ���� (C), 2013-2100

 *****************************************************************************
	  �ļ����� : ITcpClient.java
	  ����           : ���Ӹ�
	  �������� : 2013��
	
	  �汾           : 1.0
	  �������� : TCP�ͻ��˽ӿ�
	
	  �޸���ʷ :

******************************************************************************/
package device.net;

public interface ITcpClient 
{
	public static final int CONN_STATUS_CLOSE       = 0;   // �ѹر�
	public static final int CONN_STATUS_OPEN        = 1;   // �Ѵ�
	public static final int CONN_STATUS_CONNECTING  = 2;   // ��������
			
	public abstract void RecvDataFromComm(byte buffer[], int data_len);
	public abstract void ConnectStatus(int connStatus);
}
