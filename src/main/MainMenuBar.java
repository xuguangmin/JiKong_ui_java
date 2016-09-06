/* ***************************************************************************

                  		版权所有 (C), 2013-2100

 *****************************************************************************
	  文件名称 : MainMenuBar.java
	  作者           : 贾延刚
	  生成日期 : 2013年
	
	  版本           : 1.0
	  功能描述 : 主窗口的菜单
	
	  修改历史 :

******************************************************************************/
package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import utils.lang.Language;

public class MainMenuBar implements ActionListener
{
	public static final int SETTING       = 1;
	public static final int COMM_CONNECT  = 2;
	public static final int APP_CLOSE     = 3;
	
	public static final int INTERNAL_UI   = 4;
	public static final int EXTERNAL_UI   = 5;
	public static final int UI_RATIO      = 6;
	public static final int UI_TEST       = 7;
	public static final int UI_STOP_PREV  = 8;
	
	public static final int TOOL_OPTIONS  = 9;
	public static final int ABOUT         = 10;
	
	public interface OnMenuActionListener 
	{
        void MenuAction(int action);
    }
	
	private OnMenuActionListener m_menuActionListener = null;
	private JMenuBar      m_menuBar;
	private JMenu         m_menuFile;
	private JMenuItem     m_mItemFileSetting;
	private JMenuItem     m_mItemFileConnect;
	private JMenuItem     m_mItemToolExternalUI;
	private JMenuItem     m_mItemToolInternalUI;
	private JMenuItem     m_mItemToolStopPreview;
	
	private JCheckBoxMenuItem m_mItemToolRatio;
	private JMenuItem     m_mItemFileExit;
	
	private JMenu         m_menuTool;
	private JMenuItem     m_mItemToolTest;
	private JMenuItem     m_mItemToolOption;
	private JMenu         m_menuHelp;
	private JMenuItem     m_menuHelpAbout;
	
	public MainMenuBar()
	{
		InitMenu();
	}
	
	private void InitMenu()
	{
		m_menuFile         = new JMenu(Language.Get(Language.LID_STRING_FILE));
		m_mItemFileSetting = new JMenuItem(Language.Get(Language.LID_STRING_4));
		m_mItemFileConnect = new JMenuItem(Language.Get(Language.LID_STRING_OPEN));
		m_mItemFileExit    = new JMenuItem(Language.Get(Language.LID_STRING_EXIT));
		m_menuTool         = new JMenu(Language.Get(Language.LID_STRING_TOOL));
		m_mItemToolTest    = new JMenuItem(Language.Get(Language.LID_STRING_TEST));
		m_mItemToolExternalUI = new JMenuItem(Language.Get(Language.LID_STRING_6));
		m_mItemToolInternalUI = new JMenuItem(Language.Get(Language.LID_STRING_8));
		m_mItemToolRatio   = new JCheckBoxMenuItem(Language.Get(Language.LID_STRING_7), false);
		m_mItemToolOption  = new JMenuItem(Language.Get(Language.LID_STRING_OPTIONS));
		m_mItemToolStopPreview = new JMenuItem(Language.Get(Language.LID_STRING_15));
		m_menuHelp         = new JMenu(Language.Get(Language.LID_STRING_HELP));
		m_menuHelpAbout    = new JMenuItem(Language.Get(Language.LID_STRING_ABOUT));
		
		m_mItemFileSetting.addActionListener(this);
		m_mItemFileConnect.addActionListener(this);
		m_mItemFileExit.addActionListener(this);
		m_mItemToolTest.addActionListener(this);
		m_mItemToolExternalUI.addActionListener(this);
		m_mItemToolInternalUI.addActionListener(this);
		m_mItemToolRatio.addActionListener(this);
		m_mItemToolStopPreview.addActionListener(this);
		m_mItemToolOption.addActionListener(this);
		m_menuHelpAbout.addActionListener(this);
		// File
		m_menuFile.add(m_mItemFileSetting);
		m_menuFile.add(m_mItemFileConnect);
		m_menuFile.addSeparator();
		m_menuFile.add(m_mItemFileExit);
		// Tool
		//m_menuTool.add(m_mItemToolTest);   m_mItemToolTest.setEnabled(false);
		m_menuTool.add(m_mItemToolInternalUI);
		m_menuTool.add(m_mItemToolExternalUI);
		//m_menuTool.add(m_mItemToolRatio);  m_mItemToolRatio.setEnabled(false);
		m_menuTool.addSeparator();
		m_menuTool.add(m_mItemToolStopPreview); m_mItemToolStopPreview.setEnabled(false);
		// Help
		m_menuHelp.add(m_menuHelpAbout);
		
		m_menuBar = new JMenuBar();
		m_menuBar.add(m_menuFile);
		m_menuBar.add(m_menuTool);
		m_menuBar.add(m_menuHelp);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if     (e.getSource() == m_mItemFileSetting)    DoAction(SETTING);
		else if(e.getSource() == m_mItemFileConnect)    DoAction(COMM_CONNECT);
		else if(e.getSource() == m_mItemFileExit)       DoAction(APP_CLOSE);
		else if(e.getSource() == m_mItemToolInternalUI) DoAction(INTERNAL_UI);
		else if(e.getSource() == m_mItemToolExternalUI) DoAction(EXTERNAL_UI);
		else if(e.getSource() == m_mItemToolRatio)      DoAction(UI_RATIO);
		else if(e.getSource() == m_mItemToolStopPreview)DoAction(UI_STOP_PREV);
		
		else if(e.getSource() == m_mItemToolTest)       DoAction(UI_TEST);
		else if(e.getSource() == m_mItemToolOption)     DoAction(TOOL_OPTIONS);
		else if(e.getSource() == m_menuHelpAbout)       DoAction(ABOUT);
		
	}
	
	private void DoAction(int action)
	{
		if(m_menuActionListener != null) m_menuActionListener.MenuAction(action);
	}
	public void SetToolStopPreview(boolean bEnable)
	{
		m_mItemToolStopPreview.setEnabled(bEnable);
	}
	public void SetToolRatioState(boolean bCheck)
	{
		m_mItemToolRatio.setState(bCheck);
	}
	public boolean GetToolRatioState()
	{
		return m_mItemToolRatio.isSelected();
	}
	
	private boolean m_b_connected = false;
	public void SetFileConnectText(boolean b_connected)
	{
		if(b_connected == m_b_connected)
			return;
		
		int lid = b_connected ? Language.LID_STRING_CLOSE:Language.LID_STRING_CLOSE;
		m_mItemFileConnect.setText(Language.Get(lid));
		m_b_connected = b_connected;
	}
	
	public JMenuBar GetMainMenuBar(OnMenuActionListener l)
	{
		m_menuActionListener = l;
		return m_menuBar;
	}
}
