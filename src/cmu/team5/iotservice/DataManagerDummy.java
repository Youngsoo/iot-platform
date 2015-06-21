package cmu.team5.iotservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataManagerDummy implements DataManagerIF
{
	//private ArrayList<String> registeredNodeList;
	private HashMap<String, HashMap> nodeInfo;
	
	public DataManagerDummy()
	{
		//registeredNodeList = new ArrayList<String>();
		nodeInfo = new HashMap<String, HashMap>();
		
		HashMap<String, String> sensorInfo = new HashMap();
		nodeInfo.put("a2de", sensorInfo);
	}
	
	public Boolean saveLog(String nodeId, String sensorType, String value)
	{
		System.out.println("[LOG] nodeId:" + nodeId + ", sensorType:" + sensorType + ", value:" + value);
		
		if (nodeInfo.containsKey(nodeId)) {
			HashMap<String, String>sensorInfo = nodeInfo.get(nodeId);
			sensorInfo.put(sensorType, value);
		}
		
		return true;
	}

	public boolean isValidLogin(String userId, String passwd)
	{
		if (userId.equals("tony") && passwd.equals("lge123"))
			return true;
		
		return false;
	}
	
	public String getLoginErrMsg(String userId, String passwd)
	{
		String reason = "No error.";
		if (!userId.equals("tony")) reason = new String("No matching user id.");
		if (!passwd.equals("lge123")) reason = new String("Bad password.");
		return reason;
	}
	
	public void addRegisteredNode(String nodeId)
	{
		if (!nodeInfo.containsKey(nodeId)) {
			HashMap<String, String> sensorInfo = new HashMap();
			nodeInfo.put(nodeId, sensorInfo);
		}
	}
	
	public ArrayList<String> getRegisteredNode()
	{
		ArrayList<String>list = new ArrayList<String>();
		Iterator it = nodeInfo.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry node = (Map.Entry)it.next();
			list.add((String) node.getKey());
		}
		
		return list;
	}
	
	public HashMap<String, String> getNodeInfo(String nodeId)
	{
		HashMap info = new HashMap<String, String>();
		if (nodeInfo.containsKey(nodeId)) {
			HashMap<String, String> sensorInfo = nodeInfo.get(nodeId);
			info = new HashMap<String, String>(sensorInfo);
		}
		return info;
	}
}