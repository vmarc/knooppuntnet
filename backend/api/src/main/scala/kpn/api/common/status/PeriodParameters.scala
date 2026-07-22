package kpn.api.common.status

case class PeriodParameters(
  period: String,
  year: Long,
  month: Option[Long],
  week: Option[Long],
  day: Option[Long],
  hour: Option[Long]
)
