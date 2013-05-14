package test;

import java.io.File;
import java.io.FileNotFoundException;

import metagenomics.CompressionSort;
import metagenomics.ReadGenerator;

public class ExperimentalNumReads {

	private static final int lowerBound = 8;
	private static final int powerRange = 6;
	private static final int readLength = 2000;
	private static final boolean isRandom = true;

	/**
	 * Tests, varying numFiles 2^8 to 2^15
	 * ReadLength is kept constant at 2000
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("randomDNA prepended, using heap instead of files, varying numReads 2^9 to 2^15");
		for (int factor = 0; factor < powerRange; factor++) {
			for (int i = 0; i < 3; i++) {
				(new ReadGenerator("dataNR", "expNR", new File[] {
						new File("Genomes/Acidilobus-saccharovorans.fasta"),
						new File("Genomes/Caldisphaera-lagunensis.fasta") }))
						.readGenerator((int)Math.pow(2, lowerBound+factor), readLength);
				long timeStart = System.currentTimeMillis();
				CompressionSort cs;
				try {
					cs = new CompressionSort(isRandom, "dataNR", 2);
					cs.sort();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Done compression sort, took "
						+ (System.currentTimeMillis() - timeStart) + " ms.");
			}
		}

	}

}
