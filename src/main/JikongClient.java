/* ***************************************************************************

                  		版权所有 (C), 2013-2100

 *****************************************************************************
	  文件名称 : JikongClient.java
	  作者           : 贾延刚
	  生成日期 : 2013年
	
	  版本           : 1.0
	  功能描述 : 程序入口
	
	  修改历史 :

******************************************************************************/
package main;

import javax.swing.UIManager;
import utils.AppRegister;

public class JikongClient 
{
	public JikongClient()
	{	
	}
	
	public static void main(String[] args) 
	{
		try 
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e) 
		{
		}
		
		boolean m_register = AppRegister.Check();      // 程序是否已经注册
		if(!m_register)
		{
			DialogRegister dlgRegister = new DialogRegister();
			dlgRegister.setSize(800, 500);
			dlgRegister.setVisible(true);
		}
		else
		{
			javax.swing.SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						JMainFrame mainFram = new JMainFrame();
						mainFram.setSize(800, 500);
						mainFram.setVisible(true);
					}
				}
			);	
		}
	}	
}

