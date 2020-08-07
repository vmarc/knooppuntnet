# status
echo frontend-disk-space-used="$(df --output=used /kpn | tail -1)"
echo frontend-disk-space-available="$(df --output=avail /kpn | tail -1)"
echo frontend-mem-total="$(grep -E --color=none -e MemTotal /proc/meminfo | awk '{print $2}')"
echo frontend-mem-free="$(grep -E --color=none -e MemFree /proc/meminfo | awk '{print $2}')"
echo frontend-mem-swap-total="$(grep -E --color=none -e SwapTotal /proc/meminfo | awk '{print $2}')"
echo frontend-mem-swap-free="$(grep -E --color=none -e SwapFree /proc/meminfo | awk '{print $2}')"
echo frontend-mem-available="$(grep -E --color=none -e MemAvailable  /proc/meminfo | awk '{print $2}')"
echo frontend-mem-buffers="$(grep -E --color=none -e Buffers /proc/meminfo | awk '{print $2}')"
