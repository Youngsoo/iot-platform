
// 
//    FILE: sensorMgr.h
// VERSION: 0.1.00
// PURPOSE: sensorMgr Temperature & Humidity Sensor library for Arduino
//
//     URL: http://arduino.cc/playground/Main/sensorMgrLib
//
// HISTORY:
// see sensorMgr.cpp file
// 

#ifndef sensorMgr_h
#define sensorMgr_h

#include "dht.h"

#if ARDUINO >= 100
 #include "Arduino.h"
#else
 #include "WProgram.h"
#endif

#define STR_SENSOR			"sensor"
#define STR_DETECT			"detect"
#define STR_TEMPERATURE		"temperature"
#define STR_HUMIDITY		"humidity"
#define STR_DOORSTATE		"doorState"

#define STR_CLOSE			"close"
#define STR_OPEN			"open"

enum
{
	PIN_DOOR=2,
	PIN_PROXIMITY=6,
	PIN_DHT=8
};

#define STR_STRING			"String"
#define STR_INTEGER			"Integer"



class sensorMgr
{
public:
	char *  getSersorInfo(const char * sensorName);
	void 	initailize(boolean isLetterBox);
	long 	ProximityVal(int Pin);
private:	
	char 	m_pResponse[10];
	boolean	m_bIsLetterBox;
};
#endif
//
// END OF FILE
//
