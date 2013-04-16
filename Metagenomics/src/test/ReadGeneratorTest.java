/**
 * 
 */
package test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.util.Arrays;

import metagenomics.ReadGenerator;

import org.junit.Test;

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
//			System.out.println(actual);
			if (!actual.equals(previous))
				differenceExists = true;
			previous = actual;
		}
		assertTrue(differenceExists);
	}
	
	@Test
	public void testNumRuns() {
		File inputFile1 = mock(File.class);
		File inputFile2 = mock(File.class);
		ReadGenerator rg = new ReadGenerator("test", "test",
				new File[] { inputFile1, inputFile2 });
		int[] prev_numRuns = rg.getNumRuns(20);
		boolean differenceExists = false;

		for (int i = 0; i < 40; i++) {
			int[] actual = rg.getNumRuns(20);
			System.out.println(Arrays.toString(actual));
			if(!Arrays.equals(actual, prev_numRuns))
				differenceExists = true;
			prev_numRuns = actual;
		}
		assertTrue(differenceExists);
	}

}
