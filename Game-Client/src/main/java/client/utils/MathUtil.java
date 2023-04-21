package client.utils;

public class MathUtil {
	
	public static float clamp(float value, float min, float max) {
		if (value > max) value = max;
		if (value < min) value = min;
		return value;
	}
	
	

}
