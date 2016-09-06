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
	private static final String VERSION_INFO = "�汾 0.9.2.4";
	
	/* �汾˵��
	 * 
	 * 0.9.2.4
	 * ��ť��ͼƬ����ǩ�ؼ����ֶ���
	 * 
	 * 0.9.2.3
	 * �������ؼ��Ļ��������ӽ�������
	 * 
	 * 0.9.2.2
	 * ����IP�Ի������������������������Ĺ���
	 * device���д����շ����߼��������޸�
	 * 
	 * 0.9.2.1
	 * ������ҳ��ʾ�¼�
	 * �޸���Ԥ�������ļ��Ĺ���
	 * 
	 * 0.9.2.0
	 * ����ͨѶ�������Ż�����صĽ�����ʾ���ֽ������޸�
	 * 
	 * 0.9.1.0
	 * �������Խ�һ�����ơ�
	 * �������Ժ͵�����С��һ�����Ƶ��˻�����
	 * 
	 * 0.9.0.15
	 * ���ͼ��ʱ��û����������е�Ԫ��
	 * 
	 * 0.9.0.14
	 * ʹ����ͼ���flush�����ͼ��
	 * 
	 * 0.9.0.13
	 * �޸���CCC_UI�е�������ؽӿڣ������˶�ͼƬ���漰����������
	 * 
	 * 0.9.0.12
	 * �������ؼ��Ի������˻���ͻ������ı���ͼ�ʹ�С����
	 * ������checkbox��radio��progress�ؼ�
	 * �����˶����ÿؼ�����Э���֧�֣�������lua�����ÿؼ��Ĳ�������
	 * �ؼ���ͼ��ʹ��һ���������������̬�࣬������������������
	 * 
	 * 0.9.0.11
	 * ��ȡ�ļ��汾��Ϣ��Ĵ����Ƶ������߳���
	 * 
	 * 0.9.0.10
	 * �����˶�����enable�Ĵ���
	 * ui��component�ļ����µ��ļ����½���������
	 * 
	 * 0.9.0.9
	 * �����崦��������Ż�������ͨ�����¡���������״̬�µı���ɫ��ǰ��ɫ��Ԫ�ؽ������Ż�
	 * 
	 * 0.9.0.8
	 * ֱ�Ӵ�zip�ļ��л�ȡ��Ϣ�����ٽ�ѹ
	 * 
	 * 0.9.0.7
	 * �Ż���ҳ��ز���
	 * 
	 * 0.9.0.6
	 * ������ޱ���ͼʱ��������ʾ������������
	 * ����ҳ�浯��ҳ����
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
		m_lblProduct.setText("�����ż��ؿͻ��� ");
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
		// ������
		//hm.put(TextAttribute.FAMILY, xmlFont.name); 
		// �Ӵ�
		hm.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD); 
		
		if(size <= 0) size = 1;
		hm.put(TextAttribute.SIZE, size);
		 
		return (new Font(hm));    
	}
}
