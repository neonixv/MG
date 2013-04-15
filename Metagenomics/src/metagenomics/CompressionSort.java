package metagenomics;

import java.io.File;

public class CompressionSort {
	
	File inputDir;
	File outputDir;
	File[] clusterDirs;
	public CompressionSort(String inputDirName, String outputDirName, int nClusters){
		this.inputDir = new File(inputDirName);
		if(!inputDir.exists()){
			System.err.println("Cannot find input directory "+inputDirName);
		}
		this.outputDir = new File(outputDirName);
		if(outputDir.exists()){
			outputDir.delete();
		}
		outputDir.mkdir();
		clusterDirs = new File[nClusters];
		for(int i = 0; i < clusterDirs.length; i++){
			//create directory for each cluster, allocate stuff accordingly.
			clusterDirs[i] = new File(outputDir + "/" + "cluster" + i);
			if(clusterDirs[i].exists()){
				clusterDirs[i].delete();
			}
			clusterDirs[i].mkdir();
		}
		init();
	}

	private void init() {
		File[] inputReads = inputDir.listFiles();
		int roundRobin = 0;
		for (int i = 0; i < inputReads.length; i++){
			moveToCluster(inputReads[i], roundRobin);
			roundRobin = (roundRobin + 1 == clusterDirs.length) ? 0 : roundRobin+1;
		}
	}

	/**
	 * Move file to ith cluster.
	 * @param file
	 * @param i
	 */
	private void moveToCluster(File file, int i) {
		file.renameTo(new File(clusterDirs[i].getPath() +"/"+ file.getName()));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length != 3){
			System.err.println("Usage: CompressionSort inputDir outputDir nClusters");
			System.exit(0);
		}
		CompressionSort cs = new CompressionSort(args[0], args[1], Integer.parseInt(args[2]));
		System.out.println("Done compression sort");

	}

}
