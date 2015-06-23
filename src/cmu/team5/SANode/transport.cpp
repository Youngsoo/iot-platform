#ifndef transportMgr_c
#define transportMgr_c
#include "../WiFi/src/WiFi.h"
#include "transport.h"
#include <Stdio.h>
#include <IPAddress.h>


#define PACKET_MAGIC			"ToNY"
#define PACKET_MAGIC_LENGTH			4
#define PACKET_JSON_SIZE_LENGTH		8
#define PACKET_JSON_SIZE_OFFSET		4	

WiFiServer serverSocket(FIND_PORTID);
const char * sensorName[]={STR_PROXIMITY,STR_DOORSTATE,STR_HUMIDITY,STR_TEMPERATURE,STR_LIGHT,STR_ALARM};

void transportMgr::initailize(char * pSsid)
{
	int status;	
	status=WL_IDLE_STATUS;
	Serial.println("Attempting to connect to network...");
	Serial.print("SSID: ");
	Serial.println(pSsid);
	

	// Attempt to connect to Wifi network.
	while ( status != WL_CONNECTED) 
	{ 
	Serial.print("Attempting to connect to SSID: ");
	Serial.println(pSsid);
	status = WiFi.begin(pSsid);
	}  
	serverSocket.begin();
    
	printConnectionStatus();
	protocolManager.initailize();
	curCount=0;
	memset(bufJsonData,0,LENGTH_OF_JSON_STRING);
	isConntected=false;
	bAlarmed=false;
	
	//EEPROM.write(EEPROM_ADDR_NODE_REG, false);
}

#define CONFIGUABLE_TIME_AUTO_TURN_OFF		10//sec
#define CONFIGUABLE_TIME_AUTO_ALARM			20//sec
int transportMgr::handleEvent4MailBox(void)
{
	int			sensorCount=1,count=0;	
	char * curValue=NULL;
	for(count=0;count < sensorCount ; count++)
	{
		curValue=protocolManager.getSersorValue(sensorName[count]);
		if(strcmp(curValue,preSensoInfo[count]) !=0)
		{
			char * contents=NULL;
			if(strcmp(curValue,STR_DETECT)==0)	contents=STR_MAIL_DELIVERY;
			else								contents=STR_MAIL_TAKEN;	
			JsonObject& json=protocolManager.makeNotificationMessage(STR_INFORMATION,contents);
			sendMessage(client,json);
		}
		strcpy(preSensoInfo[count],curValue);
	}
}
int transportMgr::handleEvent(void)
{
	static 	unsigned long startTime4AutoLightOff=0,startTime4SetAlarm=0;
	boolean bSendEmergencyMsg=false,bAskMessage=false;
	int			sensorCount=ENUM_INDEX_MAX,count=0;	
	unsigned long curTime;
	bAlarmed=EEPROM.read(EEPROM_ADDR_ALARM);
	//bAlarmed=true;
	curTime=millis()/1000;
	if(protocolManager.isLetterBox()==true)	
	{
		return handleEvent4MailBox();
	}
	for(count=0;count < sensorCount ; count++)
	{
		char * curValue=NULL;
		Serial.println( sensorName[count]);
		curValue=protocolManager.getSersorValue(sensorName[count]);
		Serial.println( curValue);
		if(strcmp(curValue,preSensoInfo[count]) !=0)
		{
			JsonObject& json = protocolManager.makeSensorValue(sensorName[count],curValue);
			sendMessage(client,json);
			if(strcmp(sensorName[count],STR_PROXIMITY)==0)
			{
				if(strcmp(curValue,STR_UNDETECT)==0)
				{
					if(bAlarmed==false)	
					{
						bAskMessage=true;
						startTime4SetAlarm=curTime;
					}
					startTime4AutoLightOff=curTime;	
				}
				else	//detect
				{
					if(bAlarmed==true)	bSendEmergencyMsg=true;
					startTime4AutoLightOff=0;
				}
			}
			if(strcmp(sensorName[count],STR_DOORSTATE)==0)
			{
				if(strcmp(curValue,STR_OPEN)==0 && bAlarmed==true)	bSendEmergencyMsg=true;
			}			
			strcpy(preSensoInfo[count],curValue);
		}
	}
	
	//Serial.println( millis());
	//Serial.println( curTime);
	//Serial.println( startTime4AutoLightOff);
	//Serial.println( "*************************");
	//Serial.println( startTime4SetAlarm);
	if(bAskMessage==true)	
	{
		JsonObject& json=protocolManager.makeNotificationMessage(STR_INFORMATION,STR_ASK_CONTENTS);
		sendMessage(client,json);
	}
	if(bSendEmergencyMsg==true)	
	{
		JsonObject& json =protocolManager.makeNotificationMessage(STR_INFORMATION,STR_EMERGENCE_CONTENTS);	
		sendMessage(client,json);
	}
	if(startTime4AutoLightOff != 0 && curTime-startTime4AutoLightOff > CONFIGUABLE_TIME_AUTO_TURN_OFF) 
	{
		protocolManager.controlActuator(STR_LIGHT,STR_OFF);
		JsonObject& json =protocolManager.makeActuatorValue(STR_LIGHT,STR_OFF);
		sendMessage(client,json);
		startTime4AutoLightOff=0;
	}
	if(startTime4SetAlarm != 0 && curTime-startTime4SetAlarm > CONFIGUABLE_TIME_AUTO_ALARM) 
	{
		protocolManager.controlActuator(STR_ALARM,STR_ON);
		JsonObject& json =protocolManager.makeActuatorValue(STR_ALARM,STR_ON);
		sendMessage(client,json);
		if(strcmp(preSensoInfo[ENUM_INDEX_DOORSTATE],STR_OPEN)==0)
		{
			protocolManager.controlActuator(STR_DOOR,STR_CLOSE);
			JsonObject& json =protocolManager.makeActuatorValue(STR_DOOR,STR_CLOSE);	
			sendMessage(client,json);
		}
		startTime4SetAlarm=0;
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
		
		for(iCnt=PACKET_JSON_SIZE_OFFSET; iCnt<PACKET_JSON_SIZE_LENGTH+PACKET_JSON_SIZE_OFFSET ; iCnt++)
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
		//Serial.println( "jsonLength:70");
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
	//isRegistered=true;
	Serial.println(isRegistered);
	 
	if(isRegistered==true)
	{
		char pServerIP[15];
		uint8_t convServerIP[4];
		IPAddress	serverIP;
		int serverLength=EEPROM.read(EEPROM_ADDR_SERVERIP_LENGTH);
		memset(pServerIP,0,sizeof(pServerIP));
		protocolManager.readStringFromEEPROM(pServerIP,EEPROM_ADDR_SERVERIP,serverLength);
		//String stringServerIP;
		//EEPROM.get(EEPROM_ADDR_SERVERIP, stringServerIP);

		IPToNetAddr(pServerIP,convServerIP);
		serverIP=IPAddress(convServerIP);
		//serverIP=IPAddress(192,168,1,149);
		Serial.println(serverIP);
		if (!client.connected()) 
	  	{
	      	Serial.println("disconnecting.");
	    	client.stop();
	    	isConntected=false;
	    	Serial.println("try to connect");
	
	    	if (client.connect(serverIP, SERVER_PORTID))
	    	{
		        isConntected=true;
		        JsonObject& json = protocolManager.makeConnMsg();
				memset(preSensoInfo,0,sizeof(preSensoInfo));
				Serial.println("**************************");
				json.printTo(Serial);
				sendMessage(client,json);
	   	 	}    
	  	}
	}

 if(isRegistered==true && isConntected==true) 
   {   
	 int avail=client.available();    
     while (avail > 0)
      {
        //Serial.println(client.available());   
        char readChar=client.read();
        handleMessage(client,readChar);
        avail=client.available();
      }  
	 handleEvent();
      //Serial.println( "receive done...");

  } // if
  else
  {
  	 client.stop();
     
  	 listenSock = serverSocket.available();
	 if (listenSock) 
     {
       listenSock.flush();
       // We first write a debug message to the terminal then send
       // the data to the client.
       Serial.println("Reading data from client..."); 
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
	sprintf(bufHeader,"%s%08d",PACKET_MAGIC,length);
	socket.print(bufHeader);
	sendMsg.printTo(socket);
	Serial.println("sent!");
	
#endif
}

void transportMgr::printConnectionStatus() 
{
#if 0	
	// Print the basic connection and network information: Network, IP, and Subnet mask
	char pwd[5];
	
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
#else
	char pwd[5];
	byte mac[6];					  // Wifi shield MAC address
	IPAddress myip; 					// The IP address of the shield
	myip = WiFi.localIP();
	Serial.print(" IP Address:: ");
	Serial.println(myip);
	
	WiFi.macAddress(mac);
	sprintf(pwd,"%02x%02x",mac[0],mac[1]);
	protocolManager.setPassword(pwd);
#endif //if 0
} // printConnectionStatus



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

#endif //#ifndef transportMgr_c

