package test;

import java.io.File;

import metagenomics.CompressionSort;
import metagenomics.ReadGenerator;

public class ExperimentalNumFiles {

	/**
	 * Tests, varying numFiles 2^5 to 2^10
	 * ReadLength is kept constant at 1024
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("randomDNA prepended, varying numFiles 2^5 to 2^10");
		for (int factor = 0; factor < 5; factor++) {
			for (int i = 0; i < 3; i++) {
				(new ReadGenerator("expreads", "expr", new File[] {
						new File("Genomes/Acidilobus-saccharovorans.fasta"),
						new File("Genomes/Caldisphaera-lagunensis.fasta") }))
						.readGenerator((int)Math.pow(2, 5+factor), 1024);
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
