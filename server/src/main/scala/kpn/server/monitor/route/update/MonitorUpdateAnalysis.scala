package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteState
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorUpdateAnalysis(
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateStructure: MonitorUpdateStructure,
  monitorUpdateAnalyzeReference: MonitorUpdateAnalyzeReference,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorUpdateSave: MonitorUpdateSave
) {

  private val log = Log(classOf[MonitorUpdateAnalysis])

  def updateAnalysis(group: MonitorGroup, oldRoute: MonitorRoute): Unit = {
    Log.context(s"${group.name}, ${oldRoute.name}") {
      log.infoElapsed {
        val context = initializeContext(group, oldRoute)
        analyze(context, oldRoute)
        ("analysis completed", ())
      }
    }
  }

  private def initializeContext(group: MonitorGroup, oldRoute: MonitorRoute): MonitorContext = {
    val context = new MonitorContext()

    // Initialize context with basic information
    context.set(
      MonitorUpdateContext(
        user = "analyzer",
        reporter = null,
        update = null,
        referenceType = Some(oldRoute.referenceType),
        group = Some(group),
        newRoute = Some(oldRoute),
        analysisStartMillis = Some(System.currentTimeMillis()),
      )
    )

    // Update context with structure and state IDs
    context.set(monitorUpdateStructure.update(context.value))

    val oldStateIds = monitorRouteRepository.routeStateIds(context.value.routeId)
    context.set(context.value.copy(oldStateIds = oldStateIds))

    // Remove obsolete states
    monitorUpdateCommon.removeObsoleteStates(context)

    context
  }

  private def analyze(context: MonitorContext, route: MonitorRoute): Unit = {
    route.referenceType match {
      case MonitorReferenceType.multiGpx => analyzeMultiGpx(context, route)
      case MonitorReferenceType.gpx => analyzeGpx(context, route)
      case _ => analyzeOsm(context, route)
    }
    monitorUpdateSave.save(context)
  }

  private def analyzeMultiGpx(context: MonitorContext, route: MonitorRoute): Unit = {
    analyzeRelations(
      context,
      route,
    )
  }

  private def analyzeGpx(context: MonitorContext, route: MonitorRoute): Unit = {
    route.relationId.foreach { relationId =>
      analyzeReference(context, route, relationId)
    }
  }

  private def analyzeOsm(context: MonitorContext, route: MonitorRoute): Unit = {
    analyzeRelations(
      context,
      route,
    )
  }

  private def analyzeRelations(
    context: MonitorContext,
    route: MonitorRoute,
  ): Unit = {
    route.relation.foreach { rootMonitorRouteRelation =>
      val monitorRouteRelations = monitorUpdateCommon
        .composeProcessList(rootMonitorRouteRelation)
        .filter(hasReference(context, _))
      val monitorRouteRelationsSize = monitorRouteRelations.size
      monitorRouteRelations.zipWithIndex.foreach { case (monitorRouteRelation, index) =>
        Log.context(s"${index + 1}/$monitorRouteRelationsSize ${monitorRouteRelation.relationId}") {
          analyzeReference(context, route, monitorRouteRelation.relationId)
        }
      }
    }
  }

  private def analyzeReference(context: MonitorContext, route: MonitorRoute, relationId: Long): Unit = {
    monitorRouteRepository.routeReference(route._id, Some(relationId)) match {
      case None =>
        log.error("reference not found")
      case Some(reference) =>
        monitorUpdateAnalyzeReference.analyzeReference(context, reference, None) match {
          case None =>
            log.error("could not analyze")
          case Some(newState) =>
            updateStateIfChanged(context, route, relationId, newState)
        }
    }
  }

  private def updateStateIfChanged(
    context: MonitorContext,
    route: MonitorRoute,
    relationId: Long,
    newState: MonitorRouteState
  ): Unit = {
    if (hasStateChanged(route, relationId, newState)) {
      monitorRouteRepository.saveRouteState(newState)
      context.set(
        context.value.copy(
          stateChanged = true
        )
      )
    }
  }

  private def hasStateChanged(
    route: MonitorRoute,
    relationId: Long,
    newState: MonitorRouteState
  ): Boolean = {
    monitorRouteRepository.routeState(route._id, relationId) match {
      case Some(oldState) => newState.copy(timestamp = null) != oldState.copy(timestamp = null)
      case None => true
    }
  }

  private def hasReference(
    context: MonitorContext,
    monitorRouteRelation: MonitorRouteRelation
  ): Boolean = {
    context.value.referenceType match {
      case Some(MonitorReferenceType.multiGpx) =>
        monitorRouteRelation.referenceTimestamp.nonEmpty && monitorRouteRelation.referenceFilename.nonEmpty
      case _ => true
    }
  }
}
