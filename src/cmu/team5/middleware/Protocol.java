package cmu.team5.middleware;

import org.json.simple.*;

public class Protocol {
	public static String generateTerminalInitMsg(String userId)
	{
		JSONObject json = new JSONObject();
		json.put("devicetype", "terminal");
		json.put("userid", userId);
		String message = json.toString();
		return message;
	}
	
	public static String generateCommandMsg(String nodeId, String sensorType, String sensorValue)
	{
		JSONObject json = new JSONObject();
		json.put("msgtype", "command");
		json.put("nodeid", nodeId);
		json.put("sensortype", sensorType);
		json.put("value", Integer.parseInt(sensorValue));
		String message = json.toString();
		return message;
	}
	

	public static String getUserId(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("userid") != null)
			value = json.get("userid").toString();
		return value;
	}
	
	public static String getDeviceType(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("devicetype") != null)
			value = json.get("devicetype").toString();
		return value;
	}

	public static String getMessageType(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("msgtype") != null)
			value = json.get("msgtype").toString();
		return value;
	}

	public static String getNodeId(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("nodeid") != null)
			value = json.get("nodeid").toString();
		return value;
	}

	public static String getSensorType(String msg)
	{
		String value = null;
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		if (json.get("sensortype") != null)
			value = json.get("sensortype").toString();
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
}
