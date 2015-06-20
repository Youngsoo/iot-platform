package cmu.team5.iotservice;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cmu.team5.middleware.*;

public class TerminalManager
{
	private HashMap<BufferedWriter, String> terminalList;
	private DataManagerIF dataMgr;
	
	public TerminalManager()
	{
		terminalList = new HashMap<BufferedWriter, String>();
		dataMgr = new DataManagerDummy();
	}
	
	private void addTerminal(String userId, OutputStream out)
	{
		System.out.println("Adding a terminal device");
		synchronized(terminalList) {
			terminalList.put(new BufferedWriter(new OutputStreamWriter(out)), userId);
		}
	}
	
	private void sendAllTerminal(String message) throws IOException
	{
		Iterator it = terminalList.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry terminal = (Map.Entry)it.next();
			
			BufferedWriter writer = (BufferedWriter)terminal.getKey();
			System.out.println("Sending message to userId:" + terminal.getValue());
			Transport.sendMessage(writer, message);
		}
	}
	
	public void removeTerminal(String userId)
	{
		synchronized(terminalList) {
			terminalList.remove(userId);
		}
	}
	
	public void handleMessage(String message) throws IOException
	{
		sendAllTerminal(message);
	}
	
	public void handleLogin(String userId, String passwd, OutputStream out) throws IOException
	{
		if (dataMgr.isValidLogin(userId, passwd)) {
			String message = Protocol.generateLoginResultMsg(userId, true, null);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			Transport.sendMessage(bw, message);
			addTerminal(userId, out);
		} else {
			String reason = dataMgr.getLoginErrMsg(userId, passwd);
			String message = Protocol.generateLoginResultMsg(userId, false, reason);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			Transport.sendMessage(bw, message);
		}
	}
	
} // class