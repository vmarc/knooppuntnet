package kpn.server.api.analysis.pages.subset

import kpn.api.common.OrphanNodeInfo
import kpn.api.common.subset.SubsetOrphanNodesPage
import kpn.api.custom.Subset
import kpn.database.actions.subsets.MongoQuerySubsetInfo
import kpn.database.actions.subsets.MongoQuerySubsetOrphanNodes
import kpn.database.base.Database
import kpn.core.doc.OrphanNodeDoc
import kpn.core.util.Log
import kpn.server.api.analysis.pages.TimeInfoBuilder
import org.springframework.stereotype.Component

@Component
class SubsetOrphanNodesPageBuilder(database: Database) {

  private val log = Log(classOf[SubsetOrphanNodesPageBuilder])

  def build(subset: Subset): SubsetOrphanNodesPage = {
    buildPage(subset)
  }

  private def buildPage(subset: Subset): SubsetOrphanNodesPage = {
    val subsetInfo = new MongoQuerySubsetInfo(database).execute(subset, log)
    val nodes = new MongoQuerySubsetOrphanNodes(database)
      .execute(subset)
      .map(toInfo)
      .sortBy(_.name)

    SubsetOrphanNodesPage(
      TimeInfoBuilder.timeInfo,
      subsetInfo,
      nodes
    )
  }

  private def toInfo(doc: OrphanNodeDoc): OrphanNodeInfo = {
    OrphanNodeInfo(
      id = doc.nodeId,
      name = doc.name,
      longName = doc.longName.getOrElse("-"),
      proposed = doc.proposed,
      lastUpdated = doc.lastUpdated,
      lastSurvey = doc.lastSurvey.map(_.yyyymmdd).getOrElse("-"),
      factCount = doc.facts.size
    )
  }
}
