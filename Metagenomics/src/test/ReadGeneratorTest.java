/**
 * 
 */
package test;

import static org.mockito.Mockito.mock;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;
import MG.ReadGenerator;

/**
 * @author Nixie
 * 
 */
public class ReadGeneratorTest {

	@Test
	public void testRandomReads() {
		File inputFile = mock(File.class);
		ReadGenerator rg = new ReadGenerator("test", "test",
				new File[] { inputFile });
		String previous = "";
		boolean differenceExists = false;

		for (int i = 0; i < 40; i++) {
			String actual = rg.randomReads(5,
					"asdfghjklqwertyuiopzxcvbnmqwertyuiopasdfghjklzxcvbnm");
			System.out.println(actual);
			if (!actual.equals(previous))
				differenceExists = true;
			previous = actual;
		}
		assertTrue(differenceExists);
	}

}
