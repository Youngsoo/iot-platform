import org.json.simple.*;

public class Protocol {
	public static String generateMessage(String msgType, String nodeId, String sensorType, String sensorValue)
	{
		String message = null;
		return message;
	}

	public static String getMessageType(String msg)
	{
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		String msgType = (String)json.get("msgtype");
		return msgType;
	}

	public static String getNodeId(String msg)
	{
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		String nodeId = (String)json.get("nodeid");
		return nodeId;
	}

	public static String getSensorType(String msg)
	{
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		String sensorType = (String)json.get("sensortype");
		return sensorType;
	}

	public static long getSensorValue(String msg)
	{
		JSONObject json = (JSONObject)JSONValue.parse(msg);
		long sensorValue = (long)json.get("value");
		return sensorValue;
	}
}
