#ifndef transportMgr_c
#define transportMgr_c
#include "../WiFi/src/WiFi.h"
#include <EEPROM.h>

#include "transport.h"
#include <Stdio.h>

#define FIND_PORTID  551               // IP socket port ID
#define PACKET_MAGIC			"ToNY"
#define PACKET_MAGIC_LENGTH			4
#define PACKET_JSON_SIZE_LENGTH		4

/*------------------------------------------------------------------------------------------------------
|start address	|	length	|	field description	|	value description 								|
|	0			|	1		|node resiter or not 	|	0:unregister, 1:register					
|	1			|	1		|server IP length		|	7~15						
|	2			|	15		|server IP address		|
|
---------------------------------------------------

*/

WiFiServer serverSocket(FIND_PORTID);

void transportMgr::initailize(char * pSsid,IPAddress ipAddress,int pServerPort,int pFindPort)
{

	status=WL_IDLE_STATUS;
	serverPort=pServerPort;
	findPort=pFindPort;
	strcpy(ssid,pSsid);
	Serial.println("Attempting to connect to network...");
	Serial.print("SSID: ");
	Serial.println(ssid);
	

	// Attempt to connect to Wifi network.
	while ( status != WL_CONNECTED) 
	{ 
	Serial.print("Attempting to connect to SSID: ");
	Serial.println(ssid);
	status = WiFi.begin(ssid);
	}  
	serverSocket=WiFiServer(pFindPort);
	serverSocket.begin();
	printConnectionStatus();
	protocolManager.initailize();
	//serverIP=ipAddress;
	curCount=0;
	memset(bufJsonData,0,LENGTH_OF_JSON_STRING);
}

int transportMgr::handleMessage( WiFiClient socket,char readData)
{
	//Serial.println( bufJsonData);
	if(curCount==0)	
	{
		memset(bufJsonData,0,LENGTH_OF_JSON_STRING);
		jsonLength=0;
	}
	bufJsonData[curCount]=readData;
	curCount++;
	if(curCount==PACKET_MAGIC_LENGTH+PACKET_JSON_SIZE_LENGTH)	
	{
		int iCnt;
		long mul=1000;
		for(iCnt=0; iCnt<PACKET_JSON_SIZE_LENGTH ; iCnt++)
		{
			String inString= "";		
			inString+=bufJsonData[PACKET_MAGIC_LENGTH+iCnt];	
			jsonLength+=mul*inString.toInt();
			mul=mul/10;
		}		
	}
	//Serial.println( "handleMessage:64");
	//Serial.println( jsonLength);
	if(curCount==PACKET_MAGIC_LENGTH+PACKET_JSON_SIZE_LENGTH+jsonLength)
	{
		JsonObject* sendJson=NULL;
		Serial.println( "jsonLength:70");
		Serial.println( &bufJsonData[PACKET_MAGIC_LENGTH+PACKET_JSON_SIZE_LENGTH]);
		sendJson=protocolManager.parseJson(&bufJsonData[PACKET_MAGIC_LENGTH+PACKET_JSON_SIZE_LENGTH]);
		if(sendJson !=NULL)
		{
			JsonObject & tempObj=*sendJson;
			sendMessage(socket,tempObj);			
		}
		curCount=0;
		return HANDLE_RET_VAL_STOP;			
	}
	return HANDLE_RET_VAL_CONTINUE;
			
}

int transportMgr::connectionHandler(void)
{

 if (!client.connected()) 
  {
    Serial.println("disconnecting.");
    client.stop();
    isConntected=false;
    Serial.println("try to connect");
    //if (client.connect(serverIP, serverPort))
    if(0)
    {
        isConntected=true;
        JsonObject& json = protocolManager.makeConnMsg();
		//Serial.println(".....................");
		json.printTo(Serial);
		sendMessage(client,json);
    }    
  }
 isConntected=false;
 /*

 if(isConntected==true) 
  {   
    char c = ' ';      
    while ( c!= '\n' )
    {
       int avail=client.available();
        if (avail) 
        {
          c = client.read();
          protocolManager.handleMessage(c);
        }
    }
   
	 */
 if(isConntected==true) 
   {   

     int avail=client.available();    
     while (avail > 0)
      {
        //Serial.println(client.available());   
        char readChar=client.read();
        //protocolManager.handleMessage(readChar);
        handleMessage(client,readChar);
        avail=client.available();
      }  
      //Serial.println( "receive done...");

  } // if
  else
  {
     listenSock = serverSocket.available();
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
       #if 0
	   int avail=listenSock.available();
	   Serial.println(avail);
       while (avail > 0)
      {
        Serial.println(client.available());   
        char readChar=listenSock.read();
        //protocolManager.handleMessage(readChar);
        handleMessage(readChar);
        avail=client.available();
      }  
	   #else
       char inChar = ' ';      
       while ( inChar != '\n' )
       {
         // Checks to see if data is available from the client
         int avail=listenSock.available();
		 if (listenSock.available())     
         {
           // We read a character then we write on to the terminal
           inChar = listenSock.read();   
		   if(handleMessage(listenSock,inChar)==HANDLE_RET_VAL_STOP) break;
         }
       }      
	   #endif // #if 0
       Serial.println("Done!");
       Serial.println(".....................");
     }
  }
  return true;
}

int transportMgr::sendMessage(WiFiClient socket,JsonObject&  sendMsg)
{
#if 0
	long length=0;
	String header=String(PACKET_MAGIC);
	length=sendMsg.printTo(Serial);
	if(length>999)		header=header+length;
	else if(length>99)	header=header+"0"+length;
	else if(length>9)	header=header+"00"+length;
	else 				header=header+"000"+length;
	socket.print(header);
	sendMsg.printTo(socket);
	sendMsg.printTo(Serial);
	//Serial.println(".....................");
	
#else
	char bufHeader[20];
	long length=sendMsg.printTo(Serial);
	sprintf(bufHeader,"%s%04d",PACKET_MAGIC,length);
	socket.print(bufHeader);
	sendMsg.printTo(socket);
	
#endif
}

void transportMgr::printConnectionStatus() 
{
	
	// Print the basic connection and network information: Network, IP, and Subnet mask
	char pwd[5];
	myip = WiFi.localIP();
	Serial.print("Connected to ");
	Serial.print(ssid);
	Serial.print(" IP Address:: ");
	Serial.println(myip);
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
	protocolManager.setPassword(pwd);
	
	// Print the wireless signal strength:
	rssi = WiFi.RSSI();
	Serial.print("Signal strength (RSSI): ");
	Serial.print(rssi);
	Serial.println(" dBm");
} // printConnectionStatus
#endif //#ifndef transportMgr_c

