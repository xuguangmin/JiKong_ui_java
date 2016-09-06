package device.protocol;

import utils.ByteArray;

public class Protocol
{
	public static final byte DATA_TYPE_TCP                  =0;
	public static final byte DATA_TYPE_UDP                  =1;
	
	/* protocol command set */
	public static final byte PCS_CLIENT_LOGIN                =0x10;           /* 向服务器发送登录信息 */
	public static final byte PCS_DOWNLOAD_FILE               =0x12;           /* 从服务器下载文件 */
	public static final byte PCS_CLIENT_EVENT                =0x19;           /* 向服务器发送界面事件 */
	public static final byte PCS_CTRL_PROPERTY               =0x39;           /* 控件属性，来自服务器 */
	public static final byte PCS_DISCOVER_DEVICE             =(byte)0xE0;     /* 查找服务器主机 */
	
	/* protocol command set extend */
	public static final byte PCS_EX_OK 		                 =(byte)0xFE;
	public static final byte PCS_EX_ERR		                 =(byte)0xFF;
	public static final byte PCS_EX_CLIENT                   = 0x02;
	public static final byte PCS_EX_01                       = 0x01;
	public static final byte PCS_EX_02                       = 0x02;
	public static final byte PCS_EX_03                       = 0x03;
	public static final byte PCS_EX_04                       = 0x04;
	public static final byte PCS_EX_05                       = 0x05;
	public static final byte PCS_EX_11                       = 0x11;
	public static final byte PCS_EX_12                       = 0x12;
	public static final byte PCS_EX_13                       = 0x13;
	
	private PDU              m_lpPdu      = null;
	private ByteArray        m_byteSend = null;
	private IProtocol        m_iproto = null;
	public Protocol(IProtocol iproto)
	{
		m_lpPdu = new PDU();
		m_byteSend = new ByteArray(8192);
		m_iproto = iproto;
	}
	
	boolean OutputProtocolPacket(byte buffer[], int len)
	{
		if(null == m_iproto)
			return false;
		
		/*
		for(int k = 0; k < len; ++k)
		{
			System.out.printf("0x%X ", buffer[k]);	
		}*/
		// System.out.println("\n");
		return m_iproto.SendPacket(buffer, len, DATA_TYPE_TCP);
	}
	boolean OutputProtocolPacketUdp(byte buffer[], int len)
	{
		if(null == m_iproto)
			return false;
		
		return m_iproto.SendPacket(buffer, len, DATA_TYPE_UDP);
	}
	
	// 0x12 - 0x04
	public boolean GetFileVersion()
	{		
		byte data[] = {0x01};
		int len = ProtocolCore.create_protocol_packet(PCS_DOWNLOAD_FILE, PCS_EX_04, data, 1, m_byteSend.data, m_byteSend.array_len);
		if(len <= 0)
			return false;
		
		return OutputProtocolPacket(m_byteSend.data, len);
	}
	// 0x12 - 0x01
	public boolean GetFileInfo()
	{		
		byte data[] = {0x01};
		int len = ProtocolCore.create_protocol_packet(PCS_DOWNLOAD_FILE, PCS_EX_01, data, 1, m_byteSend.data, m_byteSend.array_len);
		if(len <= 0)
			return false;
	
		return OutputProtocolPacket(m_byteSend.data, len);
	}
	
	// 0x12 - 0x02
	public boolean GetFileDataStart()
	{		
		byte data[] = {0x01};
		int len = ProtocolCore.create_protocol_packet(PCS_DOWNLOAD_FILE, PCS_EX_02, data, 1, m_byteSend.data, m_byteSend.array_len);
		if(len <= 0)
			return false;
	
		return OutputProtocolPacket(m_byteSend.data, len);
	}
	// 0x12 - 0x02 0xFE
	public boolean GetFileDataAck()
	{		
		byte data[] = {(byte)0xFE, 0x01};
		int len = ProtocolCore.create_protocol_packet(PCS_DOWNLOAD_FILE, PCS_EX_02, data, 2, m_byteSend.data, m_byteSend.array_len);
		if(len <= 0)
			return false;
	
		return OutputProtocolPacket(m_byteSend.data, len);
	}
	
	// 0x10 - 0x02
	public boolean LoginToServer()
	{	
		int len = ProtocolCore.create_protocol_packet(PCS_CLIENT_LOGIN, PCS_EX_CLIENT, null, 0, m_byteSend.data, m_byteSend.array_len);
		if(len <= 0)
			return false;
		
		return OutputProtocolPacket(m_byteSend.data, len);
	}
	
	// 0xE0 - 0x01
	public boolean SearchJikong()
	{	
		int len = ProtocolCore.create_protocol_packet(PCS_DISCOVER_DEVICE, PCS_EX_01, null, 0, m_byteSend.data, m_byteSend.array_len);
		if(len <= 0)
			return false;
		
		return OutputProtocolPacketUdp(m_byteSend.data, len);
	}
	
	// TODO: 0x19 - 0x03
	public boolean SendEvent(int ctrlId, int eventId, int percent)
	{	
		byte data[] = new byte[12];
		
		data[0] = (byte)((eventId >> 24) & 0xFF);
		data[1] = (byte)((eventId >> 16) & 0xFF);
		data[2] = (byte)((eventId >> 8) & 0xFF);
		data[3] = (byte)(eventId & 0xFF);
		
		data[4] = (byte)((ctrlId >> 24) & 0xFF);
		data[5] = (byte)((ctrlId >> 16) & 0xFF);
		data[6] = (byte)((ctrlId >> 8) & 0xFF);
		data[7] = (byte)(ctrlId & 0xFF);
		
		data[8]  = (byte)((percent >> 24) & 0xFF);
		data[9]  = (byte)((percent >> 16) & 0xFF);
		data[10] = (byte)((percent >> 8) & 0xFF);
		data[11] = (byte)(percent & 0xFF);
		
		
		int len = ProtocolCore.create_protocol_packet(PCS_CLIENT_EVENT, PCS_EX_03, data, 12, m_byteSend.data, m_byteSend.array_len);
		if(len <= 0)
			return false;
		
		return OutputProtocolPacket(m_byteSend.data, len);
	}
	
	public boolean SendTestProtocolPacket()
	{
		byte data[] = {0x1, 0x0, 0x8, 0x47, (byte)0xA7, 0x6A, 0x69, 0x65, 0x78, 0x69, 0x61, 0x6E, 0x67, 0x2E, 0x7A, 0x69, 0x70};
		int len = ProtocolCore.create_protocol_packet(Protocol.PCS_DOWNLOAD_FILE, Protocol.PCS_EX_01, data, 0x11, m_byteSend.data, m_byteSend.array_len);
		
		//byte data[] = {0x01};
		//int len = ProtocolCore.create_protocol_packet(ProtocolCore.PCS_DOWNLOAD_FILE, ProtocolCore.PCS_EX_04, data, 1, m_byteSend.data, m_byteSend.array_len);
		if(len <= 0)
			return false;
	
		System.out.println("SendTestProtocolPacket");
		for(int k = 0; k < len; ++k)
		{
			System.out.printf("0x%X ", m_byteSend.data[k]);
			
		}
		System.out.println("\n");
		System.out.printf("GetFileInfo\n");	
		byte yt = (byte)0xA7;
		System.out.printf("0xA7 = %d : %d, %s\n", 0xA7, yt, Byte.toString(yt));
		return OutputProtocolPacket(m_byteSend.data, len);
	}
	
	/*
	 * 解析数据
	 *
	 * 参数：
	 *      buffer     输入数据缓存
	 *      data_len   缓存中的数据长度
	 *
	 * 返回值：
	 *      已经使用的数据的长度
	 *      
	 * 说明：
	 *      将通过接口返回有效的协议包
	 */
	public int Parse(byte[] buffer, int data_len)
	{
		int len, consume = 0;
		while(data_len > 0)
		{
			len = ProtocolCore.ParseByteStream(buffer, data_len, m_lpPdu);
			if(len <= 0)
			{
				break;
			}
			
			if(m_lpPdu.enable)
			{
				if(m_iproto != null) m_iproto.ProtocolPdu(m_lpPdu);
			}
			else
			{
				System.out.printf("package:\n  invalid data len = %d\n\n", len);	
			}

			data_len -= len;
			consume += len;
			// 复制剩余数据
			System.arraycopy(buffer, len, buffer, 0, data_len);
		}	
		return consume;
	}
}
