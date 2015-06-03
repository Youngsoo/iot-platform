/******************************************************************************************************************
* File: TryConnect.java
* Course: 2013 LG Executive Education Program
* Project: Iot
* Copyright: Copyright (c) 2013 Carnegie Mellon University
* Versions:
*	1.0 Apr 2013 - initial version
* 	1.5 May 2015 - updated some of the comments and messages
*
* Description:
*
* This class will take an IP address and port. It will attempt to connect to the server. Specifically, this class
* looks Arduinos running the CommandServer application. If an Arduino server running this application is found,
* it will connect and send a query command ("Q\n") and as a response that server will return its MAC address.
* This runs as a separate thread, and this class is used by FindServer.java
*
* Compilation and Execution Instructions:
*
*	Compiled in a command window as follows: javac FindServer.java
*	Execute the program from a command window as follows: java FindServer
*
* Parameters: 		None
*
* Internal Methods: None
*
******************************************************************************************************************/

import java.io.*;
import java.net.*;

class TryConnect extends Thread
{
	String ip;	// IP address and port to try to connect to.
	int port;

	// Here we set up the thread and local parameters

	public TryConnect ( String ip, int port )
	{
		this.ip = ip;
		this.port = port;
		setDaemon(false);

	} // constructor

	// Here is the main executable of the thread

	public void run()
	{
		try
   		{
			/*****************************************************************************
			* First we try to connect. We set the timeout to 3 1/4 seconds. This value is
			* critcal. If its too short, its not enough time to connect, but the longer it
			* is, the longer it take to scan the addresses.
			*****************************************************************************/

			SocketAddress sockaddr = new InetSocketAddress(ip, port);
			Socket sock = new Socket();
			sock.connect(sockaddr, 3250);
			System.out.print( "\n\nSERVER FOUND AT:: " + ip + "!!!" );

			/*****************************************************************************
			* If we get here, we are connected. Now we determine if is an Arduino server
			* running the CommandServer application. So wecreate the input and output
			* streams. The "out" variable is for writing to the server, and "in" is for
			* reading from the server.
			*****************************************************************************/

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			BufferedReader in = new BufferedReader( new InputStreamReader(sock.getInputStream()));

			/*****************************************************************************
			* The protocol is simple: The client sends a message, then waits for two
			* messages from the server and thats it. We send a query command the client
			* which is a "Q\n" string. If the write timesout, we will drop down to the
			* exception which means that while we have found a server, its not an Arduino
			* running the CommandServer application.
			*****************************************************************************/

			out.write( "Q\n", 0, 2 );
			out.flush();

			String inputLine = null;
		   	boolean done = false;

			/*****************************************************************************
			* If we get to here, then we found a CommandServer, so we read the MAC address
			* of the Wifi Shield on the Arduino.
			*****************************************************************************/

		   	while (!done)
			{
				if ((inputLine = in.readLine()) != null)
				{
					System.out.println(" MAC ADDRESS: " + inputLine + "\n\n");

				} else {

					done = true;
				}
			} // while

			/*****************************************************************************
			* That's it! Close the streams and close the socket.
			*****************************************************************************/

			in.close();
			out.close();
	  		sock.close();

     	} catch (IOException e) {

			// DEBUG
			// System.out.println( "NO SERVER FOUND AT::" + ip );
			// System.exit(1);
    	}

	} // main
} // class