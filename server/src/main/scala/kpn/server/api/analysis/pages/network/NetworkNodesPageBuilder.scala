package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkNodeRow
import kpn.api.common.network.NetworkNodesPage
import kpn.api.custom.ScopedRouteType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.NodeRouteRepository
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
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
          include("summary"),
          include("nodes")
        )
      )
    )
    database.networkInfos.optionAggregate[NetworkNodesPageData](pipeline, log)
  }

  private def nodesWithRouteReferences(data: NetworkNodesPageData): Seq[NetworkNodeRow] = {
    val scopedRouteType = ScopedRouteType.from(
      data.summary.networkScope,
      data.summary.routeType
    )
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
