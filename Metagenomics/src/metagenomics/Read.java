package metagenomics;

public class Read {
	String readString;

	int cluster;
	String fileName;

	public Read(String s, int c, String f) {
		this.readString = s;
		this.cluster = c;
		this.fileName = f;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cluster;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result
				+ ((readString == null) ? 0 : readString.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Read other = (Read) obj;
		if (cluster != other.cluster)
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (readString == null) {
			if (other.readString != null)
				return false;
		} else if (!readString.equals(other.readString))
			return false;
		return true;
	}

}
