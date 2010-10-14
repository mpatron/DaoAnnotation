package org.jobjects.dao.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.jobjects.dao.annotation.bean.MonBean;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mickaël Patron
 * @since 1.0.0
 */
public class DaoTableTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testSchemaName() {

  }

  @Test
  public void testTableName() {
    MonBean monBean = new MonBean();
    {
      Annotation[] annotations = monBean.getClass().getDeclaredAnnotations();
      for (Annotation annotation : annotations) {
        System.out.println("" + annotation.annotationType().getClass().getCanonicalName());
      }
      if (monBean.getClass().isAnnotationPresent(DaoTable.class)) {
        DaoTable daoTable = monBean.getClass().getAnnotation(DaoTable.class);
        System.out.println("" + daoTable.toString());
      }
    }

    {
      Field[] fields = monBean.getClass().getDeclaredFields();
      for (Field field : fields) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
          System.out.println("" + ((DaoField) annotation).fieldName());
        }
      }
    }
  }

}
