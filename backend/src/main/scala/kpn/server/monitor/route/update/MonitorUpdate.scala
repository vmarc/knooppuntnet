package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorCommand
import kpn.api.common.monitor.MonitorMessage
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.time.Time
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.state.MonitorStateStore
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class MonitorUpdate(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorOsmUpdate: MonitorOsmUpdate,
  monitorOsmNowUpdate: MonitorOsmNowUpdate,
  monitorGpxUpdate: MonitorGpxUpdate,
  monitorMultiGpxUpdate: MonitorMultiGpxUpdate,
  monitorStateStore: MonitorStateStore,
) {

  private val log = Log(classOf[MonitorUpdate])

  def execute(args: MonitorUpdateArgs): Unit = {

    initReporter(args)

    val now = Time.now
    val analysisStartMillis = System.currentTimeMillis()

    val group = monitorUpdateCommon.findGroup(args)
    val route = monitorUpdateCommon.findRoute(group, args)

    if (!monitorUpdateCommon.isRouteChanged(route, args)) {
      args.reporter.report(
        MonitorMessage(
          MonitorCommand.done("prepare"),
        )
      )
      return
    }

    val routeUpdatedProperties = updateRouteProperties(group, route, args)
    val updatedRoute = if (isCleanupNeeded(route, args)) {
      cleanup(routeUpdatedProperties, args)
    }
    else {
      routeUpdatedProperties
    }

    if (!isAnalysisNeeded(route, args)) {
      args.reporter.report(
        MonitorMessage(
          MonitorCommand.add("save"),
          MonitorCommand.active("save"),
        )
      )
      monitorRouteRepository.saveRoute(updatedRoute)
      stepSaveDone(args)
      return
    }

    args.update.referenceType match {
      case MonitorReferenceType.osmNow => monitorOsmNowUpdate.execute(group, args, route, updatedRoute, now, analysisStartMillis)
      case MonitorReferenceType.osm => monitorOsmUpdate.execute(args, route, updatedRoute, now, analysisStartMillis)
      case MonitorReferenceType.gpx => monitorGpxUpdate.execute(args, route, updatedRoute, now)
      case MonitorReferenceType.multiGpx => monitorMultiGpxUpdate.execute(args, route, updatedRoute, now)
    }
  }

  private def updateRoute(args: MonitorUpdateArgs, group: MonitorGroup, route: MonitorRoute) = {
    val groupId = updateGroupIdIfNeeded(args, group)
    route.copy(
      groupId = groupId,
      name = args.update.newRouteName.getOrElse(route.name),
      description = args.update.description.getOrElse(""),
      comment = args.update.comment,
      relationId = args.update.relationId,
      user = args.user,
      timestamp = Time.now,
      referenceType = args.update.referenceType,
      referenceTimestamp = args.update.referenceTimestamp,
      referenceFilename = args.update.referenceFilename,
    )
  }

  private def updateGroupIdIfNeeded(args: MonitorUpdateArgs, group: MonitorGroup): ObjectId = {
    args.update.newGroupName match {
      case None => group._id
      case Some(newGroupName) =>
        monitorGroupRepository.groupByName(newGroupName).map(_._id) match {
          case Some(id) => id
          case None =>
            throw new IllegalArgumentException(
              s"""Could not find group with name "$newGroupName""""
            )
        }
    }
  }

  private def initReporter(args: MonitorUpdateArgs): Unit = {
    val message = args.update.referenceType match {
      case MonitorReferenceType.osmNow => monitorOsmNowUpdate.initialMessage
      case MonitorReferenceType.osm => monitorOsmUpdate.initialMessage
      case MonitorReferenceType.gpx => monitorGpxUpdate.initialMessage
      case MonitorReferenceType.multiGpx => monitorMultiGpxUpdate.initialMessage
    }
    args.reporter.report(message)
  }

  private def stepSaveDone(args: MonitorUpdateArgs): Unit = {
    args.reporter.report(
      MonitorMessage(
        MonitorCommand.done("save"),
      )
    )
  }

  private def cleanup(route: MonitorRoute, args: MonitorUpdateArgs): MonitorRoute = {
    monitorRouteRepository.deleteReferences(route._id)
    monitorStateStore.deleteStates(route._id)
    val symbol = args.update.relationId.flatMap(relationId => route.symbol)
    val osmSegmentCount = args.update.relationId.map(relationId => route.osmSegmentCount).getOrElse(0L)
    val osmDistance = args.update.relationId.map(relationId => route.osmDistance).getOrElse(0L)
    route.copy(
      analysisTimestamp = None,
      symbol = symbol,
      referenceDistance = 0,
      deviationDistance = 0,
      deviationCount = 0,
      osmSegmentCount = osmSegmentCount,
      osmDistance = osmDistance,
      happy = false,
    )
  }

  private def isCleanupNeeded(route: MonitorRoute, args: MonitorUpdateArgs): Boolean = {

    if (route.referenceType != args.update.referenceType) {
      return true
    }

    if (args.update.referenceType == MonitorReferenceType.osm) {
      if (route.referenceTimestamp != args.update.referenceTimestamp || route.relationId != args.update.relationId) {
        return true
      }
    }
    false
  }

  private def isAnalysisNeeded(route: MonitorRoute, args: MonitorUpdateArgs): Boolean = {

    if (args.update.referenceType == MonitorReferenceType.osm) {
      if (args.update.relationId.isEmpty) {
        return false
      }
      if (route.referenceTimestamp == args.update.referenceTimestamp && route.relationId == args.update.relationId) {
        return false
      }
    }

    true
  }

  private def updateRouteProperties(group: MonitorGroup, route: MonitorRoute, args: MonitorUpdateArgs): MonitorRoute = {
    val groupId = updateGroupIdIfNeeded(args, group)
    route.copy(
      groupId = groupId,
      name = args.update.newRouteName.getOrElse(route.name),
      description = args.update.description.getOrElse(""),
      comment = args.update.comment,
      relationId = args.update.relationId,
      user = args.user,
      timestamp = Time.now,
      referenceType = args.update.referenceType,
      referenceTimestamp = args.update.referenceTimestamp,
      referenceFilename = args.update.referenceFilename,
    )
  }
}
