sudo cp webapps/ROOT.war ROOT.war.backup
sudo rm -rf webapps/*
#sudo cp /home/$1/livebroadcast.properties /www/confs/confs/pools/livebroadcast-bjzx-test/.
sudo tar -zxvf /home/$1/*-web-live-broadcast.tar.gz -C ./webapps
sudo bin/shutdown.sh
#sudo rm -f logs/*
for num in $(seq 2 10)
do
        twoDaysAgo=`date -d -$numday +%Y%m%d`
        sudo rm -f logs/*$twoDaysAgo*
done
sudo bin/startup.sh
sleep 20
sudo sh bin/switcher.sh off 127.0.0.1 6803 live.web.verifySign --type=resource