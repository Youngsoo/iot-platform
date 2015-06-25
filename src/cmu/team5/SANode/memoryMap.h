
// 
//    FILE: memoryMap.h
// VERSION: 0.1.00
// PURPOSE: memoryMap  for Arduino

#ifndef memoryMap_h
#define memoryMap_h
#include <EEPROM.h>

/*------------------------------------------------------------------------------------------------------
|start address						|	length	|	field description		|	value description 								|
--------------------------------------------------------------------------------------------------------
|	EEPROM_ADDR_NODE_REG			|	1		|node resiter or not 		|	0:unregister, 1:register					
|	EEPROM_ADDR_SERVERIP_LENGTH		|	1		|server IP length			|	7~15						
|	EEPROM_ADDR_SERVERIP			|	15		|server IP address			|	xxx.xxx.xxx.xxx
|	EEPROM_ADDR_CONFIG_ALARM		|	1		|configurable time for alarm|	second
|	EEPROM_ADDR_CONFIG_LIGHT		|	1		|configurable time for light|	second
|	EEPROM_ADDR_ALARM				|	1		|alarm value				|	0:off, 	1:on
|	EEPROM_ADDR_LIGHT				|	1		|alarm value				|	0:off, 	1:on
|	EEPROM_ADDR_DOOR				|	1		|alarm value				|	0:close,1:open



|
---------------------------------------------------

*/
#define		EEPROM_SERVERIP_LENGTH		30
enum
{
	EEPROM_ADDR_NODE_REG=1,
	EEPROM_ADDR_SERVERIP_LENGTH,
	EEPROM_ADDR_SERVERIP,
	EEPROM_ADDR_CONFIG_ALARM=EEPROM_ADDR_SERVERIP+EEPROM_SERVERIP_LENGTH,
	EEPROM_ADDR_CONFIG_LIGHT,
	EEPROM_ADDR_ALARM,
	EEPROM_ADDR_LIGHT,
	EEPROM_ADDR_DOOR,
	
};

#endif
//
// END OF FILE
//
