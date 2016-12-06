#!/usr/bin/env bash


echo "脚本的文件名: $0"
echo "脚本第一个参数 : $1"
echo "脚本第二个参数 : $2"
echo "脚本所有参数: $@"
echo "脚本所有参数: $*"
echo "脚本参数个数 : $#"

echo "\$*=" $*
echo "\"\$*\"=" "$*"
echo "\$@=" $@
echo "\"\$@\"=" "$@"

echo "-------------------------"
echo "print each param from \$*"
for var in $*
do
    echo "$var"
done

echo "-------------------------"
echo "print each param from \$@"
for var in $@
do
    echo "$var"
done

echo "-------------------------"
echo "print each param from \"\$*\""
for var in "$*"
do
    echo "$var"
done

echo "-------------------------"
echo "print each param from \"\$@\""
for var in "$@"
do
    echo "$var"
done