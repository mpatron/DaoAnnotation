package org.jobjects.dao.annotation;

/**
 * ComparatorEnum
 * @author Mickaël Patron
 * @since 1.0.0
 */
public class FinderException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * Constructor for LoadException.
   */
  public FinderException() {
    super();
  }

  /**
   * Constructor for LoadException.
   * @param message
   */
  public FinderException(String message) {
    super(message);
  }

  /**
   * Constructor for LoadException.
   * @param message
   * @param cause
   */
  public FinderException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructor for LoadException.
   * @param cause
   */
  public FinderException(Throwable cause) {
    super(cause);
  }

}
