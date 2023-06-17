package common.utils.timer;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.koossa.logger.Log;

public class Timer extends Thread {
	
	private ConcurrentLinkedDeque<TimedTask> events = new ConcurrentLinkedDeque<>();
	private LinkedList<TimedTask> toremove = new LinkedList<>();
	private boolean running;
	private static Timer instance;
	private long accuracy = 1000;
	
	public Timer() {
		setName("timer");
		setDaemon(false);
	}
	
	/**
	 * @param checkInterval ** In milliseconds (Default is 1000) and lower values gives higher accuracy but reduces performance.
	 */
	public Timer(long checkInterval) {
		this.accuracy = checkInterval;
		setName("timer");
		setDaemon(false);
	}	
	
	@Override
	public void run() {
		Log.debug(this, "Starting event timer.");
		if (instance != null) {
			return;
		}
		instance = this;
		running = true;
		long currentTime = System.currentTimeMillis();
		long prevTime = System.currentTimeMillis();
		long targetTime = accuracy;
		long delta = currentTime - prevTime;
		while (running) {
			currentTime = System.currentTimeMillis();
			delta = currentTime - prevTime;
			if (delta >= targetTime) {
				updateEventList(currentTime);
				prevTime = currentTime;
			}
			try {
				sleep(delta);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Log.debug(this, "Timer stopped");
	}
	
	private void updateEventList(long currentTime) {
		events.forEach(tt -> {
			if (tt.getCompletionTime() <= currentTime) {
				if (tt.handle()) toremove.add(tt);
			}
		});
		
		toremove.forEach(tr -> {
			events.remove(tr);
		});
	}
	
	public static void stopTimer() {
		instance.running = false;
		Log.debug(instance, "Stopping event timer.");
	}

	public static void registerNewOnceOffEventMillis(long timeToCompletionMillis, ITimedEvent event) {
		TimedTask task = new TimedTask(timeToCompletionMillis, 0, false, false, event);
		instance.events.add(task);
		Log.info(Timer.class, "Once off event added to timer: " + event);
	}
	
	public static void registerNewRepeatEventMillis(long timeToCompletionMillis, int amountOfRepeats, ITimedEvent event) {
		TimedTask task = new TimedTask(timeToCompletionMillis, amountOfRepeats, true, false, event);
		instance.events.add(task);
		Log.info(Timer.class, "Repeated event added to timer with " + amountOfRepeats + " repeats: " + event);
	}
	
	public static void registerNewInfiniteRepeatEventMillis(long timeToCompletionMillis, int amountOfRepeats, ITimedEvent event) {
		TimedTask task = new TimedTask(timeToCompletionMillis, 0, true, true, event);
		instance.events.add(task);
		Log.info(Timer.class, "Repeated event added to timer with infinite repeats: " + event);
	}

	

}
