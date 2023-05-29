package client.io.input.receivers.handlers;

public interface IMouseButtonInputHandler extends IInputHandler {

	void onMouseButtonPress(int button);

	void onMouseButtonDown(int button);

}