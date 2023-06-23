package client.audio;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector3f;
import org.lwjgl.openal.AL11;

import com.koossa.logger.Log;

import client.logic.internalEvents.IDisposable;

public class Audio implements IDisposable {
	
	private ALUtils utils;
	private Map<String, AudioSource> sources = new HashMap<String, AudioSource>();
	private Map<String, Integer> sounds = new HashMap<String, Integer>();
	private Listener listener;
	
	public Audio(Vector3f playerPosition, Vector3f playerForward, Vector3f playerUp) {
		Log.debug(this, "Starting OpenAL sound System.");
		utils = new ALUtils();
		registerDisposeHandler();
		listener = new Listener(playerPosition, playerForward, playerUp);
		Log.debug(this, "OpenAL started");
	}

	@Override
	public void dispose() {
		Log.debug(this, "Starting OpenAL disposed.");
		sources.forEach((name, source) -> {
			source.dispose();
		});
		sounds.forEach((name, id) -> {
			AL11.alDeleteBuffers(id);
		});
		utils.dispose();
		Log.debug(this, "OpenAL disposed.");
	}
	
	public AudioSource getPlayer(String name) {
		if (sources.containsKey(name)) {
			return sources.get(name);
		}
		AudioSource source = new AudioSource();
		sources.put(name, source);
		return source;
	}
	
	public int getSound(String name) {
		if (sounds.containsKey(name)) {
			return sounds.get(name);
		}
		int id = AudioLoader.loadAudioFile(name);
		sounds.put(name, id);
		return id;
	}
	
	public Listener getListener() {
		return listener;
	}

}
