#include "sensorMgr.h" 


void sensorMgr::initailize(boolean isLetterBox)
{
	Serial.println( "sensorMgr");
	m_bIsLetterBox=isLetterBox;
}


long sensorMgr::ProximityVal(int Pin)
{
    long duration = 0;
    pinMode(Pin, OUTPUT);         // Sets pin as OUTPUT
    digitalWrite(Pin, HIGH);      // Pin HIGH
    pinMode(Pin, INPUT);          // Sets pin as INPUT
    digitalWrite(Pin, LOW);       // Pin LOW
    while(digitalRead(Pin) != 0)  // Count until the pin goes
       duration++;                // LOW (cap discharges)
       
    return duration;              // Returns the duration of the pulse
}


char * sensorMgr::getSersorInfo(const char * sensorName)
{
	int DoorSwitchPin = 2;
	int val = 0;
	dht 	m_DHT;                   // This sets up an equivalence between dht and DHT.
	memset(m_pResponse,0,sizeof(m_pResponse));
	if(strcmp(sensorName,STR_DETECT)==0)
	{
		long duration = 0;
		val = ProximityVal( PIN_PROXIMITY ); 
		if(val==0) 	sprintf(m_pResponse,"%s",STR_DETECT);
		else		sprintf(m_pResponse,"un%s",STR_DETECT);	
	}
	else if(strcmp(sensorName,STR_DOORSTATE)==0)
	{
		val = digitalRead( PIN_DOOR ); 
		if(val==0) 	sprintf(m_pResponse,"%s",STR_OPEN);	
		else		sprintf(m_pResponse,"%s",STR_CLOSE);
	}
	else
	{
		m_DHT.read11(PIN_DHT);
		if(strcmp(sensorName,STR_TEMPERATURE)==0)
		{
			val=m_DHT.humidity;
		}
		else val=m_DHT.temperature;
		sprintf(m_pResponse,"%d",val);
	}
	
	return m_pResponse;
}


//
// END OF FILE
//
