package cmu.team5.iotservice;

import java.io.*;
import java.net.*;

class SearchNode
{
	public void startSearch(String serialStr) {
		new SearchRunner(serialStr).start();
	}
	
	private class SearchRunner extends Thread {
		private String serialStr;
		
		public SearchRunner(String serialStr) {
			this.serialStr = serialStr;
		}
		public void run()
	    {
			int searchSize = 2048;							// The number of IP address to scan at the same time.
			int	portNum = 550;								// Port number for server socket
			int i = 0;										// Loop index
		  	int oct1start, oct1finish;						// The first IP octet range (starting/ending IP addresses)
		  	int oct2start, oct2finish;						// The second IP octet range (starting/ending IP addresses)
		  	int oct3start, oct3finish;						// The third IP octet range (starting/ending IP addresses)
		  	int oct4start, oct4finish;						// The forth IP octet range (starting/ending IP addresses)
		  	int one, two, three, four;						// The four octets. These are used as loop indexes
		  	int octet;										// A single octet
		   	String myIpAddr = null;							// This application's IP address
		   	String mySubnetMask = null;						// The subnet mask this application is running on
		   	String searchIpAddr = null;						// The IP address to scan
		   	Thread tcList[] = new RegisterNode[searchSize];	// The list of threads that actually does the scan
		   	boolean FirstAddr = true;						// Flag indicating the first IP address
		   	boolean isNodeFixedIP = false;

			/***********************************************************************************************************
			* We get our IP address and our Subnet mask and print it to the terminal
			***********************************************************************************************************/

		   	//myIpAddr = GetMyIp();
		   	myIpAddr = "192.168.1.149";
		   	System.out.println( "\n\nMy IP Address:: " + myIpAddr );

		   	//mySubnetMask = GetSubnetMask();
		   	mySubnetMask = "255.255.255.254";
		   	System.out.println( "Subnet Mask:: " + mySubnetMask +"\n\n" );
		   	
		   	//isNodeFixedIP = true;
		   	if (isNodeFixedIP) {
		   		tcList[i] = new RegisterNode("192.168.1.117", portNum, serialStr);
		   		return;
		   	}

			/***********************************************************************************************************
			* Next we get the integer equivalent of each octet. We do this to determine the scan ranges.
			* This is the first Octet...
			***********************************************************************************************************/

		   	octet = GetOctet( 1, mySubnetMask);

		   	switch( octet )
		   	{
			   	case 255:									// No Addresses
			   		oct1start = GetOctet( 1, myIpAddr );
		  			oct1finish = oct1start + 1;
		  			break;

		  		case 0:										// All 255 Addresses
		  			oct1start = 0;
		  			oct1finish = 255;
		  			break;

		  		default:									// Partial mask
					oct1start = 0;
					oct1finish = octet;
			}

			/***********************************************************************************************************
			* This is the second Octet...
			***********************************************************************************************************/

		   	octet = GetOctet( 2, mySubnetMask);

		   	switch( octet )
		   	{
			   	case 255:									// No Addresses
			   		oct2start = GetOctet( 2, myIpAddr );
		  			oct2finish = oct2start + 1;
		  			break;

		  		case 0:										// All 255 Addresses
		  			oct2start = 0;
		  			oct2finish = 255;
		  			break;

		  		default:									// Partial mask
					oct2start = 0;
					oct2finish = octet;
			}

			/***********************************************************************************************************
			* This is the third Octet...
			***********************************************************************************************************/

			octet = GetOctet( 3, mySubnetMask);

		   	switch( octet )
		   	{
			   	case 255:									// No Addresses
			   		oct3start = GetOctet( 3, myIpAddr );
		  			oct3finish = oct3start + 1;
		  			break;

		  		case 0:										// All 255 Addresses
		  			oct3start = 0;
		  			oct3finish = 255;
		  			break;

		  		default:									// Partial mask
					oct3start = 0;
					oct3finish = octet;
			}

			/***********************************************************************************************************
			* This is the fourth Octet...
			***********************************************************************************************************/

			octet = GetOctet( 4, mySubnetMask);

		   	switch( octet )
		   	{
			   	case 255:									// No Addresses
			   		oct4start = GetOctet( 4, myIpAddr );
		  			oct4finish = oct4start + 1;
		  			break;

		  		case 0:										// All 255 Addresses
		  			oct4start = 0;
		  			oct4finish = 255;
		  			break;

		  		default:									// Partial mask
					oct4start = 0;
					oct4finish = octet;
			}

			/***********************************************************************************************************
			* Now we start the scan. This algorithm will scan about 2K addresses per minute.
			***********************************************************************************************************/

			System.out.print( "Scanning from:" + oct1start + "." + oct2start + "." + oct3start + "." + oct4start );
			System.out.println( " to:" + (oct1finish-1) + "." + (oct2finish-1) + "." + (oct3finish-1) + "." + (oct4finish-1) );

			for ( one = oct1start; one < oct1finish; one++ )						// First octet
			{
				for ( two = oct2start; two < oct2finish; two++ )					// Second octet
				{
		  			for ( three=oct3start; three < oct3finish; three++ )			// Third octet
		   			{
			   			for ( four=oct4start; four<oct4finish; four++ )			// Fourth octet
			   			{
				   			searchIpAddr = new String( String.valueOf(one));		// Here we build the IP string
				   			searchIpAddr += ".";
				   			searchIpAddr += String.valueOf(two);
				   			searchIpAddr += ".";
				   			searchIpAddr += String.valueOf(three);
				   			searchIpAddr += ".";
				   			searchIpAddr += String.valueOf(four);

			/***********************************************************************************************************
			* Here we instantiate a RegisterNode thread for each IP address. The scans happen simultaniously. We can do
			* upto searchSize at a time.
			***********************************************************************************************************/

				   			tcList[i] = new RegisterNode(searchIpAddr, portNum, serialStr);
				   			tcList[i].start();

			/***********************************************************************************************************
			* What we are checking for here is to see if the tcList thread pool is full. If so, we wait about 3 seconds
			* for the threads to finish. Then we start again. We report the scan ranges to the users so they can see
			* the progress of the scan.
			***********************************************************************************************************/

				   			if ( i == searchSize-1 )
				   			{
				   				FirstAddr = true;
				   				i = 0;
				   				try
				   				{
				   					Thread.sleep(3000); //give time for the first batch of connects to finish

			   					} catch ( Exception e ) {

				   					System.out.println( "Error in sleep " + e );
			   					}

			   				} else {
				   				if ( FirstAddr )
				   				{
					   				System.out.print( "Starting Scan..." );
					   				FirstAddr = false;
				   				}
				   				i++;
			   				}

						} // for
					} // for
				} // for
			} // for

			//System.out.println( "\nSearch is complete.\n");

	 	} // Main

		/***********************************************************************************************************
		* This method gets the specified Octet from an IP string. The octet is returned as an integer value.
		***********************************************************************************************************/

	  	private int GetOctet( int OctetNum, String ipString )
	 	{
		 	int Octet;				// The Octet
		 	String tempstring;		// Temporary string

			// Here we figure out which octet the caller wants, and then parse it from the ipString passed
			// into this method.

		 	switch( OctetNum )
		 	{
			 	case 1:
			 		Octet = Integer.valueOf(ipString.substring(0, ipString.indexOf(".")));
			 		break;

			 	case 2:
			 		tempstring = ipString.substring(ipString.indexOf(".")+1);
			 		Octet = Integer.valueOf(tempstring.substring(0, tempstring.indexOf(".")));
			 		break;

			 	case 3:
			 		tempstring = ipString.substring(ipString.indexOf(".")+1);
			 		tempstring = tempstring.substring(tempstring.indexOf(".")+1);
			 		Octet = Integer.valueOf(tempstring.substring(0, tempstring.indexOf(".")));
			 		break;

			 	case 4:
			 		tempstring = ipString.substring(ipString.indexOf(".")+1);
			 		tempstring = tempstring.substring(tempstring.indexOf(".")+1);
			 		tempstring = tempstring.substring(tempstring.indexOf(".")+1);
			 		Octet = Integer.valueOf(tempstring);
			 		break;

				default:
					Octet = -1;

			} // switch

			return( Octet );

		}// GetOctet
	}
	
} // class
