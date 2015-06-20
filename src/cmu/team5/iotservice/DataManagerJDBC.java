package cmu.team5.iotservice;

public class DataManagerJSON implements DataManagerIF
{
	public Boolean saveLog(String nodeId, String sensorType, String value)
	{
		System.out.println("[LOG] nodeId:" + nodeId + " sensorType:" + sensorType + " value:" + value);
		return true;
	}

	public boolean isValidLogin(String userId, String passwd)
	{
		if (userId.equals("tony") && passwd.equals("lge123"))
			return true;
		
		return false;
	}
	
	public String getLoginErrMsg(String userId, String passwd)
	{
		String reason = "No error.";
		if (!userId.equals("tony")) reason = new String("No matching user id.");
		if (!passwd.equals("lge123")) reason = new String("Bad password.");
		return reason;
	}
}
