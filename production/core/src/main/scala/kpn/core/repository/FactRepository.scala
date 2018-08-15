package kpn.core.repository

import akka.util.Timeout
import kpn.core.app.IntegrityCheckPage
import kpn.shared.Fact
import kpn.shared.Subset
import kpn.shared.subset.NetworkRoutesFacts

trait FactRepository {
  def routeFacts(subset: Subset, fact: Fact, timeout: Timeout, stale: Boolean = true): Seq[NetworkRoutesFacts]

  def integrityCheckFacts(country: String, networkType: String, timeout: Timeout, stale: Boolean = true): IntegrityCheckPage

  def networkCollections(): Seq[Long]
}
