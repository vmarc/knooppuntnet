package kpn.server.repository

import kpn.api.common.subset.NetworkFactRefs
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.core.database.Database
import kpn.core.database.views.analyzer.FactsPerNetworkView
import org.springframework.stereotype.Component

@Component
class FactRepositoryImpl(analysisDatabase: Database) extends FactRepository {

  override def factsPerNetwork(subset: Subset, fact: Fact, stale: Boolean): Seq[NetworkFactRefs] = {
    FactsPerNetworkView.query(analysisDatabase, subset, fact, stale)
  }

}
