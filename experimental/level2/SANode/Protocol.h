
// 
//    FILE: protocolMgr.h
// VERSION: 0.1.00
// PURPOSE: protocolMgr  for Arduino

#ifndef protocolMgr_h
#define protocolMgr_h

#include "../ArduinoJson/ArduinoJson.h"
#include "actuatorMgr.h"    

#if ARDUINO >= 100
 #include "Arduino.h"
#else
 #include "WProgram.h"
#endif

#define LENGTH_OF_JSON_STRING		200

class protocolMgr
{
public:
	void 	initailize(void);
	int 	handleMessage(char bOpen);
private:
	int  bufIndex;
	actuatorMgr m_actuatorMgr;
	char bufInString[LENGTH_OF_JSON_STRING]; 
	 int parseJson(char* bufJson);
	
};
#endif
//
// END OF FILE
//
