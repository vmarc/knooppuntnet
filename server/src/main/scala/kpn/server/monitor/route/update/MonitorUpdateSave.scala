package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentBuilder
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorRouteStateSummary
import org.springframework.stereotype.Component

@Component
class MonitorUpdateSave(
  monitorRouteRepository: MonitorRouteRepository,
  monitorRouteGapAnalyzer: MonitorRouteGapAnalyzer,
) {

  private val log = Log(classOf[MonitorUpdateSave])

  def save(context: MonitorContext): Unit = {

    if (context.value.newReferenceSummaries.nonEmpty) {
      monitorRouteRepository.superRouteReferenceSummary(context.value.routeId) match {
        case None =>
        case Some(referenceDistance) =>
          context.value.newRoute match {
            case None =>
              context.value.oldRoute match {
                case None =>
                case Some(oldRoute) =>

                  val updatedRelation = if (context.value.isReferenceTypeMultiGpx) {
                    oldRoute.relation.map { monitorRouteRelation =>
                      udpateMonitorRouteRelation(context.value, monitorRouteRelation)
                    }
                  }
                  else {
                    oldRoute.relation
                  }

                  val updatedRoute = oldRoute.copy(
                    referenceDistance = if (context.value.isReferenceTypeOsm) referenceDistance else 0,
                    relation = updatedRelation
                  )

                  context.set(
                    context.value.copy(
                      newRoute = Some(updatedRoute)
                    )
                  )
              }

            case Some(newRoute) =>
              val updatedRelation = if (context.value.isReferenceTypeMultiGpx) {
                newRoute.relation.map { monitorRouteRelation =>
                  udpateMonitorRouteRelation(context.value, monitorRouteRelation)
                }
              }
              else {
                newRoute.relation
              }
              val updatedRoute = newRoute.copy(
                referenceDistance = referenceDistance,
                relation = updatedRelation
              )
              context.set(
                context.value.copy(
                  newRoute = Some(updatedRoute)
                )
              )
          }
      }
    }

    if (context.value.structureChanged || context.value.stateChanged) {

      val stateSummaries = monitorRouteRepository.routeStateSummaries(context.value.routeId)
      val relation = context.value.route.relation.map(relation => updatedMonitorRouteRelation(context, relation, stateSummaries))
      val relationWithDistances = relation.map(updatedMonitorRouteRelationCumulativeDistance)

      val monitorRouteSegmentInfos = monitorRouteRepository.routeStateSegments(context.value.routeId)
      val superRouteSuperSegments = MonitorRouteOsmSegmentBuilder.build(monitorRouteSegmentInfos)

      val relationWithGaps = relationWithDistances.map { monitorRouteRelation =>
        monitorRouteGapAnalyzer.calculate(
          monitorRouteSegmentInfos,
          superRouteSuperSegments,
          monitorRouteRelation
        )
      }

      val symbol = relationWithGaps.flatMap(_.symbol)
      val osmWayCount = stateSummaries.map(_.osmWayCount).sum
      val osmDistance = stateSummaries.map(_.osmDistance).sum
      val deviationCount = stateSummaries.map(_.deviationCount).sum
      val deviationDistance = stateSummaries.map(_.deviationDistance).sum

      context.set(
        context.value.copy(
          newRoute = Some(
            context.value.route.copy(
              symbol = symbol,
              relation = relationWithGaps,
              osmWayCount = osmWayCount,
              osmDistance = osmDistance,
              deviationCount = deviationCount,
              deviationDistance = deviationDistance
            )
          )
        )
      )

      val happy = superRouteSuperSegments.sizeIs == 1 &&
        context.value.newRoute.map(_.deviationCount).sum == 0 &&
        context.value.newRoute.get.relation.exists(_.happy)

      val updatedRoute = context.value.route.copy(
        osmSegments = superRouteSuperSegments,
        osmSegmentCount = superRouteSuperSegments.size,
        happy = happy
      )
      context.set(
        context.value.copy(
          newRoute = Some(updatedRoute)
        )
      )
    }

    val analysisDuration = System.currentTimeMillis() - context.value.analysisStartMillis.get

    val savedRoute = context.value.route.copy(
      analysisTimestamp = Some(Time.now),
      analysisDuration = Some(analysisDuration)
    )
    monitorRouteRepository.saveRoute(savedRoute)
  }

  private def udpateMonitorRouteRelation(context: MonitorUpdateContext, monitorRouteRelation: MonitorRouteRelation): MonitorRouteRelation = {
    if (context.newReferenceSummaries.nonEmpty) {
      val relations = monitorRouteRelation.relations.map(r => udpateMonitorRouteRelation(context, r))
      context.newReferenceSummaries.find(_.relationId.get == monitorRouteRelation.relationId) match {
        case None =>
          monitorRouteRelation.copy(
            relations = relations
          )
        case Some(reference) =>
          monitorRouteRelation.copy(
            referenceTimestamp = Some(reference.referenceTimestamp),
            referenceFilename = reference.referenceFilename,
            referenceDistance = reference.referenceDistance,
            relations = relations
          )
      }
    }
    else {
      monitorRouteRelation
    }
  }

  private def updatedMonitorRouteRelation(context: MonitorContext, monitorRouteRelation: MonitorRouteRelation, stateSummaries: Seq[MonitorRouteStateSummary]): MonitorRouteRelation = {

    val updatedWithState = if (context.value.isReferenceTypeGpx) {
      stateSummaries.headOption match {
        case None => monitorRouteRelation
        case Some(stateSummary) =>
          monitorRouteRelation.copy(
            deviationDistance = stateSummary.deviationDistance,
            deviationCount = stateSummary.deviationCount,
            osmWayCount = stateSummary.osmWayCount,
            osmSegmentCount = stateSummary.osmSegmentCount,
            osmDistanceSubRelations = 0,
            happy = stateSummary.happy,
          )
      }
    }
    else {
      val updatedRelations = monitorRouteRelation.relations.map(r => updatedMonitorRouteRelation(context, r, stateSummaries))
      val subRelationsHappy = updatedRelations.forall(_.happy)
      stateSummaries.find(_.relationId == monitorRouteRelation.relationId) match {
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
            osmSegmentCount = state.osmSegmentCount,
            osmDistance = state.osmDistance,
            happy = state.happy && subRelationsHappy,
            relations = updatedRelations
          )
      }
    }

    if (context.value.isActionGpxDelete && context.value.update.relationId.get == monitorRouteRelation.relationId) {
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

  private def updatedMonitorRouteRelationCumulativeDistance(monitorRouteRelation: MonitorRouteRelation): MonitorRouteRelation = {
    val updatedRelations = monitorRouteRelation.relations.map(r => updatedMonitorRouteRelationCumulativeDistance(r))
    val osmDistanceSubRelations = monitorRouteRelation.relations.flatMap(monitorRouteRelationSubRelations).map(_.osmDistance).sum
    monitorRouteRelation.copy(
      osmDistanceSubRelations = osmDistanceSubRelations,
      relations = updatedRelations
    )
  }

  private def monitorRouteRelationSubRelations(monitorRouteRelation: MonitorRouteRelation): Seq[MonitorRouteRelation] = {
    monitorRouteRelation +: monitorRouteRelation.relations.flatMap(r => monitorRouteRelationSubRelations(r))
  }
}
