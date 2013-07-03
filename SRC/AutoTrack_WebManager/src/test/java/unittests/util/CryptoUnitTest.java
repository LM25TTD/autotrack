package unittests.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.autotrack.webmanager.util.Crypto;

public class CryptoUnitTest {

	@Test
	public void testSHA1() {
		assertEquals("7110eda4d09e062aa5e4a390b0a572ac0d2c0220", Crypto.textToSHA1("1234"));
	}
	
	@Test
	public void testMD5() {
		assertEquals("81dc9bdb52d04dc20036dbd8313ed055", Crypto.textToMD5("1234"));
	}
	
	@Test
	public void testSHA256() {
		assertEquals("03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4", Crypto.textToSHA256("1234"));
	}
	
	

}
