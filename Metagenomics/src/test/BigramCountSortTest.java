/**
 * 
 */
package test;

import java.io.FileNotFoundException;
import java.util.Map;

import metagenomics.BigramCountSort;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Nixie
 *
 */
public class BigramCountSortTest {

	/**
	 * Test method for {@link metagenomics.BigramCountSort#BigramCountSort(java.lang.String, int)}.
	 */
	@Test
	public void testBigramCountSort() {
		Map<String, Integer> counts;
		counts = BigramCountSort.countGram("AA", 2);
		assertEquals(1, (int)counts.get("AA"));
		counts = BigramCountSort.countGram("aa", 2);
		assertEquals(1, (int)counts.get("AA"));
		assertEquals(1, (int)counts.get("total"));
		
		counts = BigramCountSort.countGram("AAA", 2);
		assertEquals(2, (int)counts.get("AA"));

		counts = BigramCountSort.countGram("AAA", 5);
		assertNull(counts);
		
		counts = BigramCountSort.countGram("ABA", 2);
		assertEquals(1, (int)counts.get("AB"));
		assertEquals(1, (int)counts.get("BA"));
		assertNull(counts.get("AA"));

		counts = BigramCountSort.countGram(null, 5);
		assertNull(counts);

		counts = BigramCountSort.countGram("ABAB", 2);
		assertEquals(3, (int)counts.get("total"));
		assertEquals(2, (int)counts.get("AB"));
		assertEquals(1, (int)counts.get("BA"));
		assertNull(counts.get("AA"));
		counts = BigramCountSort.countGram("", 5);
		assertNull(counts);
		
	}

	/**
	 * Test method for {@link metagenomics.BigramCountSort#countGram(java.lang.String, int)}.
	 * @throws FileNotFoundException 
	 */
	@Test(expected=FileNotFoundException.class) 
	public void testCountGram() throws FileNotFoundException {
		new BigramCountSort("asdf", 0,0);
	}

}
