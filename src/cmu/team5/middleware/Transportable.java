package cmu.team5.middleware;

import java.io.IOException;

public interface Transportable
{
	public void addObserver(TransportObserver observer);
	public void send(String msg);
	
	public void startService() throws IOException;
}
