/* ***************************************************************************

                  		版权所有 (C), 2013-2100

 *****************************************************************************
	  文件名称 : DialogLogin.java
	  作者           : 贾延刚
	  生成日期 : 2013年
	
	  版本           : 1.0
	  功能描述 : IP设置对话框
	
	  修改历史 :

******************************************************************************/

package main;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import utils.lang.Language;

public class DialogLogin extends JDialog implements ActionListener
{
	public interface IDialogLogin 
	{
		void LoginParameter(String ipAddr, int ipPort, String hostname);
		void SearchDevice();
	}
	
	public class CmbItem 
	{
		String     ipAddr;
		String     hostname;
		
		public CmbItem(String ipAddr, String hostname)
		{
			this.ipAddr   = ipAddr;
			this.hostname = hostname;
		}
	}
	
	private static final long  serialVersionUID = 3688961012399878252L;
	private JLabel             m_lblIpAddr     = new JLabel();
	private JLabel             m_lblIpPort     = new JLabel();
	private JComboBox<String>  m_cmbIpAddr     = new JComboBox<String>();
	private JTextField         m_textIpPort    = new JTextField();
	private JButton            m_btnSearch     = new JButton();
	private JButton            m_btnSave       = new JButton();
	private JButton            m_btnClose      = new JButton();
	private IDialogLogin       m_interface     = null;		
	private Vector<CmbItem>     m_devList      = new Vector<CmbItem>();
    
	public DialogLogin(Frame owner) 
	{
		super(owner, true);	
		this.setLayout(null);
		addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e) {OnCmponentResized(e);}
		});
		this.setSize(460, 220);
		if(owner != null)
		{
			int width  = owner.getWidth();
			int height = owner.getHeight();
			int x = owner.getX() + (width - this.getWidth())/2;
			int y = owner.getY() + (height - this.getHeight())/2;
			this.setLocation(x, y);
		}
		
		m_cmbIpAddr.setEditable(true);
		m_cmbIpAddr.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent event)
			{
				if(event.getStateChange() == ItemEvent.SELECTED)
				{
					int ix = ((JComboBox<?>)event.getSource()).getSelectedIndex();	
					SetDialogTitle(ix);
				}
			}	
		});
		
		m_lblIpAddr.setText(Language.Get(Language.LID_STRING_1));
		m_lblIpPort.setText(Language.Get(Language.LID_STRING_2));
		m_btnSearch.setText(Language.Get(Language.LID_STRING_16));
		
		m_btnSave.setText(Language.Get(Language.LID_STRING_5));
		m_btnClose.setText(Language.Get(Language.LID_STRING_CANCEL));
		m_btnSave.addActionListener(this);
		m_btnClose.addActionListener(this);
		m_btnSearch.addActionListener(this);
		this.add(m_lblIpAddr);
		this.add(m_lblIpPort);
		this.add(m_cmbIpAddr);
		this.add(m_textIpPort);
		this.add(m_btnSearch);
		this.add(m_btnSave);
		this.add(m_btnClose);
	}
	
	private void OnCmponentResized(ComponentEvent e)
    {		
		MoveLoginCtrl(0, 0, this.getWidth(), this.getHeight());
    }
	private void MoveLoginCtrl(int x, int y, int width, int height) 
	{
		// 第 1 行
		x += 50;
		y += 60;
		m_lblIpAddr.setSize(100, 23);
		m_lblIpAddr.setLocation(x, y);	
		m_cmbIpAddr.setSize(200, 23);
		m_cmbIpAddr.setLocation(x+100, y);
		m_btnSearch.setSize(60, 23);
		m_btnSearch.setLocation(x + 100+200+5, y);
		
		// 第 2 行
		y += 30;
		m_lblIpPort.setSize(100, 23);
		m_lblIpPort.setLocation(x, y);	
		m_textIpPort.setSize(200, 23);
		m_textIpPort.setLocation(x+100, y);
		
		// 第 3 行
		y += 60;
		x += 150;
		m_btnSave.setSize(75, 23);
		m_btnSave.setLocation(x, y);
		m_btnClose.setSize(75, 23);
		m_btnClose.setLocation(x + 90, y);	
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(m_btnSearch == e.getSource())
		{
			if(m_interface != null) m_interface.SearchDevice();
		}
		else if(m_btnClose == e.getSource())
		{
			this.dispose();
		}
		else if(m_btnSave == e.getSource())
		{
			if(m_interface != null) 
			{
				String ipAddr = (String)m_cmbIpAddr.getEditor().getItem();
				String hostname = "Unknown";
				int ix = m_cmbIpAddr.getSelectedIndex();
				if(ix >= 0)
				{
					hostname = GetHostname(ix);	
				}
				m_interface.LoginParameter(ipAddr, Integer.parseInt(m_textIpPort.getText()), hostname);
			}
			this.dispose();
		}	
	}
	
	private String GetHostname(int ix)
	{	
		if(ix < 0 || ix >= m_devList.size())
			return "";
		
		CmbItem item = (CmbItem) m_devList.get(ix);
		return (item != null) ? item.hostname:"";
	}
	private void SetDialogTitle(int ix)
	{	
		this.setTitle(GetHostname(ix));
	}
	
	public void AddDeviceInfo(String ipAddr, String hostname)
	{	
		boolean bExist = false;
		CmbItem  item = null;
		for(int k = 0; k < m_devList.size(); ++k)
		{
			item = (CmbItem) m_devList.get(k);
			if(0 == ipAddr.compareToIgnoreCase(item.ipAddr))
			{
				item.hostname = hostname;				
				bExist = true;
				break;
			}
		}
		if(!bExist) m_devList.add(new CmbItem(ipAddr, hostname));
		
		// 刷新combobox列表
		m_cmbIpAddr.removeAllItems();
		for(int k = 0; k < m_devList.size(); ++k)
		{
			item = (CmbItem) m_devList.get(k);
			m_cmbIpAddr.addItem(item.ipAddr);
		}
	}
	public boolean ShowModal(String ipAddr, int ipPort, String hostname, IDialogLogin interf)
	{	
		m_devList.clear();
		AddDeviceInfo(ipAddr, hostname);
		m_textIpPort.setText(String.format("%d", ipPort));	
		m_interface = interf;	
		this.setVisible(true);
		return true;
	}
}