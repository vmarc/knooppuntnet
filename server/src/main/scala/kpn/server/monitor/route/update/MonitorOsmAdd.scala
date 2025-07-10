package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorRouteRepository
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class MonitorOsmAdd(
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorOsmAnalyze: MonitorOsmAnalyze
) {

  private val log = Log(classOf[MonitorOsmAdd])
  val geometryFactory = new GeometryFactory

  def execute(args: MonitorUpdateArgs): Unit = {

    val now = Time.now
    val analysisStartMillis = System.currentTimeMillis()

    initReporter(args)

    val group = monitorUpdateCommon.findGroup(args)
    monitorUpdateCommon.verifyNewRoute(group, args)

    val monitorRouteId = ObjectId()
    val route = MonitorRoute(
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
      relation = None,
      happy = false,
    )

    args.reporter.stepActive("analyze-route-structure")

    if (args.update.relationId.isEmpty) {

      args.reporter.stepActive("save")
      monitorRouteRepository.saveRoute(route)
      args.reporter.stepDone("save")

      return
    }

    monitorOsmAnalyze.execute(route, now, args, args.referenceTimestamp, analysisStartMillis)
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
