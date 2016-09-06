package ccc_ui.ui;

public final class UiEventEnum 
{
	// 页事件
	public static final int PageShow              = 0x1000;
		
	// 按钮控件事件
	public static final int ButtonControlClick    = 0x1010;
	public static final int ButtonControlPressed  = 0x1011;
	public static final int ButtonControlReleased = 0x1012;
			
	// 图片控件事件
	public static final int IamgeControlClick       = 0x1020;
	public static final int IamgeControlPressed     = 0x1021;
	public static final int IamgeControlReleased    = 0x1022;
	public static final int IamgeControlDoubleClick = 0x1023;
	
	// Radio控件事件
	public static final int RadioBoxControlClick    = 0x1040;
	public static final int RadioBoxStateChanged    = 0x1041;  // 原来是1050
	// CheckBox事件
	public static final int CheckBoxClick           = 0x1050;
	public static final int CheckBoxStateChanged    = 0x1051;
	
	// 滑动条事件
	public static final int SliderValueChange       = 0x1080;
	public static final int EVENT_COMBOBOX_SEL_INDEX_CHANGED = 0X1090;
	public static final int EVENT_LISTBOX_SEL_INDEX_CHANGED = 0X10D0;
	
	
	/**
	 * @category ComboBox控件选项改变事件
	 */
	public static final int ComboBoxSelectIndexChange=0X1090;
	/**
	 * @category ListBoxControl控件选项改变事件
	 */
	public static final int ListBoxSelectIndexChange=0X10D0;
	/**
	 * @category 定时控件计时结束事件
	 */
	public static final int TimerControlTick=0x10C0;
}
