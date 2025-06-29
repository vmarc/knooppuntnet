package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteState
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorAdd(
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateStructure: MonitorUpdateStructure,
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer,
  monitorUpdate: MonitorUpdate,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorUpdateSave: MonitorUpdateSave
) {

  private val log = Log(classOf[MonitorAdd])

  def execute(context: MonitorContext): Unit = {

    context.value.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-add", "prepare"),
          MonitorRouteUpdateStatusCommand("step-add", "analyze-route-structure"),
          MonitorRouteUpdateStatusCommand("step-active", "prepare"),
        )
      )
    )

    monitorUpdateCommon.findGroup(context)
    assertNewRoute(context)

    val referenceTimestamp = if (context.value.update.referenceNow.contains(true)) {
      Some(Time.now)
    }
    else {
      context.value.update.referenceTimestamp
    }

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
            happy = false,
            relation = None
          )
        )
      )
    )

    context.stepActive("analyze-route-structure")
    context.set(monitorUpdateStructure.update(context.value))

    if (context.value.isReferenceTypeGpx) {
      monitorUpdate.updateRouteWithGpxReference(context)
    }
    else if (context.value.isReferenceTypeMultiGpx) {
      addRouteWithMultiGpxReference(context)
    }
    else {
      monitorUpdate.updateSubRelationOsmReferences(context)
    }

    context.stepActive("save")
    monitorUpdateSave.save(context)
    context.stepDone("save")
  }

  private def addRouteWithMultiGpxReference(context: MonitorContext): Unit = {
    context.value.newRoute match {
      case None =>
      case Some(newRoute) =>
        newRoute.relation match {
          case None =>
          case Some(monitorRouteRelation) =>
            val processList = monitorUpdateCommon.composeProcessList(monitorRouteRelation)
            context.value.reporter.processList(processList)

            val processListSize = processList.size
            processList.zipWithIndex.foreach { case (mrr, index) =>
              Log.context(s"${index + 1}/$processListSize ${mrr.relationId}") {
                context.stepActive(mrr.relationId.toString)
                val updateSingleRelationRoute = index == 0 && processList.sizeIs == 1

                monitorRouteRelationRepository.loadTopLevel(None, mrr.relationId).map { relation =>

                  val wayMembers = MonitorFilter.filterWayMembers(relation.wayMembers)
                  if (wayMembers.nonEmpty) {
                    val osmSegmentAnalysis = monitorRouteOsmSegmentAnalyzer.analyze(wayMembers)

                    val id = if (context.value.isActionUpdate || context.value.isActionGpxUpload) {
                      context.value.oldStateIds.find(_.relationId == mrr.relationId) match {
                        case Some(oldStateId) => oldStateId._id
                        case None => ObjectId()
                      }
                    }
                    else {
                      ObjectId()
                    }

                    val state = MonitorRouteState(
                      id,
                      routeId = context.value.routeId,
                      relationId = mrr.relationId,
                      timestamp = Time.now,
                      matchesGeometry = None,
                      deviations = Seq.empty,
                    )

                    monitorRouteRepository.saveRouteState(state)
                    context.set(
                      context.value.copy(
                        stateChanged = true
                      )
                    )
                  }
                }
              }
            }
        }
    }
  }

  private def assertNewRoute(context: MonitorContext): Unit = {
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
