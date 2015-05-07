package sep.software.anicare;

import com.google.gson.Gson;

public class AniCareException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6815048915043610626L;

	public enum TYPE {
		NETWORK_UNAVAILABLE,
		INTERNAL_ERROR,
		SERVER_ERROR,
		ILLEGAL_ARGUMENT,
		BLOBSTORAGE_ERROR
	}
	
	private TYPE type;
	
	public AniCareException() {
		super();
	}
	public AniCareException(String message) {
		
	}
	public AniCareException(TYPE type) {
		this.type = type;
	}
	public AniCareException(TYPE type, String message) {
		super(message);
		this.type = type;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public String toString() {
		return type.toString() + " / " + getMessage();
	}
	
}
