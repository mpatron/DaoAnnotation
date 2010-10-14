/**
 * Created on 13 déc. 2002
 *
 */
package org.jobjects.dao.annotation;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

/**
 * ComparatorEnum
 * 
 * @author Mickaël Patron
 * @since 1.0.0
 */
public class ManagerTools {

  protected final String addOrderByAndWhere(String orderField, boolean orderDest, String whereField, String whereOperator, String whereValue) {
    String returnValue = StringUtils.EMPTY;

    if ((whereField != null) && (!whereField.equals("")) && (whereOperator != null) && (!whereOperator.equals(""))) {
      if ((whereValue == null) || whereValue.equals("")) {
        returnValue += " WHERE " + whereField + whereOperator + "NULL";
      } else {
        returnValue += " WHERE " + whereField + whereOperator + whereValue;
      }
    }

    if ((orderField != null) && (!orderField.equals(""))) {
      returnValue += " ORDER BY " + orderField + (orderDest ? "ASC" : "DESC");
    }
    return returnValue;
  }

  protected final void setAll(PreparedStatement pstmt, int i, Object obj) throws SQLException {
    if (obj instanceof Boolean) {
      pstmt.setBoolean(i, ((Boolean) obj).booleanValue());
    }
    if (obj instanceof Byte) {
      pstmt.setByte(i, ((Byte) obj).byteValue());
    }
    if (obj instanceof Short) {
      pstmt.setShort(i, ((Short) obj).shortValue());
    }
    if (obj instanceof Integer) {
      pstmt.setInt(i, ((Integer) obj).intValue());
    }
    if (obj instanceof Long) {
      pstmt.setLong(i, ((Long) obj).longValue());
    }
    if (obj instanceof Float) {
      pstmt.setFloat(i, ((Float) obj).floatValue());
    }
    if (obj instanceof Double) {
      pstmt.setDouble(i, ((Double) obj).doubleValue());
    }
    if (obj instanceof Timestamp) {
      pstmt.setTimestamp(i, ((Timestamp) obj));
    }
    if (obj instanceof Date) {
      pstmt.setDate(i, ((Date) obj));
    }
    if (obj instanceof BigDecimal) {
      pstmt.setBigDecimal(i, ((BigDecimal) obj));
    }
    if (obj instanceof String) {
      pstmt.setString(i, ((String) obj));
    }
  }

  // ---------------------------------------------------------------------------

  protected final String getSqlWhere(WhereFields wherefields) {
    String returnValue = StringUtils.EMPTY;
    if (wherefields != null) {
      if (wherefields.size() > 0) {
        returnValue += " WHERE ";
      }
      for (int i = 0; i < wherefields.size(); i++) {
        if (i != 0) {
          returnValue += "AND ";
        }
        returnValue += wherefields.getFieldName(i);
        returnValue += " " + wherefields.getComparator(i).getComparator();
        returnValue += " " + wherefields.getWhereAssertion(i);
      }
    }
    return returnValue;
  }

  protected final String getSqlOrder(OrderFields orderfields) {
    String returnValue = StringUtils.EMPTY;
    if (orderfields != null) {
      if (orderfields.size() > 0) {
        returnValue += " ORDER BY ";
      }
      for (int i = 0; i < orderfields.size(); i++) {
        if (i != 0) {
          returnValue += ", ";
        }
        returnValue += orderfields.getFieldName(i);
        returnValue += " " + (orderfields.getAscending(i) ? "ASC" : "DESC");
      }
    }
    return returnValue;
  }

  protected final String getSqlWhereAndOrder(WhereFields wherefields, OrderFields orderfields) {
    String returnValue = StringUtils.EMPTY;
    returnValue += getSqlWhere(wherefields);
    returnValue += getSqlOrder(orderfields);
    return returnValue;
  }
}
