class Protocol
{
public:
    Protocol();
    initNetwork();

    int sendSensorValue(int type, int value);
    int connectServer();
    int setHandleControlSensor(int type, int value);
}
