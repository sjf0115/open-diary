#!/bin/sh

kylin_name="kylin"
kylin_home="KYLIN_HOME"
kylin_location="/opt/apache-kylin-1.5.2.1-bin"

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
flume_location="/opt/apache-flume-1.6.0-bin"

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

# export_env_home ${kylin_name} ${kylin_home} ${kylin_location}
#export_env_home ${kafka_name} ${kafka_home} ${kafka_location}
#export_env_home ${scala_name} ${scala_home} ${scala_location}
#export_env_home ${mongodb_name} ${mongodb_home} ${mongodb_location}
#export_env_home ${robomongo_name} ${robomongo_home} ${robomongo_location}
export_env_home ${flume_name} ${flume_home} ${flume_location}