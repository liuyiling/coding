################################变量################################
#声明变量,变量名和等号之间不能有空格
JAVA=java
#输出变量名
echo ${JAVA}
echo "I am a ${JAVA} man!"
echo
#还可以使用语句给变量赋值
#for file in `ls /etc`
#do
#    echo ${file};
#done

#进入当前目录,一般的shell都有这句话，但其实没有什么作用
cd `dirname $0`
#单引号表示的是按照原本的命令来输入控制台，而不是输入字符串
echo `pwd`


#字符串
string="abcd"
echo ${#string}
echo ${string:2:3}

#数组
array=(0 1 2 3 4 5)
array[5]=100
echo ${array[5]}
echo ${array[@]}
echo ${#array[@]}

echo "传递参数"
echo '$1:' "$1"
echo '$*:'
for i in "$*";
do
    echo $i
done

for i in "$@";
do
    echo $i
done

val=`expr 2 + 2`
four=4
echo "sum: ${val}"
#条件表达式要放在方括号之间，并且要有空格，例如: [$a==$b] 是错误的，必须写成 [ $a == $b ]。
if [ ${val} == ${four} ]
then
    echo "val 等于${four}"
fi
echo $result

if [ 4 -lt 8 ]
then
    echo "4 小于 8"
fi

if [ 4 -gt 8 -o 4 -lt 8 ]
then
    echo "或语句的使用"
fi

if [ 10 -gt 8 -a 4 -lt 8 ]
then
    echo "与语句的使用"
elif [ 10 -gt 8 -a 4 -lt 8 ]
then
    echo "与语句的使用"
else
    echo "妈的，错了"
fi

printf "%-10s %-8s %-4.2f\n" liuyiling man 75.2

int=1
while(($int<=5))
do
    echo $int
    let int++;
done


function functionName(){
    echo "函数执行"
}

echo "函数执行开始"
functionName
echo "函数结束开始"

function functionWithReturn(){
    return 6;
}

echo "函数执行开始"
functionWithReturn
echo "函数执行结果 $? !"

function functionWithParameter(){
    echo "参数1 $1"
    echo "参数2 $2"
}
echo "函数执行开始"
functionWithParameter 100 200
echo "函数执行结果 $? !"