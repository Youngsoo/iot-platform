package cmu.team5.iotservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
	private LinkedList<LogData> logList;
	int logTime;
	
	private static final int DEFAULTLOGTIME = 300; // 5 minute
	
	public DataManagerDummy()
	{
		nodeInfoList = new HashMap<String, NodeInfo>();
		logList = new LinkedList<LogData>();
		logTime = DEFAULTLOGTIME;
		
		NodeInfo nodeInfo = new NodeInfo();
		nodeInfo.actuatorInfo.put("light", "off");
		nodeInfo.actuatorInfo.put("alarm", "off");
		nodeInfo.actuatorInfo.put("door", "close");
		//nodeInfoList.put("a2de", nodeInfo);
		//nodeInfoList.put("a35c", nodeInfo);
	}
	
	public boolean saveSensorLog(String nodeId, String sensorType, String value)
	{
		System.out.println("[LOG] nodeId:" + nodeId + ", sensorType:" + sensorType + ", value:" + value);
		
		if (nodeInfoList.containsKey(nodeId)) {
			NodeInfo nodeInfo = nodeInfoList.get(nodeId);
			nodeInfo.sensorInfo.put(sensorType, value);
		}

		ensureLogData();
		
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String time = dateFormat.format(new Date());
		LogData logData = new LogData(nodeId, "sensor", new Date(), sensorType, value);
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
		
		ensureLogData();

		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String time = dateFormat.format(new Date());
		LogData logData = new LogData(nodeId, "actuator", new Date(), actuatorType, value);
		logList.add(logData);
		
		return true;
	}
	
	public boolean saveCommandLog(String nodeId, String actuatorType, String value)
	{
		System.out.println("[LOG] nodeId:" + nodeId + ", actuatorType:" + actuatorType + ", value:" + value);
		
		/*
		if (nodeInfoList.containsKey(nodeId)) {
			NodeInfo nodeInfo = nodeInfoList.get(nodeId);
			nodeInfo.actuatorInfo.put(actuatorType, value);
		}
		*/
		
		ensureLogData();

		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String time = dateFormat.format(new Date());
		LogData logData = new LogData(nodeId, "command", new Date(), actuatorType, value);
		logList.add(logData);
		
		return true;
	}
	
	private void ensureLogData()
	{
		long configTime = new Date().getTime() - logTime*1000;
		Date configDate = new Date();
		configDate.setTime(configTime);
		
		for (Iterator<LogData> it = logList.iterator(); it.hasNext();) {
			LogData logData = it.next();
			if (logData.time.before(configDate)) {
				it.remove();
			} else {
				break;
			}
		}
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
		ensureLogData();
		return new ArrayList<LogData>(logList);
	}
	
	public boolean isRegisteredNode(String nodeId)
	{
		if (nodeInfoList.containsKey(nodeId)) return true;
		else return false;
	}
	
	public void setLogConfigTime(int time)
	{
		this.logTime = time;
		ensureLogData();
	}
}
