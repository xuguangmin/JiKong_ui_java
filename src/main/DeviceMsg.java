/* ***************************************************************************

                  		��Ȩ���� (C), 2013-2100

 *****************************************************************************
	  �ļ����� : DeviceMsg.java
	  ����           : ���Ӹ�
	  �������� : 2013��
	
	  �汾           : 1.0
	  �������� : ���豸�ӿڵķ�װ
	  
	                            ���������豸�ӿڵ�����ת������һ����Ϣ����
	                            �豸�ӿں��������ಢ����ͬһ���̣߳�������Ҫ����һ��ת��
	
	  �޸���ʷ :

******************************************************************************/
package main;

import utils.msg.Message;
import utils.msg.MsgIdUi;
import utils.msg.MsgQueue2;
import utils.msg.ParaDiscoverDev;
import device.IDevice;

public class DeviceMsg implements IDevice
{
	private MsgQueue2 m_handler = null;
	public DeviceMsg(MsgQueue2 handler)
	{
		m_handler = handler;
	}
	
	private void PostMessage(int msgId, int arg1, int arg2, Object obj)
	{
		if(null == m_handler)
			return;
		
		Message msg = new Message();
		msg.what = msgId;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		msg.obj  = obj;
		m_handler.send(msg);	
	}
	private void PostMessage(int msgId, int arg1, int arg2, int arg3)
	{
		if(null == m_handler)
			return;
		
		Message msg = new Message();
		msg.what = msgId;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		msg.arg3 = arg3;
		m_handler.send(msg);	
	}
	
	
	/* 
	 * �������ϴ������ļ�
	 * 
	 * fileType   :�ļ�����
	 */
	public void FileHasUpdated(int fileType)
	{		
		PostMessage(MsgIdUi.MSG_UI_HAS_NEW_ZIP, fileType, 0, null);     // ������Ӽ��汾��ʼ�����޷���ȡ�����ļ��İ汾��Ϣ
	}
	
	/* 
	 * �ļ����ع������
	 * 
	 * bSuccess   :���������Ƿ�ɹ�
	 * filePath   :�����ļ�����ȫ·����
	 */
	public void DownloadFinished(boolean bSuccess, int fileType, String filePath)
	{
		PostMessage(MsgIdUi.MSG_UI_DL_PERCENT, 0, 0, null);
		PostMessage(MsgIdUi.MSG_UI_DL_FINISH, bSuccess ? 1:0, fileType, filePath);
	}
	/* 
	 * ���ذٷֱ�
	 * 
	 * percent   :�ٷֱ�  0~100
	 * speed     :�����ٶȣ���һ��ƽ���ٶ�
	 */
	public void DownloadPercent(int percent, long speed)
	{
		PostMessage(MsgIdUi.MSG_UI_DL_PERCENT, percent, (int)speed, null);	
	}
	/* 
	 * ��ѯ�õ����ļ��� �ļ���С
	 * 
	 * fileType  :�ļ�����
	 * fileSize  :�ļ���С
	 * fileName  :�ļ�������·����
	 */
	public void DownloadFileInfo(int fileType, int fileSize, String fileName)
	{
		PostMessage(MsgIdUi.MSG_UI_ZIP_INFO, fileType, fileSize, fileName);
	}
	/* 
	 * ��ѯ�õ����ļ�  �汾��Ϣ
	 * 
	 * fileType :�ļ�����
	 * version  :�汾�ַ������ļ���Ψһ�Ա�ʶ��
	 */
	public void DownloadVersion(boolean bSuccess, int fileType, String version)
	{
		PostMessage(MsgIdUi.MSG_UI_VERSION_INFO, bSuccess ? 1:0, fileType, version);
	}
	
	// ����״̬֪ͨ
	public void ConnectStatus(int connStatus)
	{
		PostMessage(MsgIdUi.MSG_CONNECT_STATUS, connStatus, 0, null);
	}
	
	/* 
	 * ���������������Ŀؼ�����
	 * 
	 * property:
	 *     0x1  enable
	 *               value
	 *                    0x1 true
	 *                    0x0 false
	 *     0x2  visible
	 *               value
	 *                    0x1 true
	 *                    0x0 false
	 */
	@Override
	public void ControlProperty(int ctrlId, int property, int value) 
	{
		PostMessage(MsgIdUi.MSG_UI_CTRL_PROPERTY, ctrlId, property, value);
	}

	// ��ʾ�������ص���ҳ
	@Override
	public void InvokePage(int page_no, boolean b_show) 
	{
		PostMessage(MsgIdUi.MSG_UI_INVOKE_PAGE, page_no, b_show ? 1:0, 0);
				
	}
	
	// ���ҷ�����  �õ��ķ�����Ϣ��IP��ַ��������
	@Override
	public void DiscoverDevice(String ipAddr, String hostname) 
	{
		ParaDiscoverDev para = new ParaDiscoverDev();
		para.ipAddr   = ipAddr;
		para.hostname = hostname;
		PostMessage(MsgIdUi.MSG_UI_DISCOVER_DEV, 0, 0, para);
	}
}
