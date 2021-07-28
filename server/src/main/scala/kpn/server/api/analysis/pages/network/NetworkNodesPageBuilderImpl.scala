package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkNodeDetail
import kpn.api.common.network.NetworkNodesPage
import kpn.api.common.network.NetworkSummary
import kpn.core.mongo.Database
import kpn.core.mongo.actions.networks.MongoQueryNetwork
import kpn.core.mongo.actions.networks.MongoQueryNetworkChangeCount
import kpn.core.mongo.actions.nodes.MongoQueryNodes
import kpn.core.mongo.actions.routes.MongoQueryRouteNodeIds
import kpn.core.util.NaturalSorting
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.ChangeSetRepository
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
  database: Database,
  mongoEnabled: Boolean
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
    new MongoQueryNetwork(database).execute(networkId).map { networkDoc =>
      // TODO MONGO parallelize queries (actually use the futures in the mongo api)
      val changeCount = new MongoQueryNetworkChangeCount(database).execute(networkId)
      val networkNodeIds = networkDoc.nodeRefs.map(_.nodeId)
      val routeNodeIds = new MongoQueryRouteNodeIds(database).execute(networkDoc.routeRefs.map(_.routeId))
      val nodeIds = (networkNodeIds ++ routeNodeIds).distinct.sorted
      val nodeInfos = new MongoQueryNodes(database).execute(nodeIds)
      val networkInfoNodes = NaturalSorting.sortBy(nodeInfos)(_.name(networkDoc.scopedNetworkType))
      val routeReferences = /* TODO !!! */ nodeRouteRepository.nodesRouteReferences(networkDoc.scopedNetworkType, nodeIds)
      val nodeRouteReferencesMap = routeReferences.map(nrr => nrr.nodeId -> nrr.routeRefs).toMap

      val nodes = networkInfoNodes.map { nodeInfo =>
        val roleConnection: Boolean = networkDoc.nodeRefs.find(_.nodeId == nodeInfo._id) match {
          case Some(nodeRef) => nodeRef.role.contains("connection")
          case None => false
        }
        val ln = nodeInfo.longName(networkDoc.scopedNetworkType)
        val longName = if (ln.nonEmpty) ln else "-"
        NetworkNodeDetail(
          nodeInfo._id,
          nodeInfo.name(networkDoc.scopedNetworkType),
          longName,
          nodeInfo.latitude,
          nodeInfo.longitude,
          false, // networkInfoNode.connection, // true if all routes (in the network) that contain this node have role "connection" in the network relation
          roleConnection = roleConnection,
          definedInRelation = networkNodeIds.contains(nodeInfo._id),
          false, // networkInfoNode.definedInRoute,
          proposed = nodeInfo.labels.contains("proposed"), // networkInfoNode.proposed,
          nodeInfo.lastUpdated,
          nodeInfo.lastSurvey,
          "-", //networkInfoNode.integrityCheck.map(_.expected.toString).getOrElse("-"),
          nodeRouteReferencesMap.getOrElse(nodeInfo._id, Seq.empty),
          nodeInfo.facts,
          nodeInfo.tags // TODO MONGO should not be needed?
        )
      }

      NetworkNodesPage(
        TimeInfoBuilder.timeInfo,
        SurveyDateInfoBuilder.dateInfo,
        NetworkSummary(
          networkDoc.networkType,
          networkDoc.name,
          0, // factCount: Long,
          0, //nodeCount: Long,
          0, //routeCount: Long,
          changeCount: Long,
          active = true
        ),
        //NetworkSummaryBuilder.toSummary(networkDoc, changeCount),
        networkDoc.networkType, // TODO MONGO double?
        networkDoc.networkScope, // TODO MONGO double?
        nodes,
        Seq.empty //networkDoc.routeRefs // TODO MONGO why ???
      )
    }
  }

  private def oldBuildPage(networkId: Long): Option[NetworkNodesPage] = {
    networkRepository.network(networkId).map(oldBuildPageContents)
  }

  private def oldBuildPageContents(networkInfo: NetworkInfo): NetworkNodesPage = {
    val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)
    val networkInfoNodes = networkInfo.detail match {
      case Some(detail) => NaturalSorting.sortBy(detail.nodes)(_.name)
      case None => Seq.empty
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
}
