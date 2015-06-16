#include "protocol.h" 

void protocolMgr::initailize(void)
{
	bufIndex=0; 
	bufInString[bufIndex]=0;
	m_actuatorMgr.initailize();
	m_sensorMgr.initailize(isLetterBOx());
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
	if(value==0) m_actuatorMgr.turnOffLED(5);
	else			m_actuatorMgr.turnOnLED(5);
	m_actuatorMgr.setDoor(value);
	return true;

}
void protocolMgr::setPassword(char * pwd)
{	
	strncpy(password,pwd,LENGTH_OF_PASSWORD);
	Serial.println("setPassword");
	//Serial.println(password);
}

char * protocolMgr::getPassword(void)
{
	Serial.println("getPassword");
	Serial.println(password);
	return password;
}

boolean protocolMgr::isLetterBOx(void)
{
	if(strcmp(password,STR_HOUSE_NODE_PWD)==0) 	return false;
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

JsonObject& protocolMgr::makeConnMsg()
{
	StaticJsonBuffer<LENGTH_OF_JSON_STRING> jsonBuffer;
  	JsonObject& root = jsonBuffer.createObject();
	root["devicetype"] = "node";
#if 0
	if(isLetterBOx()==true)
			root["nodeName"] = "mailbox";
	else	root["nodeName"] = "homesecurity"; 
#endif //#if 0	
 	root["nodeid"] = getPassword(); 
	return root;
}
#if 0
JsonObject& protocolMgr::makeConnMsg()
{
	StaticJsonBuffer<LENGTH_OF_JSON_STRING> jsonBuffer;
  	JsonObject& root = jsonBuffer.createObject();
	root["devicetype"] = "node";
	if(isLetterBOx()==true)
			root["nodeName"] = "mailbox";
	else	root["nodeName"] = "homesecurity"; 
 	root["nodeid"] = getPassword(); 
	return root;
}
#endif //#if 0