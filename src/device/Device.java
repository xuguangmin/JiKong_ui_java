/* ***************************************************************************

                  		��Ȩ���� (C), 2013-2100

 *****************************************************************************
	  �ļ����� : Device.java
	  ����           : ���Ӹ�
	  �������� : 2013��
	
	  �汾           : 1.0
	  �������� : �豸��
	                             ���������豸�ĳ��󣬰����˺��豸��ͨѶ��Э��Ĵ������豸��ȡ����
	
	  �޸���ʷ :

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
	
	// -----------------  �ӿ�  ----------------- 
	
	/* ******************  �ӿ� IUdpBroadcast  ****************** */
	public void UdpRecvData(byte buffer[], int data_len)
	{
		m_udpRecvBuffer.AppendData(buffer, data_len);
	}
	/* ******************  �ӿ� ITcpClient  ****************** */
	// 7E 19 01 00 00 00 03 12 10 FE (1 BB)/(00 BB)
	// 7E 19 01 00 00 00 03 12 10 FE 00 BB
	@Override
	public void RecvDataFromComm(byte[] buffer, int data_len) 
	{
		m_recvBuffer.AppendData(buffer, data_len);
		//PrintByteArray(byteArray);
	}
	// �ӿ� ITcpClient
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
	/* ******************  �ӿ� IRecvBuffer  ****************** */ 
	public void RecvBufferData(ByteArray byteArray)
	{
		PrintByteArray(byteArray);
		int consume = m_protocol.Parse(byteArray.data, byteArray.data_len);
		
		//System.out.printf("consume = %d\n", consume);
		byteArray.data_len = byteArray.data_len - consume;
	}
	/* ******************  �ӿ� IProtocol  ****************** */ 
	public boolean SendPacket(byte buffer[], int len, int type)     // ��ȡ���ķ�������
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
	
	/* ******************  �ӿ� Runnable  ****************** */
	public void run()
	{
		//long count = System.currentTimeMillis();
		while(true)
		{
			ProcessQueueAction();
			m_udpRecvBuffer.OutputBufferData();
			m_recvBuffer.OutputBufferData();
			
			if(m_task.IsRunning())                   // ��������Ƿ��Ѿ���ʱ
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
				
				// һ������û�У���ͣһ���
				// ����������CPUռ���ʽ�����
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
		case MsgTask.TASK_DOWNLOAD:return 1000 * 60 * 30;   // 30 ����
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
	
	// ����ֵΪtrue������ִ����һ�����񣬼�ʹ����Ч������
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
					/* ��Щ����ɹ����ͳ�ȥ���Ϳ�����
					 * ��Ϊ��ЩĿǰ���޷���ֵ��
					 * TASK_SEARCH_DEV�ķ���ֵ��ȷ����
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
	 * ��������ڹ涨ʱ����δ���
	 * 
	 * ֻ�в�����������Ҫ�ȴ��� 
	 */
	private void AfterTaskTimeout(int task)
	{
		switch(task)
		{
		case MsgTask.TASK_CHECK_VERSION :
			m_ifDevice.DownloadVersion(false, -1, null);   // ��ʱ��֪���ļ����ͣ��Ƿ�Ҫ������������
			break;
		case MsgTask.TASK_DOWNLOAD :
			after_download_file(false, -1);
			break;
		}
	}
	
	// �ļ��汾
	private boolean process_protocol_download_file_cmd_ex_04(PDU lpPdu)
	{
		if(lpPdu.data_len < 2)
			return false;
		
		// success 
		// ������Ǵ����룬��Ϊ�ļ�����
		if(lpPdu.data[0] != Protocol.PCS_EX_ERR)   
		{
			String version = new String(lpPdu.data, 1, lpPdu.data_len-1);
			m_ifDevice.DownloadVersion(true, (int)lpPdu.data[0], version);
			return true;
		}
		
		m_ifDevice.DownloadVersion(false, -1, null);       // (int)lpPdu.data[0]
		return false;		
	}
	// �ļ���Ϣ
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
	// �����ļ�����
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
	// ������ļ����ݴ��͹��̽���
	private boolean process_protocol_download_file_cmd_ex_03(PDU lpPdu)
	{
		if(lpPdu.data_len < 2)
			return false;
		
		boolean bSuccess = (Protocol.PCS_EX_OK == lpPdu.data[0]) ? true:false;
		
		// ����״̬������after������ͳһ����
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
	
	/* 0x12 �����ļ� */
	private boolean process_protocol_download_file(PDU lpPdu)
	{
		switch(lpPdu.cmd_ex)
		{
		case Protocol.PCS_EX_04 :return process_protocol_download_file_cmd_ex_04(lpPdu); /* �ļ��汾 */
		case Protocol.PCS_EX_01 :return process_protocol_download_file_cmd_ex_01(lpPdu); /* �����ļ���Ϣ */
		case Protocol.PCS_EX_02 :return process_protocol_download_file_cmd_ex_02(lpPdu); /* �ļ����� */
		case Protocol.PCS_EX_03 :return process_protocol_download_file_cmd_ex_03(lpPdu); /* �ļ�������� */
		
		case Protocol.PCS_EX_05 :return process_protocol_download_file_cmd_ex_05(lpPdu); /* ����������֪ͨ���ļ���Ҫ������ */
		}
		return false;
	}
	
	// �ļ���Ϣ
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
		case Protocol.PCS_EX_11 :return process_protocol_control_property_cmd_ex_11(lpPdu); /* �������� */
		case Protocol.PCS_EX_13 :return process_protocol_control_property_cmd_ex_13(lpPdu); /* ��ʾ�����ص���ҳ */
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
		case Protocol.PCS_DOWNLOAD_FILE    :return process_protocol_download_file(lpPdu);      /* 0x12 �����ļ� */
		case Protocol.PCS_CTRL_PROPERTY    :return process_protocol_control_property(lpPdu);   /* 0x39 �ؼ����� */			
		case Protocol.PCS_DISCOVER_DEVICE  :return process_protocol_discover_device(lpPdu);    /* �����豸 */
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
						// �������ع��̵���һ������
						// ���������أ����ش���
						after_download_file(false, -1);
						m_task.Clear(MsgTask.TASK_DOWNLOAD);
					}
					else
					{
						// ���һ���������������
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
		
	// ���ͽ����¼�
	public boolean SendEvent(int ctrlId, int eventId, int percent)
	{		
		m_taskQueue.send(new MsgTask(MsgTask.TASK_SEND_EVENT, ctrlId, eventId, percent));
		return true;
	}
	// �����ļ�
	public boolean DownloadZipFile()
	{
		m_taskQueue.send(new MsgTask(MsgTask.TASK_DOWNLOAD));
		return true;
	}
	// ����ļ��汾
	public boolean CheckZipFileVersion()
	{
		m_taskQueue.send(new MsgTask(MsgTask.TASK_CHECK_VERSION));
		return true;	
	}
	// ��¼��������
	public boolean LoginToServer()
	{
		m_taskQueue.send(new MsgTask(MsgTask.TASK_LOGIN_TO));
		return true;
	}
	
	// ������������
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
	// ��ʼ����
	public boolean OpenComm()
	{
		m_actionQueue.send(new MsgAction(MsgAction.ACTION_COMM_OPEN, null, 0));
		return true;
	}
	// ����ͨ�Ų���
	public boolean SetComm(String strIpAddr, int ipPort)
	{
		m_actionQueue.send(new MsgAction(MsgAction.ACTION_SET_COMM, strIpAddr, ipPort));
		return true;
	}
	// ��ʼ��
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
