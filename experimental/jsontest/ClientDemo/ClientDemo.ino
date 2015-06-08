#include <SPI.h>
#include <WiFi.h>PORTID

#include <ArduinoJson.h>

#define PORTID  550               // IP socket port ID

char ssid[] = "LGTeam5";          // The network SSID 
char c;                           // Character read from server
int status = WL_IDLE_STATUS;      // Network connection status
IPAddress server(192,168,1,117);  // The server's IP address
WiFiClient client;                // The client (our) socket
IPAddress ip;                     // The IP address of the shield
IPAddress subnet;                 // The IP address of the shield
long rssi;                        // Wifi shield signal strength
byte mac[6];                      // Wifi shield MAC address

StaticJsonBuffer<200> jsonBuffer;


void setup()  
{
    // Initialize a serial terminal for debug messages.
    Serial.begin(9600);

    Serial.println("Attempting to connect to network...");
    Serial.print("SSID: ");
    Serial.println(ssid);

    // Attempt to connect to Wifi network.
    while ( status != WL_CONNECTED) 
    { 
        Serial.print("Attempting to connect to SSID: ");
        Serial.println(ssid);
        status = WiFi.begin(ssid);
    }  

    Serial.println( "Connected to network:" );
    Serial.println( "\n----------------------------------------" );

    // Print the basic connection and network information.
    printConnectionStatus();

    Serial.println( "\n----------------------------------------\n" );

}

void loop() 
{
    // Here we attempt connect to the server on the port specified above

    Serial.print("\nAttempting to connect to server...");

    if (client.connect(server, PORTID)) 
    {
        Serial.println("connected");

        // We write a couple of messages to the server
        JsonObject& root = jsonBuffer.createObject();
        root["sensor"] = "gps";
        root["time"] = 13413523;

        JsonArray& data = root.createNestedArray("data");
        data.add(48.234243, 6);
        data.add(2.341500, 6);

        root.printTo(Serial);
        //root.printTo(client);
        root.prettyPrintTo(client);

        // Now read a message from the server

        Serial.print("Server Message: ");

        char c = ' ';
        while ( c!= '\n' )
        {
            if (client.available()) 
            {
                c = client.read();
                Serial.write(c);
            }
        } 

        // That's it. We wait a second, then do it all again.
        client.stop();
        Serial.println( "Done...");
        delay(1000);

    } // if

} //  LOOP

/************************************************************************************************
 * The following method prints out the connection information
 ************************************************************************************************/
void printConnectionStatus() 
{
    // Print the basic connection and network information: Network, IP, and Subnet mask
    ip = WiFi.localIP();
    Serial.print("Connected to ");
    Serial.print(ssid);
    Serial.print(" IP Address:: ");
    Serial.println(ip);
    subnet = WiFi.subnetMask();
    Serial.print("Netmask: ");
    Serial.println(subnet);

    // Print our MAC address.
    WiFi.macAddress(mac);
    Serial.print("WiFi Shield MAC address: ");
    Serial.print(mac[5],HEX);
    Serial.print(":");
    Serial.print(mac[4],HEX);
    Serial.print(":");
    Serial.print(mac[3],HEX);
    Serial.print(":");
    Serial.print(mac[2],HEX);
    Serial.print(":");
    Serial.print(mac[1],HEX);
    Serial.print(":");
    Serial.println(mac[0],HEX);

    // Print the wireless signal strength:
    rssi = WiFi.RSSI();
    Serial.print("Signal strength (RSSI): ");
    Serial.print(rssi);
    Serial.println(" dBm");

} // printConnectionStatus
