#awk的基本使用方法
echo "a b c d e" | awk '{print $1; print $2; print $(NF-1); print $NF; print $0}'

#输出语句的使用
awk 'BEGIN{a=1;b="213";print "output",a,",",b;}'
awk 'BEGIN{a=1;b="213";printf("output %d,%s\n",a,b)}'

#分隔符
echo "a:b c,d" | awk -F " |,|:" '{print $1;print $2;print $NF}'

#BEGIN和AND代表在行首和行尾都执行一次
cat test.txt | awk -F " " 'BEGIN{print "begin process"} {print "process 1 " $1} {print "process 2 " $2} END {print "the end"}'

#2016:09 1    //表示2016年9月，有一个访问
#2016:06 1
#2016:06 1
#2016:01 1
#2015:01 1
#2014:01 1
#2015:01 1
#2016:02 1
cat time.txt | awk '{bytes[$1]+=$2} END { for(time in bytes) print bytes[time],time}'| sort -n

#99乘法表
awk 'BEGIN{ for(i=1;i<=3;i++) { for(j=1;j<=2;j++) { tarr[i,j]=i*j;print i,"*",j,"=",tarr[i,j]}}}'

#替换
awk 'BEGIN {info="this is a test"; sub("a","b",info); print info}'

#截取
awk 'BEGIN{print substr("1234",2,4)}'