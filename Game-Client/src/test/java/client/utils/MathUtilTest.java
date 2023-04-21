package client.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MathUtilTest {

	@Test
	void testClamp1() {
		assertEquals(5, MathUtil.clamp(10, 2, 5));
	}
	
	@Test
	void testClamp2() {
		assertEquals(5, MathUtil.clamp(5, 2, 5));
	}
	
	@Test
	void testClamp3() {
		assertEquals(3, MathUtil.clamp(3, 2, 5));
	}
	
	@Test
	void testClamp4() {
		assertEquals(2, MathUtil.clamp(2, 2, 5));
	}
	
	@Test
	void testClamp5() {
		assertEquals(2, MathUtil.clamp(-10, 2, 5));
	}
	
	@Test
	void testClamp6() {
		assertEquals(-5, MathUtil.clamp(-5, -5, 5));
	}

}
