#!/usr/bin/env bash

function transform
{
	file_name=$1
	file_path='/home/xiaosi/adv/put/'$1
	result_path='/home/xiaosi/adv/put/put_uid_total.txt'
	echo ${file_path}
	while read line
	do
	    echo ${line}"	put	"${file_name} >>${result_path}
	done < ${file_path}
}

transform lose_uids.3_1.mon.adr.high.c1