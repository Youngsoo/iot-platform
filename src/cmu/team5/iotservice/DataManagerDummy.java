package cmu.team5.iotservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataManagerDummy implements DataManagerIF
{
	private class NodeInfo {
		HashMap<String, String> sensorInfo = new HashMap();
		HashMap<String, String> actuatorInfo = new HashMap();
	}

	private HashMap<String, NodeInfo> nodeInfoList;
	
	public DataManagerDummy()
	{
		nodeInfoList = new HashMap<String, NodeInfo>();
		
		NodeInfo nodeInfo = new NodeInfo();
		nodeInfo.actuatorInfo.put("light", "off");
		nodeInfo.actuatorInfo.put("alarm", "off");
		nodeInfo.actuatorInfo.put("door", "close");
		nodeInfoList.put("a2de", nodeInfo);
	}
	
	public Boolean saveSensorLog(String nodeId, String sensorType, String value)
	{
		System.out.println("[LOG] nodeId:" + nodeId + ", sensorType:" + sensorType + ", value:" + value);
		
		if (nodeInfoList.containsKey(nodeId)) {
			NodeInfo nodeInfo = nodeInfoList.get(nodeId);
			nodeInfo.sensorInfo.put(sensorType, value);
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
		if (!nodeInfoList.containsKey(nodeId)) {
			NodeInfo nodeInfo = new NodeInfo();
			nodeInfoList.put(nodeId, nodeInfo);
		}
	}
	
	public void removeRegisteredNode(String nodeId)
	{
		nodeInfoList.remove(nodeId);
	}
	
	public ArrayList<String> getRegisteredNode()
	{
		ArrayList<String>list = new ArrayList<String>();
		Iterator it = nodeInfoList.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry node = (Map.Entry)it.next();
			list.add((String) node.getKey());
		}
		
		return list;
	}
	
	public HashMap<String, String> getNodeSensorInfo(String nodeId)
	{
		HashMap info = new HashMap<String, String>();
		if (nodeInfoList.containsKey(nodeId)) {
			NodeInfo nodeInfo = nodeInfoList.get(nodeId);
			info = new HashMap<String, String>(nodeInfo.sensorInfo);
		}
		return info;
	}
	
	public HashMap<String, String> getNodeActuatorInfo(String nodeId)
	{
		HashMap info = new HashMap<String, String>();
		if (nodeInfoList.containsKey(nodeId)) {
			NodeInfo nodeInfo = nodeInfoList.get(nodeId);
			info = new HashMap<String, String>(nodeInfo.actuatorInfo);
		}
		return info;
	}
	
	public boolean isRegisteredNode(String nodeId)
	{
		if (nodeInfoList.containsKey(nodeId)) return true;
		else return false;
	}
}