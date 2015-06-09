
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

struct eachSensorInfo 
{
	char sensorName[10];
	char valueRange;
};

class actuatorMgr
{
public:
	void initailize(void);
	int setActuator(int bOpen);
private:
	int 	servoPin;
	Servo 	m_cServo;
	
};
#endif
//
// END OF FILE
//
