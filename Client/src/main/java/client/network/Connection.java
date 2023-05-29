package client.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import com.koossa.logger.Log;

import common.network.Message;

public class Connection {
	
	private DatagramSocket socket;
	private ObjectOutputStream streamOut;
	private String username;
	
	public Connection(String username) {
		try {
			socket = new DatagramSocket(25566);
			socket.connect(new InetSocketAddress("localhost", 25565));
			this.username = username;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void connect() {
		try {
			socket.send(createPacket(new Message(null, username, "connect")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		try {
			socket.send(createPacket(new Message(null, username, "disconnect")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private DatagramPacket createPacket(Message message) {
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream(5000);
			streamOut = new ObjectOutputStream(b);
			streamOut.writeObject(message);
			byte[] data = b.toByteArray();
			return new DatagramPacket(data, data.length);
		} catch (IOException e) {
			Log.error(this, "Failed to create data packet to send to server.");
			e.printStackTrace();
		}
		return new DatagramPacket(null, 0);
	}

}
