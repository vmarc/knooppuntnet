# status
echo frontend-disk-space-used=$(df --output=used /kpn | tail -1)
echo frontend-disk-space-available=$(df --output=avail /kpn | tail -1)
echo frontend-mem-total=$(egrep --color=none -e MemTotal /proc/meminfo |  awk '{print $2}')
echo frontend-mem-free=$(egrep --color=none -e MemFree /proc/meminfo |  awk '{print $2}')
echo frontend-mem-swap-total=$(egrep --color=none -e SwapTotal /proc/meminfo | awk '{print $2}')
echo frontend-mem-swap-free=$(egrep --color=none -e SwapFree /proc/meminfo | awk '{print $2}')
echo frontend-mem-available=$(egrep --color=none -e MemAvailable  /proc/meminfo |  awk '{print $2}')
echo frontend-mem-buffers=$(egrep --color=none -e Buffers /proc/meminfo | awk '{print $2}')

