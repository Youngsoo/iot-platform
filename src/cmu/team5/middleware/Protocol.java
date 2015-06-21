package cmu.team5.middleware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.*;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

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
	
	public static String generateUnregisterMsg(String serialStr) {
		JSONObject json = new JSONObject();
		json.put("messageType", "unregister");
		json.put("serial", serialStr);
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
		
		ArrayList array = new ArrayList();
		for (Iterator<String> it = nodeList.iterator(); it.hasNext();) {
			HashMap map = new HashMap();
			map.put("nodeId", it.next());
			array.add(map);
		}
		if (array.size() > 0)
			json.put("node", array);
		
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
