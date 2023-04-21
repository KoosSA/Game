package server;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.filesystem.RootFileLocation;
import com.koossa.logger.ILogWriteAction;
import com.koossa.logger.Log;

import server.gui.LogPanel;
import server.gui.MainServerFrame;
import server.logic.ServerLoop;

public class Server {
	
	private static ServerLoop loop = null;
	private static boolean running;

	public static void main(String[] args) {
		Files.init("Resources", RootFileLocation.LOCAL);
		Log.init(Files.getCommonFolder(CommonFolders.Logs), true);
		Log.addOnLogWriteEvent(new ILogWriteAction() {
			@Override
			public void onEvent(String newEntry, String log) {
				LogPanel.getTextPane_Log().setText(LogPanel.getTextPane_Log().getText() + newEntry + "\r");
			}
		});
		MainServerFrame mainFrame = new MainServerFrame();
		mainFrame.open();
		Log.info(Server.class, "Server Application opened.");
		
	}
	
	public static void dispose() {
		Log.disposeAll();
	}

	public static void startServer() {
		if (loop != null) {
			Log.error(Server.class, "Cannot start main: Server loop already started.");
			return;
		}
		loop = new ServerLoop();
		running = true;
		new Thread(loop, "loop").start();
	}

	public static void stopServer() {
		if (loop == null) {
			Log.error(Server.class, "Cannot stop main: No main is running.");
			return;
		}
		loop.stop();
		while (loop.isRunning()) {
			Thread.yield();
		}
		running = false;
		loop = null;
	}
	
	public static boolean isServerRunning() {
		return running;
	}
	
	public static void setRunning(boolean running) {
		Server.running = running;
	}

	public static void restart() {
		Server.stopServer();
		
		Server.startServer();
	}
	
	

}
