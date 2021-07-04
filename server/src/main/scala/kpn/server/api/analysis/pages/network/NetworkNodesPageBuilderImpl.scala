package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkNodeDetail
import kpn.api.common.network.NetworkNodesPage
import kpn.core.util.NaturalSorting
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.MongoNetworkRepository
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NodeRouteRepository
import org.springframework.stereotype.Component

@Component
class NetworkNodesPageBuilderImpl(
  // old
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository,
  nodeRouteRepository: NodeRouteRepository,
  // new
  mongoEnabled: Boolean,
  mongoNetworkRepository: MongoNetworkRepository
) extends NetworkNodesPageBuilder {

  override def build(networkId: Long): Option[NetworkNodesPage] = {
    if (networkId == 1) {
      Some(NetworkNodesPageExample.nodesPage)
    }
    else {
      if (mongoEnabled) {
        mongoBuildPage(networkId)
      }
      else {
        oldBuildPage(networkId)
      }
    }
  }

  private def mongoBuildPage(networkId: Long): Option[NetworkNodesPage] = {
    mongoNetworkRepository.networkWithId(networkId).map(mongoBuildPageContents)
  }

  private def mongoBuildPageContents(networkInfo: NetworkInfo): NetworkNodesPage = {
    val changeCount = mongoNetworkRepository.networkChangeCount(networkInfo.attributes.id)
    val networkInfoNodes = networkInfo.detail match {
      case Some(detail) => NaturalSorting.sortBy(detail.nodes)(_.name)
      case None => Seq()
    }
    val nodeIds = networkInfoNodes.map(_.id)
    val routeReferences = /* TODO !!! */ nodeRouteRepository.nodesRouteReferences(networkInfo.attributes.scopedNetworkType, nodeIds)
    val nodeRouteReferencesMap = routeReferences.map(nrr => nrr.nodeId -> nrr.routeRefs).toMap

    val nodes = networkInfoNodes.map { networkInfoNode =>
      NetworkNodeDetail(
        networkInfoNode.id,
        networkInfoNode.name,
        networkInfoNode.longName.getOrElse("-"),
        networkInfoNode.latitude,
        networkInfoNode.longitude,
        networkInfoNode.connection, // true if all routes (in the network) that contain this node have role "connection" in the network relation
        networkInfoNode.roleConnection,
        networkInfoNode.definedInRelation,
        networkInfoNode.definedInRoute,
        networkInfoNode.proposed,
        networkInfoNode.timestamp,
        networkInfoNode.lastSurvey,
        networkInfoNode.integrityCheck.map(_.expected.toString).getOrElse("-"),
        nodeRouteReferencesMap.getOrElse(networkInfoNode.id, Seq.empty),
        networkInfoNode.facts,
        networkInfoNode.tags
      )
    }

    NetworkNodesPage(
      TimeInfoBuilder.timeInfo,
      SurveyDateInfoBuilder.dateInfo,
      NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
      networkInfo.attributes.networkType,
      networkInfo.attributes.networkScope,
      nodes,
      networkInfo.routeRefs
    )
  }

  private def oldBuildPage(networkId: Long): Option[NetworkNodesPage] = {
    networkRepository.network(networkId).map(oldBuildPageContents)
  }

  private def oldBuildPageContents(networkInfo: NetworkInfo): NetworkNodesPage = {
    val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)
    val networkInfoNodes = networkInfo.detail match {
      case Some(detail) => NaturalSorting.sortBy(detail.nodes)(_.name)
      case None => Seq()
    }
    val nodeIds = networkInfoNodes.map(_.id)
    val routeReferences = nodeRouteRepository.nodesRouteReferences(networkInfo.attributes.scopedNetworkType, nodeIds)
    val nodeRouteReferencesMap = routeReferences.map(nrr => nrr.nodeId -> nrr.routeRefs).toMap

    val nodes = networkInfoNodes.map { networkInfoNode =>
      NetworkNodeDetail(
        networkInfoNode.id,
        networkInfoNode.name,
        networkInfoNode.longName.getOrElse("-"),
        networkInfoNode.latitude,
        networkInfoNode.longitude,
        networkInfoNode.connection, // true if all routes (in the network) that contain this node have role "connection" in the network relation
        networkInfoNode.roleConnection,
        networkInfoNode.definedInRelation,
        networkInfoNode.definedInRoute,
        networkInfoNode.tags.has("state", "proposed"),
        networkInfoNode.timestamp,
        networkInfoNode.lastSurvey,
        networkInfoNode.integrityCheck.map(_.expected.toString).getOrElse("-"),
        nodeRouteReferencesMap.getOrElse(networkInfoNode.id, Seq.empty),
        networkInfoNode.facts,
        networkInfoNode.tags
      )
    }

    NetworkNodesPage(
      TimeInfoBuilder.timeInfo,
      SurveyDateInfoBuilder.dateInfo,
      NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
      networkInfo.attributes.networkType,
      networkInfo.attributes.networkScope,
      nodes,
      networkInfo.routeRefs
    )
  }
}
