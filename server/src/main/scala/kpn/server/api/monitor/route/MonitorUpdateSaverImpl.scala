package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentBuilder
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorUpdateSaverImpl(
  monitorRouteRepository: MonitorRouteRepository
) extends MonitorUpdateSaver {

  def save(originalContext: MonitorUpdateContext): MonitorUpdateContext = {

    var context: MonitorUpdateContext = originalContext

    if (context.removeOldReferences) {
      monitorRouteRepository.deleteRouteReferences(context.routeId)
    }

    context.newReferences.foreach { routeRelationReference =>
      monitorRouteRepository.saveRouteReference(routeRelationReference)
    }

    context.newStates.foreach { state =>
      monitorRouteRepository.saveRouteState(state)
    }

    if (context.newReferences.nonEmpty) {
      monitorRouteRepository.superRouteReferenceSummary(context.routeId) match {
        case None =>
        case Some(referenceDistance) =>
          context.newRoute match {
            case None =>
              context.oldRoute match {
                case None =>
                case Some(oldRoute) =>

                  val updatedRelation = if (context.referenceType.contains("multi-gpx")) {
                    oldRoute.relation.map { monitorRouteRelation =>
                      udpateMonitorRouteRelation(context, monitorRouteRelation)
                    }
                  }
                  else {
                    oldRoute.relation
                  }

                  val updatedRoute = oldRoute.copy(
                    referenceDistance = referenceDistance,
                    relation = updatedRelation
                  )
                  context = context.copy(
                    newRoute = Some(updatedRoute)
                  )
              }

            case Some(newRoute) =>
              val updatedRelation = if (context.referenceType.contains("multi-gpx")) {
                newRoute.relation.map { monitorRouteRelation =>
                  udpateMonitorRouteRelation(context, monitorRouteRelation)
                }
              }
              else {
                newRoute.relation
              }
              val updatedRoute = newRoute.copy(
                referenceDistance = referenceDistance,
                relation = updatedRelation
              )
              context = context.copy(
                newRoute = Some(updatedRoute)
              )
          }
      }
    }

    if (context.newStates.nonEmpty) {

      monitorRouteRepository.superRouteStateSummary(context.routeId) match {
        case None => // cannot do update
        case Some(monitorRouteStateSummary) =>

          val relation = context.route.relation.map(relation => updatedMonitorRouteRelation(context, relation))

          val updatedRoute = context.route.copy(
            deviationDistance = monitorRouteStateSummary.deviationDistance,
            deviationCount = monitorRouteStateSummary.deviationCount,
            osmWayCount = monitorRouteStateSummary.osmWayCount,
            osmDistance = monitorRouteStateSummary.osmDistance,
            relation = relation
          )
          context = context.copy(
            newRoute = Some(updatedRoute)
          )
      }

      val monitorRouteSegmentInfos = monitorRouteRepository.routeStateSegments(context.routeId)
      val superRouteSuperSegments = MonitorRouteOsmSegmentBuilder.build(monitorRouteSegmentInfos)
      val happy = superRouteSuperSegments.size == 1 && context.newRoute.map(_.deviationCount).sum == 0

      val updatedRoute = context.route.copy(
        osmSegments = superRouteSuperSegments,
        osmSegmentCount = superRouteSuperSegments.size,
        happy = happy
      )
      context = context.copy(
        newRoute = Some(updatedRoute)
      )
    }

    context.newRoute match {
      case Some(route) => monitorRouteRepository.saveRoute(route)
      case None =>
    }
    context
  }

  private def updatedMonitorRouteRelation(context: MonitorUpdateContext, monitorRouteRelation: MonitorRouteRelation): MonitorRouteRelation = {

    val updatedRelations = monitorRouteRelation.relations.map(r => updatedMonitorRouteRelation(context, r))

    context.newStates.find(_.relationId == monitorRouteRelation.relationId) match {
      case None =>
        monitorRouteRelation.copy(
          relations = updatedRelations
        )

      case Some(state) =>

        monitorRouteRelation.copy(
          deviationDistance = state.deviations.map(_.meters).sum,
          deviationCount = state.deviations.size,
          osmWayCount = state.wayCount,
          osmDistance = state.osmDistance,
          osmSegmentCount = state.osmSegments.size,
          happy = state.happy,
          relations = updatedRelations
        )
    }
  }

  private def udpateMonitorRouteRelation(context: MonitorUpdateContext, monitorRouteRelation: MonitorRouteRelation): MonitorRouteRelation = {
    if (context.newReferences.nonEmpty) {
      val relations = monitorRouteRelation.relations.map(r => udpateMonitorRouteRelation(context, r))
      context.newReferences.find(_.relationId == monitorRouteRelation.relationId) match {
        case None =>
          monitorRouteRelation.copy(
            relations = relations
          )
        case Some(reference) =>
          monitorRouteRelation.copy(
            referenceDay = Some(reference.referenceDay),
            referenceFilename = reference.filename,
            referenceDistance = reference.distance,
            relations = relations
          )
      }
    }
    else {
      monitorRouteRelation
    }
  }
}
