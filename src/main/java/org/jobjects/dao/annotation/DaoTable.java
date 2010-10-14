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
@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DaoTable {
  String dataSourceName();
  String schemaName() default "";
  String tableName();
}
