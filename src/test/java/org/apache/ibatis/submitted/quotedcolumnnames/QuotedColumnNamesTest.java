/**
 *    Copyright 2009-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.submitted.quotedcolumnnames;

import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

public class QuotedColumnNamesTest {

  protected static SqlSessionFactory sqlSessionFactory;

  @BeforeClass
  public static void setUp() throws Exception {
    Connection conn = null;

    try {
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:4000/test", "root", "");

      Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/quotedcolumnnames/CreateDB.sql");
      ScriptRunner runner = new ScriptRunner(conn);
      runner.setLogWriter(null);
      runner.setErrorLogWriter(null);
      runner.runScript(reader);
      conn.commit();
      reader.close();

      reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/quotedcolumnnames/MapperConfig.xml");
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
      reader.close();

    } finally {
      if (conn != null) {
        conn.close();
      }
    }
  }

  @Test(groups={"tidb"})
  public void testIt() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      List<Map<String, Object>> list = sqlSession.selectList("org.apache.ibatis.submitted.quotedcolumnnames.Map.doSelect");
      printList(list);
      assertColumnNames(list);
    } finally {
      sqlSession.close();
    }
  }

  @Test(groups={"tidb"})
  public void testItWithResultMap() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      List<Map<String, Object>> list = sqlSession.selectList("org.apache.ibatis.submitted.quotedcolumnnames.Map.doSelectWithResultMap");
      printList(list);
      assertColumnNames(list);
    } finally {
      sqlSession.close();
    }
  }

  private void assertColumnNames(List<Map<String, Object>> list) {
    Map<String, Object> record = list.get(0);

    Assert.assertTrue(record.containsKey("firstName"));
    Assert.assertTrue(record.containsKey("lastName"));

    Assert.assertFalse(record.containsKey("FIRST_NAME"));
    Assert.assertFalse(record.containsKey("LAST_NAME"));
  }

  private void printList(List<Map<String, Object>> list) {
    for (Map<String, Object> map : list) {
      Assert.assertNotNull(map);
    }
  }
}
