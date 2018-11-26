DROP TABLE properties_table;
CREATE table if not exists properties_table (id int primary key auto_increment, source_name varchar(128), source_key varchar(128), source_value longtext);
INSERT into properties_table (source_name, source_key, source_value) values('address', 'Kevin.Li', '1111 hi ave.');