package cmu.team5.iotservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cmu.team5.middleware.*;

public class DataManagerDummy implements DataManagerIF
{
	private class NodeInfo {
		String nodeType;
		HashMap<String, String> sensorInfo = new HashMap();
		HashMap<String, String> actuatorInfo = new HashMap();
	}
	
	private HashMap<String, NodeInfo> nodeInfoList;
	private ArrayList<LogData> logList;
	
	private static final int MAXLOGSIZE = 256;
	
	public DataManagerDummy()
	{
		nodeInfoList = new HashMap<String, NodeInfo>();
		logList = new ArrayList<LogData>(MAXLOGSIZE);
		
		NodeInfo nodeInfo = new NodeInfo();
		nodeInfo.actuatorInfo.put("light", "off");
		nodeInfo.actuatorInfo.put("alarm", "off");
		nodeInfo.actuatorInfo.put("door", "close");
		nodeInfoList.put("a2de", nodeInfo);
	}
	
	public boolean saveSensorLog(String nodeId, String sensorType, String value)
	{
		System.out.println("[LOG] nodeId:" + nodeId + ", sensorType:" + sensorType + ", value:" + value);
		
		if (nodeInfoList.containsKey(nodeId)) {
			NodeInfo nodeInfo = nodeInfoList.get(nodeId);
			nodeInfo.sensorInfo.put(sensorType, value);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = dateFormat.format(new Date());
		LogData logData = new LogData(nodeId, "sensor", time, sensorType, value);
		logList.add(logData);
		
		return true;
	}
	
	public boolean saveActuatorLog(String nodeId, String actuatorType, String value)
	{
		System.out.println("[LOG] nodeId:" + nodeId + ", actuatorType:" + actuatorType + ", value:" + value);
		
		if (nodeInfoList.containsKey(nodeId)) {
			NodeInfo nodeInfo = nodeInfoList.get(nodeId);
			nodeInfo.actuatorInfo.put(actuatorType, value);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = dateFormat.format(new Date());
		System.out.println("time :" + time);
		LogData logData = new LogData(nodeId, "actuator", time, actuatorType, value);
		logList.add(logData);
		
		return true;
	}
	
	public boolean saveCommandLog(String nodeId, String actuatorType, String value)
	{
		System.out.println("[LOG] nodeId:" + nodeId + ", actuatorType:" + actuatorType + ", value:" + value);
		
		if (nodeInfoList.containsKey(nodeId)) {
			NodeInfo nodeInfo = nodeInfoList.get(nodeId);
			nodeInfo.actuatorInfo.put(actuatorType, value);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = dateFormat.format(new Date());
		System.out.println("time :" + time);
		LogData logData = new LogData(nodeId, "command", time, actuatorType, value);
		logList.add(logData);
		
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
	
	public void addRegisteredNode(String nodeId, String nodeName)
	{
		if (!nodeInfoList.containsKey(nodeId)) {
			NodeInfo nodeInfo = new NodeInfo();
			nodeInfo.nodeType = nodeName;
			if (nodeName.equals("homesecurity")) {
				nodeInfo.actuatorInfo.put("light", "off");
				nodeInfo.actuatorInfo.put("alarm", "off");
				nodeInfo.actuatorInfo.put("door", "close");
			}
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
	
	public final ArrayList<LogData> getLogDataAll()
	{
		return new ArrayList<LogData>(logList);
	}
	
	public boolean isRegisteredNode(String nodeId)
	{
		if (nodeInfoList.containsKey(nodeId)) return true;
		else return false;
	}
}