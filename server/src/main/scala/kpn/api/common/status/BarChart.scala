package kpn.api.common.status

object BarChart {
  def apply(period: String, data: Seq[NameValue]): BarChart = {
    val xAxisTicks: Seq[Long] = period match {
      case "year" => Seq()
      case "month" => Seq()
      case "week" => Seq()
      case "day" => Seq()
      case "hour" => Seq(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55)
      case _ => Seq()
    }
    BarChart(xAxisTicks, data)
  }
}

case class BarChart(
  xAxisTicks: Seq[Long],
  data: Seq[NameValue]
)
