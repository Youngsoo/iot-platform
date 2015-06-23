
#include <EEPROM.h>
#include "actuatorMgr.h" 
#include "memoryMap.h" 



void actuatorMgr::initailize(void)
{
	servoPin=9;
	m_cServo.attach(servoPin); 
	
	handleActuator(STR_DOOR,STR_CLOSE);
	handleActuator(STR_LIGHT,STR_OFF);
	handleActuator(STR_ALARM,STR_OFF);
}

int actuatorMgr::handleActuator(const char * target,const char * commnad)
{
	if(strcmp(target,STR_DOOR)==0)
	{
		if(strcmp(commnad,STR_OPEN)==0)	setDoor(true);
		else							setDoor(false);
	}
	else if(strcmp(target,STR_LIGHT)==0)
	{
		if(strcmp(commnad,STR_ON)==0)	
		{
			turnOnLED(PIN_LIGHT);
			EEPROM.write(EEPROM_ADDR_LIGHT, true);
		}
		else							
		{
			turnOffLED(PIN_LIGHT);
			EEPROM.write(EEPROM_ADDR_LIGHT, false);
		}
	}
	else
	{
		if(strcmp(commnad,STR_ON)==0)
		{
			turnOnLED(PIN_ALARM);
			EEPROM.write(EEPROM_ADDR_ALARM, true);
		}
		else							
		{
			turnOffLED(PIN_ALARM);
			EEPROM.write(EEPROM_ADDR_ALARM, false);
		}
	}

	return true;
}

int actuatorMgr::setDoor(int bOpen)
{
	if(bOpen==1)		m_cServo.write(0);
	else			m_cServo.write(90);  // Set servo to mid-point. This closes
                      // the door.
	Serial.print( "setDoor:" );
	Serial.println( bOpen);
	
  	return true;
}

int actuatorMgr::turnOnLED(int pin)
{
	pinMode( pin, OUTPUT);      // Set the specified pin to output mode.
   	digitalWrite( pin, HIGH);   // Set the pin to 5v.
	Serial.print( "turnOnLED:" );
	Serial.println( pin);
  	return true;
}


int actuatorMgr::turnOffLED(int pin)
{
	pinMode( pin, OUTPUT);     // Set the pin to output mode.
   	digitalWrite( pin, LOW);   // Set the pin to 0 volts.
	Serial.print( "turnOffLED:" );
	Serial.println( pin);
  	return true;
}


//
// END OF FILE
//
