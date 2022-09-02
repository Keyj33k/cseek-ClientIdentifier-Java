package exceptions;

@SuppressWarnings("serial") public class InvalidOctetException extends RuntimeException {
	public InvalidOctetException() {};
	
	public InvalidOctetException(String exceptionMessage) {
		super(exceptionMessage);
	}
	
	public InvalidOctetException(Throwable causedBy) {
		super(causedBy);
	}
}
