
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


#define STR_SENSOR			"Sensor"
#define STR_TEMPERATURE		"Temperature"
#define STR_HUMIDITY		"Humidity"
#define STR_EXIST			"Exist"
#define STR_DOORSTATE		"DoorState"

#define STR_CLOSED			"Closed"
#define STR_OPENED			"Opened"

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
	char *  getSersorInfo(char * sensorName);
	void 	initailize(boolean isLetterBox);
private:	
	char 	m_pResponse[10];
	boolean	m_bIsLetterBox;
	dht 	m_DHT;                   // This sets up an equivalence between dht and DHT.
};
#endif
//
// END OF FILE
//
