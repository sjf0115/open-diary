#!/bin/sh

## 开始时间 默认为昨天
if [ -n "$1" ]
then
        start_day=$1
else
        start_day=`date +"%Y%m%d" -d "-1 days"`
fi

## 结束时间 默认为昨天
if [ -n "$2" ]
then
        end_day=$2
else
        end_day=`date +"%Y%m%d" -d "-1 days"`
fi

run_day=${start_day}

## 20160725 -> 1469376000
index_day_ms=`date -d "${run_day}" +%s`
end_day_ms=`date -d "${end_day}" +%s`

while [[ ${index_day_ms} -le ${end_day_ms} ]]
do
     echo "${index_day_ms} run ${run_day}"
     run_day=`date +"%Y%m%d" -d "${run_day} 1 days"`
     index_day_ms=`date -d "${run_day}" +%s`
done