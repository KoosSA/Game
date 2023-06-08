package common.utils.timer;

public class TimedTask {
	
	private long completionTime, timeToCompletion;
	private int numRepeats;
	private boolean repeat;
	private boolean infiniteRepeats;
	private ITimedEvent event;
	
	
	
	public TimedTask(long timeToCompletion, int numRepeats, boolean repeat, boolean infiniteRepeats, ITimedEvent event) {
		this.completionTime = System.currentTimeMillis() + timeToCompletion;
		this.timeToCompletion = timeToCompletion;
		this.numRepeats = numRepeats;
		this.repeat = repeat;
		this.event = event;
		this.infiniteRepeats = infiniteRepeats;
	}

	public long getCompletionTime() {
		return completionTime;
	}

	public int getNumRepeats() {
		return numRepeats;
	}

	public boolean isRepeat() {
		return repeat;
	}
	
	public void removeRepeat() {
		if (infiniteRepeats) return;
		numRepeats = numRepeats - 1;
		completionTime = System.currentTimeMillis() + timeToCompletion;
	}
	
	public ITimedEvent getEvent() {
		return event;
	}

	public boolean handle() {
		event.handle();
		if (infiniteRepeats) {
			completionTime = System.currentTimeMillis() + timeToCompletion;
			return false;
		}
		if (!repeat) return true;
		removeRepeat();
		if (numRepeats <= 0) return true;
		return false;
	}

}
