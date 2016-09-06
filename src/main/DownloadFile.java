/******************************************************************************

                  版权所有 (C), 2013-2100

 ******************************************************************************
  文件名称 : DownloadFile.java
  作者           : 贾延刚
  生成日期 : 2013-07-02

  版本           : 1.0
  功能描述 : 
                           保存下载文件信息，处理下载文件                         

  修改历史 :

******************************************************************************/

package main;

import utils.FileUtil;

public class DownloadFile 
{
	private String m_filename;
	private String m_uiFileVersion;
	public void SaveFilename(String filename)
	{
		m_filename = filename;
	}
	public boolean SaveFileVersion(String fileVersion)
	{
		m_uiFileVersion = fileVersion;
		return true; 
	}
	
	public String GetFileTitle()
	{
		return GetUiFileTitle(m_filename);
	}
	public String GetFileVersion()
	{
		return m_uiFileVersion;
	}
	
	public boolean ProcessDownload(String filePath, String skinDir)
	{
		if(!MoveUiFile(filePath, skinDir))
			return false;
		
		String fileTitle = GetUiFileTitle(m_filename);
		if(fileTitle == null)
			return false;
		
		String zipFile = skinDir + String.format("%s.zip", fileTitle);
		if(!FileUtil.Exists(zipFile))
			return false;
		
		return true;
	}
	
	private boolean MoveUiFile(String filePath, String skinDir)
	{
		if(!FileUtil.Exists(filePath))
			return false;
		
		FileUtil.DeleteFolder(skinDir);
		if(!FileUtil.CreateDirectory(skinDir))
			return false;
			
		if(!FileUtil.CopyFile(filePath, skinDir))
			return false;
		
		return true;
	}
	
	private String GetUiFileTitle(String fileName)
	{		
		fileName.trim();
		if(fileName.length() <= 0) 
			return null;
		
		String prefix = ".zip";
		int ix = fileName.lastIndexOf(prefix);
		if(ix <= 0)
			return null;
		if((prefix.length() + ix) != fileName.length())
			return null;
				
		return fileName.substring(0, ix);	
	}
}
