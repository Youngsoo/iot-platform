package cmu.team5.iotservice;

import java.util.ArrayList;
import java.util.HashMap;

public interface DataManagerIF
{
	public Boolean saveSensorLog(String nodeId, String sensorType, String value);
	public boolean isValidLogin(String userId, String passwd);
	public String getLoginErrMsg(String userId, String passwd);
	public ArrayList<String> getRegisteredNode();
	public void addRegisteredNode(String nodeId);
	public HashMap<String, String> getNodeSensorInfo(String nodeId);
	public boolean isRegisteredNode(String nodeId);
	public void removeRegisteredNode(String nodeId);
}
