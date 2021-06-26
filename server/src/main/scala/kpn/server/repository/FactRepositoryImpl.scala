package kpn.server.repository

import kpn.api.common.subset.NetworkFactRefs
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.core.database.views.analyzer.FactsPerNetworkView
import kpn.core.mongo.Database
import org.springframework.stereotype.Component

@Component
class FactRepositoryImpl(
  database: Database,
  // old
  analysisDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends FactRepository {

  override def factsPerNetwork(subset: Subset, fact: Fact, stale: Boolean): Seq[NetworkFactRefs] = {
    if (mongoEnabled) {
      ???
    }
    else {
      FactsPerNetworkView.query(analysisDatabase, subset, fact, stale)
    }
  }
}
