/* ***************************************************************************

                  		��Ȩ���� (C), 2013-2100

 *****************************************************************************
	  �ļ����� : JikongClient.java
	  ����           : ���Ӹ�
	  �������� : 2013��
	
	  �汾           : 1.0
	  �������� : �������
	
	  �޸���ʷ :

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
		
		boolean m_register = AppRegister.Check();      // �����Ƿ��Ѿ�ע��
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

