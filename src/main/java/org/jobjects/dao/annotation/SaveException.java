package org.jobjects.dao.annotation;

/**
 * ComparatorEnum
 * @author Mickaël Patron
 * @since 1.0.0
 */
public class SaveException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * Constructor for SaveException.
   */
  public SaveException() {
    super();
  }

  /**
   * Constructor for SaveException.
   * @param message
   */
  public SaveException(String message) {
    super(message);
  }

  /**
   * Constructor for SaveException.
   * @param message
   * @param cause
   */
  public SaveException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructor for SaveException.
   * @param cause
   */
  public SaveException(Throwable cause) {
    super(cause);
  }

}
