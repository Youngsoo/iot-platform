package cmu.team5.iotservice;

import java.io.IOException;

public class Service {

	public static void main(String[] args) {
			Broker broker = new Broker();
			
			try {
				broker.startService();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}