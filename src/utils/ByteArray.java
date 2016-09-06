package utils;

public class ByteArray 
{
	public byte data[];
	public int  data_len;
	public int  array_len;
	
	public ByteArray()
	{
		this(4096);
	}
	public ByteArray(int len)
	{
		data_len = 0;
		array_len = len;
		data = new byte[array_len];
	}
}
