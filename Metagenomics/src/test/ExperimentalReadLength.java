package test;

import java.io.File;

import metagenomics.CompressionSort;
import metagenomics.ReadGenerator;

public class ExperimentalReadLength {

	/**
	 * Tests, varying readlength from 2^9 up to 2^15.
	 * Files are kept constant at 101
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("randomDNA prepended, readLengths 2^9 to 2^15");
		for (int factor = 0; factor < 6; factor++) {
			for (int i = 0; i < 3; i++) {
				(new ReadGenerator("expreads", "expr", new File[] {
						new File("Genomes/Acidilobus-saccharovorans.fasta"),
						new File("Genomes/Caldisphaera-lagunensis.fasta") }))
						.readGenerator(101, (int)Math.pow(2, 9+factor));
				long timeStart = System.currentTimeMillis();
				CompressionSort cs = new CompressionSort("expreads", "expcluster",
						2);
				cs.sort();
				System.out.println("Done compression sort, took "
						+ (System.currentTimeMillis() - timeStart) + " ms.");
			}
		}

	}

}
