package ccc_ui.ui;

import java.util.ArrayList;
import java.util.List;

public class UiActionGroup 
{
	public List<UiAction>  m_xmlActionList;
	
	public UiActionGroup()
	{
		m_xmlActionList = new ArrayList<UiAction>();
	}
	
	public boolean AddAction(UiAction action)
	{
		return m_xmlActionList.add(action);
	}
}
