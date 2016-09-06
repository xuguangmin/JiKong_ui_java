package ccc_ui.ui;

/* ***************************************************************************

			��Ȩ���� (C), 2001-2020, ���������ſƼ��ɷ����޹�˾

******************************************************************************
	�ļ����� : SkinManager.java
	����           : ���Ӹ�
	�������� : 
	
	�汾           : 1.0
	�������� : ���桢����λͼ�������ظ�ʹ���Ѵ򿪵�λͼ
	
	�޸���ʷ :

******************************************************************************/

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class SkinManager
{
	private static List<BmpItem>   m_bmpItemList = new ArrayList<BmpItem>();

	private static boolean AddBmpItem(ImageIcon lpBmp, String filename)
	{
		for(int k = 0; k < m_bmpItemList.size(); ++k)
		{
			BmpItem bi = m_bmpItemList.get(k);
			if(0 == filename.compareToIgnoreCase(bi.filename))
			{
				bi.ref++;
				return true;
			}
		}
		
		BmpItem biNew = new BmpItem();
		biNew.lpBmp    = lpBmp;
		biNew.filename = filename;
		biNew.ref      = 1;
		return m_bmpItemList.add(biNew);
	}

	private static ImageIcon GetBitmapFromList(String filename)
	{
		//System.out.printf("------ GetBitmapFromList :%s request\n", filename);
		for(int k = 0; k < m_bmpItemList.size(); ++k)
		{
			BmpItem bi = m_bmpItemList.get(k);
			if(0 == filename.compareToIgnoreCase(bi.filename))
			{
				//System.out.printf("------ GetBitmapFromList :%s get\n", filename);
				bi.ref++;
				return bi.lpBmp;
			}
		}
		return null;	
	}
	private static void ReleaseBitmap(ImageIcon bmp)
	{
		if(bmp != null) 
		{
			bmp.getImage().flush();
		}
	}	
	public static InputStream GetXmlInputStream()
	{		
		return UiZipManager.GetXmlInputStream();
	}
	
	private static ImageIcon InternalGetBitmap(String imageFilename)
	{
		if(null == imageFilename)
			return null;
		ImageIcon result = GetBitmapFromList(imageFilename);
		if(result != null)
			return result;
		
		result = UiZipManager.GetBitmap(imageFilename);
		if(null == result)
			return null;
		
		AddBmpItem(result, imageFilename);
		return result;
	}
	
	public static void ClearAll()
	{
		for(int k = 0; k < m_bmpItemList.size(); ++k)
		{
			BmpItem bi = m_bmpItemList.get(k);
			ReleaseBitmap(bi.lpBmp);
		}
		m_bmpItemList.clear();
	}
	// TODO: �д�ɾ�����ٱ�ʹ�õ�λͼ
	public static void ClearIdle()
	{
		for(int k = 0; k < m_bmpItemList.size(); ++k)
		{
			BmpItem bi = m_bmpItemList.get(k);
			if(bi.ref <= 0) ReleaseBitmap(bi.lpBmp);
		}
	}
	
	/*
	 * int width  ϣ����ͼƬ�Ŀ��
	 * int height ϣ����ͼƬ�ĸ߶�
	 */
	public static ImageIcon GetBitmap(String filename, int width, int height)
	{	
		return InternalGetBitmap(filename);
		//return null;
	}
	public static ImageIcon GetBitmap(String filename)
	{	
		return InternalGetBitmap(filename);
		//return null;
	}
	
	public static boolean OpenZipFile(String filename)
	{
		return UiZipManager.OpenZipFile(filename);
	}
}



