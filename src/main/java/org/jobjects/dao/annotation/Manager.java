package org.jobjects.dao.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Mickaël Patron
 * @since 1.0.0
 * 
 * @param <P>
 * @param <T>
 */
public class Manager<P, T extends P> extends ManagerTools {

  private transient Log log = LogFactory.getLog(getClass());

  private Field[] fields = null;
  private Class<T> entityClass = null;
  private String usualTable;
  private String dataSourceName;

  /**
   * @param entityClass
   */
  public Manager(Class<T> entityClass) {
    this.entityClass = entityClass;
    if (entityClass.isAnnotationPresent(DaoTable.class)) {
      DaoTable daoTable = entityClass.getAnnotation(DaoTable.class);
      if (StringUtils.isBlank(daoTable.schemaName())) {
        usualTable = daoTable.tableName();
      } else {
        usualTable = daoTable.schemaName() + "." + daoTable.tableName();
      }
      dataSourceName = daoTable.dataSourceName();
      fields = entityClass.getDeclaredFields();
    }
  }

  private final Connection getConnection() throws Exception {
    Connection returnValue=null;
    Context initial = new InitialContext();
    /**
     * commenter la ligne suivant dans jndi.properties
     * #org.osjava.sj.space=java:/comp/env
     * pour utiliser le default.properties
     */
    //ou ça DataSource dataSource = (DataSource) initial.lookup("java:/comp/env/TestDS");
    //DataSource dataSource = (DataSource) initial.lookup("java:/comp/env/myDataSource");
    DataSource dataSource = (DataSource) initial.lookup(dataSourceName);
    returnValue = dataSource.getConnection();
    return returnValue;
   
  }
  
  
  /**
   * Persistance du bean annoté avec DaoTable ainsi que DaoField. Si pour une
   * raison quelconque une problème survient, l'exception CreateException sera
   * levée.
   * 
   * @param Le
   *          bean d'un type quelconque comportant des annotations daoTable et
   *          DaoField
   * @return Un bean de même type que celui passé en paramètre. Les données du
   *         bean sont rafraichi à partir de la base au cas où celle-ci aurait
   *         modifié les données grace à des trigger ou autre.
   * @throws CreateException
   *           Exception levée au sur problème de persistance.
   */
  public final T create(T bean) throws CreateException {
    T returnValue = null;
    try {
      String msg = "Cannot create " + entityClass.getCanonicalName() + " : "
          + ToStringBuilder.reflectionToString(bean, ToStringStyle.MULTI_LINE_STYLE);

      String sql = "INSERT INTO " + usualTable + " ( ";
      String buffer = StringUtils.EMPTY;

      boolean first = true;
      for (Field field : fields) {
        if (first) {
          first = false;
        } else {
          sql += ", ";
          buffer += ", ";
        }
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
          if (annotation instanceof DaoField) {
            sql += ((DaoField) annotation).fieldName();
            buffer += "?";
            break;
          }
        }
      }
      sql += ") VALUES (";
      sql += buffer;
      sql += ")";

      returnValue = entityClass.newInstance();
      try {
        Connection connection = getConnection();
        try {
          PreparedStatement pstmt = connection.prepareStatement(sql);
          try {
            int i = 1;
            for (Field field : fields) {
              Annotation[] annotations = field.getAnnotations();
              for (Annotation annotation : annotations) {
                if (annotation instanceof DaoField) {
                  Object obj = BeanUtils.getProperty(bean, field.getName());
                  if (obj == null) {
                    pstmt.setNull(i++, ((DaoField) annotation).type());
                  } else {
                    setAll(pstmt, i++, obj);
                  }
                  break;
                }
              }
            }
            pstmt.executeUpdate();
          } finally {
            pstmt.close();
          }
          pstmt = null;
        } finally {
          connection.close();
        }
        connection = null;
      } catch (SQLException sqle) {
        log.error(msg, sqle);
        throw new CreateException(msg, sqle);
      }
      try {
        returnValue = load(bean);
      } catch (FinderException le) {
        log.error(msg, le);
        throw new CreateException(msg, le);
      }
    } catch (Exception e) {
      throw new CreateException(e);
    }
    return returnValue;
  }

  /**
   * Retourne le bean à partir de la clef primaire. Si la ligne n'existe pas
   * dans la base alors une exception FinderException est levée.
   * 
   * @param Le
   *          beanPk d'un type quelconque comportant des annotations daoTable et
   *          DaoField. Il represente la clef primaire de la table.
   * @return Un bean de même type que celui passé en paramètre. Les données du
   *         bean sont rafraichi à partir de la base au cas où celle-ci aurait
   *         modifié les données grace à des trigger ou autre.
   * @throws FinderException
   */
  public final T load(P beanPk) throws FinderException {
    T returnValue = null;
    try {

      String msg = "Load error  " + entityClass.getCanonicalName() + " : "
          + ToStringBuilder.reflectionToString(beanPk, ToStringStyle.MULTI_LINE_STYLE);

      String sql = "SELECT ";

      boolean first = true;
      for (Field field : fields) {
        if (first) {
          first = false;
        } else {
          sql += ", ";
        }
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
          if (annotation instanceof DaoField) {
            sql += ((DaoField) annotation).fieldName();
            break;
          }
        }
      }

      sql += " FROM " + usualTable + " WHERE ";

      first = true;
      for (Field field : fields) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
          if (annotation instanceof DaoField) {
            if (((DaoField) annotation).isPrimary()) {
              if (first) {
                first = false;
              } else {
                sql += " AND ";
              }
              sql += ((DaoField) annotation).fieldName() + "=?";
            }
            break;
          }
        }
      }

      try {
        Connection connection = getConnection();
        try {
          PreparedStatement pstmt = connection.prepareStatement(sql);
          try {
            int i = 1;

            for (Field field : fields) {
              Annotation[] annotations = field.getAnnotations();
              for (Annotation annotation : annotations) {
                if (annotation instanceof DaoField) {
                  if (((DaoField) annotation).isPrimary()) {
                    Object obj = BeanUtils.getProperty(beanPk, field.getName());
                    if (obj == null) {
                      pstmt.setNull(i++, ((DaoField) annotation).type());
                    } else {
                      setAll(pstmt, i++, obj);
                    }
                  }
                  break;
                }
              }
            }

            ResultSet rs = pstmt.executeQuery();
            try {
              if (!rs.next()) {
                throw new FinderException(msg);
              }

              returnValue = entityClass.newInstance();

              int j = 1;
              for (Field field : fields) {
                Annotation[] annotations = field.getAnnotations();
                for (Annotation annotation : annotations) {
                  if (annotation instanceof DaoField) {
                    // field.set(returnValue, rs.getObject(j++));
                    BeanUtils.setProperty(returnValue, field.getName(), rs.getObject(j++));
                    break;
                  }
                }
              }

            } finally {
              rs.close();
            }
            rs = null;
          } finally {
            pstmt.close();
          }
          pstmt = null;
        } finally {
          connection.close();
        }
        connection = null;
      } catch (SQLException sqle) {
        log.error(msg, sqle);
        throw new FinderException(msg, sqle);
      }
    } catch (Exception e) {
      throw new FinderException(e);
    }
    return returnValue;
  }

  /**
   * @param bean
   * @return
   * @throws SaveException
   */
  public final T save(T bean) throws SaveException {
    T returnValue = null;
    String msg = "Cannot save " + entityClass.getCanonicalName() + " : "
        + ToStringBuilder.reflectionToString(bean, ToStringStyle.MULTI_LINE_STYLE);

    try {
      if (isExist(bean)) {
        saveReal(bean);
      } else {
        create(bean);
      }
      returnValue = load(bean);
    } catch (Exception ce) {
      log.error(msg, ce);
      throw new SaveException(msg, ce);
    }
    return returnValue;
  }

  /**
   * Sauvegarde le bean dans la base. Une exception SaveException peut être
   * levée si un problème survient.
   * 
   * @param bean
   * @return Un boolean positif si la mise en jour s'est réelement passé.
   * @throws SaveException
   */
  private boolean saveReal(T bean) throws SaveException {
    boolean returnValue = true;

    String msg = "Cannot save " + entityClass.getCanonicalName() + " : "
        + ToStringBuilder.reflectionToString(bean, ToStringStyle.MULTI_LINE_STYLE);

    String sql = "UPDATE " + usualTable + " SET ";

    boolean first = true;
    for (Field field : fields) {
      Annotation[] annotations = field.getAnnotations();
      for (Annotation annotation : annotations) {
        if (annotation instanceof DaoField) {
          if (!((DaoField) annotation).isPrimary()) {
            if (first) {
              first = false;
            } else {
              sql += ", ";
            }
            sql += ((DaoField) annotation).fieldName() + "=?";
          }
          break;
        }
      }
    }
    sql += " WHERE ";
    first = true;
    for (Field field : fields) {
      Annotation[] annotations = field.getAnnotations();
      for (Annotation annotation : annotations) {
        if (annotation instanceof DaoField) {
          if (((DaoField) annotation).isPrimary()) {
            if (first) {
              first = false;
            } else {
              sql += " AND ";
            }
            sql += ((DaoField) annotation).fieldName() + "=?";
          }
          break;
        }
      }
    }

    try {
      Connection connection = getConnection();
      try {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        try {
          int i = 1;

          for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
              if (annotation instanceof DaoField) {
                if (!((DaoField) annotation).isPrimary()) {
                  Object obj = BeanUtils.getProperty(bean, field.getName());
                  if (obj == null) {
                    pstmt.setNull(i++, ((DaoField) annotation).type());
                  } else {
                    setAll(pstmt, i++, obj);
                  }
                }
                break;
              }
            }
          }

          for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
              if (annotation instanceof DaoField) {
                if (((DaoField) annotation).isPrimary()) {
                  Object obj = BeanUtils.getProperty(bean, field.getName());
                  if (obj == null) {
                    pstmt.setNull(i++, ((DaoField) annotation).type());
                  } else {
                    setAll(pstmt, i++, obj);
                  }
                }
                break;
              }
            }
          }
          returnValue = pstmt.executeUpdate() == 1;
        } finally {
          pstmt.close();
        }
        pstmt = null;
      } finally {
        connection.close();
      }
      connection = null;
    } catch (Exception sqle) {
      log.error(msg, sqle);
      throw new SaveException(msg, sqle);
    }
    return returnValue;
  }

  // ---------------------------------------------------------------------------

  /**
   * @param beanPk
   * @throws RemoveException
   */
  public final void remove(P beanPk) throws RemoveException {
    String msg = "Delete error " + entityClass.getCanonicalName() + " : "
        + ToStringBuilder.reflectionToString(beanPk, ToStringStyle.MULTI_LINE_STYLE);

    String sql = "DELETE FROM " + usualTable;
    sql += " WHERE ";

    boolean first = true;
    for (Field field : fields) {
      Annotation[] annotations = field.getAnnotations();
      for (Annotation annotation : annotations) {
        if (annotation instanceof DaoField) {
          if (((DaoField) annotation).isPrimary()) {
            if (first) {
              first = false;
            } else {
              sql += " AND ";
            }
            sql += ((DaoField) annotation).fieldName() + "=?";
          }
          break;
        }
      }
    }

    try {
      Connection connection = getConnection();
      try {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        try {
          int i = 1;
          for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
              if (annotation instanceof DaoField) {
                if (((DaoField) annotation).isPrimary()) {
                  Object obj = BeanUtils.getProperty(beanPk, field.getName());
                  if (obj == null) {
                    pstmt.setNull(i++, ((DaoField) annotation).type());
                  } else {
                    setAll(pstmt, i++, obj);
                  }
                }
                break;
              }
            }
          }
          pstmt.executeUpdate();
        } finally {
          pstmt.close();
        }
        pstmt = null;
      } finally {
        connection.close();
      }
      connection = null;
    } catch (Exception sqle) {
      log.error(msg, sqle);
      throw new RemoveException(msg, sqle);
    }

  }

  /**
   * @param beanPk
   * @return
   */
  public final boolean isExist(P beanPk) {
    boolean returnValue = false;
    if (beanPk == null) {
      return returnValue;
    }
    String sql = "SELECT COUNT(*)";
    sql += " FROM " + usualTable + " WHERE ";

    boolean first = true;
    for (Field field : fields) {
      Annotation[] annotations = field.getAnnotations();
      for (Annotation annotation : annotations) {
        if (annotation instanceof DaoField) {
          if (((DaoField) annotation).isPrimary()) {
            if (first) {
              first = false;
            } else {
              sql += " AND ";
            }
            sql += ((DaoField) annotation).fieldName() + "=?";
          }
          break;
        }
      }
    }

    try {
      Connection connection = getConnection();
      try {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        try {
          int i = 1;

          for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
              if (annotation instanceof DaoField) {
                if (((DaoField) annotation).isPrimary()) {
                  Object obj = BeanUtils.getProperty(beanPk, field.getName());
                  if (obj == null) {
                    pstmt.setNull(i++, ((DaoField) annotation).type());
                  } else {
                    setAll(pstmt, i++, obj);
                  }
                }
                break;
              }
            }
          }

          ResultSet rs = pstmt.executeQuery();
          try {
            rs.next();
            returnValue = rs.getInt(1) == 1;
          } finally {
            rs.close();
          }
          rs = null;
        } finally {
          pstmt.close();
        }
        pstmt = null;
      } finally {
        connection.close();
      }
      connection = null;
    } catch (Exception sqle) {
      log.error("Exist error TrsProduitProfformBean : " + beanPk, sqle);
    }
    return returnValue;
  }

  /**
   * 
   * @param min
   * @param max
   * @param wherefields
   * @param orderfields
   * @return
   * @throws FinderException
   */
  public final List<T> findAll(int min, int max, WhereFields wherefields, OrderFields orderfields) throws FinderException {
    List<T> returnValue = null;
    try {
      returnValue = new ArrayList<T>(); // 26.375

      String sql = "SELECT ";
      boolean first = true;
      for (Field field : fields) {
        if (first) {
          first = false;
        } else {
          sql += ", ";
        }
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
          if (annotation instanceof DaoField) {
            sql += ((DaoField) annotation).fieldName();
            break;
          }
        }
      }
      sql += " FROM " + usualTable + " ";

      sql += getSqlWhereAndOrder(wherefields, orderfields);
//      sql = "SELECT * FROM (SELECT ROWNUM N, P.* FROM (" + sql;
//      sql += ") P WHERE ROWNUM < " + max + ")";
//      sql += "WHERE (N>" + min + ")AND(N<" + max + ")";
      try {
        Connection connection = getConnection();
        try {
          Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
          try {
            ResultSet rs = stmt.executeQuery(sql);
//            rs.absolute(min)
//            stmt.setMaxRows(max)
            try {
              while (rs.next()) {
                T data = entityClass.newInstance();
                int i = 1;

                for (Field field : fields) {
                  Annotation[] annotations = field.getAnnotations();
                  for (Annotation annotation : annotations) {
                    if (annotation instanceof DaoField) {
                      BeanUtils.setProperty(returnValue, field.getName(), rs.getObject(i++));
                      break;
                    }
                  }
                }

                returnValue.add(data);
              }
            } finally {
              rs.close();
            }
            rs = null;
          } finally {
            stmt.close();
          }
          stmt = null;
        } finally {
          connection.close();
        }
        connection = null;
      } catch (SQLException sqle) {
        log.error(sql, sqle);
        throw new FinderException(sql, sqle);
      }
    } catch (Exception e) {
      throw new FinderException(e);
    }
    return returnValue;

  }

}
