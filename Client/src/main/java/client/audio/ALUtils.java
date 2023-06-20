package client.audio;

import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Objects;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.ALUtil;
import org.lwjgl.openal.EXTThreadLocalContext;
import org.lwjgl.system.MemoryUtil;

import com.koossa.logger.Log;

class ALUtils {
	
	private long device;
	private ALCCapabilities deviceCaps;
	private List<String> deviceList;
	private String defaultDevice;
	private long alcContext;
	private boolean useTLC;
	private ALCapabilities caps;
	
	public ALUtils() {
		initialiseALC();
		initialiseAL();
	}
	
	private void initialiseALC() {
		device = ALC11.alcOpenDevice((ByteBuffer) null);
		if (device == NULL) {
			Log.error(this, "Default audio device could not be set.");
			throw new IllegalStateException();
		}
		deviceCaps = ALC.createCapabilities(device);
		deviceList = ALUtil.getStringList(NULL, ALC11.ALC_ALL_DEVICES_SPECIFIER);
		if (deviceList == null) checkALCError(NULL);
		defaultDevice = Objects.requireNonNull(ALC11.alcGetString(NULL, ALC11.ALC_DEFAULT_DEVICE_SPECIFIER));
		Log.info(this, "Default audio device: " + defaultDevice);
		alcContext = ALC11.alcCreateContext(device, (IntBuffer) null);
		checkALCError(device);
		useTLC = deviceCaps.ALC_EXT_thread_local_context && EXTThreadLocalContext.alcSetThreadContext(alcContext);
        if (!useTLC) {
            if (!ALC11.alcMakeContextCurrent(alcContext)) {
                throw new IllegalStateException();
            }
        }
        checkALCError(device);
	}
	
	private void initialiseAL() {
		caps = AL.createCapabilities(deviceCaps);
	}
	
	public void dispose() {
		ALC11.alcMakeContextCurrent(NULL);
		if (useTLC) {
            AL.setCurrentThread(null);
        } else {
            AL.setCurrentProcess(null);
        }
		MemoryUtil.memFree(caps.getAddressBuffer());
		ALC11.alcDestroyContext(alcContext);
		ALC11.alcCloseDevice(device);
		ALC.destroy();
	}
	
	public static void checkALCError(long device) {
        int err = ALC11.alcGetError(device);
        if (err != ALC11.ALC_NO_ERROR) {
        	String error = ALC11.alcGetString(device, err);
        	Log.error(ALUtil.class, error);
            throw new RuntimeException(error);
        }
    }

    public static void checkALError() {
        int err = AL11.alGetError();
        if (err != AL11.AL_NO_ERROR) {
        	String error = AL11.alGetString(err);
        	Log.error(ALUtil.class, error);
            throw new RuntimeException(error);
        }
    }

}
