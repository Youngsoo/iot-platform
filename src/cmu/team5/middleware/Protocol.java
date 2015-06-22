package cmu.team5.middleware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
		json.put("sensorType", sensorType);
		json.put("value", Integer.parseInt(sensorValue));
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
	
	
	public static String generateNodeInfoMsg(String NodeId)
	{
		JSONObject json = new JSONObject();
		json.put("messageType", "nodeStatus");
		json.put("nodeId", NodeId);
		String message = json.toString();
		return message;
		
	}
	
	public static String generateNodeStatusResultMsg(String nodeId, HashMap sensorInfo)
	{
		JSONObject json = new JSONObject();
		json.put("messageType", "nodeStatus");
		json.put("nodeId", nodeId);
		
		if (sensorInfo.size() > 0)
			json.put("sensor", sensorInfo);
		
		String message = json.toString();
		return message;
	}

	public static String generateRegisteredNodeMsg(){
		JSONObject json = new JSONObject();
		json.put("messageType", "nodeRegistered");
		
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

}
