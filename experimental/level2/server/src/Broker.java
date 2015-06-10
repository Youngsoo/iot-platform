import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Broker {
	
	private static final int portNum = 550;				// Port number for server socket
	
	private static ArrayList<PrintWriter> nodeList = new ArrayList<PrintWriter>();
	private static ArrayList<PrintWriter> terminalList = new ArrayList<PrintWriter>();
	
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
			try {
				BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				
				while (true) {
					String input = in.readLine();
					if (input == null) {
						break;
					}
					System.out.println(">> " + input);
					//System.out.println(">> msgtype: " + Protocol.getMessageType(input));
					//System.out.println(">> value: " + Protocol.getSensorValue(input));
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


