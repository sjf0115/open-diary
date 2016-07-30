/* 内部表 */
create table if not exists employee(
   name string comment 'employee name',
   salary float comment 'employee salary',
   subordinates array<string> comment 'names of subordinates',
   deductions map<string,float> comment 'keys are deductions values are percentages',
   address struct<street:string, city:string, state:string, zip:int> comment 'home address'
)
comment 'description of the table'
location '/user/hive/warehouse/test.db/employee'
tblproperties ('creator'='yoona','date'='20160719');

/* 内部表 指定分隔符*/
create table if not exists employee(
   name string comment 'employee name',
   salary float comment 'employee salary',
   subordinates array<string> comment 'names of subordinates',
   deductions map<string,float> comment 'keys are deductions values are percentages',
   address struct<street:string, city:string, state:string, zip:int> comment 'home address'
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\001'
COLLECTION ITEMS TERMINATED BY '\002'
MAP KEYS TERMINATED BY '\003'
LINES TERMINATED BY '\n'
STORED AS TEXTFILE;

/* 数据 */
/*yoona\0012584.6\001lily\002lucy\002tom\001tax\00350.0\002eat\003450.0\001幸福路\002青岛\002山东
yoona2\0012784.6\001yoona\002lucy\002tom\001tax\003100.0\002eat\003550.0\001健康路\002济南\002山东
yoona3\0012884.6\001yoona2\002lucy\002tom\001tax\003200.0\002eat\003650.0\001欢乐路\002淄博\002山东*/

/* 外部表 */
CREATE EXTERNAL TABLE if NOT EXISTS stocks(
   exchanges STRING,
   symbol STRING,
   ymd STRING,
   price_open FLOAT,
   price_high FLOAT,
   price_low FLOAT,
   price_close FLOAT,
   volume INT,
   price_adj_close FLOAT
)
comment 'description of the table'
row format delimited fields terminated by ','
location '/data/stocks';

/* 分区表 */
create table if not exists employee2(
   name string comment 'employee name',
   salary float comment 'employee salary',
   subordinates array<string> comment 'names of subordinates',
   deductions map<string,float> comment 'keys are deductions values are percentages',
   address struct<street:string, city:string, state:string, zip:int> comment 'home address'
)
PARTITIONED BY (country string, state string);


/* 分区表 指定分隔符*/
create table if not exists employee3(
   name string comment 'employee name',
   salary float comment 'employee salary',
   subordinates array<string> comment 'names of subordinates',
   deductions map<string,float> comment 'keys are deductions values are percentages',
   address struct<street:string, city:string, state:string, zip:int> comment 'home address'
)
PARTITIONED BY (country string, state string)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\001'
COLLECTION ITEMS TERMINATED BY '\002'
MAP KEYS TERMINATED BY '\003'
LINES TERMINATED BY '\n'
STORED AS TEXTFILE;

/* 删除分区 */
ALTER TABLE employee3 DROP if EXISTS PARTITION (country = 'China' , state = 'ShanDong');

/* 添加分区 */
ALTER TABLE employee3 ADD if NOT EXISTS PARTITION (country = 'China' , state = 'ShanDong');

/* load data */
LOAD DATA LOCAL INPATH '/home/xiaosi/hive/input/employeeData.txt'
OVERWRITE INTO TABLE employee;

LOAD DATA LOCAL INPATH '/home/xiaosi/hive/input/employeeData2.txt'
OVERWRITE INTO TABLE employee2
PARTITION (country='China',state='ShanDong');


/* 修改列信息 */
ALTER TABLE employee CHANGE COLUMN name name string comment 'employee name' AFTER salary;

/* 单个查询语句中创建表并加载数据 */
CREATE TABLE employee_part AS
SELECT name, salary, address.city, country, state
FROM employee2 e
WHERE e.country = 'China' AND e.state = 'ShanDong';

/* 导出数据 */
INSERT OVERWRITE LOCAL DIRECTORY '/home/xiaosi/hive/input/employeePart'
SELECT name, salary, address.city, address.state, country, state
FROM employee2 e
WHERE e.country = 'China' AND e.state = 'ShanDong';

//----------------------------------------------------------------------------------------------------

/* 查询 */
select name, salary, subordinates[0], deductions['eat'],
address.city, address.state, country, state from employee2;














