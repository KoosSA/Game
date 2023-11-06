package client.audio;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL11;

import client.logic.internalEvents.IUpdateHandler;

class Listener implements IUpdateHandler {
	
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f upVector;
	private FloatBuffer orientation;
	
	public Listener(Vector3f positionToLink, Vector3f forwardToLink, Vector3f upTolink) {
		registerUpdatable();
		position = positionToLink;
		rotation = forwardToLink;
		upVector = upTolink;
		orientation = BufferUtils.createFloatBuffer(6);
		
	}

	@Override
	public void update(float delta) {
		AL11.alListener3f(AL11.AL_POSITION, position.x(), position.y(), position.z());
		orientation.put(0, rotation.x());
		orientation.put(1, rotation.y());
		orientation.put(2, rotation.z());
		orientation.put(3, upVector.x());
		orientation.put(4, upVector.y());
		orientation.put(5, upVector.z());
		AL11.alListenerfv(AL11.AL_ORIENTATION, orientation);
	}
	
	
	

}
