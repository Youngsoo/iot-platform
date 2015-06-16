package cmu.team5.middleware;

public interface Transportable
{
	public void addObserver(TransportObserver observer);
	public void send(String msg);
}
