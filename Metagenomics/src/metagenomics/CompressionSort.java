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
			outputDir.delete();
		}
		outputDir.mkdir();
		clusterDirs = new File[nClusters];
		for (int i = 0; i < clusterDirs.length; i++) {
			// create directory for each cluster, allocate stuff accordingly.
			clusterDirs[i] = new File(outputDir + "/" + "cluster" + i);
			if (clusterDirs[i].exists()) {
				clusterDirs[i].delete();
			}
			clusterDirs[i].mkdir();
		}
		init();
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
		Map<File, Integer> clusterMap = new HashMap<File, Integer>();
		for (int c = 0; c < clusterDirs.length; c++) {
			File[] inputReads = clusterDirs[c].listFiles();
			for (int i = 0; i < inputReads.length; i++) {
				// compare compression lengths across clusters
				int minLength = 0;
				int belongingToCluster = 0;
				for (int j = 0; j < clusterDirs.length; j++) {
					int numBytes = getNumBytes(inputReads[i], j);
					if (j == 0) {
						minLength = numBytes;
					}
					if (numBytes < minLength) {
						minLength = numBytes;
						belongingToCluster = j;
					}
					System.out.printf(
							"numBytes: %d \t minLength: %d \t cluster: %d\n",
							numBytes, minLength, j);
				}
				// store appropriate cluster location in map.
				System.out.printf("Sort to cluster:%d \t %s\n",
						belongingToCluster, inputReads[i].getName());
				clusterMap.put(inputReads[i], belongingToCluster);

			}
		}
	}

	private int getNumBytes(File file, int c) {
		StringBuilder sb = new StringBuilder();
		File[] inputReads = clusterDirs[c].listFiles();
		for (int i = 0; i < inputReads.length; i++) {
			sb.append(getString(inputReads[i]));
		}
		sb.append(getString(file));
		byte[] b = sb.toString().getBytes();
		Deflater compresser = new Deflater();
		compresser.setInput(b);
		compresser.finish();
		return compresser.deflate(new byte[b.length]);

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
		file.renameTo(new File(clusterDirs[i].getPath() + "/" + file.getName()));
		System.out.println(file.getAbsolutePath());
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
		CompressionSort cs = new CompressionSort(args[0], args[1],
				Integer.parseInt(args[2]));
		cs.sort(0);
		System.out.println("Done compression sort");

	}

}
