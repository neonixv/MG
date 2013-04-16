package metagenomics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.Deflater;

public class CompressionSort {

	File inputDir;
	File outputDir;
	File[] clusterDirs;

	public CompressionSort(String inputDirName, String outputDirName,
			int nClusters) {
		this.inputDir = new File(inputDirName);
		if (!inputDir.exists()) {
			System.err.println("Cannot find input directory " + inputDirName);
		}
		this.outputDir = new File(outputDirName);
		if (outputDir.exists()) {
			recursiveDelete(outputDir);
			outputDir.delete();
		}
		outputDir.mkdir();
		clusterDirs = new File[nClusters];
		for (int i = 0; i < clusterDirs.length; i++) {
			// create directory for each cluster, allocate stuff accordingly.
			clusterDirs[i] = new File(outputDir + "/" + "cluster" + i);
			if (clusterDirs[i].exists()) {
				recursiveDelete(clusterDirs[i]);
				clusterDirs[i].delete();
			}
			clusterDirs[i].mkdir();
		}
		init();
	}

	public static void recursiveDelete(File dir) {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++){
			files[i].delete();
		}		
	}

	private void init() {
		File[] inputReads = inputDir.listFiles();
		int roundRobin = 0;
		for (int i = 0; i < inputReads.length; i++) {
			moveToCluster(inputReads[i], roundRobin);
			roundRobin = (roundRobin + 1 == clusterDirs.length) ? 0
					: roundRobin + 1;
		}
	}

	public void sort(int iterations) {
		for (int i = 0; i < iterations; i++) {
			sort();
		}
	}

	public void sort() {
		Map<File, Integer> clusterMap = new HashMap<File, Integer>();
		for (int c = 0; c < clusterDirs.length; c++) {
			File[] inputReads = clusterDirs[c].listFiles();
			for (int i = 0; i < inputReads.length; i++) {
				// compare compression lengths across clusters
				int minDist = 0;
				int belongingToCluster = 0;
				for (int j = 0; j < clusterDirs.length; j++) {
					//if file already belongs to same cluster, only compute ratio once?
					int compressDist = getCompressDist(inputReads[i], j);
					if (j == 0) {
						minDist = compressDist;
					}
					if (compressDist < minDist) {
						minDist = compressDist;
						belongingToCluster = j;
					}
//					 System.out.printf(
//					 "compressDist: %d \t minDist: %d \t cluster: %d\n",
//					 compressDist, minDist, j);
				}
				// store appropriate cluster location in map.
//				System.out.printf("Sort to cluster:%d \t %s\n",
//						belongingToCluster, inputReads[i].getName());
				clusterMap.put(inputReads[i], belongingToCluster);
			}
		}
		// move files to proper clusters
		for (File f : clusterMap.keySet()) {
			moveToCluster(f, clusterMap.get(f));
		}

	}

	private int getCompressDist(File file, int c) {
		StringBuilder sb = new StringBuilder();
		File[] inputReads = clusterDirs[c].listFiles();
		for (int i = 0; i < inputReads.length; i++) {
			if (!inputReads[i].getName().equals(file.getName())) {
				sb.append(getString(inputReads[i]));
			}
		}
		//calculate compression size without file
		byte[] b = sb.toString().getBytes();
		Deflater compresser = new Deflater();
		compresser.setInput(b);
		compresser.finish();
		int bytesWithoutFile = compresser.deflate(new byte[b.length]);
		//now with the file
		sb.append(getString(file));
		b = sb.toString().getBytes();
		compresser = new Deflater();
		compresser.setInput(b);
		compresser.finish();
		return compresser.deflate(new byte[b.length]) - bytesWithoutFile;
	}

	private String getString(File file) {

		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		while (sc.hasNextLine()) {
			sb.append(sc.nextLine());
		}
		sc.close();
		return sb.toString();

	}

	/**
	 * Move file to ith cluster.
	 * 
	 * @param file
	 * @param i
	 */
	private void moveToCluster(File file, int i) {
		File newPath = new File(clusterDirs[i].getPath() + "/" + file.getName());
		file.renameTo(newPath);
		System.out.println("New path" + newPath.getAbsolutePath());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			System.err
					.println("Usage: CompressionSort inputDir outputDir nClusters");
			System.exit(0);
		}
		(new ReadGenerator(args[0], args[1], new File[] {
				new File("Genomes/Acidilobus-saccharovorans.fasta"),
				new File("Genomes/Caldisphaera-lagunensis.fasta") })).run(100,
				500);
		CompressionSort cs = new CompressionSort(args[0], args[1],
				Integer.parseInt(args[2]));
		cs.sort(20);
		System.out.println("Done compression sort");

	}

}
