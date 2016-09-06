package device.protocol;

import utils.ByteArray;
import utils.Utils;

public class ProtocolCore 
{
	public static final byte PROTOCOL_HEADER              = 0x7E;
	public static final int PROTOCOL_SEG_DATA_LEN         = 4;
	public static final int PROTOCOL_SEG_CHECKSUM_LEN     = 2;
	public static final int PROTOCOL_PACKET_SIZE_MIN      = 9;
	public static final int PROTOCOL_DATA_LEN_MAX         = (4096 - 9);
	
	
	

	

	public static final byte PROTOCOL_FILE_TYPE_ZIP          =0x01;
	public static final byte PROTOCOL_FILE_TYPE_SO           =0x02;
	public static final byte PROTOCOL_FILE_TYPE_LUA          =0x03;
	public static final byte PROTOCOL_FILE_TYPE_IRDA         =0x04;
	public static final byte PROTOCOL_FILE_TYPE_ZIP_IOS      =0x05;

	public static final byte CTRL_PROPERTY_ENABLE            =0x01;
	public static final byte CTRL_PROPERTY_VISUAL            =0x0;
	
	
	public static int calcu_data_length(byte data[], int ix, int size)
	{
		return Utils.calcu_data_length(data, ix, size);
	}
	// ����ֽ�����ͷ�ֽڳ��ֵĴ���
	public static int headerstat(byte a[], int data_len)
	{
		int result = 0;
		if(a == null || data_len <= 0)
			return 0;
		
		for(int k= 0; k < data_len; ++k)
		{
			if(a[k] == PROTOCOL_HEADER)
				result ++;
		}
		return result;
	}
	
	public static int bytefind(byte a[], int data_len, byte key)
	{
		if(a == null || data_len <= 0)
			return -1;
		for(int k= 0; k < data_len; ++k)
		{
			if(a[k] == key)
				return k;
		}
		return -1;
	}

	/*
	 * TODO: ���������int��ʾ�����ķ�Χ�������������⻹���о�
	 * ����У���
	 * �ۼ�ÿ���ֽڣ�Ȼ��ֻȡ����ĺ������ֽ�
	 */
	private static int calcu_checksum(byte data[], int size)
	{
		int k;
		int result = 0x0;
		if(data == null || size <= 0)
			return 0;

		for(k = 0; k < size; ++k)
		{			
			result += (data[k] < 0) ? (256+data[k]):data[k];
		}
		return result;
	}
	
	/*
	 * ���ݸ�����Ϣ����һ��Э���
	 *
	 * ������
	 *      cmd          ָ��
	 *      cmd_ex       ��չָ��
	 *      data         ��Ч����
	 *      data_len     ��Ч���ݳ���
	 *      out_buffer   ���淵�����ݵĻ���
	 *      buffer_size  ���淵�����ݵĻ����С
	 *
	 * ����ֵ��0ʧ�ܣ����򷵻�Э������ܳ���
	 * ˵����data���ΪNULL���� data_len <= 0���������ݰ�����Ч���ݵĳ��ȶ�Ϊ0
	 */
	public static int create_protocol_packet(byte cmd, byte cmd_ex, byte data[], int data_len,
			byte out_buffer[], int buffer_size)
	{
	
		int result = 1 + 2 + 4;
		int checksum;
		if(null == out_buffer || buffer_size <= result)
			return 0;

		out_buffer[0] = PROTOCOL_HEADER;
		out_buffer[1] = cmd;
		out_buffer[2] = cmd_ex;

		//valid data length
		if(!(data_len > 0 && data != null))
			data_len = 0;

		out_buffer[3] = (byte)((data_len >> 24) & 0xFF);
		out_buffer[4] = (byte)((data_len >> 16) & 0xFF);
		out_buffer[5] = (byte)((data_len >> 8) & 0xFF);
		out_buffer[6] = (byte)(data_len & 0xFF);

		if(data_len > 0 && data != null)
		{
			System.arraycopy(data, 0, out_buffer, result, data_len);
		}
		result += data_len;
		checksum = calcu_checksum(out_buffer, result);

		out_buffer[result + 0] = (byte)((checksum >> 8) & 0xFF);
		out_buffer[result + 1] = (byte)(checksum & 0xFF);
		result += 2;
		return result;
	}
	
	/*
	 * ��������
	 *
	 *
	 * ������
	 *      in_buffer  �������ݻ��棻  ����ʣ�µ�����
	 *      size       �������ݵĳ��ȣ�����ʣ�����ݵĳ���
	 *
	 * ����ֵ��0ʧ�ܣ�����ɹ�
	 */
	public static boolean parse_packet(ByteArray in_buffer, PDU lpPdu)
	{
		int data_len;
		int valid_data_len;
		int checksum;
		int ix;
		if(in_buffer == null || in_buffer.data_len <= 0)
			return false;
		
		data_len = in_buffer.data_len;

		// find header
		ix = bytefind(in_buffer.data, in_buffer.data_len, PROTOCOL_HEADER);
		if(ix < 0)
		{
			in_buffer.data_len = 0;
			return false;
		}
	
		// discard
		if(ix > 0)
		{
			data_len -= ix;
			System.arraycopy(in_buffer.data, ix, in_buffer.data, 0, data_len);
			in_buffer.data_len = data_len;
		}
		
		// header
		ix = 0;
		if(data_len <= 1)
			return false;
		ix += 1;
		data_len -= 1;

		// cmd
		if(data_len <= 2)
			return false;
		lpPdu.cmd = in_buffer.data[ix];
		ix += 1;
		lpPdu.cmd_ex = in_buffer.data[ix];
		ix += 1;
		data_len -= 2;

		// valid data length
		if(data_len <= PROTOCOL_SEG_DATA_LEN)
			return false;
		valid_data_len = calcu_data_length(in_buffer.data, ix, PROTOCOL_SEG_DATA_LEN);
		if(valid_data_len > PROTOCOL_DATA_LEN_MAX)
		{
			System.arraycopy(in_buffer.data, 1, in_buffer.data, 0, in_buffer.data_len-1);
			in_buffer.data_len -= 1;
			return false;
		}
		lpPdu.data_len = valid_data_len;

		ix += PROTOCOL_SEG_DATA_LEN;
		data_len -= PROTOCOL_SEG_DATA_LEN;
		
		// valid data
		if(data_len <= valid_data_len)
			return false;
		//System.out.printf("valid_data_len %d, 0x%X\n", valid_data_len, valid_data_len);
		System.arraycopy(in_buffer.data, ix, lpPdu.data, 0, valid_data_len);
		ix += valid_data_len;
		data_len -= valid_data_len;

		// checksum
		if(data_len < PROTOCOL_SEG_CHECKSUM_LEN)
			return false;

		
		checksum = calcu_checksum(in_buffer.data, 1+2+PROTOCOL_SEG_DATA_LEN + valid_data_len);
		lpPdu.checksum1 = (byte)((checksum >> 8) & 0xFF);
		lpPdu.checksum2 = (byte)(checksum & 0xFF);

		if(lpPdu.checksum1 != in_buffer.data[ix] || lpPdu.checksum2 != in_buffer.data[ix+1])
		{
			System.arraycopy(in_buffer.data, 1, in_buffer.data, 0, in_buffer.data_len-1);
			in_buffer.data_len -= 1;
			return false;
		}
		ix += PROTOCOL_SEG_CHECKSUM_LEN;
		data_len -= PROTOCOL_SEG_CHECKSUM_LEN;

		// save left data
		System.arraycopy(in_buffer.data, ix, in_buffer.data, 0, data_len);
		in_buffer.data_len = data_len;
		return true;
	}
	
	/*
	 * TODO: ���������int��ʾ�����ķ�Χ�������������⻹���о�
	 * ����У���
	 * �ۼ�ÿ���ֽڣ�Ȼ��ֻȡ����ĺ������ֽ�
	 */
	private static int calcu_checksum_2(byte data[], int offset, int count)
	{
		int k;
		int result = 0x0;
		if(data == null || offset < 0 || count <= 0)
			return 0;
		if((offset + count) >= data.length)
			return 0;

		for(k = offset; k < (offset+count); ++k)
		{			
			result += (data[k] < 0) ? (256+data[k]):data[k];
		}
		return result;
	}
	
	/*
	 * ��������
	 *
	 * ������
	 *      buffer     �������ݻ���
	 *      data_len   �����е����ݳ���
	 *      lpPdu      ����Э�����ݰ������е�enable��ָ�����ݰ��Ƿ�����Ч
	 *
	 * ����ֵ��
	 *      �Ѿ�ʹ�õ����ݵĳ��ȡ�������ȡ�ߵ���Ч���ݣ�Ҳ�����Ǳ��ӵ�����Ч����
	 */
	public static int ParseByteStream(byte[] buffer, int data_len, PDU lpPdu)
	{
		int ix;
		int discard = 0;                          // ͷ������Ч���ݳ���
						
		lpPdu.enable = false;
		if(buffer == null || data_len <= 0)
			return 0;
		
		// find header
		ix = bytefind(buffer, data_len, PROTOCOL_HEADER);
		if(ix < 0)
		{
			return data_len;                        // ����Ч���ݣ�ȫ���ӵ�
		}
		discard  = ix;
		data_len -= discard;                        // ʣ����Ч���ݳ���
		
		// header
		if(data_len <= 1)
			return discard;
		ix += 1;
		data_len -= 1;

		// cmd
		if(data_len <= 2)
			return discard;
		lpPdu.cmd = buffer[ix];
		ix += 1;
		lpPdu.cmd_ex = buffer[ix];
		ix += 1;
		data_len -= 2;

		// valid data length
		if(data_len <= PROTOCOL_SEG_DATA_LEN)
			return discard;
		
		int valid_data_len = calcu_data_length(buffer, ix, PROTOCOL_SEG_DATA_LEN);
		if(valid_data_len > PROTOCOL_DATA_LEN_MAX)
		{			
			// ������������ݳ���̫�󣬰�ͷ�ӵ�������һ��ͷ
			discard++;                          
			return discard;
		}
		
		lpPdu.data_len = valid_data_len;

		ix += PROTOCOL_SEG_DATA_LEN;
		data_len -= PROTOCOL_SEG_DATA_LEN;
		
		// valid data
		if(data_len <= valid_data_len)
			return discard;
		
		System.arraycopy(buffer, ix, lpPdu.data, 0, valid_data_len);
		ix += valid_data_len;
		data_len -= valid_data_len;

		// checksum
		if(data_len < PROTOCOL_SEG_CHECKSUM_LEN)
			return discard;

		int checksum = calcu_checksum_2(buffer, discard, 1+2+PROTOCOL_SEG_DATA_LEN + valid_data_len);
		lpPdu.checksum1 = (byte)((checksum >> 8) & 0xFF);
		lpPdu.checksum2 = (byte)(checksum & 0xFF);
	
		if(lpPdu.checksum1 != buffer[ix] || lpPdu.checksum2 != buffer[ix+1])
		{			
			// У���벻�ԣ���ͷ�ӵ�������һ��ͷ
			discard++;                          
			return discard;
		}
		ix += PROTOCOL_SEG_CHECKSUM_LEN;
		lpPdu.enable = true;
		return ix;
	}
	
	
}
