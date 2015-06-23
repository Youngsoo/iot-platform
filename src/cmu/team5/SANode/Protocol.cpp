#include "protocol.h" 

void protocolMgr::initailize(void)
{
	m_actuatorMgr.initailize();
	m_sensorMgr.initailize(isLetterBox());
}
void protocolMgr::writeString2EEPROM(const char* pString,int offset,int length)
{
	int iCount=0;
	for(iCount=0; iCount< length; iCount++)
	{
		EEPROM.write(offset+iCount, pString[iCount]);			
	}
}

void protocolMgr::readStringFromEEPROM(char* pString,int offset,int length)
{
	int iCount=0;
	for(iCount=0; iCount< length; iCount++)
	{
		pString[iCount]=EEPROM.read(offset+iCount);
	}
}


void protocolMgr::setRegisterInfo(boolean bRegsiter,const char* serverIP)
{	int value;
	if(bRegsiter==true)
	{	
		EEPROM.write(EEPROM_ADDR_SERVERIP_LENGTH, strlen(serverIP));
		writeString2EEPROM(serverIP,EEPROM_ADDR_SERVERIP,strlen(serverIP));
		//EEPROM.put(EEPROM_ADDR_SERVERIP, stringServerIP);			
	}
	EEPROM.write(EEPROM_ADDR_NODE_REG, bRegsiter);
	
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
	const char* messageType = root[STR_MESSAGE_TYPE];
	if(strcmp(messageType,STR_REGISTER)==0)
	{
		const char* serial = root["serial"];
		if(strcmp(serial,password)==0)
		{
			const char* serverip = root[STR_SERVER_IP];
			setRegisterInfo(true,serverip);			
			sendJson=makeRegisterAck(true);
		}
		else
		{
			Serial.println("unmatch");
			sendJson=makeRegisterAck(false);
		}
		
	}
	else if(strcmp(messageType,STR_UNREGISTER)==0)
	{
		setRegisterInfo(false,NULL);
	}
	else if(strcmp(messageType,STR_COMMAND)==0)
	{
		const char* actuatorType = root[STR_ACTUATOR_TYPE];
		const char* value = root[STR_VALUE];
		Serial.println(actuatorType);
		Serial.println(value);
		if(strcmp(actuatorType,STR_DOOR)==0 && strcmp(value,STR_OPEN)==0)
		{
			if(EEPROM.read(EEPROM_ADDR_ALARM)==true) 
			{
				JsonObject& json=makeNotificationMessage(STR_INFORMATION,STR_DISABLE_OPEN);	
				return &json;
			}
		}
		m_actuatorMgr.handleActuator(actuatorType,value);
	}	
	return sendJson;

}
void protocolMgr::setPassword(char * pwd)
{	
	strncpy(password,pwd,LENGTH_OF_PASSWORD);
	//Serial.println("setPassword");
	Serial.println(password);
}

char * protocolMgr::getPassword(void)
{
	Serial.println("getPassword");
	Serial.println(password);
	return password;
}

boolean protocolMgr::isLetterBox(void)
{
	if(strcmp(password,STR_HOUSE_NODE_PWD)==0) 	return false;
	return true;
}

JsonObject& protocolMgr::makeConnMsg()
{
	StaticJsonBuffer<STATIC_JSON_BUFFER_SIZE> jsonBuffer;
  	JsonObject& root = jsonBuffer.createObject();
	root[STR_DEVIDE_TYPE] = STR_NODE;
	
#if 0
	if(isLetterBox()==true)
			root["nodeName"] = "mailbox";
	else	root["nodeName"] = "homesecurity"; 
#endif //#if 0	
 	root[STR_NODE_ID] = getPassword(); 
	return root;
}

JsonObject* protocolMgr::makeRegisterAck(boolean bSuccess)
{
	StaticJsonBuffer<STATIC_JSON_BUFFER_SIZE> jsonBuffer;
  	JsonObject& root = jsonBuffer.createObject();

	root[STR_MESSAGE_TYPE] = STR_REGISTER;
	if(bSuccess==true)
	{
		root[STR_DEVIDE_TYPE] = STR_NODE;
		if(isLetterBox()==true)
				root[STR_NODE_NAME] = STR_MAILBOX;
		else	root[STR_NODE_NAME] = STR_HOMESECURITY; 
		root[STR_NODE_ID] = password; 
	}
	else
	{
		root[STR_RESULT] = STR_FAIL;
		root[STR_REASON] = "serial is not correct";
	}
	
	return &root;
}

JsonObject& protocolMgr::makeSensorValue(const char *sensorName,char * value)
{
	StaticJsonBuffer<STATIC_JSON_BUFFER_SIZE> jsonBuffer;
  	JsonObject& root = jsonBuffer.createObject();

	root[STR_MESSAGE_TYPE] = STR_SENSOR;
	root[STR_SENSOR_TYPE] = sensorName;
	root[STR_NODE_ID] = password; 
	root[STR_VALUE] = value; 
	
	return root;
}
JsonObject& protocolMgr::makeActuatorValue(const char *actuatorName,char * value)
{
	StaticJsonBuffer<STATIC_JSON_BUFFER_SIZE> jsonBuffer;
  	JsonObject& root = jsonBuffer.createObject();

	root[STR_MESSAGE_TYPE] = STR_ACTUATOR;
	root[STR_ACTUATOR_TYPE] = actuatorName;
	root[STR_NODE_ID] = password; 
	root[STR_VALUE] = value; 
	
	return root;
}

JsonObject& protocolMgr::makeNotificationMessage(char *msgType,char * contents)
{
	StaticJsonBuffer<STATIC_JSON_BUFFER_SIZE> jsonBuffer;
  	JsonObject& root = jsonBuffer.createObject();

	root[STR_MESSAGE_TYPE] = msgType;
	root[STR_NODE_ID] = password; 
	root[STR_CONTENTS] = contents;
	return root;
}


char * protocolMgr::getSersorValue(const char * sensorName)
{
	return m_sensorMgr.getSersorInfo(sensorName);
}

void protocolMgr::controlActuator(const char * target,const char * commnad)
{
	m_actuatorMgr.handleActuator(target,commnad);
}


