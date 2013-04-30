package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Summary {

	public static void main(String[] args) {
		try {
			Scanner in = new Scanner(new File(args[0]));
			int iter = Integer.parseInt(args[1]);
			System.out.println(Arrays.toString(args));
			while (in.hasNextLine()) {
				String line = in.nextLine();
				// beginning init
				if (line.charAt(0) == '[') {
					System.out.println(line);
					for (int i = 0; i < iter && in.hasNextLine(); i++) {
						int[] counts = new int[4];
						do {
							line = in.nextLine();
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
						} while (line.charAt(0) != '-' && in.hasNextLine());
						System.out.println(line);
						System.out.println(Arrays.toString(counts));
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
