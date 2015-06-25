package cmu.team5.middleware;

import java.util.Date;

public class LogData
{
    public String nodeId;
    public String type;
    public Date time;
    public String name;
    public String value;
    
    public LogData(String nodeId, String type, Date time, String name, String value)
    {
        this.nodeId = nodeId;
        this.type = type;
        this.time = time;
        this.name = name;
        this.value = value;
    }
}
