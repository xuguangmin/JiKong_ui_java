/* ***************************************************************************

                  		版权所有 (C), 2013-2100

 *****************************************************************************
	  文件名称 : ITcpClient.java
	  作者           : 贾延刚
	  生成日期 : 2013年
	
	  版本           : 1.0
	  功能描述 : TCP客户端接口
	
	  修改历史 :

******************************************************************************/
package device.net;

public interface ITcpClient 
{
	public static final int CONN_STATUS_CLOSE       = 0;   // 已关闭
	public static final int CONN_STATUS_OPEN        = 1;   // 已打开
	public static final int CONN_STATUS_CONNECTING  = 2;   // 正在连接
			
	public abstract void RecvDataFromComm(byte buffer[], int data_len);
	public abstract void ConnectStatus(int connStatus);
}
