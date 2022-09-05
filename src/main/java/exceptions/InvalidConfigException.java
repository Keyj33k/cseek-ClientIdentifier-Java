package exceptions;

@SuppressWarnings("serial") public class InvalidConfigException extends RuntimeException{
	public InvalidConfigException() {};
	
	public InvalidConfigException(String exceptionMessage) {
		super(exceptionMessage);
	}
	
	public InvalidConfigException(Throwable causedBy) {
		super(causedBy);
	}
}
