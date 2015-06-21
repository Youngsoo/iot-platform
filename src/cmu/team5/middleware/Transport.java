package cmu.team5.middleware;

import java.io.*;
import java.util.Queue;

public class Transport
{
	private final static String MAGICSTRING = "ToNY";
	private Queue msgQ; // message queue to receive data from Transport class
	private Transportable transportable;
	
	public Transport(Queue q)
	{
		msgQ = q;
		
		TransportObserver observer = new TransportObserver(msgQ);
		transportable = new WiFiTransport();
		transportable.addObserver(observer);

	}
	
	public void startService() {
		new TransportRunner().start();
	}
	
	private class TransportRunner extends Thread
	{
		public void run()
		{
			try {
				transportable.startService();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int getMessageLength(InputStream stream) throws IOException {
		boolean isValidMsg = false;
		byte[] readByte = new byte[4];
		char[] magicStr = new char[4];
		int readLen = 0;
		int msgLen = 0;
		
		// check the magic string
		while(!isValidMsg) {
			readLen = stream.read(readByte, 0, 4);
			if (readLen < 0) return -1;
			
			for (int i = 0; i < readLen; i++) {
				magicStr[i] = (char)readByte[i];
			}

			if (String.valueOf(magicStr).equals(MAGICSTRING)) isValidMsg = true;
		}

		// read the message length
		readLen = stream.read(readByte, 0, 4);
		if (readLen < 0) return -1;
		
		for (int i = 0; i < readLen; i++) {
			magicStr[i] = (char)readByte[i];
		}
		
		msgLen = Integer.parseInt(String.valueOf(magicStr));
		//System.out.println("msgLen: " + msgLen);
		return msgLen;
	}
	
	public static void sendHeader(BufferedWriter out, int length) throws IOException
	{
		char[] magicString = MAGICSTRING.toCharArray();
		char[] __msgLength;
		char[] msgLength = "0000".toCharArray();
		
		__msgLength = String.valueOf(length).toCharArray();
		
		for(int i = 0; i < __msgLength.length; i++) {
			msgLength[4 - __msgLength.length + i] = __msgLength[i];
		}
		
		out.write(magicString);
		out.write(msgLength, 0, 4);

		//System.out.println("magicString: " + String.valueOf(magicString));
		//System.out.println("msgLength: " + String.valueOf(msgLength));
	}
	
	public static void sendMessage(BufferedWriter out, String message) throws IOException
	{
		char[] magicString = MAGICSTRING.toCharArray();
		char[] __msgLength;
		char[] msgLength = "0000".toCharArray();
		
		if (message == null) return;
		
		__msgLength = String.valueOf(message.length()).toCharArray();
		
		for(int i = 0; i < __msgLength.length; i++) {
			msgLength[4 - __msgLength.length + i] = __msgLength[i];
		}
		
		out.write(magicString);
		out.write(msgLength, 0, 4);
		out.write(message, 0, message.length());
		out.flush();

		//System.out.println("magicString: " + String.valueOf(magicString));
		//System.out.println("msgLength: " + String.valueOf(msgLength));
		System.out.println("Sending >> " + message);
	}
	
	public static String getMessage(InputStream in) throws IOException
	{
		byte[] buffer = new byte[1024];
    	int readBytes, leftBytes, totalBytes, msgLength;
    	
		msgLength = Transport.getMessageLength(in);
		if (msgLength < 0) return null;
		
		leftBytes = msgLength;
		readBytes = 0;
		totalBytes = 0;
		while(leftBytes > 0) {
			readBytes = in.read(buffer, totalBytes, leftBytes);
			if (readBytes < 0) return null;
			leftBytes -= readBytes;
			totalBytes += readBytes;
		}
		
		return new String(buffer, 0, msgLength);
	}
}
