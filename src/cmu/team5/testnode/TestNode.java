package cmu.team5.testnode;

import java.io.*;
import java.net.*;

import cmu.team5.middleware.Transport;

class TestNode
{	
	public static void main(String argv[]) throws Exception
 	{
  		String inputLine;					// String from the server
    	String[]clientMsg0 = {"{\n\"devicetype\":\"node\",\n\"nodeid\":\"1234\"}\n"};
    	String[]clientMsg1 = {"{\"msgtype\":\"sensor\",\"nodeid\":\"1234\",\"sensortype\":\"door1\",\"value\":0}\n"};

		Socket clientSocket = null;		// The socket.
    	int msgNum = 0;					// Index into the clientMsg array.
    	boolean done;						// Loop flag.
    	int msgCnt;						// Number for message displayed
    	String ipaddr = "localhost";	// IP address for server
    	int	portNum = 550;				// Port number for server socket

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
  		  			System.out.println ( "\n\nTrying to connect to " + ipaddr + " on port " + portNum + ".\n" );
  		  			clientSocket = new Socket(ipaddr, portNum);
   		 			done = true;
  				}
    			catch (IOException e)
        		{
        			System.err.println( "Could not connect to " + ipaddr + " on port: " + portNum + "\n");
        		}

        		Thread.sleep(3000);

    		}
  			/*****************************************************************************
    		* If we get here, we are connected. Now we create the input and output streams.
    		* The "out" variable is for writing to the server, and "in" is for reading
    		* from the server.
			*****************************************************************************/

   			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    		//BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));
    		InputStream in = clientSocket.getInputStream();

  			/*****************************************************************************
    		* The protocol is simple: The client sends a message, then waits for two
    		* messages from the server and thats it.
			*****************************************************************************/
			/* Here we write a message...												*/
    		Transport.sendHeader(out, clientMsg0[msgNum].length());
			out.write( clientMsg0[msgNum], 0, clientMsg0[msgNum].length() );
			out.flush();

			Thread.sleep(1000);

			//sendHeader(out, clientMsg1[msgNum].length());
			Transport.sendHeader(out, clientMsg1[msgNum].length());
			out.write( clientMsg1[msgNum], 0, clientMsg1[msgNum].length() );
			out.flush();

			/*****************************************************************************
    		* Now we read one messages from the server...
			*****************************************************************************/

			msgCnt = 0;
	    	done = false;

			String message;
			byte[] buffer = new byte[1024];
	    	int readBytes, leftBytes, totalBytes, msgLength;
	    	
			msgLength = Transport.getMessageLength(in);
			if (msgLength < 0) return;
			
			leftBytes = msgLength;
			readBytes = 0;
			totalBytes = 0;
			while(leftBytes > 0) {
				readBytes = in.read(buffer, totalBytes, leftBytes);
				System.out.println("readBytes: " + readBytes);
				if (readBytes < 0) return;
				leftBytes -= readBytes;
				totalBytes += readBytes;
			}
			
			message = new String(buffer, 0, msgLength);
			
			System.out.println("FROM SERVER: " + message);
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
