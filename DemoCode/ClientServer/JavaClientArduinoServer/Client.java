/******************************************************************************************************************
* File: Client.java
* Course/Project: 2014 LG Executive Education Program Studio Project
*
* Copyright: Copyright (c) 2013 Carnegie Mellon University
* Versions:
*	1.0 Apr 2013 - IOT project
*	2.0 Apr	2014 - Smart Warehouse
*
* Description:
*
* This class serves as an example for how to write a client application that connects to an Arudino server.
* There is nothing uniquely specific to the Arduino in this code, other than the application level protocol
* that is used between this application and the Arduino. The assumption is that the Arduino ServerDemo is running
* on the Arduino. When this application is started it attempts to connect to the arduino client. Once connected
* this application writes a message to the server. The message sent is alternated between two possible messages.
* There is no particular reason for this. Once client (this app) sends the message, it reads two strings from
* the server. And the process continues ad infinitum. Note that this is intended to be a minimal demo, more
* error handling should be added, especially where reading and writing data to and from server. Read errors in
* this section of the code will cause this program to crash.
*
* Compilation and Execution Instructions:
*
*	Compiled in a command window as follows: javac Client.java
*	Execute the program from a command window as follows: java Client <Server IP Address>
*
* Parameters: 		None
*
* Internal Methods: None
*
******************************************************************************************************************/

import java.io.*;
import java.net.*;

class Client
{
	public static void main(String argv[]) throws Exception
 	{
  		String inputLine;															// String from the server
    	String[]clientMsg = {	"Hello you little Arduino server, I am a PC...\n", 	// Clent messages. You can add
    							"I am a PC,... Hello little Arduino server...\n"	//  more messages here if you
    						};														//  want to.
		Socket clientSocket = null;													// The socket.
    	int msgNum = 0;																// Index into the clientMsg array.
    	boolean done;																// Loop flag.
    	int msgCnt;																	// Number for message displayed
    	int	portNum = 550;															// Port number for server socket

   		while(true)
		{
  			/*****************************************************************************
    		* First we instantiate the clent socket. The user should enter the IP address
    		* of the server on the command line
			*****************************************************************************/

			done = false;
			while (!done)
			{
				try
    			{
      		  		if (argv.length == 0)
      		  		{
      		  			System.out.println ( "\nPlease specify an IP address on the command line.\n" );
       		 			System.exit(1);
   		 			} else {
      		  			System.out.println ( "\n\nTrying to connect to " + argv[0] + " on port " + portNum + ".\n" );
      		  			clientSocket = new Socket(argv[0], portNum);
       		 			done = true;
					}
  				}
    			catch (IOException e)
        		{
        			System.err.println( "Could not connect to " + argv[0] + " on port: " + portNum + "\n");
        		}

        		Thread.sleep(3000);

    		}
  			/*****************************************************************************
    		* If we get here, we are connected. Now we create the input and output streams.
    		* The "out" variable is for writing to the server, and "in" is for reading
    		* from the server.
			*****************************************************************************/

   			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    		BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));

  			/*****************************************************************************
    		* The protocol is simple: The client sends a message, then waits for two
    		* messages from the server and thats it.
			*****************************************************************************/
			/* Here we write a message...												*/

			out.write( clientMsg[msgNum], 0, clientMsg[msgNum].length() );
			out.flush();

			/*****************************************************************************
    		* Now we switch messages, so the nxt time we write a message is different.
			*****************************************************************************/

			if (msgNum == 0)
				msgNum = 1;
			else
				msgNum = 0;

			/*****************************************************************************
    		* Now we read two messages from the server...
			*****************************************************************************/

			msgCnt = 0;
	    	done = false;

	    	while (!done)
	    	{
	    		if ((inputLine = in.readLine()) != null)
	    		{
  	  				System.out.println("FROM SERVER: " + inputLine);
  	  				msgCnt++;
	  			}
				if (msgCnt > 1) done = true;
	  		}

	  		System.out.println("-------------------------------------------------------");

			/*****************************************************************************
    		* That's it! Close the streams and close the socket.
			*****************************************************************************/
	  		in.close();
	  		out.close();
	  		clientSocket.close();

	  		Thread.sleep(3000);

  		} // while
 	} // main
} // class