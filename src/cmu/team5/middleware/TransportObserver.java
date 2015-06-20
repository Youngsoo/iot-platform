package cmu.team5.middleware;

import java.util.Queue;

public class TransportObserver
{
	private Queue msgQ;
	
	public TransportObserver(Queue q)
	{
		msgQ = q;
	}
	
	public synchronized void notify(Object msg)
	{
		msgQ.add(msg);
	}
}
