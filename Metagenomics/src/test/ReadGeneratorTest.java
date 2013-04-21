/**
 * 
 */
package test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

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
		String previous = rg.randomReads(5,
				"asdfghjklqwertyuiopzxcvbnmqwertyuiopasdfghjklzxcvbnm");;
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
	
//	@Test
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
	
	@Test
	public void testRandomDNA(){
		String previous = ReadGenerator.randomDNA(new Random(), 10);
		boolean differenceExists = false;

		for (int i = 0; i < 40; i++) {
			System.out.println(previous);
			String actual = ReadGenerator.randomDNA(new Random(), 10);
			if (!actual.equals(previous))
				differenceExists = true;
			previous = actual;
		}
		assertTrue(differenceExists);
	}

}
