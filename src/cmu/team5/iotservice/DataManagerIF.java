package cmu.team5.iotservice;

import java.util.ArrayList;
import java.util.HashMap;

public interface DataManagerIF
{
	public Boolean saveLog(String nodeId, String sensorType, String value);
	public boolean isValidLogin(String userId, String passwd);
	public String getLoginErrMsg(String userId, String passwd);
	public ArrayList<String> getRegisteredNode();
	public void addRegisteredNode(String nodeId);
	public HashMap<String, String> getNodeInfo(String nodeId);
}
