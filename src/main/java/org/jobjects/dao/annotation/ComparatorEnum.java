/*
 * Created on 6 juil. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.jobjects.dao.annotation;

/**
 * ComparatorEnum
 * 
 * @author Mickaël Patron
 * @since 1.0.0
 */
public enum ComparatorEnum {

  EQUAL("="),
  NOT_EQUAL("!="),
  LESS_THAN("<"),
  LESS_THAN_OR_EQUAL("<="),
  UPPER_THAN(">"),
  UPPER_THAN_OR_EQUAL(">=");

  /**
   * @param comparator
   */
  private ComparatorEnum(String comparator) {
    this.comparator = comparator;
  }

  private String comparator;

  public String getComparator() {
    return comparator;
  }

  public void setComparator(String comparator) {
    this.comparator = comparator;
  }

}
