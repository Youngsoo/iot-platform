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
	private HashMap<Integer, BufferedWriter> clientList;
	private DataManagerIF dataMgr;
	
	public TerminalManager()
	{
		terminalList = new HashMap<BufferedWriter, String>();
		clientList = new HashMap<Integer, BufferedWriter>();
		dataMgr = new DataManagerDummy();
	}
	
	private void addTerminal(String userId, OutputStream out, int clientNumber)
	{
		System.out.println("Adding a terminal device");
		BufferedWriter bwout = new BufferedWriter(new OutputStreamWriter(out));
		synchronized(terminalList) {
			terminalList.put(bwout, userId);
			clientList.put(clientNumber, bwout);
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
	
	public void removeTerminal(int clientNumber)
	{
		System.out.println("Remove terminal device. (clientNumber:" + clientNumber + ")");
		BufferedWriter out = clientList.get(clientNumber);
		synchronized(terminalList) {
			terminalList.remove(out);
		}
		synchronized(clientList) {
			clientList.remove(clientNumber);
		}
	}
	
	public void handleMessage(String message) throws IOException
	{
		sendAllTerminal(message);
	}
	
	public void handleLogin(String userId, String passwd, OutputStream out, int clientNumber) throws IOException
	{
		if (dataMgr.isValidLogin(userId, passwd)) {
			String message = Protocol.generateLoginResultMsg(userId, true, null);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			Transport.sendMessage(bw, message);
			addTerminal(userId, out, clientNumber);
		} else {
			String reason = dataMgr.getLoginErrMsg(userId, passwd);
			String message = Protocol.generateLoginResultMsg(userId, false, reason);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			Transport.sendMessage(bw, message);
		}
	}
	
} // class