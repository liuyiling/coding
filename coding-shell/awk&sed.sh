cat ipList.txt | while read line;
do
    ip=`echo $line| awk -F " " '{print $1}'`
    times=`echo $line| awk -F " " '{print $2}'`
    curlresult=`curl http://192.168.20.21:8090/ip/get?ip=$ip`
    countryJson=`echo $curlresult | awk '{split($0,a,",");print a[3]}'`
    countryWithChar=`echo $countryJson | awk '{split($0,b,":");print b[2]}'`
    country=`echo $countryWithChar | awk '{print substr($0,2,length($0)-2)}'`
    echo "$country $times">>jsonResult.txt
done

head  -1 ipList.txt| awk '{printf("%20s%20s%20s\n","country","nums","percentage")}'>>result.txt
awk '{sum[$1] += $2; total += $2;} END {for (i in sum) printf "%20s%20s%20s\n",i,sum[i], sum[i]*100/total"%"}' jsonResult.txt >> result.txt

sed 's/"//g' abc.txt | sed 's/{//g' | sed 's/}//g' | awk -F ":| |," '{print $1,$(NF-4)}'



#12 183.197.49.249:city:沧州,isp:移动,nation:中国,origin:,province:河北
awk -F " |,|:" ' {if($8=="中国") {total=total+$1;area=$12"-"$4; myarr[area]=myarr[area]+$1;}} END{ for(nation in myarr) {print myarr[nation],myarr[nation]*100.0/total,nation}} ' jsonResult.txt |sort -rn |awk '{print $3,$2}' >result.txt

awk -F " |,|:" '
    {
        if($8=="中国"){
            total=total+$1;
            array[$12"-"$4]=array[$12"-"$4]+$1;
        }
    }

    END{
         for(i in array){
            print array[i]*100.0/total,i;
         }
    }
' jsonResult.txt | sort -rn | awk '{print $3,$2}' > result.txt