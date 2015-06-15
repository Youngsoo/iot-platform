package cmu.team5.iotservice;

import java.io.*;
import java.net.*;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cmu.team5.framework.*;

public class Broker {
	
	private static final int portNum = 550;				// Port number for server socket
	private static HashMap<String, PrintWriter> nodeList = new HashMap<String, PrintWriter>();
	private static HashMap<String, PrintWriter> terminalList = new HashMap<String, PrintWriter>();	
	
	public void startService()
	{
		try {
			startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startServer() throws IOException
	{
		while(true)
		{
			ServerSocket listener = new ServerSocket(portNum);
			int clientNumber = 0;
			
    		try
    		{
    			while(true) {
    				System.out.println ("Waiting for connection on port " + portNum + "." );
    				new MessageHandler(listener.accept(), clientNumber++).start();
    			}		
        	}
    		catch (IOException e)
        	{
        		System.err.println("Could not instantiate socket on port: " + portNum + " " + e);
        		System.exit(1);
        	}
    		
			System.out.println (".........................\n" );

    	}
	}

	private class MessageHandler extends Thread {
		private Socket socket;
		private int clientNumber;
		private final String MAGICSTRING = "ToNY";
		
		public MessageHandler(Socket socket, int clientNumber)
		{
			this.socket = socket;
			this.clientNumber = clientNumber;
			System.out.println("New connection with client# " + clientNumber + " at " + socket);
		}
		
		private int getMessageLength(InputStream stream) throws IOException {
			boolean isValidMsg = false;
			byte[] readByte = new byte[4];
			char[] magicStr = new char[4];
			int readLen = 0;
			int msgLen = 0;
			
			while(!isValidMsg) {
				readLen = stream.read(readByte, 0, 4);
				if (readLen < 0) return -1;
				
				for (int i = 0; i < readLen; i++) {
					magicStr[i] = (char)readByte[i];
				}

				if (String.valueOf(magicStr).equals(MAGICSTRING)) isValidMsg = true;
			}

			// read the message length
			readLen = stream.read(readByte, 0, 4);
			if (readLen < 0) return -1;
			
			for (int i = 0; i < readLen; i++) {
				magicStr[i] = (char)readByte[i];
			}
			
			msgLen = Integer.parseInt(String.valueOf(magicStr));
			return msgLen;
		}
		
		public void run() {
			String message;
			byte[] buffer = new byte[1024];
			int readBytes, leftBytes, msgLength;
			
			try {
				InputStream in = socket.getInputStream();
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				
				msgLength = getMessageLength(in);
				if (msgLength < 0) return;
				
				leftBytes = msgLength;
				readBytes = 0;
				while(leftBytes > 0) {
					readBytes = in.read(buffer, readBytes, leftBytes);
					if (readBytes < 0) return;
					leftBytes -= readBytes;
				}
				
				message = new String(buffer, 0, msgLength);
				
				System.out.println(">> " + message);
				String deviceType = Protocol.getDeviceType(message);
				
				if (deviceType != null && deviceType.equals("node")) {
					System.out.println("Adding a node device");
					synchronized(nodeList) {
						nodeList.put(Protocol.getNodeId(message), out);
					}
				} else if (deviceType != null && deviceType.equals("terminal")) {
					System.out.println("Adding a terminal device");
					synchronized(terminalList) {
						terminalList.put(Protocol.getUserId(message), out);
					}
				} else {
					System.out.println("Not a node or terminal device so close the connection");
					socket.close();
					return;
				}

				while (true) {

					msgLength = getMessageLength(in);
					if (msgLength < 0) return;
					
					leftBytes = msgLength;
					readBytes = 0;
					while(leftBytes > 0) {
						readBytes = in.read(buffer, readBytes, leftBytes);
						if (readBytes < 0) return;
						leftBytes -= readBytes;
					}

					message = new String(buffer, 0, msgLength);
					System.out.println(">> " + message);
					//System.out.println(">> msgtype: " + Protocol.getMessageType(message));
					//System.out.println(">> value: " + Protocol.getSensorValue(message));
					
					String messageType = Protocol.getMessageType(message);
					if (messageType != null && messageType.equals("sensor")) {
						LogDB.saveLog(
								Protocol.getNodeId(message),
								Protocol.getSensorType(message),
								Protocol.getSensorValue(message));
					}
					 
					if (messageType != null && messageType.equals("command")) {
						Iterator it = nodeList.entrySet().iterator();
						while(it.hasNext()) {
							Map.Entry node = (Map.Entry)it.next();
							PrintWriter writer = (PrintWriter)node.getValue();
							System.out.println("Sending command to node:" + node.getKey());
							writer.println(message);
						}
					}
				}

			} catch (IOException e) {
				System.out.println("Error handling client# " + clientNumber + ": " + e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("Couldn't close a socket, what's going on?");
				}
              System.out.println("Connection with client# " + clientNumber + " closed");
			}
		}
	}
}


