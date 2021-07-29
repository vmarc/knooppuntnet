package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkNodeDetail
import kpn.api.common.network.NetworkNodesPage
import kpn.core.mongo.Database
import kpn.core.util.Log
import kpn.core.util.NaturalSorting
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NodeRouteRepository
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.springframework.stereotype.Component

@Component
class NetworkNodesPageBuilder(
  mongoEnabled: Boolean,
  // old
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository,
  nodeRouteRepository: NodeRouteRepository,
  // new
  database: Database
) {

  private val log = Log(classOf[NetworkNodesPageBuilder])

  def build(networkId: Long): Option[NetworkNodesPage] = {
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
    val pipeline = Seq(
      filter(
        equal("_id", networkId)
      ),
      project(
        fields(
          include("summary"),
          include("nodes")
        )
      )
    )
    database.networkInfos.optionAggregate[NetworkNodesPage](pipeline, log).map { page =>
      page.copy(
        timeInfo = TimeInfoBuilder.timeInfo,
        surveyDateInfo = SurveyDateInfoBuilder.dateInfo,
        networkType = page.summary.networkType, // TODO MONGO double?
        networkScope = page.summary.networkScope, // TODO MONGO double?
      )
    }
  }

  // *** old couchdb based code ***

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
        networkInfoNode.connection,
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
