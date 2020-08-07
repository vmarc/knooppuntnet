# status
echo db-disk-space-used="$(df --output=used /kpn | tail -1)"
echo db-disk-space-available="$(df --output=avail /kpn | tail -1)"
echo db-mem-total="$(grep -E --color=none -e MemTotal /proc/meminfo |  awk '{print $2}')"
echo db-mem-free="$(grep -E --color=none -e MemFree /proc/meminfo |  awk '{print $2}')"
echo db-mem-swap-total="$(grep -E --color=none -e SwapTotal /proc/meminfo | awk '{print $2}')"
echo db-mem-swap-free="$(grep -E --color=none -e SwapFree /proc/meminfo | awk '{print $2}')"
echo db-mem-available="$(grep -E --color=none -e MemAvailable  /proc/meminfo |  awk '{print $2}')"
echo db-mem-buffers="$(grep -E --color=none -e Buffers /proc/meminfo | awk '{print $2}')"

