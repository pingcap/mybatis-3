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
package org.apache.ibatis.submitted.foreach_map;

public class IntBoolMapEntry {
  public IntBoolMapEntry() {
  }

  public IntBoolMapEntry(Integer key, Boolean value) {
    this.kkey = key;
    this.vvalue = value;
  }

  public Integer getKey() {
    return kkey;
  }

  public void setKey(Integer key) {
    this.kkey = key;
  }

  public Boolean getValue() {
    return vvalue;
  }

  public void setValue(Boolean value) {
    this.vvalue = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    IntBoolMapEntry mapEntry = (IntBoolMapEntry) o;

    if (kkey != null ? !kkey.equals(mapEntry.kkey) : mapEntry.kkey != null)
      return false;
    if (vvalue != null ? !vvalue.equals(mapEntry.vvalue) : mapEntry.vvalue != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = kkey != null ? kkey.hashCode() : 0;
    result = 31 * result + (vvalue != null ? vvalue.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return '{' + kkey.toString() + '=' + vvalue + '}';
  }

  private Integer kkey;
  private Boolean vvalue;
}
