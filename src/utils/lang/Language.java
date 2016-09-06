package utils.lang;

import utils.Utils;

public final class Language 
{
	public static final int LANG_EN_US  = 0;
	public static final int LANG_ZH_CN  = 1;
	
	public static final int LID_STRING_EXIT     = 0;
	public static final int LID_STRING_FILE     = 1;
	public static final int LID_STRING_TOOL     = 2;
	public static final int LID_STRING_HELP     = 3;
	public static final int LID_STRING_ABOUT    = 4;
	public static final int LID_STRING_OPTIONS  = 5;
	public static final int LID_STRING_OPEN     = 6;
	public static final int LID_STRING_LOGIN    = 7;
	public static final int LID_STRING_CLOSE    = 8;
	public static final int LID_STRING_TEST     = 9;
	public static final int LID_STRING_CANCEL   = 10;
	public static final int LID_STRING_REGISTER = 11;
	
	public static final int LID_STRING_1        = 21;
	public static final int LID_STRING_2        = 22;
	public static final int LID_STRING_3        = 23;
	public static final int LID_STRING_4        = 24;
	public static final int LID_STRING_5        = 25;
	public static final int LID_STRING_6        = 26;
	public static final int LID_STRING_7        = 27;
	public static final int LID_STRING_8        = 28;
	public static final int LID_STRING_9        = 29;
	public static final int LID_STRING_10       = 30;
	public static final int LID_STRING_11       = 31;
	public static final int LID_STRING_12       = 32;
	public static final int LID_STRING_13       = 33;
	public static final int LID_STRING_14       = 34;
	public static final int LID_STRING_15       = 35;
	public static final int LID_STRING_16       = 36;
	
	private static final int LID_MAX_COUNT      = 37;
	
	// �����ַ���
	static final String CN_STRING_EXIT    = "�˳�";	
	static final String CN_STRING_FILE    = "�ļ�";	
	static final String CN_STRING_TOOL    = "����";
	static final String CN_STRING_HELP    = "����";
	static final String CN_STRING_ABOUT   = "����";
	static final String CN_STRING_OPTIONS = "ѡ��";
	static final String CN_STRING_OPEN    = "����";
	static final String CN_STRING_CLOSE   = "�ر�����";
	static final String CN_STRING_LOGIN   = "��¼";
	static final String CN_STRING_TEST    = "����";
	static final String CN_STRING_CANCEL  = "ȡ��";
	static final String CN_STRING_REGISTER= "ע��";

	static final String CN_STRING_1       = "��������IP��";
	static final String CN_STRING_2       = "�����˿ڣ�";
	static final String CN_STRING_3       = "���ڽ�������";
	static final String CN_STRING_4       = "���Ӳ�������  ...";
	static final String CN_STRING_5       = "����";
	static final String CN_STRING_6       = "����ⲿ����";
	static final String CN_STRING_7       = "��ԭʼ������ʾ";
	static final String CN_STRING_8       = "Ԥ�����ý���";
	static final String CN_STRING_9       = "���治����";
	static final String CN_STRING_10      = "���ļ�ʧ�ܣ�";
	
	static final String CN_STRING_11      = "��������Ų�Ʒ���ṩ�����ַ������Ի�ȡע���룺";
	static final String CN_STRING_12      = "ע������֤�ɹ�������������";
	static final String CN_STRING_13      = "ע������֤�������������ע���롣";
	static final String CN_STRING_14      = "��Ʒ��Կ��";
	static final String CN_STRING_15      = "��������ģʽ";
	static final String CN_STRING_16      = "����";
	
	// Ӣ���ַ���
	static final String EN_STRING_EXIT       = "Exit";	
	static final String EN_STRING_FILE       = "File";
	static final String EN_STRING_TOOL       = "Tool";
	static final String EN_STRING_HELP       = "Help";
	static final String EN_STRING_ABOUT      = "About";
	static final String EN_STRING_OPTIONS    = "Options";
	static final String EN_STRING_OPEN       = "Open";
	static final String EN_STRING_CLOSE      = "Close";
	static final String EN_STRING_LOGIN      = "Login";
	static final String EN_STRING_TEST       = "Test";
	static final String EN_STRING_CANCEL     = "Cancel";
	static final String EN_STRING_REGISTER   = "Register";
	
	static final String EN_STRING_1       = "Server IP:";
	static final String EN_STRING_2       = "Port:";
	static final String EN_STRING_3       = "Connecting";
	static final String EN_STRING_4       = "Connect setting ...";
	static final String EN_STRING_5       = "Save";
	static final String EN_STRING_6       = "Preview External UI";
	static final String EN_STRING_7       = "Origin Ratio";
	static final String EN_STRING_8       = "Preview Internal UI";
	static final String EN_STRING_9       = "Load UI failed";
	static final String EN_STRING_10      = "Failed to open file :";
	static final String EN_STRING_11      = "by now, it is not translate";
	static final String EN_STRING_12      = "by now, it is not translate";
	static final String EN_STRING_13      = "by now, it is not translate";
	static final String EN_STRING_14      = "by now, it is not translate";
	static final String EN_STRING_15      = "Exit Preview";
	static final String EN_STRING_16      = "Search";
	
	static String m_language[] = new String[LID_MAX_COUNT];
	static void Add(int ix, String strText)
	{
		if(ix < 0 || ix >= LID_MAX_COUNT)
			return;

		m_language[ix] = strText;
	}
	
	static void LoadExternal_ZhCn()
	{
		Add(LID_STRING_EXIT,     CN_STRING_EXIT);
		Add(LID_STRING_FILE,     CN_STRING_FILE);
		Add(LID_STRING_TOOL,     CN_STRING_TOOL);
		Add(LID_STRING_HELP,     CN_STRING_HELP);
		Add(LID_STRING_ABOUT,    CN_STRING_ABOUT);
		Add(LID_STRING_OPTIONS,  CN_STRING_OPTIONS);
		Add(LID_STRING_OPEN,     CN_STRING_OPEN);
		Add(LID_STRING_CLOSE,    CN_STRING_CLOSE);
		Add(LID_STRING_TEST,     CN_STRING_TEST);
		Add(LID_STRING_CANCEL,   CN_STRING_CANCEL);
		Add(LID_STRING_REGISTER, CN_STRING_REGISTER);
		
		Add(LID_STRING_LOGIN,    CN_STRING_LOGIN);
		Add(LID_STRING_1,        CN_STRING_1);
		Add(LID_STRING_2,        CN_STRING_2);
		Add(LID_STRING_3,        CN_STRING_3);
		Add(LID_STRING_4,        CN_STRING_4);
		Add(LID_STRING_5,        CN_STRING_5);
		Add(LID_STRING_6,        CN_STRING_6);
		Add(LID_STRING_7,        CN_STRING_7);
		Add(LID_STRING_8,        CN_STRING_8);
		Add(LID_STRING_9,        CN_STRING_9);
		Add(LID_STRING_10,       CN_STRING_10);
		
		Add(LID_STRING_11,       CN_STRING_11);
		Add(LID_STRING_12,       CN_STRING_12);
		Add(LID_STRING_13,       CN_STRING_13);
		Add(LID_STRING_14,       CN_STRING_14);
		Add(LID_STRING_15,       CN_STRING_15);
		Add(LID_STRING_16,       CN_STRING_16);
	}
	static void LoadExternal_EnUs()
	{
		Add(LID_STRING_EXIT,     EN_STRING_EXIT);
		Add(LID_STRING_FILE,     EN_STRING_FILE);
		Add(LID_STRING_TOOL,     EN_STRING_TOOL);
		Add(LID_STRING_HELP,     EN_STRING_HELP);
		Add(LID_STRING_ABOUT,    EN_STRING_ABOUT);
		Add(LID_STRING_OPTIONS,  EN_STRING_OPTIONS);
		Add(LID_STRING_OPEN,     EN_STRING_OPEN);
		Add(LID_STRING_CLOSE,    EN_STRING_CLOSE);
		Add(LID_STRING_LOGIN,    EN_STRING_LOGIN);
		Add(LID_STRING_TEST,     EN_STRING_TEST);
		Add(LID_STRING_CANCEL,   EN_STRING_CANCEL);
		Add(LID_STRING_REGISTER, EN_STRING_REGISTER);
		
		Add(LID_STRING_1,        EN_STRING_1);
		Add(LID_STRING_2,        EN_STRING_2);
		Add(LID_STRING_3,        EN_STRING_3);
		Add(LID_STRING_4,        EN_STRING_4);
		Add(LID_STRING_5,        EN_STRING_5);
		Add(LID_STRING_6,        EN_STRING_6);
		Add(LID_STRING_7,        EN_STRING_7);
		Add(LID_STRING_8,        EN_STRING_8);
		Add(LID_STRING_9,        EN_STRING_9);
		Add(LID_STRING_10,       EN_STRING_10);
		
		Add(LID_STRING_11,       EN_STRING_11);
		Add(LID_STRING_12,       EN_STRING_12);
		Add(LID_STRING_13,       EN_STRING_13);
		Add(LID_STRING_14,       EN_STRING_14);
		Add(LID_STRING_15,       EN_STRING_15);
		Add(LID_STRING_16,       EN_STRING_16);
	}
	
	static boolean LoadExternal(int langId)
	{
		switch(langId)
		{
		case LANG_EN_US :LoadExternal_EnUs(); break;
		case LANG_ZH_CN :LoadExternal_ZhCn(); break;
		}
		return true;
	}
	
	public static String Get(int ix)
	{
		if(ix < 0 || ix >= LID_MAX_COUNT)
			return "";
		
		return m_language[ix];
	}
	
	public static boolean Switch(int langId)
	{
		return LoadExternal(langId);
	}
	
	static{
		Switch(Utils.IsLinuxOS() ? LANG_EN_US:LANG_ZH_CN);
	}
}
