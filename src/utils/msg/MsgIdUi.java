package utils.msg;

public final class MsgIdUi 
{
	/*
	 * 设备通知 
	 */
	public static final int MSG_CONNECT_STATUS    = 2;   // 网络连接状态
	public static final int MSG_UI_VERSION_INFO   = 3;   // 界面zip的版本信息，是zip文件的唯一识别码
	public static final int MSG_UI_ZIP_INFO       = 4;   // 界面zip的信息
	public static final int MSG_UI_DL_PERCENT     = 5;   // 界面zip的下载百分比
	public static final int MSG_UI_DL_FINISH      = 6;   // 界面zip下载完成
	public static final int MSG_UI_HAS_NEW_ZIP    = 7;   // 服务器通知有新的界面zip文件被上传
	public static final int MSG_UI_CTRL_PROPERTY  = 8;   // 服务器设置控件属性
	public static final int MSG_UI_DISCOVER_DEV   = 9;   // 找到的集控主机
	public static final int MSG_UI_INVOKE_PAGE    = 10;  // 服务器请求显示或者隐藏指定页
	
	/*
	 * 其他消息 
	 */
	public static final int MSG_UI_LOAD          = 11;  // 显示UI文件
	public static final int MSG_NOTIFY_INFO      = 12;
}
