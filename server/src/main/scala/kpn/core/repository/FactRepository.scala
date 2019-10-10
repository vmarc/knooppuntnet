package kpn.core.repository

import akka.util.Timeout
import kpn.core.app.IntegrityCheckPage
import kpn.shared.Fact
import kpn.shared.Subset
import kpn.shared.subset.NetworkFactRefs

trait FactRepository {
  def factsPerNetwork(subset: Subset, fact: Fact, timeout: Timeout, stale: Boolean = true): Seq[NetworkFactRefs]

  def integrityCheckFacts(country: String, networkType: String, timeout: Timeout, stale: Boolean = true): IntegrityCheckPage

}
