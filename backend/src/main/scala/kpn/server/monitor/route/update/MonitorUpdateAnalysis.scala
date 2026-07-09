package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorReferenceType
import kpn.core.common.Time
import kpn.core.doc.RouteDoc
import kpn.core.util.CoordinateUtil
import kpn.core.util.Log
import kpn.core.util.Util.mergeBounds
import kpn.server.analyzer.engine.monitor.analysis.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.state.MonitorStateStore
import kpn.server.analyzer.engine.tiles.domain.CoordinateCodec
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorStateId
import kpn.server.repository.RouteRepository
import org.bson.types.ObjectId
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class MonitorUpdateAnalysis(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer,
  monitorStateStore: MonitorStateStore
) {

  private val log = Log(classOf[MonitorUpdateAnalysis])
  private val geometryFactory = new GeometryFactory

  def updateAnalysis(route: MonitorRoute): Unit = {
    route.relationId match {
      case None => resetRoute(route)
      case Some(relationId) =>
        routeRepository.findRouteById(relationId) match {
          case None => resetRoute(route)
          case Some(routeDoc) => analyze(route, routeDoc)
        }
    }
  }

  private def resetRoute(route: MonitorRoute): Unit = {
    val updatedRoute = route.copy(
      symbol = None,
      analysisTimestamp = None,
      analysisDuration = None,
      deviationDistance = 0,
      deviationCount = 0,
      osmSegmentCount = 0,
      osmDistance = 0,
      relationIds = Seq.empty,
      bounds = None,
      happy = false,
    )
    monitorRouteRepository.saveRoute(updatedRoute)
    monitorRouteRepository.deleteStates(updatedRoute._id)
  }

  private def analyze(route: MonitorRoute, routeDoc: RouteDoc): Unit = {
    val analysisStartMillis = System.currentTimeMillis()
    val references = monitorRouteRepository.references(route._id)
    val oldStateIds = monitorRouteRepository.stateIds(route._id)

    removeObsoleteStates(routeDoc.routeIds, oldStateIds)

    val stateSummaries = analyzeReferences(route, routeDoc, references, oldStateIds)

    saveRoute(
      route,
      routeDoc,
      analysisStartMillis,
      references,
      stateSummaries
    )
  }

  private def removeObsoleteStates(allRelationIds: Seq[Long], oldStateIds: Seq[MonitorStateId]): Unit = {
    val obsoleteStateIds = oldStateIds.filterNot(id => allRelationIds.contains(id.relationId))
    obsoleteStateIds.map(_._id).foreach(monitorRouteRepository.deleteStateById)
  }

  private def analyzeReferences(route: MonitorRoute, routeDoc: RouteDoc, references: Seq[MonitorReference], oldStateIds: Seq[MonitorStateId]): Seq[MonitorStateSummary] = {
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

  private def analyzeRouteReferences(route: MonitorRoute, routeDoc: RouteDoc, references: Seq[MonitorReference], oldStateIds: Seq[MonitorStateId]): Seq[MonitorStateSummary] = {
    val xx = references.flatMap { reference =>
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

    val referenceRelationIds = references.flatMap(_.relationId).toSet
    val routeRelationIds = routeDoc.routeIds.toSet
    val relationsWithoutReferenceIds = (routeRelationIds -- referenceRelationIds).toSeq.sorted

    val yy = relationsWithoutReferenceIds.map { relationId =>
      val segmentCoordinates = routeRepository.segmentCoordinates(Seq(relationId))
      val id = oldStateIds.find(_.relationId == relationId) match {
        case Some(oldStateId) => oldStateId._id
        case None => ObjectId.get()
      }
      val state = MonitorState(
        id,
        route._id,
        relationId,
        Time.now,
        Seq.empty,
        0,
        Seq.empty,
        segmentCoordinates
      )

      monitorStateStore.saveState(state)

      MonitorStateSummary(
        relationId = relationId,
        deviationCount = 0,
        deviationDistance = 0,
        matchesDistance = 0
      )
    }

    xx ++ yy
  }

  private def analyzeGpxReference(route: MonitorRoute, routeDoc: RouteDoc, references: Seq[MonitorReference], oldStateIds: Seq[MonitorStateId]): Seq[MonitorStateSummary] = {
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

  private def saveRoute(
    route: MonitorRoute,
    routeDoc: RouteDoc,
    analysisStartMillis: Long,
    references: Seq[MonitorReference],
    stateSummaries: Seq[MonitorStateSummary]
  ): Unit = {

    val analysisDuration = System.currentTimeMillis() - analysisStartMillis
    val deviationDistance = stateSummaries.map(_.deviationDistance).sum
    val deviationCount = stateSummaries.map(_.deviationCount).sum
    val osmSegmentCount = routeDoc.superSegments.size
    val osmDistance = routeDoc.superDistance

    val bounds = {
      val allBounds = references.map(_.referenceBounds) ++ routeDoc.bounds.toSeq
      if (allBounds.nonEmpty) {
        Some(mergeBounds(allBounds))
      }
      else {
        None
      }
    }

    val happy = deviationCount == 0 && osmDistance > 0 && osmSegmentCount == 1

    val updatedRoute = route.copy(
      analysisTimestamp = Some(Time.now),
      analysisDuration = Some(analysisDuration),
      deviationDistance = deviationDistance,
      deviationCount = deviationCount,
      osmSegmentCount = osmSegmentCount,
      osmDistance = osmDistance,
      relationIds = routeDoc.routeIds,
      bounds = bounds,
      happy = happy,
    )

    monitorRouteRepository.saveRoute(updatedRoute)
  }

  private def compareReferenceAndRelation(
    route: MonitorRoute,
    routeDoc: RouteDoc,
    reference: MonitorReference,
    relationId: Long,
    relationIds: Seq[Long],
    oldStateIds: Seq[MonitorStateId]
  ): MonitorStateSummary = {

    val segmentCoordinates = routeRepository.segmentCoordinates(relationIds)

    val routeLines = segmentCoordinates.map { segment =>
      val coordinates = CoordinateCodec.decode(segment.coordinates)
      geometryFactory.createLineString(coordinates)
    }
    val referenceLines = reference.referenceLines.map(CoordinateUtil.coordinatesToLineString)

    val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(routeLines, referenceLines)

    val id = oldStateIds.find(_.relationId == relationId) match {
      case Some(oldStateId) => oldStateId._id
      case None => ObjectId.get()
    }

    val state = MonitorState(
      id,
      route._id,
      relationId,
      Time.now,
      deviationAnalysis.deviations,
      deviationAnalysis.matchesDistance,
      deviationAnalysis.matchesLines,
      segmentCoordinates
    )

    monitorStateStore.saveState(state)

    MonitorStateSummary(
      relationId = relationId,
      deviationCount = deviationAnalysis.deviations.length,
      deviationDistance = deviationAnalysis.deviations.map(_.meters).sum,
      matchesDistance = deviationAnalysis.matchesDistance
    )
  }
}
