package ccc_ui;

public interface ICCC_UI 
{
	/*
	 * ����ָ����Ӧ�ó���
	 */
	void ccc_ui_invoke_app(String packageName);
	
	void ccc_ui_notify(int type, String strNotify);
	boolean ccc_ui_event(int ctrlId, int ctrlEvent, int value, boolean bWarning, String strWarning);
}
