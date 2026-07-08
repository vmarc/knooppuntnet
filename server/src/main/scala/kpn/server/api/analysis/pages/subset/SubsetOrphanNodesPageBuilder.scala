package kpn.server.api.analysis.pages.subset

import kpn.api.common.subset.SubsetOrphanNodesPage
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.subsets.MongoQuerySubsetInfo
import kpn.database.actions.subsets.MongoQuerySubsetOrphanNodes
import kpn.database.base.Database
import kpn.server.api.analysis.pages.TimeInfoBuilder
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class SubsetOrphanNodesPageBuilder(database: Database) {

  private val log = Log(classOf[SubsetOrphanNodesPageBuilder])

  def build(subset: Subset): SubsetOrphanNodesPage = {
    val subsetInfo = new MongoQuerySubsetInfo(database).execute(subset, log)
    val nodeInfos = new MongoQuerySubsetOrphanNodes(database).execute(subset)

    SubsetOrphanNodesPage(
      TimeInfoBuilder.timeInfo,
      subsetInfo,
      nodeInfos
    )
  }
}
