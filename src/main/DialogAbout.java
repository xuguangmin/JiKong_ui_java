package main;

import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.JLabel;
import utils.lang.Language;

public class DialogAbout extends JDialog
{
	private static final long   serialVersionUID = -1462477147254427119L;
	private static final String VERSION_INFO = "版本 0.9.2.4";
	
	/* 版本说明
	 * 
	 * 0.9.2.4
	 * 按钮、图片、标签控件文字对齐
	 * 
	 * 0.9.2.3
	 * 滑动条控件的滑块上增加进度数字
	 * 
	 * 0.9.2.2
	 * 设置IP对话框中增加了搜索集控主机的功能
	 * device类中处理收发的逻辑进行了修改
	 * 
	 * 0.9.2.1
	 * 增加了页显示事件
	 * 修复了预览界面文件的功能
	 * 
	 * 0.9.2.0
	 * 网络通讯进行了优化。相关的界面提示部分进行了修改
	 * 
	 * 0.9.1.0
	 * 设置属性进一步完善。
	 * 设置属性和调整大小，一部分移到了基类中
	 * 
	 * 0.9.0.15
	 * 清除图像时，没有清空数组中的元素
	 * 
	 * 0.9.0.14
	 * 使用了图像的flush来清空图像
	 * 
	 * 0.9.0.13
	 * 修改了CCC_UI中的所有相关接口，增加了对图片缓存及其他的清理
	 * 
	 * 0.9.0.12
	 * 滑动条控件自画增加了滑块和滑动条的背景图和大小设置
	 * 增加了checkbox、radio、progress控件
	 * 增加了对设置控件属性协议的支持，可以在lua中设置控件的部分属性
	 * 控件和图像使用一个对象池来管理，静态类，整个程序共享这个对象池
	 * 
	 * 0.9.0.11
	 * 获取文件版本信息后的处理移到了主线程中
	 * 
	 * 0.9.0.10
	 * 增加了对属性enable的处理
	 * ui和component文件夹下的文件重新进行了命名
	 * 
	 * 0.9.0.9
	 * 对字体处理进行了优化；对普通、按下、焦点三种状态下的背景色、前景色等元素进行了优化
	 * 
	 * 0.9.0.8
	 * 直接从zip文件中获取信息，不再解压
	 * 
	 * 0.9.0.7
	 * 优化了页相关部分
	 * 
	 * 0.9.0.6
	 * 解决了无背景图时，界面显示不正常的问题
	 * 增加页面弹出页属性
	 */
	private JLabel       m_lblProduct     = new JLabel();
	private JLabel       m_lblVersion     = new JLabel();
	private JLabel       m_lblWebSize     = new JLabel();
	
	public DialogAbout(Frame owner, boolean modal) 
	{
		super(owner, modal);
		this.setLayout(null);
		AddChild();
		addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e) {OnCmponentResized(e);}
		});
		this.setSize(460, 130);
		if(owner != null)
		{
			int width  = owner.getWidth();
			int height = owner.getHeight();
			int x = owner.getX() + (width - this.getWidth())/2;
			int y = owner.getY() + (height - this.getHeight())/2;
			this.setLocation(x, y);
		}
		
		this.setTitle(Language.Get(Language.LID_STRING_ABOUT));	
		/*
		this.setLayout(null);
		JLabel lbl = new JLabel();
		lbl.setIcon(new ImageIcon(FileUtil.GetResPath() + "logo.jpg"));
		lbl.setLocation(5, 5);
		lbl.setSize(32, 32);
		this.add(lbl);
		*/
	}
	
	private void OnCmponentResized(ComponentEvent e)
    {		
		MoveLoginCtrl(20, 30, this.getWidth(), this.getHeight());
    }
	private void MoveLoginCtrl(int x, int y, int width, int height) 
	{
		m_lblProduct.setSize(300, 25);
		m_lblProduct.setLocation(100, 10);	
		m_lblVersion.setSize(width-100, 25);
		m_lblVersion.setLocation(100, 30);
		m_lblWebSize.setSize(width-100, 25);
		m_lblWebSize.setLocation(100, 50);	
	}
	private void AddChild()
	{
		m_lblProduct.setFont(CreateFont(15));
		m_lblProduct.setText("飞利信集控客户端 ");
		m_lblVersion.setText(VERSION_INFO);
		
		m_lblProduct.setFont(CreateFont(15));
		m_lblWebSize.setText("http://www.philisense.com");
		this.add(m_lblProduct);
		this.add(m_lblVersion);
		this.add(m_lblWebSize);	
	}
	
	protected Font CreateFont(int size)
	{		
		HashMap<TextAttribute, Object> hm = new HashMap<TextAttribute, Object>(); 	
		// 字体名
		//hm.put(TextAttribute.FAMILY, xmlFont.name); 
		// 加粗
		hm.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD); 
		
		if(size <= 0) size = 1;
		hm.put(TextAttribute.SIZE, size);
		 
		return (new Font(hm));    
	}
}
