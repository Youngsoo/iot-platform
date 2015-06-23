package cmu.team5.middleware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.*;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Protocol {
	public static String generateTerminalInitMsg(String userId)
	{
		JSONObject json = new JSONObject();
		json.put("deviceType", "terminal");
		json.put("userId", userId);
		String message = json.toString();
		return message;
	}
	
	public static String generateCommandMsg(String nodeId, String sensorType, String sensorValue)
	{
		JSONObject json = new JSONObject();
		json.put("messageType", "command");
		json.put("nodeId", nodeId);
		json.put("actuatorType", sensorType);
		json.put("value", sensorValue);
		String message = json.toString();
		return message;
	}
	
	public static String generateRegisterMsg(String serverIp, String serialStr)
	{
		JSONObject json = new JSONObject();
		json.put("messageType", "register");
		json.put("serverIp", serverIp);
		json.put("serial", serialStr);
		String message = json.toString();
		return message;
	}
	
	public static String generateUnregisterMsg() {
		JSONObject json = new JSONObject();
		json.put("messageType", "unregister");
		String message = json.toString();
		return message;
	}
	
	public static String generateUnregisterNodeMsg(String NodeId) {
		JSONObject json = new JSONObject();
		json.put("messageType", "unregister");
		json.put("nodeId", NodeId);
		String message = json.toString();
		return message;
	}
	
	public static String generateResultMsg(String messageType, boolean result, String reason)
	{
		JSONObject json = new JSONObject();
		json.put("messageType", messageType);
		if (result) json.put("result", "success");
		else json.put("result",  "fail");
		if (reason != null) json.put("reason", reason);
		String message = json.toString();
		return message;
	}
	
	public static String generateLoginResultMsg(String userId, boolean result, String reason)
	{
		JSONObject json = new JSONObject();
		json.put("messageType", "login");
		json.put("userId", userId);
		if (result) json.put("result", "success");
		else json.put("result",  "fail");
		if (reason != null) json.put("reason", reason);
		String message = json.toString();
		return message;
	}
	
	public static String generateLoginMsg(String userId, String passwd)
	{
		JSONObject json = new JSONObject();
		json.put("messageType", "login");
		json.put("deviceType", "terminal");
		json.put("userId", userId);
		json.put("passwd", passwd);
		String message = json.toString();
		return message;
	}
	
	public static String generateRegisteredNodeMsg(ArrayList nodeList)
	{
		JSONObject json = new JSONObject();
		json.put("messageType", "nodeRegistered");
		/*
		ArrayList array = new ArrayList();
		for (Iterator<String> it = nodeList.iterator(); it.hasNext();) {
			HashMap map = new HashMap();
			map.put("nodeId", it.next());
			array.add(map);
		}
		if (array.size() > 0)
			json.put("nodeList", array);
		*/
		if (nodeList.size() > 0)
			json.put("nodeList", nodeList);
		
		String message = json.toString();
		return message;
	}
	
	/*
	public static String generateRegisterResultMsg(boolean result, String reason)
	{
		JSONObject json = new JSONObject();
		json.put("messageType", "register");
		if (result) json.put("result", "success");
		else json.put("result",  "fail");
		if (reason != null) json.put("reason", reason);
		String message = json.toString();
		return message;
	}
	*/
	
	public static String generateNodeInfoMsg(String NodeId)
	{
		JSONObject json = new JSONObject();
		json.put("messageType", "nodeStatus");
		json.put("nodeId", NodeId);
		String message = json.toString();
		return message;
		
	}
	
	public static String generateNodeStatusResultMsg(String nodeId, HashMap sensorInfo, HashMap actuatorInfo)
	{
		JSONObject json = new JSONObject();
		json.put("messageType", "nodeStatus");
		json.put("nodeId", nodeId);
		
		if (sensorInfo.size() > 0)
			json.put("sensor", sensorInfo);
		
		if (actuatorInfo.size() > 0)
			json.put("actuator", actuatorInfo);
		
		String message = json.toString();
		return message;
	}
	
	public static String generateLogInfoMsg(){
		JSONObject json = new JSONObject();
		json.put("messageType", "logData");
		String message = json.toString();
		return message;
	}

	public static String generateRegisteredNodeMsg(){
		JSONObject json = new JSONObject();
		json.put("messageType", "nodeRegistered");
		
		String message = json.toString();
		return message;
	}
	
	public static String generateLogDataMsg(ArrayList logList)
	{
		final int MAXLOGSIZE = 128;
		JSONObject json = new JSONObject();
		json.put("messageType", "logData");
		int logCnt = 0;
		
		ArrayList array = new ArrayList();
		for (Iterator<LogData> it = logList.iterator(); it.hasNext();) {
			HashMap map = new HashMap();
			LogData logData = it.next();
			
			map.put("nodeId", logData.nodeId);
			map.put("logType", logData.type);
			map.put("time", logData.time);
			map.put("name", logData.name);
			map.put("value", logData.value);			
			
			array.add(map);
			
			if (++logCnt > MAXLOGSIZE) break;
		}
		
		if (array.size() > 0)
			json.put("log", array);
		String message = json.toString();
		return message;
	}
	
	
	public static String generateConfigDataMsg(String NodeId, String Type, String Value)
	{
		JSONObject json = new JSONObject();
		json.put("messageType", "configurableTime");
		json.put("nodeId", NodeId);
		json.put("configurableType", Type);
		json.put("time", Value);
		json.put("unit", "sec");
		String message = json.toString();
		return message;
	}

	public static String getUserId(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("userId") != null)
			value = json.get("userId").toString();
		return value;
	}
	
	
	
	
	
	public static String getDeviceType(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("deviceType") != null)
			value = json.get("deviceType").toString();
		return value;
	}	
	
	
	public static List<HashMap<String,String>> getLogsData(String msg)
	{	List<HashMap<String,String>> Log = new ArrayList<HashMap<String, String>>();	
		String value = null;
		JSONObject json1 = (JSONObject)JSONValue.parse(msg);
		if (json1.get("log") != null){
			value = json1.get("log").toString();
			JSONArray DataArray = (JSONArray) json1.get("log");
			Iterator<JSONObject> iterator = DataArray.iterator();
			while (iterator.hasNext()) {
				HashMap<String, String> LogInfo = new HashMap<String, String>();
				JSONObject json2 = (JSONObject) iterator.next();
				System.out.println(json2.get("nodeId") + " " + json2.get("logType"));
				if (json2.get("nodeId") != null) LogInfo.put("nodeId", json2.get("nodeId").toString());
				if (json2.get("logType") != null) LogInfo.put("logType", json2.get("logType").toString());
				if (json2.get("time") != null) LogInfo.put("time", json2.get("time").toString());
				if (json2.get("name") != null) LogInfo.put("name", json2.get("name").toString());
				if (json2.get("value") != null) LogInfo.put("value", json2.get("value").toString());
				Log.add(LogInfo);
			}
		}	
		return Log;
	}

	public static HashMap<String,String> getActuratorInfo(String msg)
	{
	
		HashMap<String, String> ActuratorInfo = new HashMap<String, String>();
		ContainerFactory containerFactory = new ContainerFactory(){
		    public LinkedList creatArrayContainer() {
		      return new LinkedList();
		    }

		    public Map createObjectContainer() {
		      return new LinkedHashMap();
		    }
		                        
		  };
		
		String value = null;
		JSONObject json1 = (JSONObject)JSONValue.parse(msg);
		if (json1.get("actuator") != null){
			value = json1.get("actuator").toString();
			System.out.println(value);
			try{
				JSONParser parser = new JSONParser();
			    Map json = (Map)parser.parse(value, containerFactory);
			    Iterator iter = json.entrySet().iterator();
			    while(iter.hasNext()){
			      Map.Entry entry = (Map.Entry)iter.next();
			      ActuratorInfo.put((String)entry.getKey(), (String)entry.getValue());
			    }                        
			  }
			  catch(ParseException pe){
			    System.out.println(pe);
			  }
		
		}
		return ActuratorInfo;
	}
	
	public static HashMap<String,String> getSensorInfo(String msg)
	{
	
		HashMap<String, String> SensorInfo = new HashMap<String, String>();
		ContainerFactory containerFactory = new ContainerFactory(){
		    public LinkedList creatArrayContainer() {
		      return new LinkedList();
		    }

		    public Map createObjectContainer() {
		      return new LinkedHashMap();
		    }
		                        
		  };
		
		String value = null;
		JSONObject json1 = (JSONObject)JSONValue.parse(msg);
		if (json1.get("sensor") != null){
			value = json1.get("sensor").toString();
		
			try{
				JSONParser parser = new JSONParser();
			    Map json = (Map)parser.parse(value, containerFactory);
			    Iterator iter = json.entrySet().iterator();
			    while(iter.hasNext()){
			      Map.Entry entry = (Map.Entry)iter.next();
			      SensorInfo.put((String)entry.getKey(), (String)entry.getValue());
			    }                        
			  }
			  catch(ParseException pe){
			    System.out.println(pe);
			  }
		
		}
		return SensorInfo;
	}

	public static String getMessageType(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("messageType") != null)
			value = json.get("messageType").toString();
		return value;
	}
	
	
	public static String getMsgInformation(String msg){
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("messageType") != null){
			value = json.get("messageType").toString();
			if (value.equals("information")){
				value = json.get("contents").toString();
			}
		}
		return value;
	}
	
	public static String getMsgEmergency(String msg){
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("messageType") != null){
			value = json.get("messageType").toString();
			if (value.equals("emergency")){
				value = json.get("contents").toString();
			}
		}
		return value;
	}
	
	
	
	
	public static String getResult(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("result") != null)
			value = json.get("result").toString();
		return value;
	}
	
	public static String[] getNodeList(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("nodeList") != null){
			value = json.get("nodeList").toString();
			value = value.substring(1);
			value = value.substring(0, value.length()-1);
			value = value.replaceAll("[\"]", "");
		
			String[] NodeList = value.split(",");
			return NodeList;
		}
		return null;
	}

	public static String getNodeId(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("nodeId") != null)
			value = json.get("nodeId").toString();
		return value;
	}

	public static String getSensorType(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("sensorType") != null)
			value = json.get("sensorType").toString();
		return value;
	}

	public static String getSensorValue(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("value") != null)
			value = json.get("value").toString();
		return value;
	}
	
	public static String getSerial(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("serial") != null)
			value = json.get("serial").toString();
		return value;
	}
	
	public static String getNodeName(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("nodeName") != null)
			value = json.get("nodeName").toString();
		return value;
	}
	
	public static String getPasswd(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("passwd") != null)
			value = json.get("passwd").toString();
		return value;
	}
	
	public static String getActuatorType(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("actuatorType") != null)
			value = json.get("actuatorType").toString();
		return value;
	}
	
	public static String getValue(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("value") != null)
			value = json.get("value").toString();
		return value;
	}

}
