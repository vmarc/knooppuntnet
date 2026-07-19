package kpn.api.common.statistics

case class StatisticValues(
  _id: String,
  total: String,
  values: Seq[StatisticValue]
)
