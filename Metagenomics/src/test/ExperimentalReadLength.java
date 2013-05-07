package test;

import java.io.File;

import metagenomics.CompressionSort;
import metagenomics.ReadGenerator;

public class ExperimentalReadLength {

	private static final int lowerBound = 9;
	private static final int powerRange = 6;

	/**
	 * Tests, varying readlength from 2^9 up to 2^15. Files are kept constant at
	 * 2000
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out
				.printf("randomDNA prepended, using heap instead of files. readLengths 2^%d to 2^%d\n",
						lowerBound, lowerBound + powerRange);
		for (int factor = 0; factor < powerRange; factor++) {
			for (int i = 0; i < 3; i++) {
				(new ReadGenerator("expreads", "expr", new File[] {
						new File("Genomes/Acidilobus-saccharovorans.fasta"),
						new File("Genomes/Caldisphaera-lagunensis.fasta") }))
						.readGenerator(2000,
								(int) Math.pow(2, lowerBound + factor));
				long timeStart = System.currentTimeMillis();
				CompressionSort cs = new CompressionSort("expreads",
						"expcluster", 2);
				cs.sort();
				System.out.println("Done compression sort, took "
						+ (System.currentTimeMillis() - timeStart) + " ms.");
			}
		}

	}

}
