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

 #include <ArduinoJson.h>
 #include <EEPROM.h>
 #include "dht.h"           // Note that the DHT file must be in your Arduino installation 
                           // folder, in the library foler.
#include <Servo.h>  
#include <WiFi.h>
#include "transport.h"

#define WIFI_SSID		"LGTeam5"
#define SERVER_PORTID  550               // IP socket port ID
#define FIND_PORTID  551               // IP socket port ID



 transportMgr TransPortManager;


 void setup()  
 {
  // Initialize a serial terminal for debug messages.
  Serial.begin(9600);
  IPAddress server(192,168,1,149); 
  String tmpingServerIP;
	
  //IPAddress server(192,168,1,117);
  TransPortManager.initailize(WIFI_SSID,server,SERVER_PORTID,FIND_PORTID);
  //EEPROM.write(1, false);

}



void loop() 
{
  // Here we attempt connect to the server on the port specified above
  //Serial.print("\nAttempting to connect to server...");
  // if the server's disconnected, stop the client:
  Serial.println("----------------------------------");
  TransPortManager.connectionHandler();
  delay(500);
  Serial.println("+++++++++++++++++++++++++++++++++++");
} //  LOOP

