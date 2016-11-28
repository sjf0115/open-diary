#!/bin/sh

## 循环运行脚本
# $1 开始运行日期 $2 截止运行日期 $3 运行脚本名称
# 注意：脚本只有一个参数 为日期参数 格式20161128

# 开始日期
if [ -n "$1" ]
then
    begin_date=`date +"%Y%m%d" -d "$1"`
else
    begin_date=`date +'%Y%m%d' -d "1 days ago"`
fi

# 截止日期
if [ -n "$2" ]
then
    end_date=`date +"%Y%m%d" -d "$2"`
else
    end_date=`date +'%Y%m%d' -d "1 days ago"`
fi

# 脚本名称
if [ -n "$3" ]
then
    sh_name=$3
else
    exit
fi

beg_s=`date -d "${begin_date}" +%s`
end_s=`date -d "${end_date}" +%s`


while [ "$beg_s" -le "$end_s" ] ;do
        format_date=`date -d "@$beg_s" +"%Y%m%d"`
        sh -x ${sh_name} ${format_date}
        beg_s=$((beg_s+86400))
done