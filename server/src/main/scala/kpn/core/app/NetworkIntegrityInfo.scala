package kpn.core.app

import kpn.core.util.Formatter.percentage
import kpn.shared.NetworkIntegrityCheckFailed

case class NetworkIntegrityInfo(networkId: Long, networkName: String, detail: NetworkIntegrityCheckFailed) {
  def checkCount: Int = detail.checks.size

  def failedCount: Int = detail.checks.count(_.failed)

  def failedRate: String = percentage(failedCount, checkCount)
}
