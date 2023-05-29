package server.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Connection {
	
	private DatagramSocket socket;
	protected List<DatagramPacket> receivedMesages;
	protected List<DatagramPacket> sendMesages;
	
	public Connection(int port) {
		try {
			socket = new DatagramSocket(25565);
			receivedMesages = Collections.synchronizedList(new ArrayList<DatagramPacket>());
			ClientConnectionReceiver ccr = new ClientConnectionReceiver(socket, this);
			new Thread(ccr, "server_message_receiver").start();
			sendMesages = Collections.synchronizedList(new ArrayList<DatagramPacket>());
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

}
