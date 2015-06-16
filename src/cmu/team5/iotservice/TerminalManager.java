package cmu.team5.iotservice;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

public class TerminalManager
{
	private HashMap<String, PrintWriter> terminalList;
	
	public TerminalManager()
	{
		terminalList = new HashMap<String, PrintWriter>();
	}
	
	public void addTerminal(String userId, OutputStream out)
	{
		System.out.println("Adding a terminal device");
		synchronized(terminalList) {
			terminalList.put(userId, new PrintWriter(out, true));
		}
	}
	
	public void removeTerminal(String userId)
	{
		synchronized(terminalList) {
			terminalList.remove(userId);
		}
	}
}