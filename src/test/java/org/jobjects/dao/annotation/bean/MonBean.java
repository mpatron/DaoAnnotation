package org.jobjects.dao.annotation.bean;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import org.jobjects.dao.annotation.DaoField;
import org.jobjects.dao.annotation.DaoTable;

/**
 * @author Mickaël Patron
 * @since 1.0.0
 */
@DaoTable(dataSourceName="java:/comp/env/myDataSource", schemaName = "MyDerbyDB", tableName = "MyTable")
public class MonBean {
  public MonBean() {
  }

  @DaoField(fieldName = "MonChampsTexte", isNullable = false, isPrimary = true, size = 6, type = java.sql.Types.VARCHAR)
  private String monChampsTexte;

  @DaoField(fieldName = "MonChampsChar", isNullable = false, isPrimary = true, size = 2, type = java.sql.Types.CHAR)
  private String monChampsChar;

  @DaoField(fieldName = "MonChampsDate", isNullable = false, isPrimary = false, size = 6, type = java.sql.Types.DATE)
  private Date monChampsDate;

  @DaoField(fieldName = "MonChampsDateTime", isNullable = true, isPrimary = false, size = 6, type = java.sql.Types.TIMESTAMP)
  private Timestamp monChampsDateTime;

  @DaoField(fieldName = "MonChampsDecimal", isNullable = true, size = 6, type = java.sql.Types.DECIMAL)
  private BigDecimal monChampsDecimal;

  public String getMonChampsTexte() {
    return monChampsTexte;
  }

  public void setMonChampsTexte(String monChampsTexte) {
    this.monChampsTexte = monChampsTexte;
  }

  public String getMonChampsChar() {
    return monChampsChar;
  }

  public void setMonChampsChar(String monChampsChar) {
    this.monChampsChar = monChampsChar;
  }

  public Date getMonChampsDate() {
    return monChampsDate;
  }

  public void setMonChampsDate(Date monChampsDate) {
    this.monChampsDate = monChampsDate;
  }

  public Timestamp getMonChampsDateTime() {
    return monChampsDateTime;
  }

  public void setMonChampsDateTime(Timestamp monChampsDateTime) {
    this.monChampsDateTime = monChampsDateTime;
  }

  public BigDecimal getMonChampsDecimal() {
    return monChampsDecimal;
  }

  public void setMonChampsDecimal(BigDecimal monChampsDecimal) {
    this.monChampsDecimal = monChampsDecimal;
  }

}
