package kpn.server.api.analysis.pages.subset

import kpn.api.common.OrphanNodeInfo
import kpn.api.common.subset.SubsetOrphanNodesPage
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.actions.subsets.MongoQuerySubsetInfo
import kpn.core.mongo.actions.subsets.MongoQuerySubsetOrphanNodes
import kpn.core.mongo.doc.OrphanNodeDoc
import kpn.core.util.Log
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.OrphanRepository
import kpn.server.repository.OverviewRepository
import kpn.server.repository.SubsetRepository
import org.springframework.stereotype.Component

@Component
class SubsetOrphanNodesPageBuilder(
  mongoEnabled: Boolean,
  database: Database,
  overviewRepository: OverviewRepository,
  orphanRepository: OrphanRepository,
  subsetRepository: SubsetRepository
) {

  private val log = Log(classOf[SubsetOrphanNodesPageBuilder])

  def build(subset: Subset): SubsetOrphanNodesPage = {
    if (mongoEnabled) {
      buildPage(subset)
    }
    else {
      oldBuildPage(subset)
    }
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

  private def oldBuildPage(subset: Subset): SubsetOrphanNodesPage = {
    val subsetInfo = if (mongoEnabled) {
      subsetRepository.subsetInfo(subset)
    }
    else {
      val figures = overviewRepository.figures()
      SubsetInfoBuilder.newSubsetInfo(subset, figures)
    }

    val nodes = orphanRepository.orphanNodes(subset)

    val orphanNodeInfos = nodes.map { node =>
      OrphanNodeInfo(
        id = node.id,
        name = node.networkTypeName(subset.networkType),
        longName = node.networkTypeLongName(subset.networkType).getOrElse("-"),
        proposed = node.networkTypeProposed(subset.networkType),
        lastUpdated = node.lastUpdated,
        lastSurvey = node.lastSurvey.map(_.yyyymmdd).getOrElse("-"),
        factCount = node.facts.size
      )
    }

    val sortedOrphanNodeInfos = orphanNodeInfos.sortWith((a, b) => a.name < b.name)

    SubsetOrphanNodesPage(
      TimeInfoBuilder.timeInfo,
      subsetInfo,
      sortedOrphanNodeInfos.toVector
    )
  }
}
