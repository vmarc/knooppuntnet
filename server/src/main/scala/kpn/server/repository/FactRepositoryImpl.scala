package kpn.server.repository

import akka.util.Timeout
import kpn.core.database.Database
import kpn.core.database.views.analyzer.FactsPerNetworkView
import kpn.shared.Fact
import kpn.shared.Subset
import kpn.shared.subset.NetworkFactRefs
import org.springframework.stereotype.Component

@Component
class FactRepositoryImpl(analysisDatabase: Database) extends FactRepository {

  override def factsPerNetwork(subset: Subset, fact: Fact, timeout: Timeout, stale: Boolean): Seq[NetworkFactRefs] = {
    FactsPerNetworkView.query(analysisDatabase, subset, fact, stale)
  }

}
