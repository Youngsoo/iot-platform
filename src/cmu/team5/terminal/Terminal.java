package cmu.team5.terminal;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;

import cmu.team5.middleware.*;


public class Terminal extends JFrame{
	
	public static JFrame Window;
	public Terminalwindow t;
		
	public Terminal(){
		super("Iot Terminal");
		  Window = new JFrame();
	      Window.setSize(560,400);   
	      t = new Terminalwindow();
	      Window.setJMenuBar(t.AddmenuBar());
	      Window.setDefaultCloseOperation(EXIT_ON_CLOSE);
	      Window.add(t);
	      Window.setTitle("IoT Terminal System");
	      Window.setVisible(true);
	      
	}

	
	public class SocketScanner implements Runnable {

		InputStream in=null;	
		public void run() {
			while (true) {
	            String msg;
	            String MsgType;
	          try {
					if (t.ClientSocket != null){
						in = t.ClientSocket.getInputStream();
						msg = Transport.getMessage(in);
						System.out.println("<MsgReceived>" + msg);
						MsgType = Protocol.getMessageType(msg);
						t.HandleServerResponse(MsgType, msg);			
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	        }
	    }
	}

	public static void main(String[] args) throws Exception {
			
	    Terminal terminal = new Terminal();
	    Thread inService = new Thread(terminal.new SocketScanner());
	    inService.start();			
	}
}
