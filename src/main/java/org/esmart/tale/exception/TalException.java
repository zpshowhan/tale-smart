package org.esmart.tale.exception;

public class TalException extends RuntimeException{

    /** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;

	public TalException() {
    }

    public TalException(String message) {
        super(message);
    }

    public TalException(String message, Throwable cause) {
        super(message, cause);
    }

    public TalException(Throwable cause) {
        super(cause);
    }
}
