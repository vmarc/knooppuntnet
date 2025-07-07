package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteSubRelation
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteReferenceUtil
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.json.Json
import kpn.server.monitor.MonitorUtil
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.locationtech.jts.io.geojson.GeoJsonWriter
import org.springframework.stereotype.Component

@Component
class MonitorAddOsm(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorRouteStructureLoader: MonitorRouteStructureLoader,
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer,
) {

  private val log = Log(classOf[MonitorAddOsm])
  val geometryFactory = new GeometryFactory

  def execute(args: MonitorUpdateArgs): Unit = {

    val now = Time.now
    val analysisStartMillis = System.currentTimeMillis()

    initReporter(args)

    val group = monitorUpdateCommon.findGroup(args)
    monitorUpdateCommon.verifyNewRoute(group, args)

    val monitorRouteId = ObjectId()

    args.reporter.stepActive("analyze-route-structure")

    val monitorRouteRelation = monitorRouteStructureLoader.load(Some(args.referenceTimestamp), args.relationId).getOrElse(throw new RuntimeException("could not load route structure"))
    val relations = MonitorUtil.subRelationsInRouteRelation(monitorRouteRelation)

    val routeDoc = routeRepository.findRouteById(args.relationId).getOrElse(throw new RuntimeException(s"Could not find RouteDoc with id ${args.relationId}"))

    updateReporterSteps(args, relations)

    val summaries = processRelations(args, now, monitorRouteId, relations)
    // TODO redesign - do not forget to add analysis results for relationsIds in RouteDoc that are not in included in the overpass query result

    val referenceDistance = summaries.map(_.referenceDistance).sum
    val deviationDistance = summaries.map(_.deviationDistance).sum
    val deviationCount = summaries.map(_.deviationCount).sum

    val osmDistance = routeDoc.superDistance

    val analysisDuration = System.currentTimeMillis() - analysisStartMillis

    val route = buildRoute(args, now, group, monitorRouteId, routeDoc, osmDistance, analysisDuration)

    args.reporter.stepActive("save")
    monitorRouteRepository.saveRoute(route)
    args.reporter.stepDone("save")
  }

  private def processRelations(args: MonitorUpdateArgs, now: Timestamp, monitorRouteId: ObjectId, relations: Seq[MonitorRouteSubRelation]): Seq[MonitorRouteDeviationAnalysisSummary] = {
    relations.flatMap { relation =>
      updateReporterActiveStep(args, relation.relationId)
      processRelation(args, now, monitorRouteId, relation)
    }
  }

  private def processRelation(args: MonitorUpdateArgs, now: Timestamp, monitorRouteId: ObjectId, relation: MonitorRouteSubRelation): Option[MonitorRouteDeviationAnalysisSummary] = {
    readReference(args, now, monitorRouteId, relation) match {
      case None => None
      case Some(reference) =>

        val routeCoordinateArrays = routeRepository.coordinatesArrays(Seq(relation.relationId))
        if (routeCoordinateArrays.isEmpty) {
          // the relation does not exist anymore or does not contain ways, the entire reference becomes a deviation
          val deviation = MonitorRouteDeviation(
            id = 1,
            meters = reference.referenceDistance,
            distance = reference.referenceDistance,
            bounds = reference.referenceBounds,
            reference.referenceGeoJson

          )
          val state = MonitorRouteState(
            _id = ObjectId(),
            routeId = monitorRouteId,
            relationId = relation.relationId,
            timestamp = now,
            matchesDistance = 0,
            matchesGeometry = None,
            deviations = Seq(deviation),
          )
          monitorRouteRepository.saveRouteState(state)
          Some(
            MonitorRouteDeviationAnalysisSummary(
              relation.relationId,
              referenceDistance = reference.referenceDistance,
              deviationDistance = deviation.distance,
              deviationCount = 1,
            )
          )
        }
        else {
          val routeLines = routeCoordinateArrays.map { coordinateArray =>
            val flipped = coordinateArray.map(c => new Coordinate(c.y, c.x))
            geometryFactory.createLineString(flipped)
          }

          val referenceLines = {
            val referenceGeometry = new GeoJsonReader().read(reference.referenceGeoJson)
            MonitorRouteReferenceUtil.toLineStrings(referenceGeometry)
          }

          val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(routeLines, referenceLines)

          val state = MonitorRouteState(
            ObjectId(),
            monitorRouteId,
            relation.relationId,
            now,
            deviationAnalysis.matchesDistance,
            deviationAnalysis.matchesGeometry,
            deviationAnalysis.deviations,
          )
          monitorRouteRepository.saveRouteState(state)

          Some(
            MonitorRouteDeviationAnalysisSummary(
              relation.relationId,
              referenceDistance = reference.referenceDistance,
              deviationDistance = deviationAnalysis.deviations.map(_.distance).sum,
              deviationCount = deviationAnalysis.deviations.length,
            )
          )
        }
    }
  }

  private def readReference(args: MonitorUpdateArgs, now: Timestamp, monitorRouteId: ObjectId, relation: MonitorRouteSubRelation): Option[MonitorRouteReference] = {

    log.info(s"${relation.name}")
    monitorRouteRelationRepository.loadTopLevel(Some(args.referenceTimestamp), relation.relationId) match {
      case None =>
        val error = s"Could not load relation ${relation.relationId} at ${args.referenceTimestamp.yyyymmddhhmmss}"
        args.reporter.report( // TODO redesign - should do try/catch at higher level??
          MonitorRouteUpdateStatusMessage(
            errors = Some(Seq(error))
          )
        )
        None

      case Some(subRelation) =>
        val wayMembers = MonitorFilter.filterWayMembers(subRelation.wayMembers)
        if (wayMembers.nonEmpty) {
          val bounds = Bounds.from(wayMembers.flatMap(_.way.nodes))
          val analysis = monitorRouteOsmSegmentAnalyzer.analyze(wayMembers)

          val geomFactory = new GeometryFactory
          val geometryCollection = new GeometryCollection(analysis.routeSegments.flatMap(_.lineStrings).toArray, geomFactory)
          val geoJsonWriter = new GeoJsonWriter()
          geoJsonWriter.setEncodeCRS(false)
          val geometry = geoJsonWriter.write(geometryCollection)

          val ref = MonitorRouteReference(
            ObjectId(),
            monitorRouteId,
            Some(subRelation.id),
            Time.now,
            args.user,
            bounds,
            MonitorReferenceType.osm,
            args.referenceTimestamp,
            analysis.osmDistance,
            analysis.routeSegments.size,
            None,
            geometry
          )

          monitorRouteRepository.saveRouteReference(ref)
          Some(ref)
        }
        else {
          None
        }
    }
  }

  private def buildGeoJson(baseRouteDoc: BaseRouteDoc) = {
    val lineStrings = baseRouteDoc.segmentElements.map { segmentElement =>
      val coordinates = Json.value(segmentElement.coordinates, classOf[CoordinateArray]).coordinates
      val flipped = coordinates.map(c => new Coordinate(c.y, c.x))
      geometryFactory.createLineString(flipped)
    }
    val geometryCollection = new GeometryCollection(lineStrings.toArray, geometryFactory)
    val geoJsonWriter = new GeoJsonWriter()
    geoJsonWriter.setEncodeCRS(false)
    val geoJson = geoJsonWriter.write(geometryCollection)
    geoJson
  }

  private def updateReporterActiveStep(args: MonitorUpdateArgs, routeId: Long): Unit = {
    args.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand(
            "step-active",
            routeId.toString
          )
        )
      )
    )
  }

  private def updateReporterSteps(args: MonitorUpdateArgs, relations: Seq[MonitorRouteSubRelation]): Unit = {
    val subRelationSteps = relations.zipWithIndex.map { case (relation, index) =>
      val desciption = s"${index + 1}/${relations.length} ${relation.name}"
      MonitorRouteUpdateStatusCommand(
        "step-add",
        relation.relationId.toString,
        Some(desciption)
      )
    }

    val saveStep = MonitorRouteUpdateStatusCommand(
      "step-add",
      "save"
    )

    args.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = subRelationSteps :+ saveStep
      )
    )
  }

  private def buildRoute(
    args: MonitorUpdateArgs,
    now: Timestamp,
    group: MonitorGroup,
    monitorRouteId: ObjectId,
    routeDoc: RouteDoc,
    distance: Long,
    analysisDuration: Long
  ): MonitorRoute = {

    MonitorRoute(
      _id = monitorRouteId,
      groupId = group._id,
      name = args.update.routeName,
      description = args.update.description.getOrElse(""),
      comment = args.update.comment,
      relationId = args.update.relationId,
      user = args.user,
      timestamp = now,
      symbol = None,
      analysisTimestamp = Some(now),
      analysisDuration = Some(analysisDuration),
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(args.referenceTimestamp),
      referenceFilename = None,
      referenceDistance = distance,
      deviationDistance = 0,
      deviationCount = 0,
      osmSegmentCount = routeDoc.superSegments.size,
      osmDistance = distance,
      relation = None,
      happy = true, // always true because reference will automatically match current state
    )
  }

  private def initReporter(args: MonitorUpdateArgs): Unit = {
    args.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-add", "prepare"),
          MonitorRouteUpdateStatusCommand("step-add", "analyze-route-structure"),
          MonitorRouteUpdateStatusCommand("step-active", "prepare"),
        )
      )
    )
  }
}
