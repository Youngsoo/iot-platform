package cmu.team5.testdevice;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import cmu.team5.middleware.Protocol;
import cmu.team5.middleware.Transport;


class MessageReceiver extends Thread {
	private InputStream in;
	
	public MessageReceiver(InputStream in) {
		this.in = in;
	}
	
	public void run() {
		while(true) {
			try {
				String message = Transport.getMessage(in);
				if (message == null) {
					System.out.println("Connection closed by server.");
					return;
				}
				
				System.out.println("Received << " + message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class TestDevice
{
	private static final int portNum = 550;
	
	public static void main(String[] args) throws Exception {
		Socket clientSocket;
		Scanner scanner = new Scanner(System.in);
	
		//System.out.print("Enter the server IP: ");
		String serverip = "localhost";//scanner.next();
		
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
		
		InputStream in = clientSocket.getInputStream();
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		
		new MessageReceiver(in).start();
		
		while(true) {
			System.out.print("Enter command: ");
			String command = scanner.next();
			String message = null;
			
			if (command.equals("login")) {
				message = "{\"messageType\":\"login\",\"deviceType\":\"terminal\",\"userId\":\"tony\",\"passwd\":\"lge123\"}";
				
			} else if (command.equals("node")) {
				message =  "{\"deviceType\":\"node\",\"nodeId\":\"1234\"}";
				
			} else if (command.equals("sensor")) {
				message = "{\"msgType\":\"sensor\",\"nodeId\":\"1234\",\"sensorType\":\"door1\",\"value\"}";
				
			} else if (command.equals("register")) {
				message = "{\"msgType\":\"register\",\"serial\":\"1234\"}";
				
			} else {
				System.out.println("Bad command: " + command);
				continue;
			}
			
			Transport.sendMessage(out, message);
			//System.out.println("Sending >> " + message);
			
		} // while
		
	} // main

	
} // class
