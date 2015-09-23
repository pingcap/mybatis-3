--
--    Copyright 2009-2012 the original author or authors.
--
--    Licensed under the Apache License, Version 2.0 (the "License");
--    you may not use this file except in compliance with the License.
--    You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing, software
--    distributed under the License is distributed on an "AS IS" BASIS,
--    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--    See the License for the specific language governing permissions and
--    limitations under the License.
--

drop table if exists awfultable;

create table awfultable (
  CuStOmEriD int NOT NULL AUTO_INCREMENT ,
  firstname varchar(30) default 'Mabel',
  first_name varchar(30),
  lastname varchar(30),
  E_MAIL varchar(30),
  _id1 int not null,
  id2 int not null,
  id5_ int not null,
  id6 int not null,
  id7 int not null,
  EmailAddress varchar(30),
  fromm varchar(30),
  active int,
  primary key(CuStOmEriD)
);
