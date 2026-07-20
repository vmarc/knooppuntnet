package kpn.api.common.network;

public record Integrity(
  Boolean isOk,
  Boolean hasChecks,
  Long count,
  Long okCount,
  Long nokCount,
  String coverage,
  String okRate,
  String nokRate
) {
}

/* TODO migrate
package kpn.api.common.network

case class Integrity(
  isOk: Boolean = true,
  hasChecks: Boolean = false,
  count: Long = 0,
  okCount: Long = 0,
  nokCount: Long = 0,
  coverage: String = "-",
  okRate: String = "-",
  nokRate: String = "-"
) {
  def okRateOk: Boolean = okRate == "100,00%" || okRate == "-"
}

*/
