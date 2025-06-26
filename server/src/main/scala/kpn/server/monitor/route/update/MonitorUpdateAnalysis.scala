package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
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

        val context = new MonitorContext()
        val reporter = new MonitorUpdateReporterLogger()
        context.set(
          MonitorUpdateContext(
            user = "analyzer",
            reporter = null,
            update = null, //update,
            referenceType = Some(oldRoute.referenceType),
            group = Some(group),
            newRoute = Some(oldRoute),
            analysisStartMillis = Some(System.currentTimeMillis()),
          )
        )

        context.set(
          monitorUpdateStructure.update(context.value)
        )

        val oldStateIds = monitorRouteRepository.routeStateIds(context.value.routeId)
        context.set(
          context.value.copy(
            oldStateIds = oldStateIds
          )
        )

        monitorUpdateCommon.removeObsoleteStates(context)

        if (oldRoute.referenceType == MonitorReferenceType.multiGpx) {
          analyzeMultiGpx(context, oldRoute)
        }
        else if (oldRoute.referenceType == MonitorReferenceType.gpx) {
          analyzeGpx(context, oldRoute)
        }
        else {
          analyzeOsm(context, oldRoute)
        }

        monitorUpdateSave.save(context)
        ("analysis completed", ())
      }
    }
  }

  private def analyzeMultiGpx(context: MonitorContext, route: MonitorRoute): Unit = {
    route.relation match {
      case None =>
      case Some(rootMonitorRouteRelation) =>
        val monitorRouteRelations = monitorUpdateCommon.composeProcessList(rootMonitorRouteRelation)
        val monitorRouteRelationsSize = monitorRouteRelations.size
        monitorRouteRelations.zipWithIndex.foreach { case (monitorRouteRelation, index) =>
          Log.context(s"${index + 1}/$monitorRouteRelationsSize ${monitorRouteRelation.relationId}") {
            if (monitorRouteRelation.referenceTimestamp.nonEmpty && monitorRouteRelation.referenceFilename.nonEmpty) {
              monitorRouteRepository.routeReference(route._id, Some(monitorRouteRelation.relationId)) match {
                case None => log.error("could not find reference")
                case Some(reference) =>
                  monitorUpdateAnalyzeReference.analyzeReference(context, reference, None) match {
                    case None => log.error("could not analyze")
                    case Some(newState) =>
                      val shouldUpdate = monitorRouteRepository.routeState(route._id, monitorRouteRelation.relationId) match {
                        case None => true
                        case Some(oldState) =>
                          newState.copy(timestamp = null) != oldState.copy(timestamp = null)
                      }

                      if (shouldUpdate) {
                        monitorRouteRepository.saveRouteState(newState)
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
  }

  private def analyzeGpx(context: MonitorContext, route: MonitorRoute): Unit = {
    monitorRouteRepository.routeReference(route._id, route.relationId) match {
      case None => log.error("reference not found")
      case Some(reference) =>
        monitorUpdateAnalyzeReference.analyzeReference(context, reference, None) match {
          case None => log.error("could not analyze")
          case Some(newState) =>

            val shouldUpdate = route.relationId match {
              case None => false
              case Some(relationId) =>
                monitorRouteRepository.routeState(route._id, relationId) match {
                  case None => true
                  case Some(oldState) =>
                    newState.copy(timestamp = null) != oldState.copy(timestamp = null)
                }
            }

            if (shouldUpdate) {
              monitorRouteRepository.saveRouteState(newState)
              context.set(
                context.value.copy(
                  stateChanged = true
                )
              )
            }
        }
    }
  }

  private def analyzeOsm(context: MonitorContext, route: MonitorRoute): Unit = {
    route.relation match {
      case None =>
      case Some(rootMonitorRouteRelation) =>
        val monitorRouteRelations = monitorUpdateCommon.composeProcessList(rootMonitorRouteRelation)
        val monitorRouteRelationsSize = monitorRouteRelations.size
        monitorRouteRelations.zipWithIndex.foreach { case (monitorRouteRelation, index) =>
          Log.context(s"${index + 1}/$monitorRouteRelationsSize ${monitorRouteRelation.relationId}") {
            monitorRouteRepository.routeReference(route._id, Some(monitorRouteRelation.relationId)) match {
              case None =>
                log.info("could not find reference") // TODO ???
              case Some(reference) =>
                monitorUpdateAnalyzeReference.analyzeReference(context, reference, None) match {
                  case None =>
                    log.error("could not analyze")
                  case Some(newState) =>

                    val shouldUpdate = monitorRouteRepository.routeState(route._id, monitorRouteRelation.relationId) match {
                      case None =>
                        true
                      case Some(oldState) =>
                        newState.copy(timestamp = null) != oldState.copy(timestamp = null)
                    }

                    if (shouldUpdate) {
                      monitorRouteRepository.saveRouteState(newState)
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

  private def updateMonitorRouteRelation(
    monitorRouteRelation: MonitorRouteRelation,
    reference: MonitorRouteReference,
    stateOption: Option[MonitorRouteState]
  ): MonitorRouteRelation = {

    reference.relationId match {
      case None => monitorRouteRelation
      case Some(referenceRelationId) =>

        if (referenceRelationId == monitorRouteRelation.relationId) {

          val deviationDistance = stateOption match {
            case None => 0
            case Some(state) => state.deviations.map(_.distance).sum
          }
          val deviationCount = stateOption match {
            case None => 0
            case Some(state) => state.deviations.size
          }
          val happy = stateOption match {
            case None => false
            case Some(state) => state.happy
          }

          monitorRouteRelation.copy(
            referenceTimestamp = Some(reference.referenceTimestamp),
            referenceFilename = reference.referenceFilename,
            referenceDistance = reference.referenceDistance,
            deviationDistance = deviationDistance,
            deviationCount = deviationCount,
            // TODO update happy, taking into account subrelations
            happy = happy
          )
        }
        else {
          val relations = monitorRouteRelation.relations.map { subRelation =>
            updateMonitorRouteRelation(subRelation, reference, stateOption)
          }
          monitorRouteRelation.copy(
            relations = relations
            // TODO update happy, taking into account subrelations
          )
        }
    }
  }

  private def resetReference(monitorRouteRelation: MonitorRouteRelation, subRelationId: Long): MonitorRouteRelation = {
    if (monitorRouteRelation.relationId == subRelationId) {
      monitorRouteRelation.copy(
        referenceTimestamp = None,
        referenceFilename = None,
        referenceDistance = 0,
        deviationDistance = 0,
        deviationCount = 0,
      )
    }
    else {
      monitorRouteRelation.copy(
        relations = monitorRouteRelation.relations.map(rel => resetReference(rel, subRelationId))
      )
    }
  }
}
