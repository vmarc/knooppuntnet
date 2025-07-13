package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorCommand
import kpn.api.common.monitor.MonitorMessage
import kpn.api.common.monitor.MonitorReferenceType
import kpn.core.util.Log
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorGpxDelete(
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorUpdateSave: MonitorUpdateSave,
) {

  private val log = Log(classOf[MonitorGpxDelete])

  def execute(args: MonitorUpdateArgs): Unit = {

    if (args.update.referenceType != MonitorReferenceType.multiGpx) {
      throw new RuntimeException(s"invalid reference type ${args.update.referenceType} for gpx upload")
    }

    initReporter(args)

    val group = monitorUpdateCommon.findGroup(args)
    val route = monitorUpdateCommon.findRoute(group, args)

    val superRelationId = route.relationId.getOrElse(throw new RuntimeException("route relation id needed for gpx-delete"))
    val subRelationId = args.update.relationId.getOrElse(throw new RuntimeException("subrelation id needed for gpx-delete"))

    monitorRouteRepository.deleteReference(route._id, subRelationId)
    monitorRouteRepository.deleteState(route._id, subRelationId)

    val updatedRoute = monitorUpdateCommon.updateSuperRoute(route)

    args.reporter.stepActive("save")
    monitorRouteRepository.saveRoute(updatedRoute)
    args.reporter.stepDone("save")
  }

  private def initReporter(args: MonitorUpdateArgs): Unit = {
    args.reporter.report(
      MonitorMessage(
        MonitorCommand.add("delete"),
        MonitorCommand.add("save"),
        MonitorCommand.active("delete"),
      )
    )
  }
}
