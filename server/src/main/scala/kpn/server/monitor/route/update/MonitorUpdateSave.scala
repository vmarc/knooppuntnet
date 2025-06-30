package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentBuilder
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorRouteStateSummary
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorUpdateSave(
  routeRepository: RouteRepository,
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
      val deviationCount = stateSummaries.map(_.deviationCount).sum
      val deviationDistance = stateSummaries.map(_.deviationDistance).sum

      val segmentCount: Long = context.value.update.relationId.flatMap(routeRepository.routeSegmentCount).getOrElse(0)

      val happy = context.value.update.relationId.nonEmpty &&
        context.value.newRoute.map(_.deviationCount).sum == 0 &&
        context.value.newRoute.get.relation.exists(_.happy)

      context.set(
        context.value.copy(
          newRoute = Some(
            context.value.route.copy(
              symbol = symbol,
              relation = relationWithGaps,
              deviationCount = deviationCount,
              deviationDistance = deviationDistance,
              osmSegmentCount = segmentCount,
              happy = happy
            )
          )
        )
      )
    }

    val analysisDuration = System.currentTimeMillis() - context.value.analysisStartMillis.get

    val happy = context.value.references.nonEmpty &&
      context.value.references.forall(_.referenceDistance > 0) &&
      context.value.route.happy &&
      context.value.update.relationId.nonEmpty

    val savedRoute = context.value.route.copy(
      analysisTimestamp = Some(Time.now),
      analysisDuration = Some(analysisDuration),
      happy = happy,
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
            happy = subRelationsHappy,
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
    monitorRouteRelation.copy(
      relations = updatedRelations
    )
  }

  private def monitorRouteRelationSubRelations(monitorRouteRelation: MonitorRouteRelation): Seq[MonitorRouteRelation] = {
    monitorRouteRelation +: monitorRouteRelation.relations.flatMap(r => monitorRouteRelationSubRelations(r))
  }
}
