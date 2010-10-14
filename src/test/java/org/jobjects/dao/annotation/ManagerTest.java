package org.jobjects.dao.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.jobjects.dao.annotation.bean.MonBean;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Mickaël Patron
 * @since 1.0.0
 */
public class ManagerTest {

  MonBean bean = null;

  @BeforeClass
  public static void beforeClass() {
    try {
      String driver = "org.apache.derby.jdbc.EmbeddedDriver";
      Class.forName(driver).newInstance();
      Properties p = new Properties();
      p.setProperty("user", "sa");
      p.setProperty("password", "manager");
      p.setProperty("create", "true");

      Connection conn = DriverManager.getConnection("jdbc:derby:memory:MyDerbyDB", p);
      Statement stmt = conn.createStatement();
      String sql = "CREATE TABLE MyDerbyDB.MyTable (";
      sql += " MonChampsTexte VARCHAR(6) NOT NULL,";
      sql += " MonChampsChar CHAR(2) NOT NULL,";
      sql += " MonChampsDate DATE,";
      sql += " MonChampsDateTime TIMESTAMP,";
      sql += " MonChampsDecimal DOUBLE";
      sql += " )";
      stmt.execute(sql);

      stmt.execute("ALTER TABLE MyDerbyDB.MyTable ADD PRIMARY KEY (MonChampsTexte, MonChampsChar)");

      stmt.close();

      final ResultSet tables = conn.getMetaData().getTables(null, null, "%", new String[] { "TABLE" });
      List<String> tableNames = new ArrayList<String>();
      while (tables.next()) {
        tableNames.add(tables.getString("TABLE_NAME").toLowerCase());
      }

      conn.close();
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @AfterClass
  public static void afterClass() {
    try {
      // DriverManager.getConnection("jdbc:derby:memory:MyDerbyDB;shutdown=true");
      DriverManager.getConnection("jdbc:derby:;shutdown=true");
    } catch (Exception ignored) {
    }
  }

  @Before
  public void setUp() throws Exception {
    bean = new MonBean();
    bean.setMonChampsTexte("TOTO");
    bean.setMonChampsChar("XY");

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    bean.setMonChampsDate(new Date(sdf.parse("25/12/2010").getTime()));
    bean.setMonChampsDateTime(new Timestamp(System.currentTimeMillis()));
    bean.setMonChampsDecimal(new BigDecimal(1.1));
  }

  @Test
  public void testManager() {
    Manager<MonBean, MonBean> manager = new Manager<MonBean, MonBean>(MonBean.class);
    assertNotNull(manager);
  }

  @Test
  public void testCreate() {
    Manager<MonBean, MonBean> manager = new Manager<MonBean, MonBean>(MonBean.class);
    try {
      if (manager.isExist(bean)) {
        manager.remove(bean);
      }
      MonBean mb = manager.create(bean);
      assertNotNull(mb);
      assertEquals("TOTO", bean.getMonChampsTexte());

    } catch (Exception e) {
      e.printStackTrace();
      fail("" + e.getMessage());
    }
  }

  @Test
  public void testLoad() {
    Manager<MonBean, MonBean> manager = new Manager<MonBean, MonBean>(MonBean.class);
    MonBean beanPk = new MonBean();
    beanPk.setMonChampsTexte("TOTO");
    beanPk.setMonChampsChar("XY");
    try {
      if (!manager.isExist(beanPk)) {
        manager.create(bean);
      }
      MonBean mb = manager.load(beanPk);
      assertNotNull(mb);
      assertEquals("TOTO", mb.getMonChampsTexte());
    } catch (Exception e) {
      e.printStackTrace();
      fail("" + e.getMessage());
    }
  }

  @Test
  public void testSave() {
    Manager<MonBean, MonBean> manager = new Manager<MonBean, MonBean>(MonBean.class);
    try {
      manager.save(bean);
    } catch (Exception e) {
      e.printStackTrace();
      fail("" + e.getMessage());
    }
  }

  @Test
  public void testRemove() {
    Manager<MonBean, MonBean> manager = new Manager<MonBean, MonBean>(MonBean.class);
    try {
      if (!manager.isExist(bean)) {
        manager.create(bean);
      }
      manager.remove(bean);
    } catch (Exception e) {
      e.printStackTrace();
      fail("" + e.getMessage());
    }
  }

  @Test
  public void testIsExist() {
    Manager<MonBean, MonBean> manager = new Manager<MonBean, MonBean>(MonBean.class);
    try {
      if (!manager.isExist(bean)) {
        manager.create(bean);
      }
      assertTrue(manager.isExist(bean));
    } catch (Exception e) {
      e.printStackTrace();
      fail("" + e.getMessage());
    }
  }

  @Test
  public void testFindAll() {
    Manager<MonBean, MonBean> manager = new Manager<MonBean, MonBean>(MonBean.class);    
    try {
      if (!manager.isExist(bean)) {
        manager.create(bean);
      }
      List<MonBean> list = manager.findAll(Integer.MIN_VALUE, Integer.MAX_VALUE, null, null);
      assertTrue(list.size() > 0);
      for (MonBean monBean : list) {
        StringUtils.length(monBean.getMonChampsTexte());
      }
    } catch (Exception e) {
      e.printStackTrace();
      fail("" + e.getMessage());
    }
  }

}
