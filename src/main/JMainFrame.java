/* ***************************************************************************

                  		版权所有 (C), 2013-2100

 *****************************************************************************
	  文件名称 : JMainFrame.java
	  作者           : 贾延刚
	  生成日期 : 2013年
	
	  版本           : 1.0
	  功能描述 : 主窗口类
	
	  修改历史 :

******************************************************************************/
package main;

import java.io.File;

import javax.swing.*;

import java.awt.event.*;

import utils.*;
import utils.msg.*;
import utils.lang.*;
import ccc_ui.ICCC_UI;
import ccc_ui.CCC_UI;
import device.Device;
import device.net.ITcpClient;

public class JMainFrame extends JFrame implements ICCC_UI
{
	private static final long serialVersionUID    = 1L;  
	private boolean       m_show_how     = false;   // 程序当前是否仅用来演示界面
	private Device        m_device       = null;
    private CCC_UI        m_cccUI        = null;
    private PanelInfo     m_panelInfo    = null;
    private Configure     m_configure    = new Configure();
    private MsgQueue2     m_msgQueue2    = new MsgQueue2();
    private DownloadFile  m_downloadFile = new DownloadFile();
    private ConnStatus    m_connStatus   = new ConnStatus();
    private DeviceMsg     m_deviceMsg    = null;
    private Timer         m_timer;
    private MainMenuBar   m_mainMenu     = null;
    private DialogLogin   m_dlgLogin     = null;
    private String        m_titlePartA   = "";
    private String        m_titlePartB   = "";
 	
	public JMainFrame()
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(getToolkit().getImage(FileUtil.GetResPath() + "logo.png"));
		//getToolkit().getImage()
		//ImageIcon img1 = new ImageIcon(getClass().getResource("/images/logo.png"));

		//this.setIconImage(img1.getImage());
		
		
		//System.out.println(getClass().getResource(FileUtil.GetResPath() + "logo.png").toString());
				
		InitialUI();
		m_deviceMsg = new DeviceMsg(m_msgQueue2);
		ApplicationStart();
		/*
		Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    	for(int i=0;i<fonts.length;i++){ 
    		System.out.println(fonts[i].getFamily());//获取字体
    	} */
	}
	
	private void SetTitleA(String partA)
	{
		m_titlePartA = partA;
		this.setTitle(String.format("%s - %s", partA, m_titlePartB));
	}
	private void SetTitleB(String partB)
	{
		m_titlePartB = partB;
		this.setTitle(String.format("%s - %s", m_titlePartA, partB));
	}
	private boolean InitialUI()
	{
		this.setLayout(null);
		SetTitleB(FileUtil.GetAppPath());
		
		//ImageIcon ii = new ImageIcon(JMainFrame.class.getResource("\\res\\logo.jpg"));
		
		
		// 主菜单
		m_mainMenu = new MainMenuBar();
		this.setJMenuBar(m_mainMenu.GetMainMenuBar(new MainMenuBar.OnMenuActionListener(){
			@Override
			public void MenuAction(int action) {OnMenuAction(action);}
		}));
		
		
		m_panelInfo  = new PanelInfo();
		m_panelInfo.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) 
			{
				// 双击
				if(2 == e.getClickCount()) OnMenuDialogSetting(true);
			}
		});
		this.getContentPane().add(m_panelInfo);
			
		// CCC UI
		m_cccUI = new CCC_UI();
		if(m_cccUI.Initial(this))
		{
			this.getContentPane().add(m_cccUI.GetPanel());	
		}
		
		
		this.addComponentListener(new ComponentAdapter(){
				public void componentResized(ComponentEvent e) {OnCmponentResized(e);}
			});
		this.addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e)  {}
			public void windowClosing(WindowEvent e) {OnMenuApplicationClose();}
		});
		return true;
	}

	private void OnCmponentResized(ComponentEvent e)
    {	
		ControlMove();
    }
	void ControlMove()
	{
		int statusHeight = 20;
		int width  = this.getContentPane().getWidth();
		int height = this.getContentPane().getHeight();
		m_panelInfo.setLocation(0, height - statusHeight);
		m_panelInfo.setSize(width, statusHeight);
		
		m_cccUI.GetPanel().setLocation(0, 0);
		m_cccUI.GetPanel().setSize(width, height - statusHeight);	
	}
	private void OnMenuUIRatio()
	{
		boolean bCheck = m_mainMenu.GetToolRatioState();
		System.out.println(bCheck ? "select check":"not check");
		
		m_mainMenu.SetToolRatioState(bCheck);
	}
	private void CommConnect()
	{
		if(m_connStatus.IsConnected())
		{
			this.m_device.CloseComm();
		}
		else
		{
			this.m_device.OpenComm();
		}
	}
		
	//int temp_count = 0;
	private void OnMenuDialogAbout()
	{
		DialogAbout dlg = new DialogAbout(this, true);
		dlg.setLocation(this.getX()+(this.getWidth() - dlg.getWidth())/2,this.getY()+(this.getHeight() - dlg.getHeight())/2);
		dlg.setVisible(true);
	
		//OnMsgControlProperty(2, 3, temp_count);
		//temp_count += 10;		
		
	}
	private void OnMenuDialogOptions()
	{
		JDialog dlg = new JDialog(this, "选项", true);
		dlg.setSize(500, 300);
		dlg.setVisible(true);	
	}
	private void OnlyTest()
	{
		//SendString("OnlyTest");
		//this.m_device.CheckZipFileVersion();//TestPacket();//LoginToServer();
		//m_device.LoginToServer();
	}
	
	private void OnMenuDialogSetting(boolean bLogin)
	{
		int ipPort = m_configure.GetIpPort();
		String ipAddr = m_configure.GetIpAddress();
		String hostname = m_configure.GetHostname();
		
		m_dlgLogin = new DialogLogin(this);
		m_dlgLogin.ShowModal(ipAddr, ipPort, hostname, new DialogLogin.IDialogLogin(){
			@Override
			public void LoginParameter(String ipAddr, int ipPort, String hostname) 
			{
				OnLoginParameter(ipAddr, ipPort, hostname);
				m_dlgLogin = null;
			}
			public void SearchDevice()
			{
				OnSearchDevice();
			}
		});		
	}
	public void OnLoginParameter(String ipAddr, int ipPort, String hostname)
	{
		m_configure.SetIpAddress(ipAddr, ipPort, hostname);
		SetTitleA(hostname);
		m_panelInfo.SetConnInfo(String.format("%s %d", ipAddr, ipPort));
		
		m_device.CloseComm();
		m_device.SetComm(ipAddr, ipPort);
		this.m_device.OpenComm();
	}
	public void OnSearchDevice()
	{
		m_device.SearchJikong(7000);
	}
	
	
	public void OnMenuAction(int action) 
	{	
		//System.out.printf("action = %d\n", action);
		switch(action)
		{
		case MainMenuBar.SETTING       :OnMenuDialogSetting(true);break;
		case MainMenuBar.COMM_CONNECT  :CommConnect();break;
		case MainMenuBar.APP_CLOSE     :OnMenuApplicationClose();break;
		case MainMenuBar.INTERNAL_UI   :OnMenuLoadUIFileInternal();break;
		case MainMenuBar.EXTERNAL_UI   :OnMenuLoadUIFileExternal();break;
		case MainMenuBar.UI_STOP_PREV  :OnMenuStopUIFilePreview();break;
		case MainMenuBar.UI_RATIO      :OnMenuUIRatio();break;
								
		case MainMenuBar.UI_TEST       :OnlyTest();break;
		case MainMenuBar.TOOL_OPTIONS  :OnMenuDialogOptions();break;
		case MainMenuBar.ABOUT         :OnMenuDialogAbout();break;
		}
	}
	
	private void ApplicationStart()
	{
		// 启动时钟循环
		m_timer = new Timer(100, new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {ProcessMsgQueue();}
		});
		m_timer.start();
	
		m_configure.LoadFromXml(FileUtil.GetConfigFilePath());
		int ipPort = m_configure.GetIpPort();
		String ipAddr = m_configure.GetIpAddress();
		SetTitleA(m_configure.GetHostname());
		m_panelInfo.SetConnInfo(String.format("%s %d", ipAddr, ipPort));
		
		m_device = new Device();
		m_device.Initial(m_deviceMsg, FileUtil.GetTempPath());
		m_device.SetComm(ipAddr, ipPort);
		CommConnect();
	}
	
	private void OnMenuApplicationClose()
	{
		m_device.CloseComm();
		FileUtil.DeleteFolder(FileUtil.GetTempPath());
		m_configure.Save();
		System.out.printf("OnMenuApplicationClose\n");
		System.exit(0);
	}
	
	private String GetZipFileTitle(String fileName)
	{			
		return FileUtil.GetFileTitle(fileName, ".zip");
	}
	
	private void PostMessage(int msgId, int arg1, int arg2, Object obj)
	{
		if(null == m_msgQueue2)
			return;
		
		Message msg = new Message();
		msg.what = msgId;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		msg.obj  = obj;
		m_msgQueue2.send(msg);	
	}
	private boolean LoadUIFileExternal(String strZipFileName)
	{	
		// 取title，顺便检查是否是zip文件
		String fileTitle = GetZipFileTitle(strZipFileName);
		if(fileTitle.length() <= 0)
			return false;
		
		String dirTemp = FileUtil.GetTempPath();
		FileUtil.DeleteFolder(dirTemp);
		if(!FileUtil.CreateDirectory(dirTemp))
			return false;
		
		if(!FileUtil.CopyFile(strZipFileName, dirTemp))
			return false;
				
		strZipFileName = String.format("%s%s.zip", dirTemp, fileTitle);
		NotifyLoadUIFile(strZipFileName);	
		return true;
	}
	
	// 停止UI文件的演示，进入正常模式
	private void OnMenuStopUIFilePreview()
	{
		m_mainMenu.SetToolStopPreview(false);
		m_show_how = false;
	}
	// 装载外部的UI文件
	private boolean OnMenuLoadUIFileExternal()
	{
		JFileChooser fileOpen = new JFileChooser();
		fileOpen.setCurrentDirectory(new File(m_configure.GetExternalUiPath()));
		int option = fileOpen.showOpenDialog(this);
		if(option == JFileChooser.APPROVE_OPTION)
		{
			m_show_how = true;
			m_mainMenu.SetToolStopPreview(true);
			String strZipFileName = fileOpen.getSelectedFile().toString();
			if(LoadUIFileExternal(strZipFileName))
			{
				m_configure.SaveExternalUiPath(fileOpen.getCurrentDirectory().toString());
				return true;
			}
		}
		return false;
	}
	// 直接装载程序目录下已下载的UI文件
	private void OnMenuLoadUIFileInternal()
	{
		//System.out.printf("OnMenuLoadUIFileInternal %s\n", m_configure.GetFileTitle());
		NotifyLoadUIFile(GetInternalUiZip(m_configure.GetFileTitle()));
		m_show_how = true;
		m_mainMenu.SetToolStopPreview(true);
	}
	
	private void NotifyLoadUIFile(String filename)
	{
		//PostNotifyInfo(Language.Get(Language.LID_STRING_10));	
		PostMessage(MsgIdUi.MSG_UI_LOAD, m_show_how ?1:0, 0, filename);//m_show_how ?1:0, 0, fileTitle, 200);
	}
	
	private void OnMsgLoadLocalUI(String filename, boolean b_showHow)
	{
		if(LoadUIFile(filename))
		{}
		else
		{
			if(m_show_how)           // 演示版时
			{
				String strWarning = String.format("%s%s", Language.Get(Language.LID_STRING_10), filename);
				//JOptionPane.showConfirmDialog(this, strWarning, "", JOptionPane.DEFAULT_OPTION);
				JOptionPane.showMessageDialog(this, strWarning);
			}	
			else
			{
				this.m_configure.SaveFileInfo("", "");
				m_device.CheckZipFileVersion();
			}
		}	
	}
	private boolean LoadUIFile(String zipFile)
	{			
		if(!FileUtil.Exists(zipFile))
		{
			System.out.printf("LoadLocalUIFile :%s not exist\n", zipFile);
			return false;
		}
		return m_cccUI.LoadUI(zipFile);
	}
		
	private void OnMsgConnectStatus(int connStatus)
	{
		m_connStatus.SaveStatus(connStatus);
		boolean b_connected = m_connStatus.IsConnected();
		
		
		m_panelInfo.SetConnStatus(b_connected);
		m_mainMenu.SetFileConnectText(b_connected);
		if(!b_connected) m_cccUI.LoadDefaultUI();
		
		
		String strInfo = "";
		switch(connStatus)
		{
		case ITcpClient.CONN_STATUS_CONNECTING:
			strInfo = m_connStatus.GetConnectingNotify();
			break;
		}
		m_panelInfo.SetMessage(strInfo);
	}
	
	private String GetInternalUiZip(String fileTitle)
	{
		return String.format("%s%s.zip", FileUtil.GetSkinPath(), fileTitle);	
	}
	// 下载过程完成
	private void OnMsgDownloadFinished(boolean bSuccess, int fileType, String filePath)
	{
		if(bSuccess)
		{
			boolean bResult = false;
			if(m_downloadFile.ProcessDownload(filePath, FileUtil.GetSkinPath()))
			{
				String fileTitle = m_downloadFile.GetFileTitle();		
				if(fileTitle != null)
				{
					m_configure.SaveFileInfo(fileTitle, m_downloadFile.GetFileVersion());
					NotifyLoadUIFile(GetInternalUiZip(fileTitle));
					bResult = true;
				}
			}
			bSuccess = bResult;
		}
		
		if(!bSuccess)
		{	
			//this.PostNotifyInfo("下载文件失败");
			m_device.CheckZipFileVersion();
		}	
	}
	// 收到文件版本信息
	private void OnMsgVersionInfo(boolean bSuccess, int fileType, String version)
	{
		if(bSuccess)
		{
			boolean b_download = false;
			String oldVersion = m_configure.GetFileVersion();
			if(version.equalsIgnoreCase(oldVersion))	
			{
				System.out.printf("%s = %s\n", version, oldVersion);
				NotifyLoadUIFile(GetInternalUiZip(m_configure.GetFileTitle()));	
			}
			else
			{
				System.out.printf("%s != %s\n", version, oldVersion);
				b_download = true;
			}
			
			m_downloadFile.SaveFileVersion(version);
			if(b_download) 
			{
				m_device.DownloadZipFile();
			}
		}
		else
		{
			m_device.CheckZipFileVersion();
		}
	}
	private void OnMsgDownloadPercent(int percent, int speed)
	{
		//SetPercent(percent, String.format("%d%% %dKB/s", percent, speed));
		m_panelInfo.SetProgress(percent, (double)speed);
	}
	private void OnMsgControlProperty(int ctrlId, int property, int value)
	{
		this.m_cccUI.SetControlProperty(ctrlId, property, value);
	}
	private void OnMsgInvokePage(int page_no, boolean b_show)
	{
		this.m_cccUI.InvokePage(page_no, b_show);
	}
	
	private void OnMsgDiscoverDevice(String ipAddr, String hostname)
	{
		if(m_dlgLogin != null) m_dlgLogin.AddDeviceInfo(ipAddr, hostname);
	}
	
	private void ProcessDeviceMsg(Message msg)
	{
		if(m_show_how)           // 演示版时
			return;
		
		switch(msg.what)
		{
		case MsgIdUi.MSG_UI_HAS_NEW_ZIP:
			m_device.CheckZipFileVersion();       // 如果不从检查版本开始，则无法获取到新文件的版本信息
			break;
		case MsgIdUi.MSG_UI_ZIP_INFO:
			m_downloadFile.SaveFilename((String)msg.obj); 
			break;
		case MsgIdUi.MSG_UI_DL_PERCENT:
			OnMsgDownloadPercent(msg.arg1, msg.arg2);
			break;
		case MsgIdUi.MSG_CONNECT_STATUS:
			OnMsgConnectStatus(msg.arg1);
			break;
		case MsgIdUi.MSG_UI_DL_FINISH:
			OnMsgDownloadFinished((0 == msg.arg1)?false:true, msg.arg2, (String)msg.obj);
			break;
		case MsgIdUi.MSG_UI_VERSION_INFO:
			OnMsgVersionInfo((0 == msg.arg1) ? false:true, msg.arg2, (String)msg.obj);
			break;
		case MsgIdUi.MSG_UI_CTRL_PROPERTY:
			OnMsgControlProperty(msg.arg1, msg.arg2, msg.arg3);
			break;
		case MsgIdUi.MSG_UI_INVOKE_PAGE:
			OnMsgInvokePage(msg.arg1, (0 == msg.arg2) ? false:true);
			break;
		case MsgIdUi.MSG_UI_DISCOVER_DEV:
			ParaDiscoverDev para = (ParaDiscoverDev)msg.obj;
			OnMsgDiscoverDevice(para.ipAddr, para.hostname);
			break;
		}	
	}
	
	private void OnMsgNotifyInfo(String strText)
	{	
		//SetLogMessage(strText);
	}

	private void ProcessMsgQueue()
	{
		while(true)
		{
			Message msg = m_msgQueue2.recv();
			if(msg == null)
				break;
			
			switch(msg.what)
			{
			case MsgIdUi.MSG_CONNECT_STATUS:
			case MsgIdUi.MSG_UI_VERSION_INFO:
			case MsgIdUi.MSG_UI_ZIP_INFO:
			case MsgIdUi.MSG_UI_DL_PERCENT:
			case MsgIdUi.MSG_UI_DL_FINISH:
			case MsgIdUi.MSG_UI_HAS_NEW_ZIP:
			case MsgIdUi.MSG_UI_CTRL_PROPERTY:
			case MsgIdUi.MSG_UI_INVOKE_PAGE:
			case MsgIdUi.MSG_UI_DISCOVER_DEV:
				ProcessDeviceMsg(msg);
				break;
				
			case MsgIdUi.MSG_NOTIFY_INFO:
				OnMsgNotifyInfo((String)msg.obj);
				break;
			case MsgIdUi.MSG_UI_LOAD:
				OnMsgLoadLocalUI((String)msg.obj, (0 == msg.arg1) ? false:true);
				break;
			}
		}
	}
	
	// -------------------  interface ICCC_UI  -------------------
	@Override
	public void ccc_ui_notify(int type, String strNotify)
	{
		switch(type)
		{
		case CCC_UI.PAGE_TITLE:
			SetTitleB(strNotify);
			break;
		case CCC_UI.LOAD_UI_FAILED:
			SetTitleB(Language.Get(Language.LID_STRING_9));
			break;
		}
	}
	@Override
	public boolean ccc_ui_event(int ctrlId, int ctrlEvent, int value, boolean bWarning, String strWarning) 
	{
		if(bWarning)
		{
			int result = JOptionPane.showConfirmDialog(this, strWarning, this.getTitle(), JOptionPane.YES_NO_OPTION);
			if(JOptionPane.NO_OPTION == result)
				return false;			
		}
		m_device.SendEvent(ctrlId, ctrlEvent, value);
		return true;
	}
	@Override
	public void ccc_ui_invoke_app(String packageName) 
	{	
		InvokeApp.Run(packageName);
	}
}