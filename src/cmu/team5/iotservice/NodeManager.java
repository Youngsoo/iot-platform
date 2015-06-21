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
	private DataManagerIF dataMgr;
	private SearchNode searchNode;
	
	public NodeManager()
	{
		nodeList = new HashMap<String, BufferedWriter>();
		dataMgr = new DataManagerDummy();
		searchNode = new SearchNode();
	}
	
	private void sendNode(String nodeId, String message) throws IOException
	{
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
	}
	
	public void addNode(String nodeId, OutputStream out)
	{
		System.out.println("Adding a node device");
		synchronized(nodeList) {
			nodeList.put(nodeId, new BufferedWriter(new OutputStreamWriter(out)));
		}
	}
	
	public void removeNode(String nodeId)
	{
		synchronized(nodeList) {
			nodeList.remove(nodeId);
		}
	}
	
	public void registerNode(String nodeId, String nodeName)
	{
		dataMgr.addRegisteredNode(nodeId);
		System.out.println("Register (" + nodeName + ":" + nodeId + ")");
	}
	
	public boolean isRegisteredNode(String nodeId)
	{
		// TODO: check nodeList
		return true;
	}
	
	public void sendCommandMsg(String nodeId, String message) throws IOException
	{
		sendNode(nodeId, message);
	}
	
	public void handleSensorMsg(String nodeId, String sensorType, String sensorValue)
	{
		dataMgr.saveLog(nodeId, sensorType, sensorValue);
	}
	
	public void handleRegisterRequest(String serialStr, OutputStream terminalOut)
	{
		searchNode.startSearch(serialStr, this, terminalOut);
	}
	
	public void handleUnregisterRequest(String nodeId, String serialStr) throws IOException
	{
		String message = Protocol.generateUnregisterMsg(serialStr);
		sendNode(nodeId, message);
		removeNode(nodeId);
		
	}
	
	public ArrayList getRegisteredNode()
	{
		return dataMgr.getRegisteredNode();
	}
}