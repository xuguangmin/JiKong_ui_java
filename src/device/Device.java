/* ***************************************************************************

                  		版权所有 (C), 2013-2100

 *****************************************************************************
	  文件名称 : Device.java
	  作者           : 贾延刚
	  生成日期 : 2013年
	
	  版本           : 1.0
	  功能描述 : 设备类
	                             对所操作设备的抽象，包括了和设备的通讯、协议的处理、从设备获取数据
	
	  修改历史 :

******************************************************************************/
package device;

import utils.ByteArray;
import utils.Utils;
import utils.msg.*;
import device.net.*;
import device.protocol.*;

public class Device implements Runnable, ITcpClient, IUdpBroadcast, IRecvBuffer, IProtocol
{
	private TcpClient         m_tcpClient  = null;
	private UdpBroadcast      m_udpClient  = null;
	private RecvBuffer        m_recvBuffer = null;
	private RecvBuffer        m_udpRecvBuffer = null;
	private Protocol          m_protocol   = null;
	private Thread            m_thread     = null;

	private MsgQueue          m_actionQueue = new MsgQueue();
	private MsgQueue          m_taskQueue   = new MsgQueue();
	private Task              m_task        = new Task();
	
	private DownloadProgress  m_dlProgress   = new DownloadProgress();
	private DownloadFile      m_downloadFile = new DownloadFile();
	private volatile boolean  m_bConnected = false;
	private IDevice           m_ifDevice;
	private int               m_udpPort      = 0;
		
	public Device()
	{}
	
	// -----------------  接口  ----------------- 
	
	/* ******************  接口 IUdpBroadcast  ****************** */
	public void UdpRecvData(byte buffer[], int data_len)
	{
		m_udpRecvBuffer.AppendData(buffer, data_len);
	}
	/* ******************  接口 ITcpClient  ****************** */
	// 7E 19 01 00 00 00 03 12 10 FE (1 BB)/(00 BB)
	// 7E 19 01 00 00 00 03 12 10 FE 00 BB
	@Override
	public void RecvDataFromComm(byte[] buffer, int data_len) 
	{
		m_recvBuffer.AppendData(buffer, data_len);
		//PrintByteArray(byteArray);
	}
	// 接口 ITcpClient
	public void ConnectStatus(int connStatus)
	{
		m_bConnected = (ITcpClient.CONN_STATUS_OPEN == connStatus) ? true:false;
		m_ifDevice.ConnectStatus(connStatus);
		if(m_bConnected)
		{
			LoginToServer();
			CheckZipFileVersion();
		}
	}
	/* ******************  接口 IRecvBuffer  ****************** */ 
	public void RecvBufferData(ByteArray byteArray)
	{
		PrintByteArray(byteArray);
		int consume = m_protocol.Parse(byteArray.data, byteArray.data_len);
		
		//System.out.printf("consume = %d\n", consume);
		byteArray.data_len = byteArray.data_len - consume;
	}
	/* ******************  接口 IProtocol  ****************** */ 
	public boolean SendPacket(byte buffer[], int len, int type)     // 获取到的发送数据
	{
		if(Protocol.DATA_TYPE_UDP == type)
		{
			m_udpClient.Broadcast(m_udpPort, buffer, len);
			return true;
		}
		
		if(!m_bConnected)
			return false;
		
		//System.out.println("SendPacket");
		return m_tcpClient.Send(buffer, len);
	}
	public void ProtocolPdu(PDU pdu)
	{
		PrintPdu(pdu);
		boolean bResult = process_protocol_pdu(pdu);
		after_process_protocol_pdu(bResult, pdu);
	}
		
	private void PrintPdu(PDU lpPdu)
	{
		if(lpPdu == null)
			return;
		
		//System.out.printf("\npdu :0x%X 0x%X(%d)\n", lpPdu.cmd, lpPdu.cmd_ex, lpPdu.data_len);	
		/*
		System.out.println("");
		System.out.printf("cmd 0x%X\n", lpPdu.cmd);	
		System.out.printf("cmd_ex 0x%X\n", lpPdu.cmd_ex);	
		System.out.printf("len %d\ndata: ", lpPdu.data_len);
		for(int k = 0; k < lpPdu.data_len; ++k)
		{	
			System.out.printf("0x%X ", lpPdu.data[k]);	
		}
		
		System.out.println("");
		System.out.printf("checksum1 0x%X\n", lpPdu.checksum1);	
		System.out.printf("checksum2 0x%X\n", lpPdu.checksum2);	
		*/
	}
	private void PrintByteArray(ByteArray byteArray)
	{
		//if(byteArray == null)
			//return;
		
		//for(int k = 0; k < byteArray.data_len; ++k)
		//{
			//System.out.printf("0x%X ", byteArray.data[k]);	
		//}
		
		//String str = new String(byteArray.data, 0, byteArray.data_len);
		//System.out.println(str);
	}
	
	/* ******************  接口 Runnable  ****************** */
	public void run()
	{
		//long count = System.currentTimeMillis();
		while(true)
		{
			ProcessQueueAction();
			m_udpRecvBuffer.OutputBufferData();
			m_recvBuffer.OutputBufferData();
			
			if(m_task.IsRunning())                   // 检查命令是否已经超时
			{
				if(m_task.IsTimeout()) 
				{
					AfterTaskTimeout(m_task.Current());
					m_task.Clear();
				}
			}
			else
			{
				ProcessQueueTask(); 	
			}
			
			try 
			{
				Thread.sleep(1);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
			/*
			if(!(has_1 || has_2 || has_3 || has_4))
			{
				//System.out.printf("System.%d %d\n", System.currentTimeMillis(), count);
				
				// 一个任务都没有，暂停一会儿
				// 这样可以让CPU占用率降下来
				if((System.currentTimeMillis() - count) > 1000)
				{
					try 
					{
						Thread.sleep(1000);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					count = System.currentTimeMillis();
				}
			}
			*/
		}
	}	
	private boolean ProcessQueueAction()
	{
		boolean result = false;
		while(true)
		{
			MsgObject msg = m_actionQueue.recv();
			if(msg == null)
				break;

			result = true;
			if(MsgType.MSG_ACTION == msg.GetMsgType())
				ProcessAction((MsgAction)msg);
		}
		return result;
	}
	private void ProcessAction(MsgAction msgAction)
	{
		switch(msgAction.m_action)
		{
		case MsgAction.ACTION_SET_COMM:
			m_tcpClient.SetIpAddr(msgAction.m_strValue, msgAction.m_ivalue);
			break;
		case MsgAction.ACTION_COMM_OPEN:
			m_tcpClient.Open();
			break;
		case MsgAction.ACTION_COMM_CLOSE:
			m_tcpClient.Close();
			break;
		//case MsgAction.ACTION_CLEAR_COMMAND:
			//m_taskQueue.Clear();
			//break;
		}
	}
	
	private int GetTaskTimeout(int task)
	{
		switch(task)
		{
		case MsgTask.TASK_DOWNLOAD:return 1000 * 60 * 30;   // 30 分钟
		}
		return 5000;
	}
	private boolean ProcessTask(MsgTask msgTask)
	{
		switch(msgTask.m_task)
		{
		case MsgTask.TASK_SEARCH_DEV:
			m_udpPort = msgTask.m_ctrlId;
			return m_protocol.SearchJikong();
			
		case MsgTask.TASK_LOGIN_TO      :return m_protocol.LoginToServer();
		case MsgTask.TASK_CHECK_VERSION :return m_protocol.GetFileVersion();
		case MsgTask.TASK_DOWNLOAD      :return m_protocol.GetFileInfo();
		case MsgTask.TASK_SEND_EVENT    :return m_protocol.SendEvent(msgTask.m_ctrlId, msgTask.m_eventId, msgTask.m_percent);
		}
		return false;
	}
	
	// 返回值为true，表明执行了一个任务，即使是无效的任务
	private boolean ProcessQueueTask()
	{
		MsgObject msg = m_taskQueue.recv();
		if(msg == null)
			return false;

		//System.out.println("ProcessQueueTask");
		if(msg.GetMsgType() != MsgType.MSG_TASK)
			return true;
		
		MsgTask msgTask = (MsgTask)msg;
		if(ProcessTask(msgTask)) 
		{
			switch(msgTask.m_task)
			{
			case MsgTask.TASK_LOGIN_TO:
			case MsgTask.TASK_SEND_EVENT:
			case MsgTask.TASK_SEARCH_DEV:	
				{
					/* 这些命令成功发送出去，就可以了
					 * 因为有些目前并无返回值，
					 * TASK_SEARCH_DEV的返回值不确定有
					 */
				}
				break;
			default:
				m_task.NewTask(msgTask.m_task, GetTaskTimeout(msgTask.m_task));
				break;
			}
		}
		return true;
	}
	
	/* 
	 * 如果任务在规定时间内未完成
	 * 
	 * 只有部分任务是需要等待的 
	 */
	private void AfterTaskTimeout(int task)
	{
		switch(task)
		{
		case MsgTask.TASK_CHECK_VERSION :
			m_ifDevice.DownloadVersion(false, -1, null);   // 此时不知道文件类型，是否要在任务中增加
			break;
		case MsgTask.TASK_DOWNLOAD :
			after_download_file(false, -1);
			break;
		}
	}
	
	// 文件版本
	private boolean process_protocol_download_file_cmd_ex_04(PDU lpPdu)
	{
		if(lpPdu.data_len < 2)
			return false;
		
		// success 
		// 如果不是错误码，则为文件类型
		if(lpPdu.data[0] != Protocol.PCS_EX_ERR)   
		{
			String version = new String(lpPdu.data, 1, lpPdu.data_len-1);
			m_ifDevice.DownloadVersion(true, (int)lpPdu.data[0], version);
			return true;
		}
		
		m_ifDevice.DownloadVersion(false, -1, null);       // (int)lpPdu.data[0]
		return false;		
	}
	// 文件信息
	private boolean process_protocol_download_file_cmd_ex_01(PDU lpPdu)
	{
		if(lpPdu.data_len < 2)
			return false;
			
		switch(lpPdu.data[0])
		{
		case Protocol.PCS_EX_ERR:
			return false;
		case 0x01:
			{
				if(lpPdu.data_len < 6)
					return false;
								
				int fileSize = Utils.calcu_data_length(lpPdu.data, 1, 4);
				String fileName = new String(lpPdu.data, 5, lpPdu.data_len-5);
				if(!m_downloadFile.CreateFile(fileName))
					return false;
				
				m_dlProgress.Initial(fileSize);	
				m_ifDevice.DownloadFileInfo((int)lpPdu.data[0], fileSize, fileName);
				
				if(!m_protocol.GetFileDataStart())
					return false;
			}
			break;
		}
		
		return true;
	}
	// 传送文件数据
	private boolean process_protocol_download_file_cmd_ex_02(PDU lpPdu)
	{
		if(lpPdu.data_len < 2)
			return false;
		
		switch(lpPdu.data[0])
		{
		case Protocol.PCS_EX_ERR:
			return false;
		case 0x01:
			{
				if(!m_downloadFile.WriteFile(lpPdu.data, 1, lpPdu.data_len-1))
					return false;
				
				m_dlProgress.SetAccumulateSize(lpPdu.data_len - 1);
				m_ifDevice.DownloadPercent(m_dlProgress.GetPercent(), m_dlProgress.GetSpeed());
				if(!m_protocol.GetFileDataAck())
					return false;
			}
			break;
		}
		return true;
	}
	// 服务端文件数据传送过程结束
	private boolean process_protocol_download_file_cmd_ex_03(PDU lpPdu)
	{
		if(lpPdu.data_len < 2)
			return false;
		
		boolean bSuccess = (Protocol.PCS_EX_OK == lpPdu.data[0]) ? true:false;
		
		// 错误状态，留到after函数中统一处理
		if(bSuccess) after_download_file(true, (int)lpPdu.data[0]);
		return bSuccess;
	}
	
	private void after_download_file(boolean bSuccess, int fileType)
	{
		long elapse = m_dlProgress.GetElapse();
		m_downloadFile.Close();
		m_dlProgress.Clear();
		if(!bSuccess)
		{
			m_ifDevice.DownloadFinished(false, -1, null);
			return;
		}
		
		m_ifDevice.DownloadFinished(bSuccess, fileType, m_downloadFile.GetFilePath());
		System.out.printf("Device :download time :%d ms\n", elapse);
	}
	private boolean process_protocol_download_file_cmd_ex_05(PDU lpPdu)
	{
		if(lpPdu.data_len < 1)
			return false;
		
		m_ifDevice.FileHasUpdated((int)lpPdu.data[0]);	
		return true;
	}
	
	/* 0x12 下载文件 */
	private boolean process_protocol_download_file(PDU lpPdu)
	{
		switch(lpPdu.cmd_ex)
		{
		case Protocol.PCS_EX_04 :return process_protocol_download_file_cmd_ex_04(lpPdu); /* 文件版本 */
		case Protocol.PCS_EX_01 :return process_protocol_download_file_cmd_ex_01(lpPdu); /* 下载文件信息 */
		case Protocol.PCS_EX_02 :return process_protocol_download_file_cmd_ex_02(lpPdu); /* 文件数据 */
		case Protocol.PCS_EX_03 :return process_protocol_download_file_cmd_ex_03(lpPdu); /* 文件传送完成 */
		
		case Protocol.PCS_EX_05 :return process_protocol_download_file_cmd_ex_05(lpPdu); /* 服务器主动通知，文件需要被更新 */
		}
		return false;
	}
	
	// 文件信息
	private boolean process_protocol_control_property_cmd_ex_11(PDU lpPdu)
	{
		if(lpPdu.data_len < 12)
			return false;
			
		//for(int k = 0; k < lpPdu.data_len; ++k)
		//{	
			//System.out.printf("0x%X ", lpPdu.data[k]);	
		//}
				
		int ctrlId   = Utils.calcu_data_length(lpPdu.data, 0, 4);
		int property = Utils.calcu_data_length(lpPdu.data, 4, 4);
		int value    = Utils.calcu_data_length(lpPdu.data, 8, 4);
		m_ifDevice.ControlProperty(ctrlId, property, value); 		
		return true;
	}
	
	private boolean process_protocol_control_property_cmd_ex_13(PDU lpPdu)
	{
		if(lpPdu.data_len < 8)
			return false;
							
		int page_no = Utils.calcu_data_length(lpPdu.data, 0, 4);
		int b_show  = Utils.calcu_data_length(lpPdu.data, 4, 4);
		
		System.out.printf("process_protocol_control_property_cmd_ex_13 page_no= %d b_show = %d\n", page_no, b_show);
		m_ifDevice.InvokePage(page_no, (0 == b_show) ? false:true); 		
		return true;
	}
		
	private boolean process_protocol_control_property(PDU lpPdu)
	{
		switch(lpPdu.cmd_ex)
		{
		case Protocol.PCS_EX_11 :return process_protocol_control_property_cmd_ex_11(lpPdu); /* 设置属性 */
		case Protocol.PCS_EX_13 :return process_protocol_control_property_cmd_ex_13(lpPdu); /* 显示或隐藏弹出页 */
		}
		return false;
	}
	
	// 0xE0 0x01
	private boolean process_protocol_discover_device_cmd_ex_01(PDU lpPdu)
	{
		if(lpPdu.data_len < 12)
			return false;
					
		int step = 0;
		int len   = Utils.TwoBytesToInt(lpPdu.data, 0, 2);
		step += 2;
		
		// IP
		String ipAddr = new String(lpPdu.data, 2, len);
		step += len;
		
	
		len = Utils.TwoBytesToInt(lpPdu.data, step, 2);
		step += 2;
		String hostname = new String(lpPdu.data, step, len);
				
		m_ifDevice.DiscoverDevice(ipAddr, hostname);
		return true;
	}
	private boolean process_protocol_discover_device(PDU lpPdu)
	{
		switch(lpPdu.cmd_ex)
		{
		case Protocol.PCS_EX_01 :return process_protocol_discover_device_cmd_ex_01(lpPdu); 
		}
		return false;
	}
	
	private boolean process_protocol_pdu(PDU lpPdu)
	{
		if(lpPdu == null) return false;
		//PrintPdu(lpPdu);	
		
		switch(lpPdu.cmd)
		{
		case Protocol.PCS_DOWNLOAD_FILE    :return process_protocol_download_file(lpPdu);      /* 0x12 下载文件 */
		case Protocol.PCS_CTRL_PROPERTY    :return process_protocol_control_property(lpPdu);   /* 0x39 控件属性 */			
		case Protocol.PCS_DISCOVER_DEVICE  :return process_protocol_discover_device(lpPdu);    /* 发现设备 */
		default:
			System.out.println("This data packet is bad: unknown command\n");
			break;
		}
		return true;
	}
	
	/*
	 * 
	 */
	private void after_process_protocol_pdu(boolean bSuccess, PDU lpPdu)
	{
		if(lpPdu == null) return;
		switch(lpPdu.cmd)
		{
		case Protocol.PCS_DOWNLOAD_FILE :
			{
				switch(lpPdu.cmd_ex)
				{
				case Protocol.PCS_EX_04:
					m_task.Clear(MsgTask.TASK_CHECK_VERSION);
					break;
				case Protocol.PCS_EX_01 :
				case Protocol.PCS_EX_02 :
				case Protocol.PCS_EX_03 :
					if(!bSuccess)
					{
						// 整个下载过程的任一步出错，
						// 都结束下载，返回错误
						after_download_file(false, -1);
						m_task.Clear(MsgTask.TASK_DOWNLOAD);
					}
					else
					{
						// 最后一步结束，清除任务
						if(Protocol.PCS_EX_03 == lpPdu.cmd_ex) m_task.Clear(MsgTask.TASK_DOWNLOAD);
						
					}
					break;
				}
			}
			break;
		case Protocol.PCS_CTRL_PROPERTY   :break;	
		case Protocol.PCS_DISCOVER_DEVICE :break;
		}
	}
		
	// 发送界面事件
	public boolean SendEvent(int ctrlId, int eventId, int percent)
	{		
		m_taskQueue.send(new MsgTask(MsgTask.TASK_SEND_EVENT, ctrlId, eventId, percent));
		return true;
	}
	// 下载文件
	public boolean DownloadZipFile()
	{
		m_taskQueue.send(new MsgTask(MsgTask.TASK_DOWNLOAD));
		return true;
	}
	// 检查文件版本
	public boolean CheckZipFileVersion()
	{
		m_taskQueue.send(new MsgTask(MsgTask.TASK_CHECK_VERSION));
		return true;	
	}
	// 登录到服务器
	public boolean LoginToServer()
	{
		m_taskQueue.send(new MsgTask(MsgTask.TASK_LOGIN_TO));
		return true;
	}
	
	// 搜索集控主机
	public boolean SearchJikong(int udpPort)
	{
		m_taskQueue.send(new MsgTask(MsgTask.TASK_SEARCH_DEV, udpPort, 0, 0));
		return true;
	}
		
	public boolean CloseComm()
	{
		m_actionQueue.send(new MsgAction(MsgAction.ACTION_COMM_CLOSE, null, 0));
		return true;
	}
	// 开始运行
	public boolean OpenComm()
	{
		m_actionQueue.send(new MsgAction(MsgAction.ACTION_COMM_OPEN, null, 0));
		return true;
	}
	// 设置通信参数
	public boolean SetComm(String strIpAddr, int ipPort)
	{
		m_actionQueue.send(new MsgAction(MsgAction.ACTION_SET_COMM, strIpAddr, ipPort));
		return true;
	}
	// 初始化
	public boolean Initial(IDevice interf, String dirDownload)
	{
		m_protocol  = new Protocol(this);
		m_recvBuffer = new RecvBuffer();
		m_recvBuffer.Initial(this, 8192);
		m_udpRecvBuffer = new RecvBuffer();
		m_udpRecvBuffer.Initial(this, 8192);
		
		m_tcpClient = new TcpClient();
		m_tcpClient.Initial(this);
		m_udpClient = new UdpBroadcast();
		m_udpClient.Initial(this);
				
		m_thread = new Thread(this);
		m_thread.start();
		m_ifDevice = interf;
		m_downloadFile.SetSaveFolder(dirDownload);
		return true;
	}
}
