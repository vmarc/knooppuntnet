package kpn.api.common.status

object BarChart2D {
  def apply(
    period: String,
    xAxisLabel: String,
    yAxisLabel: String,
    legendTitle: String,
    data: Seq[BarChart2dValue]
  ): BarChart2D = {
    val xAxisTicks: Seq[Long] = period match {
      case "year" => Seq()
      case "month" => Seq()
      case "week" => Seq()
      case "day" => Seq()
      case "hour" => Seq(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60)
      case _ => Seq()
    }
    BarChart2D(
      xAxisLabel,
      yAxisLabel,
      xAxisTicks,
      legendTitle,
      data
    )
  }
}

case class BarChart2D(
  xAxisLabel: String,
  yAxisLabel: String,
  xAxisTicks: Seq[Long],
  legendTitle: String,
  data: Seq[BarChart2dValue]
)
