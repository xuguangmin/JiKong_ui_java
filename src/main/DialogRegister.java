package main;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import utils.AppRegister;
import utils.FileUtil;
import utils.lang.Language;

public class DialogRegister extends JDialog
{
	private static final long serialVersionUID = -3025022352522090304L;
	private PanelRegister m_panelRegister = null;
	
	public DialogRegister()
	{
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setIconImage(getToolkit().getImage(FileUtil.GetLogoPath()));
		
		m_panelRegister = new PanelRegister();
		m_panelRegister.SetBackgroundImage(FileUtil.GetBkImagePath());
		
		String strNofity = Language.Get(Language.LID_STRING_11);
		String strKey    = AppRegister.GetKey();
		m_panelRegister.GetSerialNo(strNofity, strKey, new PanelRegister.IPanelRegister(){
			@Override
			public void SerialNoString(String snString) {OnSerialNoString(snString);}
		});
		
		this.add(m_panelRegister);
		return;
	}
	
	public void OnSerialNoString(String snString) 
	{
		System.out.println(snString);
		if(AppRegister.Register(snString))
		{
			JOptionPane.showMessageDialog(this, Language.Get(Language.LID_STRING_12));			
		}
		else
		{
			JOptionPane.showMessageDialog(this, Language.Get(Language.LID_STRING_13));
		}
	}
}