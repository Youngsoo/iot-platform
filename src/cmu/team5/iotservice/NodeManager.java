package cmu.team5.iotservice;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cmu.team5.middleware.Protocol;
import cmu.team5.middleware.Transport;

public class NodeManager
{
	private HashMap<String, BufferedWriter> nodeList;
	private HashMap<Integer, String> clientList;
	private DataManagerIF dataMgr;
	private SearchNode searchNode;
	
	public NodeManager()
	{
		nodeList = new HashMap<String, BufferedWriter>();
		clientList = new HashMap<Integer, String>();
		dataMgr = new DataManagerDummy();
		searchNode = new SearchNode();
	}
	
	private boolean sendNode(String nodeId, String message) throws IOException
	{
		boolean ret = false;
		/*
		Iterator it = nodeList.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry node = (Map.Entry)it.next();
			
			if (nodeId.equals(node.getKey().toString())) {
				BufferedWriter writer = (BufferedWriter)node.getValue();
				System.out.println("Sending command to node:" + node.getKey());
				Transport.sendHeader(writer, message.length());
				writer.write(message, 0, message.length());
				writer.flush();
				break;
			}
		}
		*/
		BufferedWriter out = nodeList.get(nodeId);
		if (out != null) {
			Transport.sendMessage(out, message);
			ret = true;
		} else {
			System.out.println("Node(id:" + nodeId + ") is not ready.");
			ret = false;
		}
		
		return ret;
	}
	
	public void addNode(String nodeId, OutputStream out, int clientNumber)
	{
		System.out.println("Adding a node device");
		synchronized(nodeList) {
			nodeList.put(nodeId, new BufferedWriter(new OutputStreamWriter(out)));
		}
		synchronized(clientList) {
			clientList.put(clientNumber, nodeId);
		}
	}
	
	public void removeNode(String nodeId)
	{
		System.out.println("Remove node device");
		/*
		synchronized(nodeList) {
			nodeList.remove(nodeId);
		}
		*/
		BufferedWriter out = nodeList.get(nodeId);
		/*
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		dataMgr.removeRegisteredNode(nodeId);
	}
	
	public void removeNode(int clientNumber)
	{
		System.out.println("Remove node device. (clientNumber:" + clientNumber + ")");
		String nodeId = clientList.get(clientNumber);
		synchronized(nodeList) {
			nodeList.remove(nodeId);
			clientList.remove(clientNumber);
		}
	}
	
	public void registerNode(String nodeId, String nodeName)
	{
		dataMgr.addRegisteredNode(nodeId, nodeName);
		System.out.println("Register (" + nodeName + ":" + nodeId + ")");
	}
	
	public boolean isRegisteredNode(String nodeId)
	{
		return dataMgr.isRegisteredNode(nodeId);
	}
	
	public void sendCommandMsg(String nodeId, String message) throws IOException
	{
		boolean result = sendNode(nodeId, message);
		if (result) {
			String actuatorType = Protocol.getActuatorType(message);
			String value = Protocol.getValue(message);
			dataMgr.saveCommandLog(nodeId, actuatorType, value);
		}
	}
	
	public void sendConfigMsg(String nodeId, String message) throws IOException
	{
		sendNode(nodeId, message);
	}
	
	public void handleSensorMsg(String nodeId, String sensorType, String sensorValue)
	{
		dataMgr.saveSensorLog(nodeId, sensorType, sensorValue);
	}
	
	public void handleActuatorMsg(String nodeId, String actuatorType, String actuatorValue)
	{
		dataMgr.saveActuatorLog(nodeId, actuatorType, actuatorValue);
	}
	
	public void handleRegisterRequest(String serialStr, OutputStream terminalOut)
	{
		searchNode.startSearch(serialStr, this, terminalOut);
	}
	
	public void handleUnregisterRequest(String nodeId, BufferedWriter out) throws IOException
	{
		String message = Protocol.generateUnregisterMsg();
		boolean result = sendNode(nodeId, message);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		removeNode(nodeId);
		String resultMsg = Protocol.generateResultMsg("unregister", result, null);
		Transport.sendMessage(out, resultMsg);
	}
	
	public ArrayList getRegisteredNode()
	{
		return dataMgr.getRegisteredNode();
	}
	
	public HashMap getNodeSensorInfo(String nodeId)
	{
		return dataMgr.getNodeSensorInfo(nodeId);
	}
	
	public HashMap getNodeActuatorInfo(String nodeId)
	{
		return dataMgr.getNodeActuatorInfo(nodeId);
	}
	
	public ArrayList getLogDataAll()
	{
		return dataMgr.getLogDataAll();
	}
	
	public void setLogConfigTime(String time)
	{
		int logTime = Integer.parseInt(time);
		dataMgr.setLogConfigTime(logTime);
	}
}