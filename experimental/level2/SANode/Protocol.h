
// 
//    FILE: protocolMgr.h
// VERSION: 0.1.00
// PURPOSE: protocolMgr  for Arduino

#ifndef protocolMgr_h
#define protocolMgr_h

#include "../ArduinoJson/ArduinoJson.h"
#include "actuatorMgr.h"    
#include "sensorMgr.h" 

#if ARDUINO >= 100
 #include "Arduino.h"
#else
 #include "WProgram.h"
#endif

#define LENGTH_OF_JSON_STRING		200
#define LENGTH_OF_PASSWORD			4
#define STR_HOUSE_NODE_PWD			"a2de"

class protocolMgr
{
public:
	void 		initailize(void);
	int 		handleMessage(char bOpen);
	JsonObject&  makeConnMsg(void);
	void 		setPassword(char * pwd);
	char *		getPassword(void);
	boolean		isLetterBOx(void);
	int 		sendMessage(JsonObject&  sendMsg);
	
private:
	int  	bufIndex;
	int 	nodeType;
	char	password[5];
	actuatorMgr m_actuatorMgr;
	sensorMgr	m_sensorMgr;
	char bufInString[LENGTH_OF_JSON_STRING]; 
	int parseJson(char* bufJson);
};
#endif
//
// END OF FILE
//
