
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
enum
{
	HANDLE_RET_VAL_CONTINUE=0,
	HANDLE_RET_VAL_STOP=1	
};
enum
{
	ENUM_INDEX_DETECT=0,
	ENUM_INDEX_DOORSTATE,
	ENUM_INDEX_HUMIDITY,
	ENUM_INDEX_TEMPERATURE,
	ENUM_INDEX_MAX,
	ENUM_INDEX_LIGHT,
	ENUM_INDEX_ALARM
};
#define SERVER_PORTID  550               // IP socket port ID
#define FIND_PORTID  551               // IP socket port ID
#define WIFI_SSID		"LGTeam5"
#define STR_LENGTH_SPLIT	30

class transportMgr
{
public:
	void 	initailize(char * pSsid);	
	void 	printConnectionStatus() ;
	int		connectionHandler(void);
	int 	sendMessage(WiFiClient socket,JsonObject&  sendMsg);
	int 	sendMessage(WiFiClient socket,String sendBuf);
	int 	handleMessage(WiFiClient socket,char readData);
	int 	IPToNetAddr (char * IPStr, uint8_t * NetAddr);	
	int 	handleEvent(void);
	int 	handleEvent4MailBox(void);
private:
	boolean isConntected;
	WiFiClient client;                // The client (our) socket
	WiFiClient listenSock;            // The client (our) socket
	int	curCount;
	long jsonLength;
	char bufJsonData[LENGTH_OF_JSON_STRING]; 
	protocolMgr protocolManager;
	
	char		preSensoInfo[ENUM_INDEX_MAX][10];
	boolean 	bAlarmed;
};
#endif
//
// END OF FILE
//
