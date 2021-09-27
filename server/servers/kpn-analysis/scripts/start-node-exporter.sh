#!/usr/bin/env bash
nohup /kpn/soft/node_exporter/node_exporter \
  --web.listen-address="localhost:9100" >> /kpn/logs/node-exporter.log 2>&1 &
# --collector.disable-defaults \
# --collector.<name> \
# --collector.<name> \
# --collector.<name> \
# --collector.<name> \
# --collector.<name>
