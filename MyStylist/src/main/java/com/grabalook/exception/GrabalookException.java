package com.grabalook.exception;

public class GrabalookException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7875656819467776383L;
	
	public GrabalookException(Throwable cause) {
        super(cause);
    }
    
    public GrabalookException(String message) {
        super(message);
    }

    public GrabalookException(String message, Throwable cause) {
        super(message, cause);
    }

}
