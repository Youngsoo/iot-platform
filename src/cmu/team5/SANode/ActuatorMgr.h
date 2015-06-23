
// 
//    FILE: actuatorMgr.h
// VERSION: 0.1.00
// PURPOSE: actuatorMgr Temperature & Humidity Sensor library for Arduino
//
//     URL: http://arduino.cc/playground/Main/actuatorMgrLib
//
// HISTORY:
// see actuatorMgr.cpp file
// 

#ifndef actuatorMgr_h
#define actuatorMgr_h

#include "../Servo/src/Servo.h"
#if ARDUINO >= 100
 #include "Arduino.h"
#else
 #include "WProgram.h"
#endif

#define STR_LIGHT	"light"
#define STR_ALARM	"alarm"
#define STR_DOOR	"door"
#define STR_CLOSE			"close"
#define STR_OPEN			"open"
#define STR_ON			"on"
#define STR_OFF			"off"

enum
{
	PIN_LIGHT=5,
	PIN_ALARM=3
};

class actuatorMgr
{
public:
	void initailize(void);
	int setDoor(int bOpen);
	int turnOnLED(int pin);
	int turnOffLED(int pin);
	int handleActuator(const char * target,const char * commnad);
private:
	int 	servoPin;
	Servo 	m_cServo;
	
};
#endif
//
// END OF FILE
//
