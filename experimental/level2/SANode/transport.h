
// 
//    FILE: transportMgr.h
// VERSION: 0.1.00
// PURPOSE: transportMgr  for Arduino

#ifndef transportMgr_h
#define transportMgr_h
#include <SPI.h>
#include "../ArduinoJson/ArduinoJson.h"
#include "protocol.h" 

#if ARDUINO >= 100
 #include "Arduino.h"
#else
 #include "WProgram.h"
#endif

class transportMgr
{
public:
	void 	initailize(char * pSsid,IPAddress ipAddress,int pServerPort,int pFindPort);	
	void 	printConnectionStatus() ;
	int		connectionHandler(void);
	int 	sendMessage(JsonObject&  sendMsg);
	int 	handleMessage(char readData);
private:
	char ssid[20];
	int status;
	boolean isConntected;
	long rssi;                        // Wifi shield signal strength
	byte mac[6];                      // Wifi shield MAC address
	WiFiClient client;                // The client (our) socket
	IPAddress myip;                     // The IP address of the shield
	IPAddress subnet;                 // The IP address of the shield
	int serverPort;
	int findPort;
	int	curCount;
	long jsonLength;
	char bufJsonData[LENGTH_OF_JSON_STRING]; 
	protocolMgr protocolManager;
	IPAddress	serverIP;
};
#endif
//
// END OF FILE
//
