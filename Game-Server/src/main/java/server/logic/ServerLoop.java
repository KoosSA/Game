package server.logic;

import com.koossa.logger.Log;

import server.Server;
import server.gui.StatPanel;
import server.network.Connection;

public class ServerLoop implements IServerLogic, Runnable {
	
	private boolean isRunning = true;
	private double averageTPU = 1000;
	private long TARGET_UPDATES_PER_SECOND = 60;
	private boolean run = false;

	@Override
	public void init() {
		Log.debug(this, "Starting main initialisation");
		new Connection(25565);
		
		Log.debug(this, "Server initialisation finished");
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		Log.debug(this, "Starting main disposal.");
		
		Log.debug(this, "Server disposed.");
		Server.dispose();
	}

	@Override
	public void run() {
		init();
		long previousTime = System.nanoTime();
		long deltaTime = 0;
		long targetTime = 1000000000 / TARGET_UPDATES_PER_SECOND;
		averageTPU = targetTime * 0.000000001;
		run = true;
		while (isRunning) {
			deltaTime = (System.nanoTime() - previousTime);
			if (deltaTime >= targetTime) {
				update((float) deltaTime * 0.000000001f);
				averageTPU = ((deltaTime * 0.000000001) + averageTPU) * 0.5;
				previousTime = System.nanoTime();
				StatPanel.updateStats(getTimePerUpdate(), getUpdatesPerSecond(), isRunning());
			} else {
				try {
					Thread.sleep((long) ((targetTime - deltaTime) * 0.0000003) );
				} catch (InterruptedException e) {
					Log.error(this, "Thread failed to sleep.");
					e.printStackTrace();
				}
			}
		}
		Log.debug(this, "Server loop stopped.");
		dispose();
		run = false;
	}
	
	public void stop() {
		Log.debug(this, "Stopping main.");
		isRunning = false;
		
		StatPanel.updateStats(0, 0, false);
	}
	
	public double getTimePerUpdate() {
		return averageTPU;
	}
	
	public int getUpdatesPerSecond() {
		return (int) (1.0 / averageTPU);
	}
	
	public boolean isRunning() {
		return run;
	}

}
