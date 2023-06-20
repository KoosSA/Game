package client.audio;

import org.joml.Vector3f;
import org.lwjgl.openal.AL11;

public class AudioSource {
	
	private int id;
	
	public AudioSource() {
		id = AL11.alGenSources();
		AL11.alSource3f(id, AL11.AL_POSITION, 0, 0, 0);
		setPitch(1);
		setVolume(1);
	}
	
	public AudioSource addSound(int audioFileId) {
		AL11.alSourcei(id, AL11.AL_BUFFER, audioFileId);
		return this;
	}
	
	public AudioSource play() {
		AL11.alSourcePlay(id);
		return this;
	}
	
	public AudioSource stop() {
		AL11.alSourceStop(id);
		return this;
	}
	
	public AudioSource pause() {
		AL11.alSourcePause(id);
		return this;
	}
	
	public AudioSource setPitch(float pitch) {
		AL11.alSourcef(id, AL11.AL_PITCH, pitch);
		return this;
	}
	
	public AudioSource setVolume(float volume) {
		AL11.alSourcef(id, AL11.AL_GAIN, volume);
		return this;
	}
	
	public boolean isPlaying() {
		return AL11.alGetSourcei(id, AL11.AL_SOURCE_STATE) == AL11.AL_PLAYING;
	}

	public void dispose() {
		AL11.alDeleteSources(id);
	}
	
	public void setPosition(Vector3f position) {
		AL11.alSource3f(id, AL11.AL_POSITION, position.x(), position.y(), position.z());
	}

	public AudioSource setLoop(boolean looping) {
		AL11.alSourcei(id, AL11.AL_LOOPING, looping ? AL11.AL_TRUE : AL11.AL_FALSE);
		return this;
	}
}
