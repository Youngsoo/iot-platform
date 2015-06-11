import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
		
		public MessageHandler(Socket socket, int clientNumber)
		{
			this.socket = socket;
			this.clientNumber = clientNumber;
			System.out.println("New connection with client# " + clientNumber + " at " + socket);
		}
		
		public void run() {
			String message;
			
			try {
				BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				
				message = in.readLine();
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
					message = in.readLine();
					if (message == null) {
						break;
					}
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


