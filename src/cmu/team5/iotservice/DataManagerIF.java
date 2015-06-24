package cmu.team5.iotservice;

import java.util.ArrayList;
import java.util.HashMap;

import cmu.team5.middleware.LogData;

public interface DataManagerIF
{
	public boolean saveSensorLog(String nodeId, String sensorType, String value);
	public boolean saveActuatorLog(String nodeId, String actuatorType, String value);
	public boolean saveCommandLog(String nodeId, String actuatorType, String value);
	public boolean isValidLogin(String userId, String passwd);
	public String getLoginErrMsg(String userId, String passwd);
	public ArrayList<String> getRegisteredNode();
	public void addRegisteredNode(String nodeId, String nodeName);
	public HashMap<String, String> getNodeSensorInfo(String nodeId);
	public HashMap<String, String> getNodeActuatorInfo(String nodeId);
	public boolean isRegisteredNode(String nodeId);
	public void removeRegisteredNode(String nodeId);
	public ArrayList<LogData> getLogDataAll();
	public void setLogConfigTime(int time);
}
