package ccc_ui.ui;

public class UiAction 
{
	public static final int ACTION_VALID       = 0;
	public static final int ACTION_HIDE        = 1;
	public static final int ACTION_SHOW        = 2;
	public static final int ACTION_WRITE       = 3;
	public static final int ACTION_ENABLE      = 4;
	public static final int ACTION_DISABLE     = 5;
	public static final int ACTION_INVOKE_APP  = 6;            // 调用外部程序
	
	public int action; 
	public int targetId; 
	public String parameter = null;            // 不同的动作会有不同的参数
}
