package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorAdd(
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateStructure: MonitorUpdateStructure,
  monitorUpdate: MonitorUpdate,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorUpdateSave: MonitorUpdateSave,
  monitorAddMultiGpx: MonitorAddMultiGpx,
  monitorAddOsmNow: MonitorAddOsmNow
) {

  private val log = Log(classOf[MonitorAdd])

  def execute(context: MonitorContext): Unit = {
    {
      val args = MonitorUpdateArgs(
        context.value.user,
        context.value.reporter,
        context.value.update,
      )

      if (args.update.referenceType == MonitorReferenceType.multiGpx) {
        monitorAddMultiGpx.execute(args)
        return
      }
      if (args.update.referenceType == MonitorReferenceType.osm && args.update.referenceNow.contains(true)) {
        monitorAddOsmNow.execute(args)
        return
      }
    }

    initReporter(context)

    monitorUpdateCommon.oldFindGroup(context)
    verifyNewRoute(context)

    val referenceTimestamp = determineReferenceTimestamp(context)

    buildMonitorRoute(context, referenceTimestamp)

    context.stepActive("analyze-route-structure")
    context.set(monitorUpdateStructure.update(context.value))

    if (context.value.isReferenceTypeGpx) {
      monitorUpdate.updateRouteWithGpxReference(context)
    }
    else if (context.value.isReferenceTypeMultiGpx) {
    }
    else {
      monitorUpdate.updateSubRelationOsmReferences(context)
    }

    context.stepActive("save")
    monitorUpdateSave.save(context)
    context.stepDone("save")
  }

  private def buildMonitorRoute(context: MonitorContext, referenceTimestamp: Option[Timestamp]): Unit = {
    context.set(
      context.value.copy(
        newRoute = Some(
          MonitorRoute(
            ObjectId(),
            context.value.group.get._id,
            context.value.update.routeName,
            context.value.update.description.getOrElse(""),
            context.value.update.comment,
            context.value.update.relationId,
            context.value.user,
            Time.now,
            None,
            None,
            None,
            referenceType = context.value.update.referenceType,
            referenceTimestamp = referenceTimestamp,
            referenceFilename = context.value.update.referenceFilename,
            referenceDistance = 0,
            deviationDistance = 0,
            deviationCount = 0,
            osmSegmentCount = 0,
            osmDistance = 0,
            relation = None,
            happy = false,
          )
        )
      )
    )
  }

  private def determineReferenceTimestamp(context: MonitorContext): Option[Timestamp] = {
    val referenceTimestamp = if (context.value.update.referenceNow.contains(true)) {
      Some(Time.now)
    }
    else {
      context.value.update.referenceTimestamp
    }
    referenceTimestamp
  }

  private def initReporter(context: MonitorContext): Unit = {
    context.value.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-add", "prepare"),
          MonitorRouteUpdateStatusCommand("step-add", "analyze-route-structure"),
          MonitorRouteUpdateStatusCommand("step-active", "prepare"),
        )
      )
    )
  }

  private def verifyNewRoute(context: MonitorContext): Unit = {
    val group = context.value.group.get
    val routeName = context.value.update.routeName
    monitorRouteRepository.routeByName(group._id, routeName) match {
      case None => // OK: no route with this name yet
      case Some(route) =>
        throw new IllegalStateException(
          s"""Could not add route with name "$routeName": already exists (_id=${route._id.oid}) in group with name "${group.name}""""
        )
    }
  }
}
