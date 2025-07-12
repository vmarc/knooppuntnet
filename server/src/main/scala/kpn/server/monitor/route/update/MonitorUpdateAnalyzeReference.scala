package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.custom.Relation
import kpn.core.common.Time
import kpn.core.doc.RouteRelation
import kpn.core.util.CoordinateUtil
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import kpn.server.repository.RouteRepository
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.springframework.stereotype.Component

@Component
class MonitorUpdateAnalyzeReference(
  routeRepository: RouteRepository,
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer,
) {

  private val log = Log(classOf[MonitorUpdateAnalyzeReference])
  private val geometryFactory = new GeometryFactory

  def analyzeReference(context: MonitorContext, reference: MonitorRouteReference, currentRelation: Option[Relation]): Option[MonitorRouteState] = {
    reference.relationId.flatMap { relationId =>
      compareReferenceAndRelation(context, reference, currentRelation, relationId)
    }
  }

  private def compareReferenceAndRelation(context: MonitorContext, reference: MonitorRouteReference, currentRelation: Option[Relation], relationId: Long): Option[MonitorRouteState] = {

    val routeLines = routeLinesFromBaseRouteDocs(context, relationId)
    val referenceLines = reference.referenceLines.map(CoordinateUtil.coordinatesToLineString)

    val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(routeLines, referenceLines)

    val id = context.value.oldStateIds.find(_.relationId == relationId) match {
      case Some(oldStateId) => oldStateId._id
      case None => ObjectId()
    }

    Some(
      MonitorRouteState(
        id,
        context.value.routeId,
        relationId,
        Time.now,
        deviationAnalysis.deviations,
        deviationAnalysis.matchesDistance,
        deviationAnalysis.matchesLines,
      )
    )
  }

  private def routeLinesFromBaseRouteDocs(context: MonitorContext, relationId: Long): Seq[LineString] = {
    val relationIds = if (context.value.isReferenceTypeGpx) {
      routeRepository.subRelationTree(relationId) match {
        case Some(routeRelation) => Seq(relationId) ++ RouteRelation.relationIds(routeRelation)
        case None => Seq(relationId)
      }
    }
    else {
      Seq(relationId)
    }

    val routeCoordinateArrays = routeRepository.coordinatesArrays(relationIds)
    routeCoordinateArrays.map(geometryFactory.createLineString)
  }
}
