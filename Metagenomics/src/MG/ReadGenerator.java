package MG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class ReadGenerator {

	private String outputDir;
	private String outputPrefix;
	private File[] inputFiles;

	public ReadGenerator(String outputDir, String outputPrefix,
			File[] inputFiles) {
		this.outputDir = outputDir;
		this.outputPrefix = outputPrefix;
		this.inputFiles = inputFiles;

	}

	public void run(int nFiles, int readLength) {
		readGenerator(nFiles, readLength);

	}

	private String randomReads(int readLength, String sequence) {
		int startPos = (int) Math.random() * (sequence.length() - readLength);
		return sequence.substring(startPos, startPos + readLength);
	}

	public void readGenerator(int numFiles, int readLength) {
		// half the runs to be distributed uniformly.
		int[] numRuns = new int[inputFiles.length];
		Arrays.fill(numRuns, (numFiles / 2) / inputFiles.length);
		numFiles -= numFiles / 2;
		while (numFiles > 0) {
			int pos = (int) Math.random() * inputFiles.length;
			int nReads = ((int) Math.random() * numFiles) + 1;
			numRuns[pos] += nReads;
			numFiles -= nReads;
		}
		for (int i = 0; i < inputFiles.length; i++) {
			BufferedReader br = null;
			PrintWriter pw = null;
			try {
				br = new BufferedReader(new FileReader(inputFiles[i]));
				String seq = br.readLine();
				br.close();
				File outputFile = new File(outputDir + "/" + outputPrefix + i
						+ ".txt");
				if (outputFile.exists())
					outputFile.delete();
				outputFile.createNewFile();
				pw = new PrintWriter(outputFile);
				for (int j = 0; j < numRuns[i]; j++)
					pw.write(randomReads(readLength, seq));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				pw.close();
			}

		}
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
		String[] inputFileNames = Arrays.copyOfRange(args, 2, args.length - 1);
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
		(new ReadGenerator(args[0], args[1], inputFiles)).run(20, 100);

	}

}
