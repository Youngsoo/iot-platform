#ifndef transportMgr_c
#define transportMgr_c
#include "../WiFi/src/WiFi.h"
#include "transport.h"
#include <Stdio.h>
#include <IPAddress.h>

#define FIND_PORTID  550               // IP socket port ID
#define PACKET_MAGIC			"ToNY"
#define PACKET_MAGIC_LENGTH			4
#define PACKET_JSON_SIZE_LENGTH		4

WiFiServer serverSocket(FIND_PORTID);
const char * sensorName[4]={STR_DETECT,STR_TEMPERATURE,STR_HUMIDITY,STR_DOORSTATE};
	

int transportMgr::IPToNetAddr (char * IPStr, uint8_t * NetAddr)
{
	uint32_t _IP [4] = {0, 0, 0, 0};
	uint8_t cnt = 0;
	uint8_t idx = 0;
	char _str = NULL;
	// Check input paramters vaild
	if (IPStr == NULL || NetAddr == NULL)
	{
		return (-1);
	}

	while((_str=*IPStr++) != NULL)
	{
		if (_str == '.')
		{
		// Clear the count, cnt MUST <= 3
			cnt = 0;
			if (_IP [idx]> 255)
			{
				return (-1);
			}
		idx = idx + 1;
		}
		else if (_str >= '0'&& _str <= '9') // '0 '-> '9'
		{
			if (cnt++ <3 && idx <= 3)
			{
				_IP [idx] = (10 * _IP [idx]) + (_str - '0');
			}
			else
			{
				return (-1);
			}
		}
		else
		{
			return (-1);
		}
	}

	if (_IP [idx]> 255)
	{
		return (-1);
	}

	// Copy Buffer to user space
	NetAddr [0] = (uint8_t) _IP [0];
	NetAddr [1] = (uint8_t) _IP [1];
	NetAddr [2] = (uint8_t) _IP [2];
	NetAddr [3] = (uint8_t) _IP [3];

	return (0);
}

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
	isConntected=false;
	EEPROM.write(EEPROM_ADDR_NODE_REG, false);
}


int transportMgr::handleEvent(void)
{
	int			sensorCount=4,count=0;	
	if(protocolManager.isLetterBox()==true)	sensorCount=1;
	for(count=0;count < sensorCount ; count++)
	{
		char * curValue=NULL;
		Serial.println( sensorName[count]);
		curValue=protocolManager.getSersorValue(sensorName[count]);
		Serial.println( curValue);
		if(strcmp(curValue,preSensoInfo[count]) !=0)
		{
			JsonObject& json = protocolManager.makeSensorValue(sensorName[count],curValue);
			//json.printTo(Serial);
			sendMessage(client,json);
				
			strcpy(preSensoInfo[count],curValue);
		}
	}
	return true;
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
	int isRegistered=EEPROM.read(EEPROM_ADDR_NODE_REG);
	//isRegistered=false;
	Serial.println(isRegistered);
	 
	if(isRegistered==true)
	{
		char pServerIP[15];
		uint8_t convServerIP[4];
		int serverLength=EEPROM.read(EEPROM_ADDR_SERVERIP_LENGTH);
		memset(pServerIP,0,sizeof(pServerIP));
		protocolManager.readStringFromEEPROM(pServerIP,EEPROM_ADDR_SERVERIP,serverLength);
		//String stringServerIP;
		//EEPROM.get(EEPROM_ADDR_SERVERIP, stringServerIP);

		IPToNetAddr(pServerIP,convServerIP);
		serverIP=IPAddress(convServerIP);
		Serial.println(serverIP);
		//serverIP=IPAddress("192.168.1.149");
		if (!client.connected()) 
	  	{
	      	Serial.println("disconnecting.");
	    	client.stop();
	    	isConntected=false;
	    	Serial.println("try to connect");
	
	    	if (client.connect(serverIP, serverPort))
	    	{
		        isConntected=true;
		        JsonObject& json = protocolManager.makeConnMsg();
				//Serial.println(".....................");
				json.printTo(Serial);
				sendMessage(client,json);
	   	 	}    
	  	}
	}
 

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
	

 if(isRegistered==true) //&& isConntected==true) 
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
	 handleEvent();
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

