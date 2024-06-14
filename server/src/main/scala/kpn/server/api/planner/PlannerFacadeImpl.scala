package kpn.server.api.planner

import kpn.api.common.PoiPage
import kpn.api.common.node.MapNodeDetail
import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.LegEnd
import kpn.api.common.planner.PlanLegDetail
import kpn.api.common.route.MapRouteDetail
import kpn.api.common.tiles.ClientPoiConfiguration
import kpn.api.custom.ApiResponse
import kpn.api.custom.NetworkType
import kpn.core.common.TimestampLocal
import kpn.core.poi.PoiConfiguration
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.api.Api
import kpn.server.api.analysis.pages.PoiPageBuilder
import kpn.server.api.analysis.pages.node.MapNodeDetailBuilder
import kpn.server.api.analysis.pages.route.MapRouteDetailBuilder
import kpn.server.api.planner.leg.LegBuilder
import kpn.server.repository.AnalysisRepository
import org.springframework.stereotype.Component

@Component
class PlannerFacadeImpl(
  api: Api,
  analysisRepository: AnalysisRepository,
  poiPageBuilder: PoiPageBuilder,
  legBuilder: LegBuilder,
  mapNodeDetailBuilder: MapNodeDetailBuilder,
  mapRouteDetailBuilder: MapRouteDetailBuilder
) extends PlannerFacade {

  override def mapNodeDetail(networkType: NetworkType, nodeId: Long): ApiResponse[MapNodeDetail] = {
    val args = s"${networkType.name}, $nodeId"
    execute("map-node-detail", args) {
      mapNodeDetailBuilder.build(networkType, nodeId)
    }
  }

  override def mapRouteDetail(routeId: Long): ApiResponse[MapRouteDetail] = {
    execute("map-route-detail", routeId.toString) {
      mapRouteDetailBuilder.build(routeId)
    }
  }

  override def poiConfiguration(): ApiResponse[ClientPoiConfiguration] = {
    api.execute("poiConfiguration", "") {
      ApiResponse(None, 1, Some(PoiConfiguration.instance.toClient))
    }
  }

  override def poi(poiRef: PoiRef): ApiResponse[PoiPage] = {
    api.execute("poi", s"${poiRef.elementType}, ${poiRef.elementId}") {
      val poiPage = poiPageBuilder.build(poiRef)
      ApiResponse(None, 1, poiPage) // analysis timestamp not needed here
    }
  }

  override def leg(params: LegBuildParams): ApiResponse[PlanLegDetail] = {
    val args = s"${legEndString(params.source)} to ${legEndString(params.sink)}, proposed=${params.proposed}"
    api.execute("leg", args) {
      val leg = legBuilder.leg(params)
      ApiResponse(None, 1, leg)
    }
  }

  override def plan(
    networkType: NetworkType,
    planString: String,
    proposed: Boolean
  ): ApiResponse[Seq[PlanLegDetail]] = {
    val args = s"${networkType.name}: $planString"
    api.execute("plan", args) {
      val legs = legBuilder.plan(networkType, planString, proposed = proposed)
      ApiResponse(None, 1, legs)
    }
  }

  private def legEndString(legEnd: LegEnd): String = {
    legEnd.node match {
      case Some(node) => s"node(${node.nodeId})"
      case None =>
        legEnd.route match {
          case Some(route) => s"route(${route.trackPathKeys.map(_.key).mkString("|")})"
          case None => ""
        }
    }
  }

  // TODO share with AnalysisFacadeImpl ?
  private def execute[T](action: String, args: String)(result: Option[T]): ApiResponse[T] = {
    api.execute(action, args) {
      reply(result)
    }
  }

  // TODO share with AnalysisFacadeImpl ?
  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(analysisRepository.lastUpdated(), 1, result)
    TimestampLocal.localize(response)
    response
  }
}
