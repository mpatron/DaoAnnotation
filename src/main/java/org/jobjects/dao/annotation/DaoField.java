/**
 * 
 */
package org.jobjects.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mickaël Patron
 * @since 1.0.0
 */
@Target({ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DaoField {
  String fieldName();
  int type() default java.sql.Types.VARCHAR;
  int size() default 0;
  int precision() default 0;
  boolean isNullable() default true;
  boolean isPrimary() default false;
}
