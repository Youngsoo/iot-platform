package cmu.team5.iotservice;

import java.io.*;
import java.net.*;

import cmu.team5.middleware.Protocol;
import cmu.team5.middleware.Transport;

class RegisterNode extends Thread
{
	String ip;	// IP address and port to try to connect to.
	int port;
	String serialStr;

	// Here we set up the thread and local parameters

	public RegisterNode ( String ip, int port, String serialStr )
	{
		this.ip = ip;
		this.port = port;
		this.serialStr = serialStr;
		setDaemon(false);

	} // constructor

	// Here is the main executable of the thread

	public void run()
	{
		boolean isNullRegister = false;
		if (isNullRegister) {
			System.out.println("Searching IP:" + ip + " port:" + port);
			return;
		}
		//System.out.println("Searching IP:" + ip + " port:" + port);
		
		try
   		{
			/*****************************************************************************
			* First we try to connect. We set the timeout to 3 1/4 seconds. This value is
			* critical. If its too short, its not enough time to connect, but the longer it
			* is, the longer it take to scan the addresses.
			*****************************************************************************/

			SocketAddress sockaddr = new InetSocketAddress(ip, port);
			Socket sock = new Socket();
			sock.connect(sockaddr, 3250);
			System.out.println( "\n\nNODE FOUND AT:: " + ip + "!!!" );

			/*****************************************************************************
			* If we get here, we are connected. Now we determine if is an Arduino server
			* running the CommandServer application. So we create the input and output
			* streams. The "out" variable is for writing to the server, and "in" is for
			* reading from the server.
			*****************************************************************************/

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			InputStream in = sock.getInputStream();
			
			String message = Protocol.generateRegisterMsg(sock.getLocalAddress().getHostAddress(), serialStr);
			Transport.sendMessage(out, message);
			Thread.sleep(1000);
			Transport.sendMessage(out, message);
			
			// Receive ack message from node
			message = Transport.getMessage(in);
			if (message != null) {
				// TODO: send result to terminal and receive node info
			}

			/*****************************************************************************
			* That's it! Close the streams and close the socket.
			*****************************************************************************/

			in.close();
			out.close();
	  		sock.close();

     	} catch (IOException e) {

			// DEBUG
			//System.out.println( "NO SERVER FOUND AT::" + ip );
			// System.exit(1);
    	} catch (InterruptedException e) {
			e.printStackTrace();
		}

	} // main
} // class
