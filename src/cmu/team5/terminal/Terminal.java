package cmu.team5.terminal;

import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import cmu.team5.middleware.*;


public class Terminal extends JFrame{
	
	private static final int portNum = 550;
	
	public static JFrame Window;
	public Terminalwindow t;
	

	public void MakeMsgHeader(){
		
	}
	
	public void MakeMsgData(){
		
	}
	
	
	
	public Terminal(){
		super("Iot Terminal");
		  Window = new JFrame();
	      Window.setSize(560,400);   
	       //Create object for the Class you generated using JFromDesigner         
	      t = new Terminalwindow();
	      Window.setJMenuBar(t.AddmenuBar());
	      Window.setDefaultCloseOperation(EXIT_ON_CLOSE);
	      Window.setVisible(true);
	      //Add the JFormDesigner to this current Main from
	      Window.add(t);
	      
	}
	
	
	
	public class SocketScanner implements Runnable {

		InputStream in=null;	
		
		Socket ClientSocket = t.ClientSocket;

		public void run() {
			while (true) {
	            String msg;
	            String MsgType;
	          try {
					if (t.ClientSocket != null){
						in = t.ClientSocket.getInputStream();
						msg = Transport.getMessage(in);
						System.out.println("***" + msg);
						MsgType = Protocol.getMessageType(msg);
						t.HandleServerResponse(MsgType, msg);			
					}
					else{
						t.ClientSocket = t.Connection();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	        }
	    }
	}

	
	private static void StartRecevingMessage(){
//		Thread t1 = new Thread(new SocketScanner());
	//    t1.start();
	}
	

	public static void main(String[] args) throws Exception {
			
	    Terminal t1 = new Terminal();
	    //t1.StartRecevingMessage();
	    Thread t11 = new Thread(t1.new SocketScanner());
	    t11.start();
	    
	    
		System.out.println("Connected to the server");
		
	/*	BufferedReader in = new BufferedReader(
				new InputStreamReader(clientSocket.getInputStream()));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		
		String message = null;
		message = Protocol.generateTerminalInitMsg("drabble");
		
		Transport.sendHeader(out, message.length());
		out.write(message, 0, message.length());
		out.flush();
		System.out.println(">> " + message);*/
/*
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
*/
	}
}
