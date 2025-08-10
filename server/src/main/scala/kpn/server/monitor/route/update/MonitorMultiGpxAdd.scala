package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorCommand
import kpn.api.common.monitor.MonitorMessage
import kpn.core.common.Time
import kpn.core.doc.RouteDoc
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class MonitorMultiGpxAdd(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
) {

  private val log = Log(classOf[MonitorMultiGpxAdd])

  def execute(group: MonitorGroup, args: MonitorUpdateArgs): Unit = {

    // TODO redesign - replace with query that hust picks up what is needed
    //   superSegmentCount
    //   superDistance
    //   bounds
    //   relationIds
    val routeDoc = routeRepository.findRouteById(args.relationId)

    val route = buildRoute(args, group, routeDoc)

    monitorRouteRepository.saveRoute(route)
    args.reporter.stepDone("save")
  }

  def initialMessage: MonitorMessage = {
    MonitorMessage(
      MonitorCommand.add("save"),
      MonitorCommand.active("save"),
    )
  }

  private def buildRoute(
    args: MonitorUpdateArgs,
    group: MonitorGroup,
    routeDoc: Option[RouteDoc],
  ) = {

    val superSegmentCount = routeDoc.map(_.superSegments.length.toLong).getOrElse(0L)
    val superDistance = routeDoc.map(_.superDistance).getOrElse(0L)
    val bounds = routeDoc.flatMap(_.bounds)
    val relationIds = routeDoc.toSeq.flatMap(_.routeIds)

    MonitorRoute(
      ObjectId.get(),
      group._id,
      args.update.routeName,
      args.update.description.getOrElse(""),
      args.update.comment,
      args.update.relationId,
      args.user,
      Time.now,
      None,
      analysisTimestamp = Some(Time.now),
      analysisDuration = None,
      referenceType = args.update.referenceType,
      referenceTimestamp = args.update.referenceTimestamp,
      referenceFilename = args.update.referenceFilename,
      referenceDistance = 0,
      deviationDistance = 0,
      deviationCount = 0,
      osmSegmentCount = superSegmentCount,
      osmDistance = superDistance,
      relationIds = relationIds,
      bounds = bounds,
      happy = false, // cannot be happy yet, there are no gpx references yet
    )
  }
}
