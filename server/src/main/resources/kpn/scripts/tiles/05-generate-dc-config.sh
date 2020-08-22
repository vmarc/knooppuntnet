# generate-dc-config
#
nohup /bin/bash << ***eof*** >> /kpn/logs/05-generate-dc-config.log 2>&1 &
echo ---
echo $(date "+%Y-%m-%d %H:%M:%S") Start
cd /kpn/openmaptiles
/bin/bash -x -c "make --debug generate-dc-config"
echo $(date "+%Y-%m-%d %H:%M:%S") Done
***eof***
