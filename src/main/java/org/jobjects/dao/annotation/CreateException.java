package org.jobjects.dao.annotation;

/**
 * ComparatorEnum
 * @author Mickaël Patron
 * @since 1.0.0
 */
public class CreateException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * Constructor for CreateDBException.
   */
  public CreateException() {
    super();
  }

  /**
   * Constructor for CreateDBException.
   * @param message
   */
  public CreateException(String message) {
    super(message);
  }

  /**
   * Constructor for CreateDBException.
   * @param message
   * @param cause
   */
  public CreateException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructor for CreateDBException.
   * @param cause
   */
  public CreateException(Throwable cause) {
    super(cause);
  }

}
