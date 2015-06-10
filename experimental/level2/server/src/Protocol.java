import org.json.simple.*;

public class Protocol {
	public static String generateMessage(String msgType, String nodeId, String sensorType, String sensorValue)
	{
		String message = null;
		return message;
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
