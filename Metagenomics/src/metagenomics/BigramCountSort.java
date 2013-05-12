package metagenomics;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Nixie
 *
 */
public class BigramCountSort {

	private File inputDir;
	private int totalFiles;
	private final double CLUSTERDIFFTHRESHOLD = 0.1;
	private ArrayList<ArrayList<Read>> readClusters;
	

	/**
	 * @param inputDirName
	 * @param nClusters
	 */
	public BigramCountSort(String inputDirName, int nClusters) {
		readClusters = new ArrayList<ArrayList<Read>>(nClusters);
		for(int i = 0; i < nClusters; i++){
			readClusters.add(new ArrayList<Read>());
		}
		this.inputDir = new File(inputDirName);
		if (!inputDir.exists()) {
			System.err.println("Cannot find input directory " + inputDirName);
		}
		init();
	}
	
	/**
	 * Load reads into memory
	 */
	private void init() {
		File[] inputReads = inputDir.listFiles();
		totalFiles = inputReads.length;
		// shuffle array first
		Collections.shuffle(Arrays.asList(inputReads));
		int roundRobin = 0;
		for (int i = 0; i < inputReads.length; i++) {
			readClusters.get(roundRobin).add(
					new Read(CompressionSort.getString(inputReads[i]), roundRobin,
							inputReads[i].getName()));
			roundRobin = (roundRobin + 1 == readClusters.size()) ? 0
					: roundRobin + 1;
			boolean correctCluster = Integer.parseInt(inputReads[i].getName()
					.charAt(5) + "") == roundRobin;
			System.out.printf("\t%d,%s,%d\n", roundRobin,
					inputReads[i].getName(), (correctCluster) ? 1 : 0);
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
