package kpn.server.monitor.route

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentBuilder
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorRouteStateSummary
import org.springframework.stereotype.Component

@Component
class MonitorUpdateSaverImpl(
  monitorRouteRepository: MonitorRouteRepository
) extends MonitorUpdateSaver {

  def save(originalContext: MonitorUpdateContext, gpxDeleted: Boolean): MonitorUpdateContext = {

    var context: MonitorUpdateContext = originalContext

    //    if (context.removeOldReferences) {
    //      monitorRouteRepository.deleteRouteReferences(context.routeId)
    //      monitorRouteRepository.deleteRouteStates(context.routeId)
    //    }

    //    context.newReferences.foreach { routeRelationReference =>
    //      monitorRouteRepository.saveRouteReference(routeRelationReference)
    //    }

    //    context.newStates.foreach { state =>
    //      monitorRouteRepository.saveRouteState(state)
    //    }


    if (gpxDeleted) {

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

                  val updatedRelation = if (context.update.referenceType.contains("multi-gpx")) {
                    oldRoute.relation.map { monitorRouteRelation =>
                      udpateMonitorRouteRelation(context, monitorRouteRelation)
                    }
                  }
                  else {
                    oldRoute.relation
                  }

                  val updatedRoute = oldRoute.copy(
                    referenceDistance = if (context.update.referenceType == "osm") referenceDistance else 0,
                    relation = updatedRelation
                  )

                  context = context.copy(
                    newRoute = Some(updatedRoute)
                  )
              }

            case Some(newRoute) =>
              val updatedRelation = if (context.update.referenceType.contains("multi-gpx")) {
                newRoute.relation.map { monitorRouteRelation =>
                  udpateMonitorRouteRelation(context, monitorRouteRelation)
                }
              }
              else {
                newRoute.relation
              }
              val updatedRoute = newRoute.copy(
                referenceDistance = if (context.update.referenceType != "multi-gpx") referenceDistance else 0,
                relation = updatedRelation
              )
              context = context.copy(
                newRoute = Some(updatedRoute)
              )
          }
      }
    }

    if (context.newStates.nonEmpty || gpxDeleted) {

      val stateSummaries = monitorRouteRepository.routeStateSummaries(context.routeId)
      val relation = context.route.relation.map(relation => updatedMonitorRouteRelation(context, relation, stateSummaries))

      val osmWayCount = stateSummaries.map(_.osmWayCount).sum
      val osmDistance = stateSummaries.map(_.osmDistance).sum

      context = context.copy(
        newRoute = Some(
          context.route.copy(
            relation = relation,
            //      deviationDistance = relation.deviationDistance
            //      deviationCount = monitorRouteStateSummary.deviationCount
            osmWayCount = osmWayCount,
            osmDistance = osmDistance
          )
        )
      )

      val monitorRouteSegmentInfos = monitorRouteRepository.routeStateSegments(context.routeId)
      val superRouteSuperSegments = MonitorRouteOsmSegmentBuilder.build(monitorRouteSegmentInfos)
      val happy = superRouteSuperSegments.size == 1 &&
        context.newRoute.map(_.deviationCount).sum == 0 &&
        (context.newRoute.get.relation.map(_.happy).getOrElse(false)) // TODO happy derived from root, no need to traverse tree?

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

  private def updatedMonitorRouteRelation(context: MonitorUpdateContext, monitorRouteRelation: MonitorRouteRelation, stateSummaries: Seq[MonitorRouteStateSummary]): MonitorRouteRelation = {

    println("here...")
    val updatedRelations = monitorRouteRelation.relations.map(r => updatedMonitorRouteRelation(context, r, stateSummaries))
    val subRelationsHappy = updatedRelations.forall(_.happy)

    val updatedWithState = stateSummaries.find(_.relationId == monitorRouteRelation.relationId) match {
      case None =>
        monitorRouteRelation.copy(
          relations = updatedRelations,
          happy = subRelationsHappy
        )

      case Some(state) =>

        monitorRouteRelation.copy(
          deviationDistance = state.deviationDistance,
          deviationCount = state.deviationCount,
          osmWayCount = state.osmWayCount,
          osmDistance = state.osmDistance,
          osmSegmentCount = state.osmSegmentCount,
          happy = state.happy && subRelationsHappy,
          relations = updatedRelations
        )

    }
    if (context.update.action == "gpx-delete" && context.update.relationId.get == monitorRouteRelation.relationId) {
      updatedWithState.copy(
        referenceTimestamp = None,
        referenceFilename = None,
        referenceDistance = 0
      )
    }
    else {
      updatedWithState
    }
  }

  private def udpateMonitorRouteRelation(context: MonitorUpdateContext, monitorRouteRelation: MonitorRouteRelation): MonitorRouteRelation = {
    if (context.newReferences.nonEmpty) {
      val relations = monitorRouteRelation.relations.map(r => udpateMonitorRouteRelation(context, r))
      context.newReferences.find(_.relationId.get == monitorRouteRelation.relationId) match {
        case None =>
          monitorRouteRelation.copy(
            relations = relations
          )
        case Some(reference) =>
          monitorRouteRelation.copy(
            referenceTimestamp = Some(reference.referenceTimestamp),
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
