package kpn.server.api.analysis.pages.network

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.network.NetworkNodeRow
import kpn.api.common.network.NetworkNodesPage
import kpn.api.custom.ScopedRouteType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.NodeRouteRepository
import org.springframework.stereotype.Component

@Component
class NetworkNodesPageBuilder(
  database: Database,
  nodeRouteRepository: NodeRouteRepository
) {

  private val log = Log(classOf[NetworkNodesPageBuilder])

  def build(networkId: Long): Option[NetworkNodesPage] = {
    if (networkId == 1) {
      Some(NetworkNodesPageExample.nodesPage)
    }
    else {
      buildPage(networkId)
    }
  }

  private def buildPage(networkId: Long): Option[NetworkNodesPage] = {
    queryNodes(networkId).map { data =>
      val nodes = nodesWithRouteReferences(data)
      NetworkNodesPage(
        timeInfo = TimeInfoBuilder.timeInfo,
        surveyDateInfo = SurveyDateInfoBuilder.dateInfo,
        data.summary,
        nodes
      )
    }
  }

  private def queryNodes(networkId: Long): Option[NetworkNodesPageData] = {
    val pipeline = Seq(
      filter(
        equal("_id", networkId)
      ),
      project(
        fields(
          excludeId(),
          include("summary"),
          include("nodes")
        )
      )
    )
    database.networks.optionAggregate(pipeline, classOf[NetworkNodesPageData], log)
  }

  private def nodesWithRouteReferences(data: NetworkNodesPageData): Seq[NetworkNodeRow] = {
    val scopedRouteType = ScopedRouteType.from(data.summary.routeType, data.summary.routeScope)
    val allRouteReferences = nodeRouteRepository.nodesRouteReferences(scopedRouteType, data.nodes.map(_.id))
    data.nodes.map { networkNodeDetail =>
      val routeReferences = allRouteReferences.filter(_.nodeId == networkNodeDetail.id).flatMap(_.routeRefs)
      NetworkNodeRow(
        networkNodeDetail,
        routeReferences
      )
    }
  }
}
