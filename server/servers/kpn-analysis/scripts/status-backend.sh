# status
echo backend-disk-space-used=$(df --output=used /kpn | tail -1)
echo backend-disk-space-available=$(df --output=avail /kpn | tail -1)
echo backend-disk-space-overpass=$(du -s /kpn/database | cut -f1)
echo backend-mem-total=$(egrep --color=none -e MemTotal /proc/meminfo |  awk '{print $2}')
echo backend-mem-free=$(egrep --color=none -e MemFree /proc/meminfo |  awk '{print $2}')
echo backend-mem-swap-total=$(egrep --color=none -e SwapTotal /proc/meminfo | awk '{print $2}')
echo backend-mem-swap-free=$(egrep --color=none -e SwapFree /proc/meminfo | awk '{print $2}')
echo backend-mem-available=$(egrep --color=none -e MemAvailable  /proc/meminfo |  awk '{print $2}')
echo backend-mem-buffers=$(egrep --color=none -e Buffers /proc/meminfo | awk '{print $2}')