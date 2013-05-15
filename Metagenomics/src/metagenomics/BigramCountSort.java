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
	private final int iter_threshold;
	private final double clusterDiff_threshold = 0.1;
	private final int GRAMLENGTH = 2;
	private ArrayList<ArrayList<Read>> readClusters;
	

	/**
	 * @param inputDirName
	 * @param nClusters
	 * @throws FileNotFoundException 
	 */
	public BigramCountSort(String inputDirName, int nClusters, int iter) throws FileNotFoundException {
		readClusters = new ArrayList<ArrayList<Read>>(nClusters);
		if(iter >= 0)
			iter_threshold = iter;
		else
			iter_threshold = 250;
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

	private void sort() {
		int i = 0;
		do {
			System.out.printf("----BEGINNING ITERATION %d----\n", i);
			i++;
			// keep count of read allocations as you iterate, then use that to
			// compare against cluster diff threshold
			int[] lengths = new int[] { readClusters.get(0).size(),
					readClusters.get(1).size() };
			double fileDiff = Math.min((double) lengths[0] / lengths[1],
					(double) lengths[1] / lengths[0]);
			if (fileDiff < clusterDiff_threshold) {
				System.out
						.printf("Relative cluster size max exceeded. fileDiff %f totalFiles %d\n",
								fileDiff, totalFiles);
				return;
			}
			if (i > iter_threshold)
				return;
		} while (bigramCountSort());
	}
	
	private boolean bigramCountSort() {
		//calculate gram counts in all clusters.
		Map[] clusterCounts = new Map[readClusters.size()];
		for(int i = 0; i < readClusters.size(); i++){
			String s = concat(readClusters.get(i));
			clusterCounts[i] = BigramCountSort.countGram(s, GRAMLENGTH);
		}
		
		//go through each cluster and compare gram counts.

		Map<Read, Integer> clusterMap = new HashMap<Read, Integer>();
		for(ArrayList<Read> cluster: readClusters){
			for(Read r: cluster){
				Map counts = BigramCountSort.countGram(r.getReadString(), GRAMLENGTH);
				Integer belongingToCluster = 0; //TODO change this
				//determine similarity??
				//reassign read to proper cluster
				clusterMap.put(r, belongingToCluster );
			}
		}
		return false;
	}

	private String concat(ArrayList<Read> cluster) {
		StringBuilder sb = new StringBuilder();
		for(Read r : cluster){
			sb.append(r.getReadString());
		}
		return sb.toString();
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
	
	/**
	 * TODO should somehow return total count as well. Perhaps as a GramCount object?
	 * @param s
	 * @param length
	 * @return null if length of s is less than gramlength.
	 */
	public static Map<String, Integer> countGram(String s, int length){
		if(s == null || s.length() < length)
			return null;
		//all strings are treated as upper case.
		s = s.toUpperCase();
		Map<String, Integer> counts = new HashMap<String, Integer>();
		for(int i = 0; i < s.length() - length + 1; i++){
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
		boolean isRandom = false;
		int iterThreshold = 250;
		String inputDir = null;
		int nClusters = 0;
		if (args.length < 2) {
			System.err.println("Usage: BigramCountSort <flags> inputDir nClusters");
			System.exit(0);
		}
		
		//parse flags
		for (int i = 0; i < args.length; i++){
			if(args[i].charAt(0)=='-'){
				switch(args[i].charAt(1)){
				case 'r':
					isRandom = true;
					i++;
					break;
				case 'i':
					iterThreshold = Integer.parseInt(args[++i]);
					i++;
					break;
				}	
			}
			inputDir = args[i++];
			nClusters = Integer.parseInt(args[i++]);
		}
		
		for (int i = 0; i < 5; i++) {
			(new ReadGenerator("tempBP", "reads", new File[] {
					new File("Genomes/Acidilobus-saccharovorans.fasta"),
					new File("Genomes/Caldisphaera-lagunensis.fasta") }))
					.readGenerator(40, 1024);
			long timeStart = System.currentTimeMillis();
			BigramCountSort bcs;
			try {
				bcs = new BigramCountSort("temp", nClusters, iterThreshold);
				bcs.sort();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Done compression sort, took "
					+ (System.currentTimeMillis() - timeStart) + " ms.");
		}

	}



}
