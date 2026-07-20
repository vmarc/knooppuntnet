package kpn.api.common.status;

import kpn.api.common.status.NameValue;

import com.google.common.collect.ImmutableList;

public record BarChart(
  ImmutableList<Long> xAxisTicks,
  ImmutableList<NameValue> data
) {
}

/* TODO migrate
package kpn.api.common.status

object BarChart {
  def apply(period: String, data: Seq[NameValue]): BarChart = {
    val xAxisTicks: Seq[Long] = period match {
      case "year" => Seq.empty
      case "month" => Seq.empty
      case "week" => Seq.empty
      case "day" => Seq.empty
      case "hour" => Seq(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55)
      case _ => Seq.empty
    }
    BarChart(xAxisTicks, data)
  }
}

case class BarChart(
  xAxisTicks: Seq[Long],
  data: Seq[NameValue]
)

*/
