#include "protocol.h" 

void protocolMgr::initailize(void)
{
	m_actuatorMgr.initailize();
	m_sensorMgr.initailize(isLetterBOx());
}

JsonObject* protocolMgr::parseJson(char* bufJson)
{
   	StaticJsonBuffer<STATIC_JSON_BUFFER_SIZE> jsonBuffer;
   	JsonObject& root = jsonBuffer.parseObject(bufJson);
	JsonObject* sendJson=NULL;
    if (!root.success()) 
     {
        Serial.println("parseObject() failed");
        return sendJson;
    }
	const char* messageType = root["messageType"];
	if(strcmp(messageType,"register")==0)
	{
		const char* serial = root["serial"];
		if(strcmp(serial,password)==0)
		{
			Serial.println("match");
			sendJson=makeRegisterAck(true);
		}
		else
		{
			Serial.println("unmatch");
			sendJson=makeRegisterAck(false);
		}
		
	}
	return sendJson;
	/*
	int nodeid = root["nodeid"];
	int value = root["value"];
	if(value==0) m_actuatorMgr.turnOffLED(5);
	else			m_actuatorMgr.turnOnLED(5);
	m_actuatorMgr.setDoor(value);*/
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
#if 0
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
#endif 
JsonObject& protocolMgr::makeConnMsg()
{
	StaticJsonBuffer<STATIC_JSON_BUFFER_SIZE> jsonBuffer;
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


JsonObject* protocolMgr::makeRegisterAck(boolean bSuccess)
{
	StaticJsonBuffer<STATIC_JSON_BUFFER_SIZE> jsonBuffer;
  	JsonObject& root = jsonBuffer.createObject();

	root["messageType"] = "register";
	if(bSuccess==true)
	{
		root["deviceType"] = "node";
		if(isLetterBOx()==true)
				root["nodeName"] = "mailbox";
		else	root["nodeName"] = "homesecurity"; 
		root["nodeId"] = password; 
	}
	else
	{
		root["result"] = "fail";
		root["reason"] = "serial is not correct";
	}
	
	return &root;
}
