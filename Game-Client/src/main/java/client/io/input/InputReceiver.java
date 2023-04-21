package client.io.input;

public abstract class InputReceiver {
	
	protected abstract void setCallBacks();
	protected abstract void freeCallbacks();

	public void activate() {
		setCallBacks();
	}
	
	public void reset() {
		setCallBacks();
	}
	 
	public void dispose() {
		freeCallbacks();
	}
	
}
