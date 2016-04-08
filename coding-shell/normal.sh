#进入当前目录,一般的shell都有这句话，但其实没有什么作用
cd `dirname $0`
#单引号表示的是按照原本的命令来输入控制台，而不是输入字符串
echo `pwd`
#声明变量
JAVA=java
#输出变量名
echo $JAVA
