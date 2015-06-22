package cmu.team5.iotservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.sql.*;

public class DataManagerJDBC implements DataManagerIF
{
	private HashMap<String, HashMap> nodeInfo;
	
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "password";
	
	public DataManagerJDBC()
	{
		nodeInfo = new HashMap<String, HashMap>();
		
		HashMap<String, String> sensorInfo = new HashMap();
		nodeInfo.put("a2de", sensorInfo);
	}
	
	public void InitDatabase()
	{
		Connection conn = null;
		Statement stmt = null;
		try {
			//Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			
			//Execute a query
			System.out.println("Creating database...");
			stmt = conn.createStatement();
			
			String sql = "CREATE DATABASE STUDENTS";
			stmt.executeUpdate(sql);
			System.out.println("Database created successfully...");
			
		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if(stmt!=null) stmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} //end try

	}
	
	public Boolean saveSensorLog(String nodeId, String sensorType, String value)
	{
		System.out.println("[LOG] nodeId:" + nodeId + ", sensorType:" + sensorType + ", value:" + value);
		
		if (nodeInfo.containsKey(nodeId)) {
			HashMap<String, String>sensorInfo = nodeInfo.get(nodeId);
			sensorInfo.put(sensorType, value);
		}
		
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

	public ArrayList<String> getRegisteredNode()
	{
		ArrayList<String>list = new ArrayList<String>();
		Iterator it = nodeInfo.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry node = (Map.Entry)it.next();
			list.add((String) node.getKey());
		}
		
		return list;
	}

	public void addRegisteredNode(String nodeId)
	{
		if (!nodeInfo.containsKey(nodeId)) {
			HashMap<String, String> sensorInfo = new HashMap();
			nodeInfo.put(nodeId, sensorInfo);
		}
	}

	public HashMap<String, String> getNodeSensorInfo(String nodeId)
	{
		HashMap info = new HashMap<String, String>();
		if (nodeInfo.containsKey(nodeId)) {
			HashMap<String, String> sensorInfo = nodeInfo.get(nodeId);
			info = new HashMap<String, String>(sensorInfo);
		}
		return info;
	}
	
	public HashMap<String, String> getNodeActuatorInfo(String nodeId) {
		return null;
	}

	public boolean isRegisteredNode(String nodeId)
	{
		return false;
	}

	public void removeRegisteredNode(String nodeId) {
		
	}

}
