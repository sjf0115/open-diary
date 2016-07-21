#!/bin/sh

kylin_name="kylin"
kylin_home="KYLIN_HOME"
kylin_location="/opt/apache-kylin-1.5.2.1-bin"


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
    source /etc/profile
}

export_env_home ${kylin_name} ${kylin_home} ${kylin_location}