package ccc_ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SkinManager 
{
	static final int BUFFER_SIZE = 4096;
	private ZipFile m_zipfile = null;
	private String m_dir = null;
	private String m_fileTitle = null;
	private byte[] m_tempBuffer = new byte[BUFFER_SIZE];
	private ByteArrayOutputStream m_byteArry = new ByteArrayOutputStream();
	
	private String GetUiFileDir(String fileName)
	{		
		fileName.trim();
		if(fileName.length() <= 0) 
			return null;
		
		String prefix = File.separator;
		int ix = fileName.lastIndexOf(prefix);
		if(ix < 0)
			return null;
				  		
		return fileName.substring(0, ix+1);
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
		fileName = fileName.substring(0, ix);
		
		ix = fileName.lastIndexOf(File.separator);
  		if(ix <= 0)
  			return null;
  		
		return fileName.substring(ix+1, fileName.length());	
	}
	
	private InputStream GetEntryInputStream(String filename)
	{
		if(null == m_zipfile)
			return null;
		
		try
		{
			ZipEntry entry;
			Enumeration<? extends ZipEntry> entrys = m_zipfile.entries();
			while (entrys.hasMoreElements()) 
			{
				entry = (ZipEntry)entrys.nextElement();
				if (!entry.isDirectory() &&
				    entry.getName().compareToIgnoreCase(filename) == 0) 
				{					
					return m_zipfile.getInputStream(entry);
				}				
			}			
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public boolean UnzipXmlFile()
	{		
		try 
		{
			String xmlFilename = String.format("%s.xml", m_fileTitle);
			InputStream is = GetEntryInputStream(xmlFilename);
			if(is == null)
				return false;
			
			FileOutputStream os = new FileOutputStream(String.format("%s%s", m_dir, xmlFilename));
			int len = is.read(m_tempBuffer, 0, BUFFER_SIZE);
			while (len > 0) 
			{
				os.write(m_tempBuffer, 0, len);
				len = is.read(m_tempBuffer, 0, BUFFER_SIZE);
			}
			os.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public InputStream GetXmlInputStream()
	{		
		return GetEntryInputStream(String.format("%s.xml", m_fileTitle));
	}
	
	public byte [] GetImageByteArray(String filename)
	{		
		try
		{
			InputStream is = GetEntryInputStream(String.format("resource/%s", filename));
			if(is == null)
				return null;
			
			m_byteArry.reset();
			int len = is.read(m_tempBuffer, 0, BUFFER_SIZE);
			while (len > 0) 
			{
				m_byteArry.write(m_tempBuffer, 0, len);
				len = is.read(m_tempBuffer, 0, BUFFER_SIZE);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return m_byteArry.toByteArray();
	}
	public boolean OpenZipFile(String filename)
	{
		try
		{
			m_zipfile = new ZipFile(filename);
			
			m_dir = GetUiFileDir(filename);
			m_fileTitle = GetUiFileTitle(filename);		
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
