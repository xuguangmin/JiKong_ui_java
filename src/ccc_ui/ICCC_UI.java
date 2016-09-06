package ccc_ui;

public interface ICCC_UI 
{
	/*
	 * 调用指定的应用程序
	 */
	void ccc_ui_invoke_app(String packageName);
	
	void ccc_ui_notify(int type, String strNotify);
	boolean ccc_ui_event(int ctrlId, int ctrlEvent, int value, boolean bWarning, String strWarning);
}
