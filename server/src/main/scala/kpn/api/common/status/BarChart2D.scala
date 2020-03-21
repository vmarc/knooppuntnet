package kpn.api.common.status

case class BarChart2D(
  xAxisLabel: String,
  yAxisLabel: String,
  legendTitle: String,
  data: Seq[BarChart2dValue]
)
