package client.io.input.receivers.handlers;

public interface IKeyInputHandler extends IInputHandler {
	
	void onKeyPress(int key, int mods);
	void onKeyDown(int key, int mods);

}
