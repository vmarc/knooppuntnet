package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorCommand
import kpn.api.common.monitor.MonitorMessage
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.custom.Timestamp
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import org.springframework.stereotype.Component

@Component
class MonitorGpxAdd(
  monitorGpxAnalyze: MonitorGpxAnalyze,
) {

  def execute(group: MonitorGroup, args: MonitorUpdateArgs, now: Timestamp): Unit = {
    val route = buildRoute(group, args, now)
    monitorGpxAnalyze.execute(args, route, now)
  }

  def initialMessage: MonitorMessage = {
    MonitorMessage(
      MonitorCommand.add("prepare"),
      MonitorCommand.add("analyze-route-structure"),
      MonitorCommand.active("prepare"),
    )
  }

  private def buildRoute(group: MonitorGroup, args: MonitorUpdateArgs, now: Timestamp): MonitorRoute = {
    MonitorRoute(
      _id = ObjectId(),
      groupId = group._id,
      name = args.update.routeName,
      description = args.update.description.getOrElse(""),
      comment = args.update.comment,
      relationId = args.update.relationId,
      user = args.user,
      timestamp = now,
      symbol = None,
      analysisTimestamp = None,
      analysisDuration = None,
      referenceType = MonitorReferenceType.gpx,
      referenceTimestamp = Some(args.referenceTimestamp),
      referenceFilename = args.update.referenceFilename,
      referenceDistance = 0,
      deviationDistance = 0,
      deviationCount = 0,
      osmSegmentCount = 0,
      osmDistance = 0,
      happy = false,
    )
  }
}
