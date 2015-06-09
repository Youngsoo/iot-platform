#include "protocol.h" 

void protocolMgr::initailize(void)
{
	bufIndex=0; 
	bufInString[bufIndex]=0;
	m_actuatorMgr.initailize();
}

int protocolMgr::parseJson(char* bufJson)
{
       StaticJsonBuffer<LENGTH_OF_JSON_STRING> jsonBuffer;
       JsonObject& root = jsonBuffer.parseObject(bufJson);
        if (!root.success()) 
         {
            Serial.println("parseObject() failed");
            return false;
        }
	const char* msgtype = root["msgtype"];
	const char* sensortype = root["sensortype"];
	int nodeid = root["nodeid"];
	int value = root["value"];
	m_actuatorMgr.setActuator(value);
	return true;

}

int protocolMgr::handleMessage(char data)
{
	int closeBraces=false;
	if(bufIndex==0 && data !='{') 
        {
            bufIndex=0;
           return false;
        }
	bufInString[bufIndex]=data;		
	bufIndex++;
        bufInString[bufIndex]=0;

	if(bufIndex>0 && data=='}') 
	{  
        	parseJson(bufInString);
                bufIndex=0;
                bufInString[bufIndex]=0;
	}
	return true;
}


