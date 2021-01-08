package kpn.core.tools.log

case class LogRecordAnalysis(
  api: Boolean = false,
  tile: Boolean = false,
  analysis: Boolean = false,
  asset: Boolean = false,
  application: Boolean = false,
  robot: Boolean = false
) {

  def other: Boolean = !(api || tile || analysis || asset || application)

}
