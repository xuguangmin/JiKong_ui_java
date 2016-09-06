package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @category 文件操作工具类
 * @author MING HAN
 */
public final class FileUtil 
{
	private static final String XML_IMAGE_DIRECTORY = "resource";
	private static final String SKIN_DIRECTORY      = "skin";
	private static final String TEMP_DIRECTORY      = "temp";
	private static final String RES_DIRECTORY       = "res";
	
	public static String GetAppPath()
	{
		return System.getProperty("user.dir") + File.separator;
	}
	public static String GetResPath()
	{
		return GetAppPath() + RES_DIRECTORY + File.separator;
	}
	public static String GetLogoPath()
	{
		return FileUtil.GetResPath() + "logo.png";
	}
	public static String GetBkImagePath()
	{
		return FileUtil.GetResPath() + "start.jpg";
	}
	public static String GetConfigFilePath()
	{
		return GetResPath() + "config.xml";
	}
	public static String GetSkinPath()
	{
		return FileUtil.GetAppPath() + SKIN_DIRECTORY + File.separator;
	}
	public static String GetSkinResourcePath()
	{
		return String.format("%s%s%s%s", GetSkinPath(), File.separator, XML_IMAGE_DIRECTORY, File.separator);
	}
	
	public static String GetTempPath()
	{
		return FileUtil.GetAppPath() + TEMP_DIRECTORY + File.separator;
	}
	public static String GetTempResourcePath()
	{
		return String.format("%s%s%s%s", GetTempPath(), File.separator, XML_IMAGE_DIRECTORY, File.separator);
	}
	
	// 从一个路径中 获取文件title
	public static String GetFileTitle(String filePath, String prefix)
	{	
		filePath.trim();
		prefix.trim();
		if(filePath.length() <= 0 || prefix.length() <= 0) 
			return "";
		
		int ix = filePath.lastIndexOf(File.separator);
		if(ix <= 0) return "";
		filePath = filePath.substring(ix+1);
		
		// trim .zip
		ix = filePath.lastIndexOf(prefix);
		if(ix <= 0)
			return "";
		
		return filePath.substring(0, ix);
	}
		
	public static String GetFileName(String filePath)
	{		
		filePath.trim();
		if(filePath.length() <= 0) 
			return "";
		
		String prefix = File.separator;
		int ix = filePath.lastIndexOf(prefix);
		if(ix < 0)
			return filePath;
				  		
		return filePath.substring(ix+1, filePath.length());
	}
	
	public static void DeleteFolder(String strFolder) //throws IOException
	{
		File f = new File(strFolder);       
		if(f.exists() && f.isDirectory())   //判断是文件还是目录
		{
			if(0 == f.listFiles().length)     //若目录下没有文件则直接删除
			{
				f.delete();
			}
		    else                            //若有则把文件放进数组，并判断是否有下级目录
		    {
		    	File delFile[] = f.listFiles();
		        int i = f.listFiles().length;
		        for(int j = 0; j < i; j++)
		        {
		           if(delFile[j].isDirectory())
		           {
		              DeleteFolder(delFile[j].getAbsolutePath());//递归调用并取得子目录路径
		           }
		           delFile[j].delete();//删除文件
		        }
		    }
		}
	}
	
	/*
	 * 复制文件，如果目标文件夹不存在，或者源文件不存在
	 * 都将返回false 
	 * dstFolder 需要有后缀 \ or /
	 */
	public static boolean CopyFile(String srcFile, String dstFolder) 
	{
		if (!FileUtil.Exists(srcFile))
			return false;
		if (!FileUtil.Exists(dstFolder))
			return false;

		String filename = GetFileName(srcFile);
		if (filename.isEmpty())
			return false;

		try 
		{
			FileInputStream fis  = new FileInputStream(srcFile); 
			FileOutputStream fos = new FileOutputStream(String.format("%s%s",dstFolder, filename));
			
			int byteread = 0;
			byte[] buffer = new byte[4096];
			while ((byteread = fis.read(buffer, 0, 4096)) != -1) 
			{
				fos.write(buffer, 0, byteread);
			}
			fos.close();
			fis.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * @category 获得文件拓展名
	 * @param strFileName 文件全路径
	 * @return 文件拓展名
	 */
	public static String GetFileExtention(String strFileName)
	{
		String strRetExtName="";
		if(strFileName!=null&&strFileName.trim().length()>0)
		{
			int index=strFileName.lastIndexOf(".");
			if(index>0)
			{
				strRetExtName=strFileName.substring(index, strFileName.length());
			}
		}
		return strRetExtName.toLowerCase();
	}
    /**
     * 判断文件是否存在
     * @param fileName
     * @return 若存在返回true,否则返回false;
     */
	public static boolean Exists(String fileName)
	{
		boolean result = false;
		try
		{
			File file = new File(fileName);
			result = file.exists();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return result;
	}
	/**
	 * @category 删除文件
	 * @param fileName 要删除的文件全路径
	 * @return 是否删除成功,若成功返回true,否则返回false;
	 */
	public static boolean DeleteFile(String fileName)
	{
		boolean bRetValue=false;
		File file=new File(fileName);
		if(file.exists()&&file.isFile())
		{
			try
			{
				bRetValue=file.delete();
			}
			catch(SecurityException se)
			{
				
			}
		}
		return bRetValue;
	}
	/**
	 * @category 创建文件
	 * @param fileAbsolutePath 文件的绝对路径
	 * @return 是否创建成功，若成功返回true,否则返回false;
	 */
	public static File CreateFile(String fileAbsolutePath)
	{
		File fileRetValue=null;
		File file=new File(fileAbsolutePath);
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch(SecurityException se)
			{
				fileRetValue=null;
			}
			catch (IOException e)
			{
				fileRetValue=null;
			}
		}
		fileRetValue=file;
		return fileRetValue;
	}	
	/**
	 * @category 创建文件夹
	 * @param strDirectoryName 文件夹路径名称
	 * @return 如果创建成功则返回文件夹对象，否则返回null
	 */
	public static boolean CreateDirectory(String strDirectoryName)
	{
		File directory = new File(strDirectoryName);
		if(directory.exists())
			return true;
		
		try
		{
			if(directory.mkdirs())
			{
				return true;
			}
		}
		catch(SecurityException se)
		{
			return false;
		}
		
		return false;
	}
	/**
	 * 删除文件夹
	 * @param strDirectoryName 文件夹路径
	 * @param deleteSubFile 是否要删除文件夹包含的文件及子文件夹,true删除子文件夹及文件
	 * @return 是否删除成功,若成功返回true,否则返回false;
	 */
	public static boolean DeleteDrirectory(String strDirectoryName,boolean deleteSubFile)
	{
		boolean bRetValue=false;
		File directory=new File(strDirectoryName);
		if(directory.exists())
		{//1
			try
			{//try
				File[] files=directory.listFiles();
				if(files!=null&&deleteSubFile)
				{//2
					for(File file:files)
					{//for
						try
						{
							if(file.isDirectory())
							{
								DeleteDrirectory(file.getAbsolutePath(),true);
							}
							else
							{
								file.delete();
							}	
							bRetValue=true;
						}
						catch(SecurityException se){bRetValue=false;}
					}//end for
				}//2
				bRetValue=directory.delete()&&bRetValue;
			}
			catch(SecurityException se)
			{}//end try
		}//1
		return bRetValue;
	}
	
	public static String GetSize(final int sizeInByte)
	{
		Double dSize=sizeInByte+0.0;
		String strRetValue=String.format("%dB", sizeInByte);
		if(dSize>1024){
			dSize=dSize/1024;
			strRetValue=String.format("%2fK", dSize);
			if(dSize>1024){
				dSize=dSize/1024;
				strRetValue=String.format("%2fM", dSize);
				if(dSize>1024){
					dSize=dSize/1024;
					strRetValue=String.format("%2fG", dSize);
				}
			}
		}
		return strRetValue;
	}
}
