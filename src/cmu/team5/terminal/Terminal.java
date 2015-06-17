package cmu.team5.terminal;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.JFrame;

import cmu.team5.middleware.*;


public class Terminal extends JFrame{
	
	private static final int portNum = 550;
	
	public void Connection(String serverip){
		Socket clientSocket;
		try {
			clientSocket = new Socket(serverip, portNum);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void MakeMsgHeader(){
		
	}
	
	public void MakeMsgData(){
		
	}
	
	
	
	
	public Terminal(){
		super("Iot Terminal");
	      setSize(560,400);   
	       //Create object for the Class you generated using JFromDesigner         
	      Terminalwindow t = new Terminalwindow();
		  setDefaultCloseOperation(EXIT_ON_CLOSE);
	      setVisible(true);
	      //Add the JFormDesigner to this current Main from
	      add(t);
	}

	public static void main(String[] args) throws Exception {
		Socket clientSocket;
		
		
	    Terminal t = new Terminal(); 	
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
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		
		String message = null;
		message = Protocol.generateTerminalInitMsg("drabble");
		
		Transport.sendHeader(out, message.length());
		out.write(message, 0, message.length());
		out.flush();
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
				Transport.sendHeader(out, message.length());
				out.write(message, 0, message.length());
				out.flush();
			}
		}

	}
}
