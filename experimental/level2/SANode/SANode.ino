/**************************************************************
* File: ClientDemo
* Project: LG Exec Ed Program
* Copyright: Copyright (c) 2013 Anthony J. Lattanze
* Versions:
* 1.0 May 2013.
* 1.5 April 2014 - added #define for port id
*
* Description:
*
* This program runs on an Arduino processor with a WIFI shield. 
* This program is a client demo. This runs in a loop communicating
* with a server process that also runs in a loop. The protocol is
* that after we connect to a server, this process sends three '\n'
* terminated strings. The final string is "bye\n" which is a 
* signal to the server that we are done sending data. Then
* this process waits for the server to send a single string back. This illustrates basic client
* server connection and two-way communication.
*
* Compilation and Execution Instructions: Only Compile using 
* Arduino IDE VERSION 1.0.4
*
* Parameters: None
*
* Internal Methods: void printConnectionStatus()
*
************************************************************************************************/

 #include <SPI.h>
 #include <WiFi.h>PORTID
 #include <ArduinoJson.h>
 #include <EEPROM.h>
 #include "dht.h"           // Note that the DHT file must be in your Arduino installation 
                           // folder, in the library foler.
#include <Servo.h>  
#include "protocol.h" 

#define PORTID  550               // IP socket port ID
#define FIND_PORTID  551               // IP socket port ID

char ssid[] = "LGTeam5";             // The network SSID 
 //char ssid[] = "Shadyside Inn";             // The network SSID 
char pass[] = "hotel5405"; 
int status = WL_IDLE_STATUS;      // Network connection status
	IPAddress server(192,168,1,149);  // The server's IP address
//IPAddress server(192,168,1,117);  // The server's IP address
 //IPAddress server(10,255,110,152);  // The server's IP address
WiFiClient client;                // The client (our) socket
IPAddress ip;                     // The IP address of the shield
IPAddress subnet;                 // The IP address of the shield
WiFiServer serverSocket(FIND_PORTID);     // Server connection and port
long rssi;                        // Wifi shield signal strength
byte mac[6];                      // Wifi shield MAC address
boolean isConntected=false;
protocolMgr protocol;

 void setup()  
 {
  // Initialize a serial terminal for debug messages.
  Serial.begin(9600);

  Serial.println("Attempting to connect to network...");
  Serial.print("SSID: ");
  Serial.println(ssid);
  
  // Attempt to connect to Wifi network.
  while ( status != WL_CONNECTED) 
  { 
     Serial.print("Attempting to connect to SSID: ");
     Serial.println(ssid);
     //status = WiFi.begin(ssid,pass);
     status = WiFi.begin(ssid);
  }  
  serverSocket.begin();
  Serial.println( "Connected to network:" );
  Serial.println( "\n----------------------------------------" );

  // Print the basic connection and network information.
  printConnectionStatus();
  protocol.initailize();
  
  Serial.println( protocol.isLetterBOx() );  
  Serial.println( "\n----------------------------------------\n" );  
}



void loop() 
{
  // Here we attempt connect to the server on the port specified above
  //Serial.print("\nAttempting to connect to server...");
  // if the server's disconnected, stop the client:
  Serial.println("----------------------------------");
  if (!client.connected()) 
  {
     Serial.println("disconnecting.");
    client.stop();
    isConntected=false;
     Serial.println("try to connect");
    if (client.connect(server, PORTID))
    {
         isConntected=true;
         JsonObject& json = protocol.makeConnMsg();
		 json.printTo(Serial);
       	 json.printTo(client);
		protocol.sendMessage(json);
    }    
  }
 
  if(isConntected==true) 
  {   
    char c = ' ';      
    while ( c!= '\n' )
    {
       int avail=client.available();
        if (avail) 
        {
          c = client.read();
          protocol.handleMessage(c);
        }
    }
   
 /*
     int avail=client.available();    
     while (avail > 0)
      {
        Serial.println(client.available());   
        char readChar= client.read();
        protocol.handleMessage(readChar);
        avail=client.available();
      }  
      //Serial.println( "receive done...");
 */
  } // if
  else
  {
    WiFiClient listenSock = serverSocket.available();
     if (listenSock) 
     {
       // Clear the input buffer.
       listenSock.flush();
   
       // We first write a debug message to the terminal then send
       // the data to the client.
       Serial.println("Sending data to client...");
      // listenSock.println("Hello there client!"); 
       //listenSock.println("I am an Arduio Server!"); 
       
       // Now we switch to read mode...
       Serial.println("Reading data from client..."); 
       
       char inChar = ' ';      
       while ( inChar != '\n' )
       {
         // Checks to see if data is available from the client
         if (listenSock.available())     
         {
           // We read a character then we write on to the terminal
           inChar = listenSock.read();   
           Serial.write(inChar);
         }
       }      
       Serial.println("Done!");
       Serial.println(".....................");
     }
  }
  delay(1000);
      

  Serial.println("+++++++++++++++++++++++++++++++++++");
} //  LOOP

/************************************************************************************************
* The following method prints out the connection information
************************************************************************************************/

 void printConnectionStatus() 
 {
     // Print the basic connection and network information: Network, IP, and Subnet mask
     char pwd[5];
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
	 sprintf(pwd,"%02x%02x",mac[0],mac[1]);
	 protocol.setPassword(pwd);
     // Print the wireless signal strength:
     rssi = WiFi.RSSI();
     Serial.print("Signal strength (RSSI): ");
     Serial.print(rssi);
     Serial.println(" dBm");

 } // printConnectionStatus
