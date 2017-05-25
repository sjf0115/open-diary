#!/usr/bin/env bash

num1=100
num2=100
if test $[num1] -eq $[num2]
then
    echo '两个数相等'
else
    echo '两个数不相等'
fi

# 两个数相等


str1="2016-11-22 12:34:21"
str2="2016-11-22 12:34:21"
if test "${str1}" = "${str2}"
then
    echo '两个日期相同'
else
    echo '两个日期不相同'
fi

# 两个日期相同


if test -e /home/xiaosi/error.txt
then
    echo '文件存在'
else
    echo '文件不存在'
fi

# 文件存在

if test -r /home/xiaosi/error.txt
then
    echo '文件可读'
else
    echo '文件不可读'
fi

# 文件可读

if test -s /home/xiaosi/error.txt
then
    echo '文件不为空'
else
    echo '文件为空'
fi

# 文件为空

if test -d /home/xiaosi
then
    echo '文件为目录'
else
    echo '文件不为目录'
fi

# 文件为目录


str="2016-11-21"
if test "${str}" = "2016-11-21" -a -s /home/xiaosi/error.txt
then
    echo '日期正确 并且 文件不为空'
else
    echo '日期错误 或者 文件为空'
fi

# 日期错误 或者 文件为空