
// 
//    FILE: protocolMgr.h
// VERSION: 0.1.00
// PURPOSE: protocolMgr  for Arduino

#ifndef protocolMgr_h
#define protocolMgr_h
#include <EEPROM.h>


#include "../ArduinoJson/ArduinoJson.h"
#include "actuatorMgr.h"    
#include "sensorMgr.h" 

#include "memoryMap.h" 


#if ARDUINO >= 100
 #include "Arduino.h"
#else
 #include "WProgram.h"
#endif

#define LENGTH_OF_JSON_STRING		100	
#define STATIC_JSON_BUFFER_SIZE		200

#define LENGTH_OF_PASSWORD			4
#define STR_HOUSE_NODE_PWD			"a2de"


#define STR_MESSAGE_TYPE			"messageType"	
#define	STR_DEVIDE_TYPE				"deviceType"
#define	STR_NODE_NAME				"nodeName"
#define	STR_NODE_ID					"nodeId"
#define	STR_SENSOR_TYPE				"sensorType"
#define	STR_SENSOR					"sensor"
#define	STR_ACTUATOR				"actuator"
#define	STR_COMMAND					"command"
#define	STR_CONFIGURABLET_TIME		"configTime"

#define	STR_NODE_DATA				"nodeData"

#define	STR_VALUE					"value"
#define	STR_NODE					"node"
#define	STR_REGISTER				"register"
#define	STR_UNREGISTER				"unregister"
#define STR_SERVER_IP				"serverIp"
#define STR_ACTUATOR_TYPE			"actuatorType"
#define STR_ACTUATOR				"actuator"
#define	STR_MAILBOX					"mailbox"
#define	STR_HOMESECURITY			"homesecurity"
#define	STR_RESULT					"result"
#define	STR_FAIL					"fail"
#define	STR_REASON					"reason"
#define STR_INFORMATION				"information"
#define STR_EMERGENCY				"emergency"

#define STR_CONTENTS				"contents"
#define STR_CONFIGURABLE_TYPE		"configType"
#define STR_TIME					"time"



#define STR_ASK_CONTENTS			"House is vacant, set alarm?"
#define STR_EMERGENCE_CONTENTS		"Alarm by door open or sudden occupation"
#define STR_DISABLE_OPEN			"The door is unable to open because alarm is set"
#define STR_MAIL_DELIVERY			"Mail is delivered!"
#define STR_MAIL_TAKEN				"Mailbox is empty!"



class protocolMgr
{
public:
	void 		initailize(void);
	//int 		handleMessage(char bOpen);
	JsonObject&  makeConnMsg(void);
	void 		setPassword(char * pwd);
	char *		getPassword(void);
	boolean		isLetterBox(void);
	JsonObject* parseJson(char* bufJson);
	JsonObject* makeRegisterAck(boolean bSuccess);
	void 		setRegisterInfo(boolean bRegsiter,const char* serverIP);
	void 		readStringFromEEPROM(char* pString,int offset,int length);
	void 		writeString2EEPROM(const char* pString,int offset,int length);
	char * 		getSersorValue(const char * sensorName);
	JsonObject& makeSensorValue(const char *sensorName,char * value);
	JsonObject& makeNotificationMessage(char *msgType,char * contents);
	void 		controlActuator(const char * target,const char * commnad);
	JsonObject& makeActuatorValue(const char *actuatorName,char * value);
	int 		getAutoTurnOffLight(void);
	int 		getAutoAlarmTime(void);
private:
	int 	nodeType;
	char	password[5];
	actuatorMgr m_actuatorMgr;
	sensorMgr	m_sensorMgr;
	int			m_nAutoAlarmTime;
	int			m_nAutoTurnOffLight;
};
#endif
//
// END OF FILE
//
