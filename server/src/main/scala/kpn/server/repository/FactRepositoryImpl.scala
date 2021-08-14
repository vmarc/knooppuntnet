package kpn.server.repository

import kpn.api.common.subset.NetworkFactRefs
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import org.springframework.stereotype.Component

@Component
class FactRepositoryImpl(database: Database) extends FactRepository {

  override def factsPerNetwork(subset: Subset, fact: Fact): Seq[NetworkFactRefs] = {
    //FactsPerNetworkView.query(analysisDatabase, subset, fact, stale)
    Seq.empty
  }
}
