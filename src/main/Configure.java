/* ***************************************************************************

                  		版权所有 (C), 2013-2100

 *****************************************************************************
	  文件名称 : Configure.java
	  作者           : 贾延刚
	  生成日期 : 2013年
	
	  版本           : 1.0
	  功能描述 : 处理程序的配置信息 
	
	  修改历史 :

******************************************************************************/
package main;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
final class FirstLevel
{
	public static final String FIRST_DEVICE  = "Device";
	public static final String FIRST_UI_FILE = "UiFile";
	public static final String FIRST_PATH    = "Path";
	public static final String FIRST_OPTION  = "Option";
}

final class DeviceAttrName
{
	public static final String IP        = "IP";
	public static final String PORT      = "Port";
	public static final String HOSTNAME  = "Hostname";
}
final class UiFileAttrName
{
	public static final String VERSION   = "Version";
	public static final String FILENAME  = "FileName";
}
final class PathAttrName
{
	public static final String EXTERNAL_UI = "ExternalUI";
}

public class Configure 
{
	private String   m_filename      = "";
	
	private String   m_ipAddr        = "192.168.1.100";
	private int      m_ipPort        = 9000;
	private String   m_hostname      = "Unknown";
	private String   m_uiFileVersion = "";
	private String   m_uiFileTitle   = "";
	private String   m_uiBrowse      = "";    // 上次打开的界面目录
	
	public Configure()
	{}
		
	public String GetIpAddress()
	{
		return m_ipAddr;
	}
	public int GetIpPort()
	{
		return m_ipPort;
	}
	public String GetHostname()
	{
		return m_hostname;
	}
	public boolean SetIpAddress(String ipAddr, int port, String hostname)
	{
		m_ipAddr = ipAddr;
		m_ipPort = port;
		m_hostname = hostname;
		return true;
	}
		
	public String GetFileVersion()
	{
		return m_uiFileVersion;
	}
	public String GetFileTitle()
	{
		return m_uiFileTitle;
	}
	public boolean SaveFileInfo(String fileTitle, String fileVersion)
	{
		m_uiFileTitle = fileTitle;
		m_uiFileVersion = fileVersion;
		return true; 
	}
	
	public String GetExternalUiPath()
	{
		return m_uiBrowse; 
	}
	public boolean SaveExternalUiPath(String strPath)
	{
		m_uiBrowse = strPath;
		return true; 
	}
		
	public boolean LoadFromXml(String filename)
	{
		m_filename = filename;
		File f = new File(filename);
		return parseXmlStream(f);
	}
	
	/**
	 * 解析配置XML文件
	 * @param xmlInputStream XML文件流
	 * @return 是否解析成功
	 */
	private boolean parseXmlStream(File xmlInput)
	{
		boolean bRetValue = true;		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try 
		{
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			Document document = docBuilder.parse(xmlInput);
			
			if(document!=null)
			{
				Element rootElement=document.getDocumentElement();
				if(rootElement!=null)
				{	
					bRetValue = parseXmlRootElement(rootElement);
				}
			}
			//xmlInputStream.close();
		} 
		//catch (ParserConfigurationException e){} 
		//catch (SAXException e) {}
		//catch (IOException e) {}
		catch(Exception ex){}
		
		//System.out.printf("m_uiFileVersion :%s\n",  m_uiFileVersion);
		//System.out.printf("m_uiFileTitle :%s\n",  m_uiFileTitle);
		return bRetValue;
	}
	
	private boolean parseXmlRootElement(Element rootElement)
	{
		if(null == rootElement)
			return false;
				
		// page
		Node childNode = rootElement.getFirstChild();
		while(childNode != null)
		{
			//System.out.println(childNode.getNodeName());
			if(childNode.getNodeName().equalsIgnoreCase(FirstLevel.FIRST_DEVICE))	
				ParseNodeDevice(childNode);
			else if(childNode.getNodeName().equalsIgnoreCase(FirstLevel.FIRST_UI_FILE))
				ParseNodeUiFile(childNode);
			else if(childNode.getNodeName().equalsIgnoreCase(FirstLevel.FIRST_PATH))
				ParseNodePath(childNode);
			else if(childNode.getNodeName().equalsIgnoreCase(FirstLevel.FIRST_OPTION))
			{
				ParseNodeOption(childNode);
			}
			
			childNode = childNode.getNextSibling();
		}
		return true;
	}
	
	boolean ParseNodeOption(Node optNode)
	{
		return false;
	}
	boolean ParseNodePath(Node pathNode)
	{
		if(null == pathNode)
			return false;
		
		NamedNodeMap nameMap = pathNode.getAttributes();
		if(null == nameMap)
			return false;
		
		Node attrNode; 
		for(int k = 0; k < nameMap.getLength(); ++k)
		{
			attrNode = nameMap.item(k);
			if(attrNode.getNodeName().equalsIgnoreCase(PathAttrName.EXTERNAL_UI))
				m_uiBrowse = attrNode.getNodeValue();
		}
		return true;
	}
	boolean ParseNodeUiFile(Node filNode)
	{
		if(null == filNode)
			return false;
		
		NamedNodeMap nameMap = filNode.getAttributes();
		if(null == nameMap)
			return false;
		
		Node attrNode; 
		for(int k = 0; k < nameMap.getLength(); ++k)
		{
			attrNode = nameMap.item(k);
			if(attrNode.getNodeName().equalsIgnoreCase(UiFileAttrName.VERSION))
				m_uiFileVersion = attrNode.getNodeValue();
			else if(attrNode.getNodeName().equalsIgnoreCase(UiFileAttrName.FILENAME))
				m_uiFileTitle = attrNode.getNodeValue();
		}
		return true;
	}
	
	boolean ParseNodeDevice(Node devNode)
	{
		if(null == devNode)
			return false;
		
		NamedNodeMap nameMap = devNode.getAttributes();
		if(null == nameMap)
			return false;
		
		Node attrNode; 
		for(int k = 0; k < nameMap.getLength(); ++k)
		{
			attrNode = nameMap.item(k);
			if(attrNode.getNodeName().equalsIgnoreCase(DeviceAttrName.IP))
				m_ipAddr = attrNode.getNodeValue();
			else if(attrNode.getNodeName().equalsIgnoreCase(DeviceAttrName.PORT))
				m_ipPort = Integer.parseInt(attrNode.getNodeValue());
			if(attrNode.getNodeName().equalsIgnoreCase(DeviceAttrName.HOSTNAME))
				m_hostname = attrNode.getNodeValue();
		}
		return true;
	}
	
	public boolean Save()
	{
		Document doc=null;
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder builder=factory.newDocumentBuilder();
            doc=builder.newDocument();//新建DOM
        }catch(ParserConfigurationException e)
        {
            System.out.println("Parse DOM builder error:");
        }
        
        Element root = createRootElement(doc, "App");
        Element device = createElement(root, FirstLevel.FIRST_DEVICE, "");
        createAttribute(device, DeviceAttrName.IP,       m_ipAddr);
        createAttribute(device, DeviceAttrName.PORT,     String.format("%d", m_ipPort));
        createAttribute(device, DeviceAttrName.HOSTNAME, m_hostname);
        
        Element uiFile = createElement(root, FirstLevel.FIRST_UI_FILE, "");
        createAttribute(uiFile, UiFileAttrName.VERSION,  m_uiFileVersion);
        createAttribute(uiFile, UiFileAttrName.FILENAME, m_uiFileTitle);
        // path
        Element path = createElement(root, FirstLevel.FIRST_PATH, "");
        createAttribute(path, PathAttrName.EXTERNAL_UI,  m_uiBrowse);
        
        TransformerFactory tfactory=TransformerFactory.newInstance();
        try
        {
            Transformer transformer=tfactory.newTransformer();
            DOMSource source=new DOMSource(doc);
 
            StreamResult result=new StreamResult(new File(m_filename));
           
            transformer.setOutputProperty("encoding","UTF-8");
            transformer.transform(source,result);
           
        }catch(TransformerConfigurationException e)
        {
            //logger.error("Create Transformer error:"+e);
        }catch(TransformerException e)
        {
            //logger.error("Transformer XML file error:"+e);
        }
		return true;
	}
	
	public Element createRootElement(Document doc, String rootTagName)
    {     
        if(doc.getDocumentElement()==null)
        {
        	//System.out.println("create root element '"+rootTagName+"' success.");
            Element root=doc.createElement(rootTagName);
            doc.appendChild(root);
            return root;
        }
        System.out.println("this dom's root element is exist,create fail.");
        return doc.getDocumentElement();
    }
	
	public Element createElement(Element parent, String tagName, String value)
    {
        Document doc=parent.getOwnerDocument();
        Element child=doc.createElement(tagName);
        XmlOper.setElementValue(child,value);
        parent.appendChild(child);
        return child;
    }
	/**
    * 方法名称：createAttribute<p>
    * 方法功能：在parent结点下增加属性 <p>
    * 参数说明：@param parent
    * 参数说明：@param attrName 属性名
    * 参数说明：@param attrValue 属性值<p>
    * 返回：void <p>
    * 作者：luoc
    * 日期：2005-6-22
    **/
    public void createAttribute(Element parent,String attrName,String attrValue)
    {
        XmlOper.setElementAttr(parent,attrName,attrValue);        
    }
}
