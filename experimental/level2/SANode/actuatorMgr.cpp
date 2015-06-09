#include "actuatorMgr.h" 


void actuatorMgr::initailize(void)
{
	servoPin=9;
	m_cServo.attach(servoPin); 
Serial.println( "actuatorMgr");
}


int actuatorMgr::setActuator(int bOpen)
{
	if(bOpen==1)		m_cServo.write(0);
	else			m_cServo.write(90);  // Set servo to mid-point. This closes
                      // the door.
	Serial.print( "setActuator:" );
	Serial.println( bOpen);
  
}



//
// END OF FILE
//
