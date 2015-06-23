
// 
//    FILE: memoryMap.h
// VERSION: 0.1.00
// PURPOSE: memoryMap  for Arduino

#ifndef memoryMap_h
#define memoryMap_h
#include <EEPROM.h>

/*------------------------------------------------------------------------------------------------------
|start address	|	length	|	field description	|	value description 								|
--------------------------------------------------------------------------------------------------------
|	0			|	1		|node resiter or not 	|	0:unregister, 1:register					
|	1			|	1		|server IP length		|	7~15						
|	2			|	15		|server IP address		|
|
---------------------------------------------------

*/
#define		EEPROM_SERVERIP_LENGTH		30
enum
{
	EEPROM_ADDR_NODE_REG=1,
	EEPROM_ADDR_SERVERIP_LENGTH,
	EEPROM_ADDR_SERVERIP,
	EEPROM_ADDR_ALARM_TIME_MIN=EEPROM_ADDR_SERVERIP+EEPROM_SERVERIP_LENGTH,
	EEPROM_ADDR_WINDOW_HOUR,
};

#endif
//
// END OF FILE
//
