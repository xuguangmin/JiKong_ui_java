package ccc_ui.ui;

public interface IUiListener 
{	
	/* 
	 * �ؼ����¼�
	 * int value ��ͬ���¼��в�ͬ��ֵ
	 */
	void UiEvent(int ctrlId, int ctrlEvent, int value, boolean bWarning, String strWarning, 
			        int jumpPage, UiActionGroup actionGroup);
	
	/* 
	 * ҳ���¼�
	 * Ŀǰֻ����ҳ
	 */
	public void PageEvent(int jumpPage);
}
