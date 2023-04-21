package client.rendering.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RenderMathsTest {

	@Test
	void testGetAspectRatio1() {
		assertEquals((float) 800 / (float) 600, RenderMaths.getAspectRatio(800, 600));
	}
	
	@Test
	void testGetAspectRatio2() {
		assertEquals(728.00000000f / 300.00000000f, RenderMaths.getAspectRatio(728, 300));
	}

}
