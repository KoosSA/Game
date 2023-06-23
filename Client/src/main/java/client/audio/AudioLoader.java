package client.audio;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL11;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryUtil;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

class AudioLoader {
	
	private static int sampleRate;
	private static int channels;
	private static IntBuffer err;
	
	static {
		err = BufferUtils.createIntBuffer(1);
	}
	
	public static int loadAudioFile(String fileName) {
		Log.debug(AudioLoader.class, "Loading audio file: " + fileName);
		String path = Files.getCommonFolderPath(CommonFolders.Sounds) + "/" + fileName;
		err.clear();
		ShortBuffer data = null;
		if (fileName.endsWith(".ogg")) {
			STBVorbisInfo vinfo = STBVorbisInfo.create();
			long pointer = STBVorbis.stb_vorbis_open_filename(path, err, null);
			if (pointer == MemoryUtil.NULL) {
				Log.error(AudioLoader.class, "STBVorbis error: " + STBVorbis.stb_vorbis_get_error(pointer));
				return 0;
			}
			STBVorbis.stb_vorbis_get_info(pointer, vinfo);
			channels = vinfo.channels();
			sampleRate = vinfo.sample_rate();
			data = BufferUtils.createShortBuffer(STBVorbis.stb_vorbis_stream_length_in_samples(pointer) * channels);
			STBVorbis.stb_vorbis_get_samples_short_interleaved(pointer, channels, data);
			STBVorbis.stb_vorbis_close(pointer);
		}
		if (data == null) {
			Log.error(AudioLoader.class, "Could not load data for audio file: " + fileName);
			return 0;
		}
		
		int id = AL11.alGenBuffers();
		AL11.alBufferData(id, channels == 1 ? AL11.AL_FORMAT_MONO16 : AL11.AL_FORMAT_STEREO16, data, sampleRate);
		Log.debug(AudioLoader.class, "Audio file " + fileName + " loaded with id: " + id);
		return id;
	}
	
	

}
