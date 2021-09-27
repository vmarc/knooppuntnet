# nohup /kpn/soft/prometheus/prometheus --web.listen-address="localhost:9090"  >> /kpn/logs/prometheus.log 2>&1 &
/kpn/soft/prometheus/prometheus --web.listen-address="localhost:9090" --config.file=/kpn/conf/prometheus.yml >> /kpn/logs/prometheus.log 2>&1
