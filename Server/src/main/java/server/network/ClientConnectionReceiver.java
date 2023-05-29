package server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.koossa.logger.Log;

import server.Server;

public class ClientConnectionReceiver implements Runnable {
	
	private DatagramSocket socket;
	private DatagramPacket packet;
	private Connection conn;
	
	public ClientConnectionReceiver(DatagramSocket socket, Connection connection) {
		this.socket = socket;
		this.conn = connection;
	}

	@Override
	public void run() {
		Log.debug(this, "Starting main message receiver.");
		try {
			socket.setSoTimeout(5000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		RequestHandler rh = new RequestHandler(socket, conn);
		new Thread(rh, "server_request_handler").start();
		while (Server.isServerRunning()) {
			try {
				packet = new DatagramPacket(new byte[5000], 4999);
				socket.receive(packet);
				if (! (packet.getData().length <= 0)) {
					conn.receivedMesages.add(packet);
					Log.debug(this, "Data packet received from: " + packet.getAddress() + ":" + packet.getPort());
				} else {
					Log.debug(this, "Discarding invalid data packet.");
				}
			} catch (SocketTimeoutException te) {
				//Log.debug(this, "Socket timeout.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.debug(this, "Shutting down main message receiver.");
		socket.close();
	}

}
