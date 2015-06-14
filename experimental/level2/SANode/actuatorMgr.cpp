#include "actuatorMgr.h" 


void actuatorMgr::initailize(void)
{
	servoPin=9;
	m_cServo.attach(servoPin); 
Serial.println( "actuatorMgr");
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
