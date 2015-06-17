package cmu.team5.middleware;

import java.io.OutputStream;

public class IoTMessage
{
	private String message;
	private OutputStream stream;
	
	public IoTMessage(String msg, OutputStream stream)
	{
		this.message = msg;
		this.stream = stream;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public OutputStream getStream()
	{
		return stream;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public void setStream(OutputStream stream)
	{
		this.stream = stream;
	}
}
