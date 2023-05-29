package client.io.input.receivers.handlers;

public interface IMouseMovementInputHandler extends IInputHandler {

	void onCursorMovement(float deltaX, float deltaY);

}