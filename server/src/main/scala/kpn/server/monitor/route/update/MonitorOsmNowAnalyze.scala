package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorCommand
import kpn.api.common.monitor.MonitorMessage
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.custom.Timestamp
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class MonitorOsmNowAnalyze(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorReferenceBuilder: MonitorReferenceBuilder,
  monitorStateBuilder: MonitorStateBuilder,
) {

  private val log = Log(classOf[MonitorOsmNowAnalyze])
  val geometryFactory = new GeometryFactory

  def execute(group: MonitorGroup, args: MonitorUpdateArgs, now: Timestamp, monitorRouteId: ObjectId, analysisStartMillis: Long): Unit = {

    args.reporter.stepActive("analyze-route-structure")

    val routeDoc = routeRepository.findRouteById(args.relationId).getOrElse(throw new RuntimeException(s"Could not find RouteDoc with id ${args.relationId}"))

    updateReporterSteps(args, routeDoc)

    processRelations(args, now, monitorRouteId, routeDoc)

    val distance = routeDoc.superDistance

    val analysisDuration = System.currentTimeMillis() - analysisStartMillis

    val route = buildRoute(args, now, group, monitorRouteId, routeDoc, distance, analysisDuration)

    args.reporter.stepActive("save")
    monitorRouteRepository.saveRoute(route)
    args.reporter.stepDone("save")
  }

  private def processRelations(args: MonitorUpdateArgs, now: Timestamp, monitorRouteId: ObjectId, routeDoc: RouteDoc): Unit = {
    routeDoc.routeIds.foreach { routeId =>
      updateReporterActiveStep(args, routeId)
      processRelation(args, now, monitorRouteId, routeId)
    }
  }

  private def processRelation(args: MonitorUpdateArgs, now: Timestamp, monitorRouteId: ObjectId, routeId: Long): Unit = {
    val baseRouteDoc = routeRepository.findBaseRouteById(routeId).getOrElse(throw new RuntimeException(s"Could not find BaseRouteDoc with id $routeId"))
    if (baseRouteDoc.segmentElements.nonEmpty) {
      val distance = baseRouteDoc.segmentElements.map(_.meters).sum
      val bounds = baseRouteDoc.bounds.get
      val lines = buildLines(baseRouteDoc)
      buildReference(args.user, now, monitorRouteId, baseRouteDoc, lines, bounds, distance)
      buildState(now, monitorRouteId, baseRouteDoc, lines, distance)
    }
  }

  private def buildLines(baseRouteDoc: BaseRouteDoc): Seq[String] = {
    baseRouteDoc.segmentElements.map(_.coordinates)
  }

  private def updateReporterActiveStep(args: MonitorUpdateArgs, routeId: Long): Unit = {
    args.reporter.report(
      MonitorMessage(
        MonitorCommand.active(routeId.toString)
      )
    )
  }

  private def updateReporterSteps(args: MonitorUpdateArgs, routeDoc: RouteDoc): Unit = {
    val mainRelationInfo = (args.relationId.toString, routeDoc.summary.name)
    val subRelationInfos = routeDoc.structureRows.filter(_.relation.isDefined).map(row => (row.id.toString, row.name.getOrElse("")))
    val relationInfos = mainRelationInfo +: subRelationInfos

    val subRelationSteps = relationInfos.zipWithIndex.map { case ((relationId: String, description: String), index) =>
      val desc = s"${index + 1}/${relationInfos.length} $description"
      MonitorCommand.add(relationId, Some(desc))
    }

    val saveStep = MonitorCommand.add("save")

    args.reporter.report(
      MonitorMessage(subRelationSteps :+ saveStep)
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
      referenceTimestamp = Some(now),
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

  private def buildReference(
    user: String,
    now: Timestamp,
    monitorRouteId: ObjectId,
    baseRouteDoc: BaseRouteDoc,
    referenceLines: Seq[String],
    bounds: Bounds,
    distance: Long
  ): Unit = {

    val reference = monitorReferenceBuilder.build(
      MonitorReference(
        _id = ObjectId(),
        routeId = monitorRouteId,
        relationId = Some(baseRouteDoc._id),
        timestamp = now,
        user = user,
        referenceBounds = bounds,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = now,
        referenceDistance = distance,
        referenceSegmentCount = baseRouteDoc.segments.length,
        referenceFilename = None,
        referenceLines = referenceLines,
        tiles = Seq.empty,
      )
    )
    monitorRouteRepository.saveReference(reference)
  }

  private def buildState(
    now: Timestamp,
    monitorRouteId: ObjectId,
    baseRouteDoc: BaseRouteDoc,
    matchesLines: Seq[String],
    distance: Long
  ): Unit = {

    val state = monitorStateBuilder.build(
      MonitorState(
        _id = ObjectId(),
        routeId = monitorRouteId,
        relationId = baseRouteDoc._id,
        timestamp = now, // time of most recent analysis
        matchesDistance = distance,
        deviations = Seq.empty,
        matchesLines = matchesLines,
        tiles = Seq.empty
      )

    )
    monitorRouteRepository.saveState(state)
  }

  private def initReporter(args: MonitorUpdateArgs): Unit = {
    args.reporter.report(
      MonitorMessage(
        MonitorCommand.add("prepare"),
        MonitorCommand.add("analyze-route-structure"),
        MonitorCommand.active("prepare"),
      )
    )
  }
}
