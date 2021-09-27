#!/usr/bin/env bash
nohup /kpn/soft/grafana/bin/grafana-server \
  -config /kpn/conf/grafana.ini \
  -homepath /kpn/soft/grafana >> /kpn/logs/grafana.log 2>&1 &
