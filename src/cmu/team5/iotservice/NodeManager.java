package cmu.team5.iotservice;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cmu.team5.middleware.Transport;

public class NodeManager
{
	private HashMap<String, BufferedWriter> nodeList;
	private DataManager dataMgr;
	
	public NodeManager()
	{
		nodeList = new HashMap<String, BufferedWriter>();
		dataMgr = new DataManager();
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
	
	public void sendCommandMsg(String nodeId, String message) throws IOException
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
	
	public void handleNodeMsg(String nodeId, String sensorType, String sensorValue)
	{
		dataMgr.saveLog(nodeId, sensorType, sensorValue);
	}
}