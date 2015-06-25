package cmu.team5.middleware;

import java.io.OutputStream;

public class IoTMessage
{
	private int clientNumber;
	private String message;
	private OutputStream stream;
	private boolean isClosed;
	
	public IoTMessage(int clientNumber, String msg, OutputStream stream)
	{
		this.clientNumber = clientNumber;
		this.message = msg;
		this.stream = stream;
		this.isClosed = false;
	}
	
	public IoTMessage(int clientNumber, boolean isClosed)
	{
		this.clientNumber = clientNumber;
		this.isClosed = isClosed;
		this.message = null;
		this.stream = null;
	}
	
	public int getClientNumber()
	{
		return clientNumber;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public OutputStream getStream()
	{
		return stream;
	}
	
	public void setClosed(boolean isClosed)
	{
		this.isClosed = isClosed;
	}
	
	public boolean isClosed()
	{
		return isClosed;
	}
	
}
