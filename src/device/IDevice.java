/* ***************************************************************************

                  		��Ȩ���� (C), 2013-2100

 *****************************************************************************
	  �ļ����� : IDevice.java
	  ����           : ���Ӹ�
	  �������� : 2013��
	
	  �汾           : 1.0
	  �������� : �豸�����ṩ�Ľӿ�
	
	  �޸���ʷ :

******************************************************************************/
package device;

public interface IDevice 
{
	public abstract void ConnectStatus(int connStatus);
	
	public abstract void DownloadVersion(boolean bSuccess, int fileType, String version);
	public abstract void DownloadFileInfo(int fileType, int fileSize, String fileName);
	/*
	 * percent :1 - 100
	 * speed   :byte/ms 
	 */
	public abstract void DownloadPercent(int percent, long speed);
	public abstract void DownloadFinished(boolean bSuccess, int fileType, String filePath);
	
	/* ����������֪ͨ���������ϵ��ļ��������� */
	public abstract void FileHasUpdated(int fileType);
	public abstract void ControlProperty(int ctrlId, int property, int value);
	public abstract void InvokePage(int page_no, boolean b_show);
	public abstract void DiscoverDevice(String ipAddr, String hostname);
}
