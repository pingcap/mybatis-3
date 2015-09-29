/**
 * Copyright 2009-2015 the original author or authors.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ibatis.submitted.lazyload_proxyfactory_comparison;

import java.io.Reader;
import java.sql.Connection;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public abstract class AbstractLazyTest {

    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;
    private Mapper mapper;

    protected abstract String getConfiguration();

    @BeforeMethod
    public void before() throws Exception {
        // create a SqlSessionFactory
        Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/lazyload_proxyfactory_comparison/mybatis-config-" + getConfiguration() + ".xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        reader.close();

        // populate in-memory database
        SqlSession session = sqlSessionFactory.openSession();
        Connection conn = session.getConnection();
        reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/lazyload_proxyfactory_comparison/CreateDB.sql");
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setLogWriter(null);
        runner.runScript(reader);
        reader.close();
        session.close();

        sqlSession = sqlSessionFactory.openSession();
        mapper = sqlSession.getMapper(Mapper.class);
    }

    @AfterMethod
    public void after() {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }

    @Test(groups = {"tidb"})
    public void lazyLoadUserWithGetObjectWithInterface() throws Exception {
        Assert.assertNotNull(mapper.getUserWithGetObjectWithInterface(1).getOwner());
    }

    @Test(groups = {"tidb"})
    public void lazyLoadUserWithGetObjectWithoutInterface() throws Exception {
        Assert.assertNotNull(mapper.getUserWithGetObjectWithoutInterface(1).getOwner());
    }

    @Test(groups = {"tidb"})
    public void lazyLoadUserWithGetXxxWithInterface() throws Exception {
        Assert.assertNotNull(mapper.getUserWithGetXxxWithInterface(1).getOwner());
    }

    @Test(groups = {"tidb"})
    public void lazyLoadUserWithGetXxxWithoutInterface() throws Exception {
        Assert.assertNotNull(mapper.getUserWithGetXxxWithoutInterface(1).getOwner());
    }

    @Test(groups = {"tidb"})
    public void lazyLoadUserWithNothingWithInterface() throws Exception {
        Assert.assertNotNull(mapper.getUserWithNothingWithInterface(1).getOwner());
    }

    @Test(groups = {"tidb"})
    public void lazyLoadUserWithNothingWithoutInterface() throws Exception {
        Assert.assertNotNull(mapper.getUserWithNothingWithoutInterface(1).getOwner());
    }
}
