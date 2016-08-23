#!/bin/sh

if [ -n "$1" ]
then
        day=`date +'%Y%m%d' -d $1`
else
        day=`date +"%Y%m%d" -d "-1 days"`
fi

fileName=/home/xiaosi/flight${day}.log
echo ${fileName}

if [ ! -f ${fileName} ]
then
    echo "文件存在"
fi