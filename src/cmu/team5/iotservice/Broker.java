package cmu.team5.iotservice;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import cmu.team5.middleware.*;

enum DeviceType { UNKNOWN, NODE, TERMINAL };

public class Broker {
	
	private static final int portNum = 550;
	private static final int MAXQSIZE = 1024;
	private NodeManager nodeMgr;
	private TerminalManager terminalMgr;
	private BlockingQueue<IoTMessage> msgQ;
	private Transport transport;

	public Broker()
	{
		nodeMgr = new NodeManager();
		terminalMgr = new TerminalManager();
		
		msgQ = new ArrayBlockingQueue<IoTMessage>(MAXQSIZE);
		transport = new Transport(msgQ);
	}
	
	private IoTMessage getQueue() {
		IoTMessage message = null;
		
		synchronized(msgQ) {
			try {
				message = msgQ.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return message;
	}
	
	public void startService()
	{
		transport.startService();
	
		try {
			while(true) {
				IoTMessage iotMsg = getQueue();
				handleMessage(iotMsg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleMessage(IoTMessage iotMsg) throws IOException
	{
		String message = iotMsg.getMessage();
		OutputStream out = iotMsg.getStream();
		
		//System.out.println("Received << " + message);
		
		String deviceTypeStr = Protocol.getDeviceType(message);
		if (deviceTypeStr != null) {
			// NOTE: "deviceType" only appears at the first message
			handleDeviceInitMsg(message, out);
			return;
		}

		String messageType = Protocol.getMessageType(message);
		if (messageType != null) {
			if (messageType.equals("sensor")) {
				nodeMgr.handleSensorMsg(
						Protocol.getNodeId(message),
						Protocol.getSensorType(message),
						Protocol.getSensorValue(message));
				return;
			}
			
			if (messageType.equals("command")) {
				nodeMgr.sendCommandMsg(Protocol.getNodeId(message), message);
				return;
			}
			
			if (messageType.equals("register")) {
				nodeMgr.handleRegisterRequest(Protocol.getSerial(message), out);
				return;
			}
			
			if (messageType.equals("unregister")) {
				String nodeId = Protocol.getNodeId(message);
				if (nodeMgr.isRegisteredNode(nodeId)) {
					nodeMgr.handleUnregisterRequest(nodeId, new BufferedWriter(new OutputStreamWriter(out)));
				}
				return;
			}
			
			if (messageType.equals("emergency") || messageType.equals("information")) {
				terminalMgr.handleMessage(message);
				return;
			}
			
			if (messageType.equals("nodeRegistered")) {
				ArrayList list = nodeMgr.getRegisteredNode();
				String nodeRegMsg = Protocol.generateRegisteredNodeMsg(list);
				Transport.sendMessage(new BufferedWriter(new OutputStreamWriter(out)), nodeRegMsg);
				return;
			}
			
			if (messageType.equals("nodeStatus")) {
				String nodeId = Protocol.getNodeId(message);
				HashMap sensorInfo = nodeMgr.getNodeSensorInfo(nodeId);
				HashMap actuatorInfo = nodeMgr.getNodeActuatorInfo(nodeId);
				String nodeStatusMsg = Protocol.generateNodeStatusResultMsg(nodeId, sensorInfo, actuatorInfo); 
				Transport.sendMessage(new BufferedWriter(new OutputStreamWriter(out)), nodeStatusMsg);
				return;
			}
			
			if (messageType.equals("logData")) {
				ArrayList logList = nodeMgr.getLogDataAll();
				String logDataMsg = Protocol.generateLogDataMsg(logList);
				Transport.sendMessage(new BufferedWriter(new OutputStreamWriter(out)), logDataMsg);
				return;
			}

		}
	}
	
	private void handleDeviceInitMsg(String message, OutputStream out) throws IOException
	{
		String deviceTypeStr = Protocol.getDeviceType(message);
		String deviceKey = Protocol.getNodeId(message);
		
		if (deviceTypeStr.equals("node")) {
			//if (deviceKey != null && nodeMgr.isRegisteredNode(deviceKey)) {
			if (deviceKey != null) {
				nodeMgr.addNode(deviceKey, out);
				return;
			}
			
			System.out.println("Not a registered node so close the connection");
			out.close();
			return;			
		} // node
		
		if (deviceTypeStr.equals("terminal")) {
			String messageType = Protocol.getMessageType(message);
			if (messageType != null && messageType.equals("login")) {
				terminalMgr.handleLogin(Protocol.getUserId(message), Protocol.getPasswd(message), out);
				return;
			}
		} // terminal
		
		System.out.println("Not a node or terminal device so close the connection");
		out.close();
	}
	
} // class Broker
