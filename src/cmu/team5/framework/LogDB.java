package cmu.team5.framework;

public class LogDB {
	public static Boolean saveLog(String nodeId, String sensorType, String value)
	{
		System.out.println("[LOG] nodeId:" + nodeId + " sensorType:" + sensorType + " value:" + value);
		return true;
	}
}
