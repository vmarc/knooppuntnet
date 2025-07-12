package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorReferenceType
import kpn.core.common.Time
import kpn.core.doc.RouteDoc
import kpn.core.util.CoordinateUtil
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorRouteStateId
import kpn.server.repository.RouteRepository
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.springframework.stereotype.Component

@Component
class MonitorUpdateAnalysis(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer,
) {

  private val log = Log(classOf[MonitorUpdateAnalysis])
  private val geometryFactory = new GeometryFactory

  def updateAnalysis(route: MonitorRoute): Unit = {
    route.relationId.flatMap(routeRepository.findRouteById).foreach(routeDoc => analyze(route, routeDoc))
  }

  private def analyze(route: MonitorRoute, routeDoc: RouteDoc): Unit = {
    val analysisStartMillis = System.currentTimeMillis()
    val references = monitorRouteRepository.routeReferences(route._id)
    val oldStateIds = monitorRouteRepository.routeStateIds(route._id)

    removeObsoleteStates(routeDoc.routeIds, oldStateIds)

    val stateSummaries = analyzeReferences(route, routeDoc, references, oldStateIds)

    saveRoute(route, routeDoc, analysisStartMillis, stateSummaries)
  }

  private def removeObsoleteStates(allRelationIds: Seq[Long], oldStateIds: Seq[MonitorRouteStateId]): Unit = {
    val obsoleteStateIds = oldStateIds.filterNot(id => allRelationIds.contains(id.relationId))
    obsoleteStateIds.map(_._id).foreach(monitorRouteRepository.deleteRouteStateById)
  }

  private def analyzeReferences(route: MonitorRoute, routeDoc: RouteDoc, references: Seq[MonitorRouteReference], oldStateIds: Seq[MonitorRouteStateId]) = {
    if (route.referenceType == MonitorReferenceType.osm || route.referenceType == MonitorReferenceType.multiGpx) {
      analyzeRouteReferences(route, routeDoc, references, oldStateIds)
    }
    else if (route.referenceType == MonitorReferenceType.gpx) {
      analyzeGpxReference(route, routeDoc, references, oldStateIds)
    }
    else {
      throw new IllegalArgumentException(s"unexpected reference type: '${route.referenceType.entryName}'")
    }
  }

  private def analyzeRouteReferences(route: MonitorRoute, routeDoc: RouteDoc, references: Seq[MonitorRouteReference], oldStateIds: Seq[MonitorRouteStateId]) = {
    references.flatMap { reference =>
      reference.relationId.map { relationId =>
        compareReferenceAndRelation(
          route,
          routeDoc,
          reference,
          relationId,
          Seq(relationId),
          oldStateIds
        )
      }
    }
  }

  private def analyzeGpxReference(route: MonitorRoute, routeDoc: RouteDoc, references: Seq[MonitorRouteReference], oldStateIds: Seq[MonitorRouteStateId]) = {
    if (references.sizeIs != 1) {
      throw new IllegalStateException(s"expected one 'gpx' reference, but found ${references.length}")
    }
    Seq(
      compareReferenceAndRelation(
        route,
        routeDoc,
        references.head,
        routeDoc._id,
        routeDoc.routeIds,
        oldStateIds
      )
    )
  }

  private def saveRoute(route: MonitorRoute, routeDoc: RouteDoc, analysisStartMillis: Long, stateSummaries: Seq[MonitorRouteStateSummary]): Unit = {
    val analysisDuration = System.currentTimeMillis() - analysisStartMillis
    val deviationDistance = stateSummaries.map(_.deviationDistance).sum
    val deviationCount = stateSummaries.map(_.deviationCount).sum
    val osmSegmentCount = routeDoc.superSegments.size
    val osmDistance = routeDoc.superDistance
    val happy = deviationCount == 0 && osmDistance > 0 && osmSegmentCount == 1

    val updatedRoute = route.copy(
      analysisTimestamp = Some(Time.now),
      analysisDuration = Some(analysisDuration),
      deviationDistance = deviationDistance,
      deviationCount = deviationCount,
      osmSegmentCount = osmSegmentCount,
      osmDistance = osmDistance,
      happy = happy,
    )

    monitorRouteRepository.saveRoute(updatedRoute)
  }

  private def compareReferenceAndRelation(
    route: MonitorRoute,
    routeDoc: RouteDoc,
    reference: MonitorRouteReference,
    relationId: Long,
    relationIds: Seq[Long],
    oldStateIds: Seq[MonitorRouteStateId]
  ): MonitorRouteStateSummary = {

    val routeLines = {
      val routeCoordinateArrays = routeRepository.coordinatesArrays(relationIds)
      routeCoordinateArrays.map(geometryFactory.createLineString)
    }
    val referenceLines = reference.referenceLines.map(CoordinateUtil.coordinatesToLineString)

    val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(routeLines, referenceLines)

    val id = oldStateIds.find(_.relationId == relationId) match {
      case Some(oldStateId) => oldStateId._id
      case None => ObjectId()
    }

    val state = MonitorRouteState(
      id,
      route._id,
      relationId,
      Time.now,
      deviationAnalysis.deviations,
      deviationAnalysis.matchesDistance,
      deviationAnalysis.matchesLines,
    )

    monitorRouteRepository.saveRouteState(state)

    MonitorRouteStateSummary(
      relationId = relationId,
      deviationCount = deviationAnalysis.deviations.length,
      deviationDistance = deviationAnalysis.deviations.map(_.meters).sum,
      matchesDistance = deviationAnalysis.matchesDistance
    )
  }

  private def routeLinesFromBaseRouteDocs(relationIds: Seq[Long], relationId: Long): Seq[LineString] = {
    val routeCoordinateArrays = routeRepository.coordinatesArrays(relationIds)
    routeCoordinateArrays.map(geometryFactory.createLineString)
  }
}
