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

drop table if exists string_string;

create table string_string (
    id int NOT NULL AUTO_INCREMENT,
    kkey varchar(255),
    vvalue varchar(255),

    PRIMARY KEY (id)
);

drop table if exists int_bool;

create table int_bool (
    id int NOT NULL AUTO_INCREMENT,
    kkey int,
    vvalue boolean,

    PRIMARY KEY (id)
);

drop table if exists nested_bean;

create table nested_bean (
    id int NOT NULL AUTO_INCREMENT,
    keya int,
    keyb boolean,
    valuea int,
    valueb boolean,

    PRIMARY KEY (id)
);

drop table if exists key_cols;

create table key_cols (
    id int NOT NULL AUTO_INCREMENT,
    col_a int,
    col_b int,

    PRIMARY KEY (id)
);

insert into key_cols (id, col_a, col_b) values (1, 11, 222);
insert into key_cols (id, col_a, col_b) values (2, 22, 222);
insert into key_cols (id, col_a, col_b) values (3, 22, 333);
