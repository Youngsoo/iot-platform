import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Terminal {
	
	private static final int portNum = 550;

	public static void main(String[] args) throws Exception {
		Socket clientSocket;
		
		Scanner scanner = new Scanner(System.in);
			
		System.out.print("Enter the server IP: ");
		String serverip = scanner.next();
		
		while(true) {
	       try {
	    	   System.out.println ("Trying to connect to " + serverip + " on port " + portNum);
	    	   clientSocket = new Socket(serverip, portNum);
	    	   break;
			} catch (IOException e) {
				System.err.println("Could not connect to " + serverip + " on port: " + portNum);
			}
	       
	       Thread.sleep(3000);
		}
		
		System.out.println("Connected to the server");
		
		BufferedReader in = new BufferedReader(
				new InputStreamReader(clientSocket.getInputStream()));
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		

			String message = null;
			message = Protocol.generateTerminalInitMsg("drabble");
			out.println(message);
			System.out.println(">> " + message);

		while(true) {
			System.out.println("\n### SEND COMMAND ###");
			
			System.out.print("Enter node id: ");
			String nodeId = scanner.next();
			
			System.out.print("Enter sensor type: ");
			String sensorType = scanner.next();
			
			System.out.print("Enter sensor value: ");
			String sensorValue = scanner.next();
			
			message = Protocol.generateCommandMsg(nodeId, sensorType, sensorValue);
			System.out.println("MESSAGE: " + message);
			
			System.out.print("Send this command?[y/N] ");
			String sendCmd = scanner.next();
			if (sendCmd.equals("y")) {
				out.println(message);
			}
		}

	}
}
