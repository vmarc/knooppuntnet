# status
echo db-disk-space-used=$(df --output=used /kpn | tail -1)
echo db-disk-space-available=$(df --output=avail /kpn | tail -1)
echo db-mem-total=$(egrep --color=none -e MemTotal /proc/meminfo |  awk '{print $2}')
echo db-mem-free=$(egrep --color=none -e MemFree /proc/meminfo |  awk '{print $2}')
echo db-mem-swap-total=$(egrep --color=none -e SwapTotal /proc/meminfo | awk '{print $2}')
echo db-mem-swap-free=$(egrep --color=none -e SwapFree /proc/meminfo | awk '{print $2}')
echo db-mem-available=$(egrep --color=none -e MemAvailable  /proc/meminfo |  awk '{print $2}')
echo db-mem-buffers=$(egrep --color=none -e Buffers /proc/meminfo | awk '{print $2}')

