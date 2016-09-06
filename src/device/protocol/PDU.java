package device.protocol;

public class PDU 
{
	public byte cmd;
	public byte cmd_ex;
	public byte data[] = new byte[8192];
	public int  data_len;
	public byte checksum1;
	public byte checksum2;
	public boolean enable;         //  «∑Ò”––ß
}
