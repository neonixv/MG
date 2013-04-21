package metagenomics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class ReadGenerator {

	private File outputDir;
	private String outputPrefix;
	private File[] inputFiles;

	/**
	 * @param outputDir
	 * @param outputPrefix
	 * @param inputFiles
	 */
	public ReadGenerator(String outputDir, String outputPrefix,
			File[] inputFiles) {
		this.outputDir = new File(outputDir);
		if(this.outputDir.exists()){
			CompressionSort.recursiveDelete(this.outputDir);
			this.outputDir.delete();
		}
		this.outputDir.mkdir();
		this.outputPrefix = outputPrefix;
		this.inputFiles = inputFiles;
	}


	public String randomReads(int readLength, String sequence) {
		int startPos = (int) (Math.random() * (sequence.length() - readLength));
		return sequence.substring(startPos, startPos + readLength);
	}

	public void readGenerator(int numFiles, int readLength) {
		System.out.printf("Running with %d numfiles, with %d long reads. \n", numFiles, readLength);
		int[] numRuns = getNumRuns(numFiles);
		int ithOutput = 0;
		for (int i = 0; i < inputFiles.length; i++) {
			Scanner sc = null;
			PrintWriter pw = null;
			try {
				sc = new Scanner(inputFiles[i]);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StringBuilder sb = new StringBuilder();
			while (sc.hasNextLine()) {
				sb.append(sc.nextLine());
			}
			String seq = sb.toString();
			sc.close();
			for (int j = 0; j < numRuns[i]; j++) {
				File outputFile = new File(outputDir + "/" + outputPrefix + i +"_"+ ithOutput
						+ ".txt");
				if (outputFile.exists())
					outputFile.delete();
				try {
					outputFile.createNewFile();
					pw = new PrintWriter(outputFile);
					pw.write(randomReads(readLength, seq));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally{
					pw.close();
					ithOutput++;
				}

			}
		}
	}

	public int[] getNumRuns(int numFiles) {
		// half the runs to be distributed uniformly.
		int[] numRuns = new int[inputFiles.length];
		System.out.println(numFiles);
		/*
		Arrays.fill(numRuns, numFiles / (2*inputFiles.length));
		numFiles -= numRuns[0] * numRuns.length;
		while (numFiles > 0) {
			int pos = (int) (Math.random() * inputFiles.length);
			int nReads = (int)(Math.random() * numFiles) + 1;
			numRuns[pos] += nReads;
			numFiles -= nReads;
		}
		*/

		Arrays.fill(numRuns, numFiles / inputFiles.length);
		numFiles -= numRuns[0] * numRuns.length;
		int pos = 0;
		while (numFiles > 0) {
			numRuns[pos]++;
			numFiles--;
			pos = (pos + 1 == inputFiles.length) ? 0 : pos + 1;
		}
		System.out.println(Arrays.toString(numRuns));
		return numRuns;
	}

	public static String randomDNA(Random rng, int length){
		String nucleotides = "ATCG";
		char[] c = new char[length];
		for(int i = 0; i < length; i++){
			c[i] = nucleotides.charAt(rng.nextInt(nucleotides.length())); 
		}
		return new String(c);
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length < 3) {
			System.err
					.println("Usage: ReadGenerator outputDir, outputPrefix, inputFile...");
			System.exit(0);
		}
		String[] inputFileNames = Arrays.copyOfRange(args, 2, args.length);
		File[] inputFiles = new File[inputFileNames.length];
		for (int i = 0; i < inputFileNames.length; i++) {
			File inputFile = new File(inputFileNames[i]);
			if (!inputFile.exists()) {
				System.err.println("File " + inputFileNames[i]
						+ " does not exist.");
				System.exit(0);
			}
			inputFiles[i] = inputFile;
		}
		(new ReadGenerator(args[0], args[1], inputFiles)).readGenerator(1000, 500);
		System.out.println("Done generating reads.");

	}

}
