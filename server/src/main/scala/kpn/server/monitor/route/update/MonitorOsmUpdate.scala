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

    if (!monitorUpdateCommon.isRouteChanged(route, args)) {

      args.reporter.report(
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-done", "prepare"),
          )
        )
      )

      return
    }

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

    val updatedRoute = route.copy(
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

    //    context.set(monitorUpdateStructure.update(context.value))

    //    val oldReferences = monitorRouteRepository.routeReferences(route._id)
    //    val oldReferenceIds = monitorRouteRepository.routeReferenceIds(route._id)
    //    val oldStateIds = monitorRouteRepository.routeStateIds(route._id)

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

        if (args.update.relationId.isEmpty) {

          args.reporter.report(
            MonitorRouteUpdateStatusMessage(
              commands = Seq(
                MonitorRouteUpdateStatusCommand("step-add", "save"),
                MonitorRouteUpdateStatusCommand("step-active", "save"),
              )
            )
          )

          monitorRouteRepository.deleteRouteReferences(route._id)
          monitorRouteRepository.deleteRouteStates(route._id)

          val cleanedUpRoute = updatedRoute.copy(
            analysisTimestamp = None,
            symbol = None,
            referenceDistance = 0,
            deviationDistance = 0,
            deviationCount = 0,
            osmSegmentCount = 0,
            osmDistance = 0,
            relation = None,
            happy = false,
          )
          monitorRouteRepository.saveRoute(cleanedUpRoute)

          args.reporter.stepDone("save")
        }
        else {
          args.reporter.report(
            MonitorRouteUpdateStatusMessage(
              commands = Seq(
                MonitorRouteUpdateStatusCommand("step-add", "analyze-route-structure"),
                MonitorRouteUpdateStatusCommand("step-active", "analyze-route-structure"),
              )
            )
          )

          if (route.relationId != args.update.relationId) {
            monitorRouteRepository.deleteRouteReferences(route._id)
            monitorRouteRepository.deleteRouteStates(route._id)
          }

          monitorOsmAnalyze.execute(
            updatedRoute,
            now,
            args,
            analysisStartMillis
          )
        }
      }
      else {
        // nothing to do, but saving updatedRoute?
        args.reporter.report(
          MonitorRouteUpdateStatusMessage(
            commands = Seq(
              MonitorRouteUpdateStatusCommand("step-add", "save"),
              MonitorRouteUpdateStatusCommand("step-active", "save"),
            )
          )
        )
        monitorRouteRepository.saveRoute(updatedRoute)
        args.reporter.stepDone("save")
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
          MonitorRouteUpdateStatusCommand("step-active", "prepare"),
        )
      )
    )
  }
}
