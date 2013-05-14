package metagenomics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nixie
 *
 */
public class BigramCountSort {

	private final File inputDir;
	private final int totalFiles;
	private final double CLUSTERDIFFTHRESHOLD = 0.1;
	private ArrayList<ArrayList<Read>> readClusters;
	

	/**
	 * @param inputDirName
	 * @param nClusters
	 * @throws FileNotFoundException 
	 */
	public BigramCountSort(String inputDirName, int nClusters) throws FileNotFoundException {
		readClusters = new ArrayList<ArrayList<Read>>(nClusters);
		for(int i = 0; i < nClusters; i++){
			readClusters.add(new ArrayList<Read>());
		}
		this.inputDir = new File(inputDirName);
		if (!inputDir.exists()) {
			throw new FileNotFoundException("Cannot find input directory: " + inputDirName);
		}
		File[] inputReads = inputDir.listFiles();
		totalFiles = inputReads.length;
		init(inputReads);
	}
	
	
	/**
	 * Load reads into memory
	 * @param inputReads 
	 */
	private void init(File[] inputReads) {
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
	
	public static Map<String, Integer> countGram(String s, int length){
		Map<String, Integer> counts = new HashMap<String, Integer>();
		for(int i = 0; i < s.length() - length; i++){
			String bigram = s.substring(i, i+length);
			if(!counts.containsKey(bigram))
				counts.put(bigram, 0);
			counts.put(bigram, counts.get(bigram)+1);
		}
		return counts;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
