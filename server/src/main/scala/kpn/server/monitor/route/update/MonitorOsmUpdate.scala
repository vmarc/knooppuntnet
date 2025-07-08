package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorOsmUpdate(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateStructure: MonitorUpdateStructure,
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer,
  monitorUpdateAnalyzeReference: MonitorUpdateAnalyzeReference,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorUpdateSave: MonitorUpdateSave,
  monitorOsmAnalyze: MonitorOsmAnalyze
) {

  private val log = Log(classOf[MonitorOsmUpdate])

  def execute(args: MonitorUpdateArgs): Unit = {

    val now = Time.now
    val analysisStartMillis = System.currentTimeMillis()

    initReporter(args)

    val group = monitorUpdateCommon.findGroup(args)
    val route = monitorUpdateCommon.findRoute(args, group)

    val updatedRoute = if (monitorUpdateCommon.isRouteChanged(route, args)) {

      val groupId = args.update.newGroupName match {
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

      route.copy(
        groupId = groupId,
        name = args.update.newRouteName.getOrElse(route.name),
        description = args.update.description.getOrElse(""),
        comment = args.update.comment,
        relationId = args.update.relationId,
        user = args.user,
        timestamp = Time.now,
        referenceType = args.update.referenceType, // TODO hard code osm?
        referenceTimestamp = args.update.referenceTimestamp,
        referenceFilename = args.update.referenceFilename, // TODO hard code None?
      )
    }
    else {
      route
    }

    args.reporter.stepActive("analyze-route-structure")
    //    context.set(monitorUpdateStructure.update(context.value))

    val oldReferences = monitorRouteRepository.routeReferences(route._id)
    val oldReferenceIds = monitorRouteRepository.routeReferenceIds(route._id)
    val oldStateIds = monitorRouteRepository.routeStateIds(route._id)
    //    context.set(
    //      context.value.copy(
    //        oldReferenceIds = oldReferenceIds,
    //        oldStateIds = oldStateIds,
    //        references = oldReferences,
    //      )
    //    )

    //    context.set(monitorUpdateCommon.removeObsoleteReferences(context))
    //    monitorUpdateCommon.removeObsoleteStates(context)

    if (route.referenceType == MonitorReferenceType.osm && args.update.referenceType == MonitorReferenceType.osm) {
      if (route.referenceTimestamp != args.update.referenceTimestamp || route.relationId != args.update.relationId) {
        // perform reference update and reanalyze deviations
        //        updateSubRelationOsmReferences(context)
        monitorOsmAnalyze.execute(
          updatedRoute,
          now,
          args,
          analysisStartMillis
        )
      }
      else {
        // nothing to do, but saving updatedRoute?
      }
    }
    else {
      throw new IllegalStateException(s"reference type change from ${route.referenceType} to ${args.update.referenceType} not implemented yet")
    }
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
