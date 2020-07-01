package kpn.server.api.planner

import kpn.api.common.PoiPage
import kpn.api.common.node.MapNodeDetail
import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.LegEnd
import kpn.api.common.planner.RouteLeg
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
import kpn.server.api.planner.leg.PlanUtil
import kpn.server.repository.AnalysisRepository
import org.geotools.geometry.jts.JTS
import org.geotools.referencing.CRS
import org.locationtech.jts.geom.Coordinate
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

  override def mapNodeDetail(user: Option[String], networkType: NetworkType, nodeId: Long): ApiResponse[MapNodeDetail] = {
    val args = s"${networkType.name}, $nodeId"
    execute(user, "map-node-detail", args) {
      mapNodeDetailBuilder.build(user, networkType, nodeId)
    }
  }

  override def mapRouteDetail(user: Option[String], routeId: Long): ApiResponse[MapRouteDetail] = {
    execute(user, "map-route-detail", routeId.toString) {
      mapRouteDetailBuilder.build(user, routeId)
    }
  }

  override def poiConfiguration(user: Option[String]): ApiResponse[ClientPoiConfiguration] = {
    api.execute(user, "poiConfiguration", "") {
      ApiResponse(None, 1, Some(PoiConfiguration.instance.toClient))
    }
  }

  override def poi(user: Option[String], poiRef: PoiRef): ApiResponse[PoiPage] = {
    api.execute(user, "poi", s"${poiRef.elementType}, ${poiRef.elementId}") {
      val poiPage = poiPageBuilder.build(poiRef)
      ApiResponse(None, 1, poiPage) // analysis timestamp not needed here
    }
  }

  override def leg(user: Option[String], params: LegBuildParams): ApiResponse[RouteLeg] = {
    val args = s"${params.legId}: ${legEndString(params.source)} to ${legEndString(params.sink)}"
    api.execute(user, "leg", args) {
      val leg = legBuilder.build(params)
      ApiResponse(None, 1, leg)
    }
  }

  override def plan(user: Option[String], networkType: NetworkType, planString: String): ApiResponse[Seq[RouteLeg]] = {
    val args = s"${networkType.name}: ${planString}"
    api.execute(user, "plan", args) {
      val legs = legBuilder.load(networkType, planString)


      val sinks = legs match {
        case None => Seq()
        case Some(routeLegs) => routeLegs.flatMap(routeLeg => routeLeg.routes.map(rlr => rlr.sink))
      }
      sinks.foreach { sink =>
        val coordinate = PlanUtil.toCoordinate(sink.lat.toDouble, sink.lon.toDouble)

        println("node=" + sink.nodeName + ", coordinate " + coordinate[0] + ", " + coordinate[1] + ", lat=" + sink.lat + ", lon=" + sink.lon)
      }

      ApiResponse(None, 1, legs)
    }
  }

  private def legEndString(legEnd: LegEnd): String = {
    legEnd.node match {
      case Some(node) => s"node(${node.nodeId})"
      case None =>
        legEnd.route match {
          case Some(route) => s"route(${route.routeId}-${route.pathId})"
          case None => ""
        }
    }
  }

  // TODO share with AnalysisFacadeImpl ?
  private def execute[T](user: Option[String], action: String, args: String)(result: Option[T]): ApiResponse[T] = {
    api.execute(user, action, args) {
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
