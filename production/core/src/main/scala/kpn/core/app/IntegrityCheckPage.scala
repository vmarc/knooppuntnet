package kpn.core.app

import kpn.core.util.Formatter.percentage
import kpn.shared.NetworkIntegrityCheckFailed

case class IntegrityCheckPage(country: String, networkType: String, networkInfos: Seq[NetworkIntegrityInfo]) {
  def checkCount: Int = networkInfos.map(_.checkCount).sum
  def failedCount: Int = networkInfos.map(_.failedCount).sum
  def failedRate: String = percentage(failedCount, checkCount)
}
