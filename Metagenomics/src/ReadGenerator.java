
public class ReadGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private String randomReads(int readLength, String sequence){
		int startPos = (int) Math.random() * (sequence.length() - readLength);
		return sequence.substring(startPos, startPos+readLength);
	}

}
