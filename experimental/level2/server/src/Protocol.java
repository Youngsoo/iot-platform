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
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		String value = json.get("userid").toString();
		return value;
	}
	
	public static String getDeviceType(String msg)
	{
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		String value = json.get("devicetype").toString();
		return value;
	}

	public static String getMessageType(String msg)
	{
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		String value = json.get("msgtype").toString();
		return value;
	}

	public static String getNodeId(String msg)
	{
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		String value = json.get("nodeid").toString();
		return value;
	}

	public static String getSensorType(String msg)
	{
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		String value = json.get("sensortype").toString();
		return value;
	}

	public static String getSensorValue(String msg)
	{
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		String value = json.get("value").toString();
		return value;
	}
}
