package test;

import java.io.File;

import metagenomics.CompressionSort;
import metagenomics.ReadGenerator;

public class ExperimentalReadLength {

	/**
	 * Tests, varying readlength from 2^8 up to 2^15.
	 * Files are kept constant at 2000
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("randomDNA prepended, using heap instead of files. readLengths 2^9 to 2^15");
		for (int factor = 0; factor < 6; factor++) {
			for (int i = 0; i < 3; i++) {
				(new ReadGenerator("expreads", "expr", new File[] {
						new File("Genomes/Acidilobus-saccharovorans.fasta"),
						new File("Genomes/Caldisphaera-lagunensis.fasta") }))
						.readGenerator(2000, (int)Math.pow(2, 8+factor));
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
