package org.jobjects.dao.annotation;

/**
 * ComparatorEnum
 * @author Mickaël Patron
 * @since 1.0.0
 */
public class RemoveException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * Constructor for RemoveException.
   */
  public RemoveException() {
    super();
  }

  /**
   * Constructor for RemoveException.
   * @param message
   */
  public RemoveException(String message) {
    super(message);
  }

  /**
   * Constructor for RemoveException.
   * @param message
   * @param cause
   */
  public RemoveException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructor for RemoveException.
   * @param cause
   */
  public RemoveException(Throwable cause) {
    super(cause);
  }

}
