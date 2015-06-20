package cmu.team5.iotservice;

public class DataManager
{
	public Boolean saveLog(String nodeId, String sensorType, String value)
	{
		System.out.println("[LOG] nodeId:" + nodeId + " sensorType:" + sensorType + " value:" + value);
		return true;
	}

	public boolean isValidLogin(String userId, String passwd)
	{
		return true;
	}
	
	public String getLoginErrMsg(String usreId, String passwd)
	{
		String reason = "Bad password.";
		return reason;
	}
}