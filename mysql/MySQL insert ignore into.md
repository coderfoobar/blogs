
# insert ignore into

INSERT IGNORE INTO 会忽略数据库中已经存在的数据，如果没有数据，就插入新的数据，如果有数据的话就跳过这条数据，返回影响了0条数据。这样就可以保留数据已经存在的数据，达到在间隙中插入数据的目的。

