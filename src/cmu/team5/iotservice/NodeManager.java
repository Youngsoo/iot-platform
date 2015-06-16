package cmu.team5.iotservice;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cmu.team5.middleware.LogDB;

public class NodeManager
{
	private HashMap<String, PrintWriter> nodeList;
	
	public NodeManager()
	{
		nodeList = new HashMap<String, PrintWriter>();
	}
	
	public void addNode(String nodeId, OutputStream out)
	{
		System.out.println("Adding a node device");
		synchronized(nodeList) {
			nodeList.put(nodeId, new PrintWriter(out, true));
		}
	}
	
	public void removeNode(String nodeId)
	{
		synchronized(nodeList) {
			nodeList.remove(nodeId);
		}
	}
	
	public void sendCommandMsg(String nodeId, String message)
	{
		Iterator it = nodeList.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry node = (Map.Entry)it.next();
			
			if (nodeId.equals(node.getKey().toString())) {
				PrintWriter writer = (PrintWriter)node.getValue();
				System.out.println("Sending command to node:" + node.getKey());
				writer.println(message);
				break;
			}
		}
	}
	
	public void handleNodeMsg(String nodeId, String sensorType, String sensorValue)
	{
		LogDB.saveLog(nodeId, sensorType, sensorValue);
	}
}