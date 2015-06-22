#include "sensorMgr.h" 


void sensorMgr::initailize(boolean isLetterBox)
{
	Serial.println( "sensorMgr");
	m_bIsLetterBox=isLetterBox;
}


char * sensorMgr::getSersorInfo(char * sensorName)
{
	int DoorSwitchPin = 2;
	int val = 0;
	dht 	m_DHT;                   // This sets up an equivalence between dht and DHT.
	memset(m_pResponse,0,sizeof(m_pResponse));
	if(strcmp(sensorName,STR_EXIST)==0)
	{
		val = digitalRead( PIN_PROXIMITY ); 
		if(val==0) 	sprintf(m_pResponse,"%s",STR_EXIST);
		else		sprintf(m_pResponse,"Not %s",STR_EXIST);	
	}
	else if(strcmp(sensorName,STR_DOORSTATE)==0)
	{
		val = digitalRead( PIN_DOOR ); 
		if(val==0) 	sprintf(m_pResponse,"%s",STR_CLOSED);
		else		sprintf(m_pResponse,"%s",STR_OPENED);	
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
