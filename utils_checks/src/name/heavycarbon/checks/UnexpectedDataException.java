package name.heavycarbon.checks;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 *******************************************************************************
 * Runtime Exception (so no need to declare it) thrown by "check" methods.
 * 
 * 2014.02.01 - Namespace changed from "com.mplify.checkers" to "com.example"
 * 2015.08.07 - Namespace changed from "com.example" to "name.heavycarbon.checks"
 ******************************************************************************/

@SuppressWarnings("serial")
public class UnexpectedDataException extends RuntimeException {

    public UnexpectedDataException() {
        super();
    }

    public UnexpectedDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedDataException(String message) {
        super(message);
    }

    public UnexpectedDataException(Throwable cause) {
        super(cause);
    }
    
}
