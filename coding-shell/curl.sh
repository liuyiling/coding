#遍历ids.txt中的每一行，然后写入curl的结果到result.txt中
cat ids.txt | while read line; do curl http://192.168.20.57:8993/live_monitor/getinfo.json?id=$line>>result.txt;echo>>result.txt; done