#!/bin/sh

hadoop_name="hadoop"
hadoop_home="HADOOP_HOME"
hadoop_location="/home/xiaosi/opt/hadoop-2.7.3"

kylin_name="kylin"
kylin_home="KYLIN_HOME"
kylin_location="/home/xiaosi/opt/kylin-1.6"

kafka_name="kafka"
kafka_home="KAFKA_HOME"
kafka_location="/opt/kafka_2.11-0.10.0.0"

scala_name="scala"
scala_home="SCALA_HOME"
scala_location="/opt/scala-2.11.8"

mongodb_name="mongodb"
mongodb_home="MONGODB_HOME"
mongodb_location="/opt/mongodb-ubuntu1404-3.2.8"

robomongo_name="robomongo"
robomongo_home="ROBOMONGO_HOME"
robomongo_location="/opt/robomongo-0.9.0"

flume_name="flume"
flume_home="FLUME_HOME"
flume_location="/home/xiaosi/opt/flume-1.6.0"

hbase_name="hbase"
hbase_home="HBASE_HOME"
hbase_location="/home/xiaosi/opt/hbase-1.2.2"

kibana_name="kibana"
kibana_home="KIBANA_HOME"
kibana_location="/home/xiaosi/opt/kibana-4.5.1"


## 配置环境
# home_desc -> export 注释
# home_key ->  export key
# home_value -> export location
function export_env_home
{
    echo "------------ 配置环境"
    home_desc=$1
    home_key=$2
    home_value=$3
    sudo echo "# ${home_desc}"  >>  /etc/profile
    sudo echo "export ${home_key}=${home_value}"  >>  /etc/profile
    sudo echo 'export PATH=${'"${home_key}"'}/bin:$PATH'  >>  /etc/profile
}

#export_env_home ${kylin_name} ${kylin_home} ${kylin_location}
#export_env_home ${kafka_name} ${kafka_home} ${kafka_location}
#export_env_home ${scala_name} ${scala_home} ${scala_location}
#export_env_home ${mongodb_name} ${mongodb_home} ${mongodb_location}
#export_env_home ${robomongo_name} ${robomongo_home} ${robomongo_location}
#export_env_home ${flume_name} ${flume_home} ${flume_location}
#export_env_home ${hbase_name} ${hbase_home} ${hbase_location}
export_env_home ${kibana_name} ${kibana_home} ${kibana_location}