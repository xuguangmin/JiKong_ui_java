/* ***************************************************************************

                  		版权所有 (C), 2013-2100

 *****************************************************************************
	  文件名称 : DeviceMsg.java
	  作者           : 贾延刚
	  生成日期 : 2013年
	
	  版本           : 1.0
	  功能描述 : 对设备接口的封装
	  
	                            并把来自设备接口的数据转放入了一个消息队列
	                            设备接口和主窗口类并不在同一个线程，所以需要这样一个转化
	
	  修改历史 :

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
	 * 服务器上传了新文件
	 * 
	 * fileType   :文件类型
	 */
	public void FileHasUpdated(int fileType)
	{		
		PostMessage(MsgIdUi.MSG_UI_HAS_NEW_ZIP, fileType, 0, null);     // 如果不从检查版本开始，则无法获取到新文件的版本信息
	}
	
	/* 
	 * 文件下载过程完成
	 * 
	 * bSuccess   :本次下载是否成功
	 * filePath   :下载文件名（全路径）
	 */
	public void DownloadFinished(boolean bSuccess, int fileType, String filePath)
	{
		PostMessage(MsgIdUi.MSG_UI_DL_PERCENT, 0, 0, null);
		PostMessage(MsgIdUi.MSG_UI_DL_FINISH, bSuccess ? 1:0, fileType, filePath);
	}
	/* 
	 * 下载百分比
	 * 
	 * percent   :百分比  0~100
	 * speed     :下载速度，是一个平均速度
	 */
	public void DownloadPercent(int percent, long speed)
	{
		PostMessage(MsgIdUi.MSG_UI_DL_PERCENT, percent, (int)speed, null);	
	}
	/* 
	 * 查询得到的文件名 文件大小
	 * 
	 * fileType  :文件类型
	 * fileSize  :文件大小
	 * fileName  :文件名（无路径）
	 */
	public void DownloadFileInfo(int fileType, int fileSize, String fileName)
	{
		PostMessage(MsgIdUi.MSG_UI_ZIP_INFO, fileType, fileSize, fileName);
	}
	/* 
	 * 查询得到的文件  版本信息
	 * 
	 * fileType :文件类型
	 * version  :版本字符串（文件的唯一性标识）
	 */
	public void DownloadVersion(boolean bSuccess, int fileType, String version)
	{
		PostMessage(MsgIdUi.MSG_UI_VERSION_INFO, bSuccess ? 1:0, fileType, version);
	}
	
	// 连接状态通知
	public void ConnectStatus(int connStatus)
	{
		PostMessage(MsgIdUi.MSG_CONNECT_STATUS, connStatus, 0, null);
	}
	
	/* 
	 * 服务器主动发来的控件属性
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

	// 显示或者隐藏弹出页
	@Override
	public void InvokePage(int page_no, boolean b_show) 
	{
		PostMessage(MsgIdUi.MSG_UI_INVOKE_PAGE, page_no, b_show ? 1:0, 0);
				
	}
	
	// 查找服务器  得到的返回信息：IP地址、主机名
	@Override
	public void DiscoverDevice(String ipAddr, String hostname) 
	{
		ParaDiscoverDev para = new ParaDiscoverDev();
		para.ipAddr   = ipAddr;
		para.hostname = hostname;
		PostMessage(MsgIdUi.MSG_UI_DISCOVER_DEV, 0, 0, para);
	}
}
