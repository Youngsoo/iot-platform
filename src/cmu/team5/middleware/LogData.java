package cmu.team5.middleware;

public class LogData
{
    String nodeId;
    String type;
    String time;
    String name;
    String value;
    
    public LogData(String nodeId, String type, String time, String name, String value)
    {
        this.nodeId = nodeId;
        this.type = type;
        this.time = time;
        this.name = name;
        this.value = value;
    }
}
