package cmu.team5.middleware;

import org.json.simple.*;

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
		json.put("msgType", "command");
		json.put("nodeId", nodeId);
		json.put("sensorType", sensorType);
		json.put("value", Integer.parseInt(sensorValue));
		String message = json.toString();
		return message;
	}
	
	public static String generateRegisterMsg(String serverIp, String serialStr)
	{
		JSONObject json = new JSONObject();
		json.put("msgType", "register");
		json.put("serverIp", serverIp);
		json.put("serial", serialStr);
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
		if (json.get("msgType") != null)
			value = json.get("msgType").toString();
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
}
