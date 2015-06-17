package cmu.team5.iotservice;

import java.io.*;
import java.net.*;
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
	
	public void startService() throws IOException
	{
		IoTMessage iotMsg = null;
		String message = null;
		DeviceType deviceType = DeviceType.UNKNOWN;
		String deviceKey;

		transport.startService();
		
		while(true) {
			iotMsg = getQueue();
			message = iotMsg.getMessage();
			
			System.out.println(">> " + message);
			
			String deviceTypeStr = Protocol.getDeviceType(message);
			if (deviceTypeStr != null) {
				OutputStream out = iotMsg.getStream();
				
				if (deviceTypeStr.equals("node")) {

					deviceType = DeviceType.NODE;
					deviceKey = Protocol.getNodeId(message);
					nodeMgr.addNode(deviceKey, out);
				} else if (deviceTypeStr.equals("terminal")) {
					deviceType = DeviceType.TERMINAL;
					deviceKey = Protocol.getUserId(message);
					terminalMgr.addTerminal(deviceKey, out);
				} else {
					System.out.println("Not a node or terminal device so close the connection");
					out.close();
					return;
				}

			} else {
				handleMessage(message);
			}
			
		}
	}
	
	private void handleMessage(String message) throws IOException
	{
		String messageType = Protocol.getMessageType(message);
		if (messageType != null) {
			if (messageType.equals("sensor")) {
				nodeMgr.handleNodeMsg(
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
				
				return;
			}

		}
	}
	
} // class Broker




