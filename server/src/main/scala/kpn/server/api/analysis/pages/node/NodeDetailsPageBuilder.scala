package kpn.server.api.analysis.pages.node

import kpn.api.common.Language
import kpn.api.common.LocationInfo
import kpn.api.common.NodeInfo
import kpn.api.common.common.Reference
import kpn.api.common.node.NodeDetailsPage
import kpn.core.doc.NodeDoc
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class NodeDetailsPageBuilder(
  nodeRepository: NodeRepository,
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository,
  locationService: LocationService
) {
  case class NodeBuildContext(
    nodeDoc: NodeDoc,
    changeCount: Long,
    networkReferences: Seq[Reference],
    locations: Seq[LocationInfo]
  )

  def build(language: Language, nodeId: Long): Option[NodeDetailsPage] = {
    if (nodeId == 1L) {
      Some(NodeDetailsPageExample.page)
    } else {
      buildPage(language, nodeId)
    }
  }

  private def buildPage(language: Language, nodeId: Long): Option[NodeDetailsPage] = {
    nodeRepository.nodeWithId(nodeId).map { nodeDoc =>
      val context = buildContext(language, nodeId, nodeDoc)
      buildNodeDetailsPage(context)
    }
  }

  private def buildContext(language: Language, nodeId: Long, nodeDoc: NodeDoc): NodeBuildContext = {
    NodeBuildContext(
      nodeDoc = nodeDoc,
      changeCount = changeSetRepository.nodeChangesCount(nodeId),
      networkReferences = networkRepository.nodeNetworkReferences(nodeId),
      locations = locationService.toInfos(language, nodeDoc.base.locations, nodeDoc.base.locations).reverse
    )
  }

  private def buildNodeDetailsPage(context: NodeBuildContext): NodeDetailsPage = {
    NodeDetailsPage(
      nodeInfo = buildNodeInfo(context),
      mixedRouteScopes = calculateMixedRouteScopes(context.nodeDoc),
      routeReferences = context.nodeDoc.routeReferences,
      networkReferences = context.networkReferences,
      integrity = context.nodeDoc.integrity,
      changeCount = context.changeCount
    )
  }

  private def buildNodeInfo(context: NodeBuildContext): NodeInfo = {
    NodeInfo(
      id = context.nodeDoc._id,
      active = context.nodeDoc.active,
      orphan = isOrphanNode(context.nodeDoc),
      country = context.nodeDoc.base.country,
      name = context.nodeDoc.base.name.getOrElse(context.nodeDoc._id.toString),
      names = context.nodeDoc.base.names,
      latitude = context.nodeDoc.base.latitude,
      longitude = context.nodeDoc.base.longitude,
      lastUpdated = context.nodeDoc.base.raw.timestamp,
      lastSurvey = context.nodeDoc.base.lastSurvey,
      tags = context.nodeDoc.tags,
      facts = context.nodeDoc.facts,
      locations = context.locations,
      integrity = context.nodeDoc.integrity
    )
  }

  private def calculateMixedRouteScopes(nodeDoc: NodeDoc): Boolean = {
    val allScopes = nodeDoc.base.names.map(_.routeScope) ++
      nodeDoc.routeReferences.map(_.routeScope) ++
      nodeDoc.networkRelationReferences.map(_.routeScope)
    allScopes.distinct.sizeIs > 1
  }

  private def isOrphanNode(nodeDoc: NodeDoc): Boolean = {
    nodeDoc.networkRelationReferences.isEmpty && nodeDoc.routeReferences.isEmpty
  }
}
