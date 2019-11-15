package kpn.api.common.network

case class Integrity(
  isOk: Boolean = true,
  hasChecks: Boolean = false,
  count: String = "",
  okCount: Long = 0,
  nokCount: Long = 0,
  coverage: String = "",
  okRate: String = "",
  nokRate: String = ""
) {
  def okRateOk: Boolean = okRate == "100,00%" || okRate == "-"
}
