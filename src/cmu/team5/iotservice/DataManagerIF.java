package cmu.team5.iotservice;

public interface DataManagerIF
{
	public Boolean saveLog(String nodeId, String sensorType, String value);
	public boolean isValidLogin(String userId, String passwd);
	public String getLoginErrMsg(String userId, String passwd);
}
