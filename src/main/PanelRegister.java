package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.lang.Language;


public class PanelRegister extends JPanel implements ActionListener 
{
	public interface IPanelRegister
	{
		void SerialNoString(String snString);
	}

	private static final long serialVersionUID = 37013223418715370L;
	private static final int  TEXTFIELD_COUNT = 4;
	private IPanelRegister    m_interface = null;
	private String            m_notifyInfo = null;
	private String            m_password = null;
	private Rectangle         m_rectNotify = new Rectangle();
	private ImageIcon         m_bkImage = null;	
	private JButton           m_btnOK = new JButton();
	private JButton           m_btnCancel = new JButton();
	private JLabel            m_lblIpAddr = new JLabel();
	private JTextField        m_textSNs[] = new JTextField[TEXTFIELD_COUNT];

	public PanelRegister() {
		super();
		this.setLayout(null);
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				OnCmponentResized(e);
			}
		});

		m_rectNotify.setSize(0, 0);
		this.add(m_lblIpAddr);
		for (int k = 0; k < TEXTFIELD_COUNT; ++k) {
			m_textSNs[k] = new JTextField();
			this.add(m_textSNs[k]);
		}
		this.add(m_btnOK);
		this.add(m_btnCancel);

		m_btnOK.addActionListener(this);
		m_btnCancel.addActionListener(this);
		m_lblIpAddr.setText(Language.Get(Language.LID_STRING_14));
		m_btnOK.setText(Language.Get(Language.LID_STRING_REGISTER));
		m_btnCancel.setText(Language.Get(Language.LID_STRING_EXIT));
	}

	private void MoveWindow(JComponent comp, int x, int y, int width, int height) {
		comp.setSize(width, height);
		comp.setLocation(x, y);
	}

	private void ControlMoveRegister(int x, int y, int width, int height) {
		int ctrlHeight = 25;
		int ctrlWidth1 = 90;
		int ctrlWidth2 = 100;
		int ctrlY = y + (int) ((height - ctrlHeight) / 2);
		int ctrlX = x + (int) ((width - ctrlWidth2 * 4 - ctrlWidth1) / 2);
		MoveWindow(m_lblIpAddr, ctrlX, ctrlY, ctrlWidth1, ctrlHeight);

		ctrlX += ctrlWidth1;
		for (int k = 0; k < TEXTFIELD_COUNT; ++k) {
			MoveWindow(m_textSNs[k], ctrlX, ctrlY, ctrlWidth2, ctrlHeight);
			ctrlX += ctrlWidth2;
		}

		ctrlX -= ctrlWidth2 * 2;
		MoveWindow(m_btnOK, ctrlX, ctrlY + 50, ctrlWidth1, ctrlHeight);
		ctrlX += ctrlWidth2;
		MoveWindow(m_btnCancel, ctrlX, ctrlY + 50, ctrlWidth1, ctrlHeight);
	}

	private void OnCmponentResized(ComponentEvent e) {
		int width = this.getWidth();
		int height = this.getHeight();
		int y = (int) ((height - 30) / 2);
		ControlMoveRegister(0, y, width, 30);
		m_rectNotify.setRect(0, 0, width, y);
		this.repaint();
	}

	private void DrawLogoText(Graphics g, Rectangle rect, String strText,String strText2) 
	{
		// g.setFont(m_font);
		g.setColor(Color.black);
		int strWidth = g.getFontMetrics().stringWidth(strText);
		int strHeight = g.getFontMetrics().getHeight();
		g.drawString(strText, (int) ((rect.getWidth() - strWidth) / 2),
				(int) ((rect.getHeight() - strHeight) / 2));

		strWidth = g.getFontMetrics().stringWidth(strText2);
		g.drawString(strText2, (int) ((rect.getWidth() - strWidth) / 2),
				(int) ((rect.getHeight() - strHeight) / 2) + 25);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(m_bkImage != null)
		{
			g.drawImage(m_bkImage.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
		}
		if (m_notifyInfo != null) {
			DrawLogoText(g, m_rectNotify, m_notifyInfo, m_password);
			return;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == m_btnOK)
		{
			String strText = "";
			for(int k = 0; k < TEXTFIELD_COUNT; ++k)
			{
				strText += m_textSNs[k].getText();
				
			}
			m_interface.SerialNoString(strText);
		}
		else{
			System.exit(0);
		}
	}

	public void SetBackgroundImage(String filename)
	{
		m_bkImage = new ImageIcon(filename);
	}
	public void GetSerialNo(String strText, String strPass, IPanelRegister l) 
	{
		m_notifyInfo = strText;
		m_password = strPass;
		m_interface = l;
	}
}
