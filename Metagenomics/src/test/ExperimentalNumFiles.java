package test;

import java.io.File;

import metagenomics.CompressionSort;
import metagenomics.ReadGenerator;

public class ExperimentalNumFiles {

	/**
	 * Tests, varying numFiles 2^8 to 2^15
	 * ReadLength is kept constant at 2000
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("randomDNA prepended, using heap instead of files, varying numFiles 2^9 to 2^15");
		for (int factor = 0; factor < 6; factor++) {
			for (int i = 0; i < 3; i++) {
				(new ReadGenerator("expreads", "expr", new File[] {
						new File("Genomes/Acidilobus-saccharovorans.fasta"),
						new File("Genomes/Caldisphaera-lagunensis.fasta") }))
						.readGenerator((int)Math.pow(2, 8+factor), 2000);
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
