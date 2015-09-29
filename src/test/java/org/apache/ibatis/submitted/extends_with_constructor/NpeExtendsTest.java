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
package org.apache.ibatis.submitted.extends_with_constructor;

import static org.junit.Assert.*;

import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

/*
 * Test for NPE when using extends.
 * 
 * @author poitrac
 */
public class NpeExtendsTest {
    @BeforeClass
    public static void initDatabase() throws Exception {
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:4000/test", "root", "");

            Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/extends_with_constructor/CreateDB.sql");

            ScriptRunner runner = new ScriptRunner(conn);
            runner.setLogWriter(null);
            runner.setErrorLogWriter(null);
            runner.runScript(reader);
            conn.commit();
            reader.close();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Test(groups={"tidb"})
    public void testNoConstructorConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addMapper(StudentMapper.class);
        configuration.addMapper(TeacherMapper.class);
        configuration.getMappedStatementNames();
    }

    @Test(groups={"tidb"})
    public void testWithConstructorConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addMapper(StudentConstructorMapper.class);
        configuration.addMapper(TeacherMapper.class);
        configuration.getMappedStatementNames();
    }
    
    private SqlSessionFactory getSqlSessionFactoryWithConstructor() {
        UnpooledDataSourceFactory unpooledDataSourceFactory = new UnpooledDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty("driver", "com.mysql.jdbc.Driver");
        properties.setProperty("url", "jdbc:mysql://127.0.0.1:4000/test");
        properties.setProperty("username", "root");
        properties.setProperty("password", "");
        unpooledDataSourceFactory.setProperties(properties);
        Environment environment = new Environment("extends_with_constructor", new JdbcTransactionFactory(), unpooledDataSourceFactory.getDataSource());
        
        Configuration configuration = new Configuration();
        configuration.setEnvironment(environment);
        configuration.addMapper(StudentConstructorMapper.class);
        configuration.addMapper(TeacherMapper.class);
        configuration.getMappedStatementNames();
        configuration.setAutoMappingBehavior(AutoMappingBehavior.NONE);
        
        return new DefaultSqlSessionFactory(configuration);
    }

    @Test(groups={"tidb"})
    public void testSelectWithTeacher() {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactoryWithConstructor();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            StudentConstructorMapper studentConstructorMapper = sqlSession.getMapper(StudentConstructorMapper.class);
            StudentConstructor testStudent = studentConstructorMapper.selectWithTeacherById(1);
            assertEquals(1, testStudent.getConstructors().size());
            assertTrue(testStudent.getConstructors().contains(StudentConstructor.Constructor.ID_NAME));
        } finally {
            sqlSession.close();
        }
    }

    @Test(groups={"tidb"})
    public void testSelectNoName() {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactoryWithConstructor();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            StudentConstructorMapper studentConstructorMapper = sqlSession.getMapper(StudentConstructorMapper.class);
            StudentConstructor testStudent = studentConstructorMapper.selectNoNameById(1);
            assertEquals(1, testStudent.getConstructors().size());
            assertTrue(testStudent.getConstructors().contains(StudentConstructor.Constructor.ID));
            assertNull(testStudent.getName());
        } finally {
            sqlSession.close();
        }
    }
}
