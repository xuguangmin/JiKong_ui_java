package utils;

/*
 * 工具类
 */
public final class Utils 
{
	/*
	 * 计算有效数据的长度
	 * 计算有效数据长度的字节在当前的协议中，占4个字节
	 */
	public static int calcu_data_length(byte data[], int ix, int size)
	{
		if(data == null || (ix + size) >= data.length)
			return 0;
		
		//System.out.printf("%X,%X,%X,%X\n", data[ix],data[ix+1],data[ix+2],data[ix+3]);
		
		int b1 = ((data[ix]   < 0) ? (256+data[ix])  :data[ix]) & 0xFF;
		int b2 = ((data[ix+1] < 0) ? (256+data[ix+1]):data[ix+1]) & 0xFF;
		int b3 = ((data[ix+2] < 0) ? (256+data[ix+2]):data[ix+2]) & 0xFF;
		int b4 = ((data[ix+3] < 0) ? (256+data[ix+3]):data[ix+3]) & 0xFF;
		
		return ((b1 << 24) | (b2 << 16) | (b3 << 8) | b4);
		//return ((data[ix] << 24) | (data[ix+1] << 16) | (data[ix+2] << 8) | data[ix+3]);
	}
	
	public static int TwoBytesToInt(byte data[], int ix, int size)
	{
		if(data == null || (ix + size) >= data.length)
			return 0;
		
		//System.out.printf("%X,%X,%X,%X\n", data[ix],data[ix+1],data[ix+2],data[ix+3]);
		
		int b1 = ((data[ix]   < 0) ? (256+data[ix])  :data[ix]) & 0xFF;
		int b2 = ((data[ix+1] < 0) ? (256+data[ix+1]):data[ix+1]) & 0xFF;
		
		return ((b1 << 8) | b2);
		//return ((data[ix] << 24) | (data[ix+1] << 16) | (data[ix+2] << 8) | data[ix+3]);
	}
	
	public static boolean IsLinuxOS()
	{
		return System.getProperty("os.name").equalsIgnoreCase("Linux") ? true:false;
	}
}
