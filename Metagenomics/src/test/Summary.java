package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Summary {

	public static void main(String[] args) {
		if (args.length == 0)
			System.err.println("usage: Summary filename iter");
		try {
			Scanner in = new Scanner(new File(args[0]));
			int iter = Integer.parseInt(args[1]);
			System.out.println(Arrays.toString(args));
			int[] counts = new int[4];
			int iterations = 0;
			while (in.hasNextLine()) {
				String line = in.nextLine();
				if (line.charAt(0) == '['){
					iterations = 0;
				}
				if(iterations > iter){
					continue;
				}
				else if (line.charAt(0) == '\t') {
					line = line.trim();
					if (line.charAt(0) == '0') {
						if (line.charAt(line.length() - 1) == '0') {
							counts[0]++;
						} else if (line.charAt(line.length() - 1) == '1') {
							counts[1]++;
						}
					} else if (line.charAt(0) == '1') {
						if (line.charAt(line.length() - 1) == '0') {
							counts[2]++;
						} else if (line.charAt(line.length() - 1) == '1') {
							counts[3]++;
						}
					}
				} else if (line.charAt(0) == '['
						|| (line.length() > 5 && line.substring(0, 5).equals(
								"----M"))) {
//				"----B"))) {
					// print and reset counts
					if (counts[0] > 0) {
						System.out.println(Arrays.toString(counts));
						System.out.printf("%.4f\n", fMeasure(counts));
						counts = new int[4];
						iterations++;
					}
					System.out.println(line);
				} else {
					System.out.println(line);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static double fMeasure(int[] counts) {
		double trueP = Math.max(counts[0], counts[1]);
		double precision = trueP / (counts[0] + counts[1]);
		double recall = trueP / (trueP + Math.min(counts[2], counts[3]));
		return 2 * (precision * recall) / (precision + recall);
	}

}