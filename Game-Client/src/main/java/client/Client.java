package client;

import client.network.Connection;

public class Client {
	
	public static void main(String[] args) throws InterruptedException {
		Connection c = new Connection("Koos");
		c.connect();
		
		//Thread.sleep(5000);
		
		//c.disconnect();
	}

}
