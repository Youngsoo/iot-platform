package cmu.team5.testnode;

import java.io.*;
import java.net.*;

class TestNode
{
	public static void sendHeader(BufferedWriter out, int length) throws IOException
	{
		char[] magicString = "ToNY".toCharArray();
		char[] __msgLength;
		char[] msgLength = "0000".toCharArray();
		
		__msgLength = String.valueOf(length).toCharArray();
		
		for(int i = 0; i < __msgLength.length; i++) {
			msgLength[4 - __msgLength.length + i] = __msgLength[i];
		}
		
		out.write(magicString);
		out.write(msgLength, 0, 4);

		//System.out.println("magicString: " + String.valueOf(magicString));
		//System.out.println("msgLength: " + String.valueOf(msgLength));
	}
	
	public static void main(String argv[]) throws Exception
 	{
  		String inputLine;					// String from the server
    	String[]clientMsg0 = {"{\n\"devicetype\":\"node\",\n\"nodeid\":\"1234\n\"}\n"};
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
    		BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));

  			/*****************************************************************************
    		* The protocol is simple: The client sends a message, then waits for two
    		* messages from the server and thats it.
			*****************************************************************************/
			/* Here we write a message...												*/
    		sendHeader(out, clientMsg0[msgNum].length());
			out.write( clientMsg0[msgNum], 0, clientMsg0[msgNum].length() );
			out.flush();

           Thread.sleep(1000);

          sendHeader(out, clientMsg1[msgNum].length());
			out.write( clientMsg1[msgNum], 0, clientMsg1[msgNum].length() );
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
