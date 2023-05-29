package server.network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.koossa.logger.Log;

import common.network.Message;
import server.Server;
import server.gui.StatPanel;

public class RequestHandler implements Runnable {
	
	private DatagramSocket socket;
	private Connection con;
	private Map<String, SocketAddress> connectedClients;

	public RequestHandler(DatagramSocket socket, Connection conn) {
		this.con = conn;
		this.socket = socket;
		connectedClients = new HashMap<>();
	}

	@Override
	public void run() {
		Log.debug(this, "Starting request handler.");
		while (Server.isServerRunning()) {
			
			StatPanel.setReceivedQue(con.receivedMesages.size());
			if (con.receivedMesages.size() > 0) {
				processMessage(con.receivedMesages.get(0));
				con.receivedMesages.remove(0);
			}
			
			if (con.receivedMesages.size() <= 0) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Log.debug(this, "Stopping request handler.");
	}

	private void processMessage(DatagramPacket packet) {
		byte[] data = packet.getData();
		int port = packet.getPort();
		InetAddress adress = packet.getAddress();
		
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object obj = ois.readObject();
			
			if (obj instanceof Message) {
				Message m = ((Message) obj);
				switch (m.getCommand()) {
				
				case "connect": 
					connectedClients.putIfAbsent(m.getSenderID(), packet.getSocketAddress());
					Log.info(this, m.getSenderID() + " joined the server.");
					StatPanel.addPlayer(m.getSenderID());
					break;
				
				case "disconnect":
					connectedClients.remove(m.getSenderID());
					Log.info(this, m.getSenderID() + " left the server.");
					StatPanel.removePlayer(m.getSenderID());
					break;
					
				default:
					Log.error(this, m.getSenderID() + " send an incorrect message. Discarding request.");
					return;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
