/****************************************************************
* File: ServerDemo
* Project: LG Exec Ed Program
* Copyright: Copyright (c) 2013 Anthony J. Lattanze
* Versions:
* 1.0 May 2013 - initial version
* 1.5 April 2014 - added #define for port id
*
* Description:
*
* This program runs on an Arduino processor with a WIFI shield. 
* This program is a server demo. This runs in a loop communicating
* with a client process that also runs in a loop. The protocol is
* that after a client connects, this process sends two '\n' 
* terminated strings. Then this process waits for the client to 
* send a string back. This illustrates basic connection and
* two-way communication.
*
* Compilation and Execution Instructions: Must be compiled using 
* Arduino IDE VERSION 1.0.4
*
* Parameters: None
*
* Internal Methods: void printConnectionStatus()
*
****************************************************************/

 #include <SPI.h>
 #include <WiFi.h>
 
 #define PORTID 550             // IP socket port#

 char ssid[] = "LGTeam1";       // The network SSID
 int status = WL_IDLE_STATUS;   // Network connection status
 WiFiServer server(PORTID);     // Server connection and port
 char inChar;                   // Character read from client
 IPAddress ip;                  // The IP address of the shield
 IPAddress subnet;              // The IP address of the shield
 long rssi;                     // Wifi shield signal strength
 byte mac[6];                   // Wifi shield MAC address

 void setup() 
 {
   // Initialize a serial terminal for debug messages.
   Serial.begin(9600); 
  
   // Attempt to connect to Wifi network.
   while ( status != WL_CONNECTED) 
   { 
     Serial.print("Attempting to connect to SSID: ");
     Serial.println(ssid);
     status = WiFi.begin(ssid);
   }  
   
   // Print the basic connection and network information.
   printConnectionStatus();
   
   // Start the server and print and message for the user.
   server.begin();
   Serial.println("The Server is started.");
   
 } // setup

 void loop() 
 {
   // Wait for a new client:
   WiFiClient client = server.available();

   // When the client connects we send them a couple of messages.
   if (client) 
   {
     // Clear the input buffer.
     client.flush();
 
     // We first write a debug message to the terminal then send
     // the data to the client.
     Serial.println("Sending data to client...");
     client.println("Hello there client!"); 
     client.println("I am an Arduio Server!"); 
     
     // Now we switch to read mode...
     Serial.println("Reading data from client..."); 
     
     char inChar = ' ';      
     while ( inChar != '\n' )
     {
       // Checks to see if data is available from the client
       if (client.available())     
       {
         // We read a character then we write on to the terminal
         inChar = client.read();   
         Serial.write(inChar);
       }
     }      
     Serial.println("Done!");
     Serial.println(".....................");
     
   } // if we are connected
 
 } // loop
 
/***************************************************************
* The following method prints out the connection information
****************************************************************/

 void printConnectionStatus() 
 {
     // Print the basic connection and network information: 
     // Network, IP, and Subnet mask
     ip = WiFi.localIP();
     Serial.print("Connected to ");
     Serial.print(ssid);
     Serial.print(" IP Address:: ");
     Serial.println(ip);
     subnet = WiFi.subnetMask();
     Serial.print("Netmask: ");
     Serial.println(subnet);
   
     // Print our MAC address.
     WiFi.macAddress(mac);
     Serial.print("WiFi Shield MAC address: ");
     Serial.print(mac[5],HEX);
     Serial.print(":");
     Serial.print(mac[4],HEX);
     Serial.print(":");
     Serial.print(mac[3],HEX);
     Serial.print(":");
     Serial.print(mac[2],HEX);
     Serial.print(":");
     Serial.print(mac[1],HEX);
     Serial.print(":");
     Serial.println(mac[0],HEX);
   
     // Print the wireless signal strength:
     rssi = WiFi.RSSI();
     Serial.print("Signal strength (RSSI): ");
     Serial.print(rssi);
     Serial.println(" dBm");

 } // printConnectionStatus
