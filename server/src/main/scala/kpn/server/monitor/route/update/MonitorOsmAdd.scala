package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorCommand
import kpn.api.common.monitor.MonitorMessage
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.custom.Timestamp
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorRouteRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class MonitorOsmAdd(
  monitorRouteRepository: MonitorRouteRepository,
  monitorOsmAnalyze: MonitorOsmAnalyze
) {

  def execute(group: MonitorGroup, args: MonitorUpdateArgs, now: Timestamp, analysisStartMillis: Long): Unit = {

    val route = buildRoute(group, args, now)

    args.reporter.stepActive("analyze-route-structure")

    if (args.update.relationId.isEmpty) {
      saveRouteWithoutRelationId(args, route)
    }
    else {
      monitorOsmAnalyze.execute(route, now, args, args.referenceTimestamp, analysisStartMillis)
    }
  }

  def initialMessage: MonitorMessage = {
    MonitorMessage(
      MonitorCommand.add("prepare"),
      MonitorCommand.add("analyze-route-structure"),
      MonitorCommand.active("prepare"),
    )
  }

  private def buildRoute(group: MonitorGroup, args: MonitorUpdateArgs, now: Timestamp): MonitorRoute = {
    val monitorRouteId = ObjectId.get()
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
      analysisTimestamp = None,
      analysisDuration = None,
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(args.referenceTimestamp),
      referenceFilename = None,
      referenceDistance = 0,
      deviationDistance = 0,
      deviationCount = 0,
      osmSegmentCount = 0,
      osmDistance = 0,
      relationIds = Seq.empty,
      bounds = None,
      happy = false,
    )
  }

  private def saveRouteWithoutRelationId(args: MonitorUpdateArgs, route: MonitorRoute): Unit = {
    args.reporter.stepActive("save")
    monitorRouteRepository.saveRoute(route)
    args.reporter.stepDone("save")
  }
}
