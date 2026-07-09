package kpn.api.common.status

case class DiskUsage(
  frontend: BarChart,
  database: BarChart,
  backend: BarChart
)
