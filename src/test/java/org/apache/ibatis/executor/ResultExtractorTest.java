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
package org.apache.ibatis.executor;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;
import org.testng.annotations.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ResultExtractorTest {

  private ResultExtractor resultExtractor;

  @Mock
  private Configuration configuration;
  @Mock
  private ObjectFactory objectFactory;

  @BeforeMethod
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    resultExtractor = new ResultExtractor(configuration, objectFactory);
  }

  @Test(groups = {"tidb"})
  public void shouldExtractNullForNullTargetType() {
    final Object result = resultExtractor.extractObjectFromList(null, null);
    assertThat(result, nullValue());
  }

  @Test(groups = {"tidb"})
  public void shouldExtractList() {
    final List list = Arrays.asList(1, 2, 3);
    final Object result = resultExtractor.extractObjectFromList(list, List.class);
    assertThat(result, instanceOf(List.class));
    final List resultList = (List) result;
    assertThat(resultList, equalTo(list));
  }

  @Test(groups = {"tidb"})
  public void shouldExtractArray() {
    final List list = Arrays.asList(1, 2, 3);
    final Object result = resultExtractor.extractObjectFromList(list, Integer[].class);
    assertThat(result, instanceOf(Integer[].class));
    final Integer[] resultArray = (Integer[]) result;
    assertThat(resultArray, equalTo(new Integer[]{1, 2, 3}));
  }

  @Test(groups = {"tidb"})
  public void shouldExtractSet() {
    final List list = Arrays.asList(1, 2, 3);
    final Class<Set> targetType = Set.class;
    final Set set = new HashSet();
    final MetaObject metaObject = mock(MetaObject.class);
    when(objectFactory.isCollection(targetType)).thenReturn(true);
    when(objectFactory.create(targetType)).thenReturn(set);
    when(configuration.newMetaObject(set)).thenReturn(metaObject);

    final Set result = (Set) resultExtractor.extractObjectFromList(list, targetType);
    assertThat(result, sameInstance(set));

    verify(metaObject).addAll(list);
  }

  @Test(groups = {"tidb"})
  public void shouldExtractSingleObject() {
    final List list = Collections.singletonList("single object");
    assertThat((String) resultExtractor.extractObjectFromList(list, String.class), equalTo("single object"));
    assertThat((String) resultExtractor.extractObjectFromList(list, null), equalTo("single object"));
    assertThat((String) resultExtractor.extractObjectFromList(list, Integer.class), equalTo("single object"));
  }

  @Test(groups = {"tidb"}, expectedExceptions = ExecutorException.class)
  public void shouldFailWhenMutipleItemsInList() {
    final List list = Arrays.asList("first object", "second object");
    assertThat((String) resultExtractor.extractObjectFromList(list, String.class), equalTo("single object"));
  }
}
