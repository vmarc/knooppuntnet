package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteReferenceInfo
import kpn.api.common.monitor.MonitorRouteSubRelation
import kpn.api.common.Bounds
import kpn.api.common.Language
import kpn.core.common.Time
import kpn.core.util.Triplet
import kpn.core.util.Util
import kpn.server.api.monitor.MonitorUtil
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteMapPageBuilder(
  groupRepository: MonitorGroupRepository,
  routeRepository: MonitorRouteRepository
) {

  def build(language: Language, groupName: String, routeName: String, relationId: Option[Long]): Option[MonitorRouteMapPage] = {
    groupRepository.groupByName(groupName).flatMap { group =>
      routeRepository.routeByName(group._id, routeName).map { route =>

        val subRelations = MonitorUtil.subRelationsIn(route)

        val currentSubRelationId = relationId match {
          case Some(relId) =>
            if (relId == 0) {
              if (subRelations.isEmpty) {
                route.relationId.get // TODO is ok???
              }
              else {
                subRelations.head.relationId
              }
            }
            else {
              relId
            }
          case None =>
            if (subRelations.isEmpty) {
              route.relationId.get // TODO is ok???
            }
            else {
              subRelations.head.relationId
            }
        }

        val (prevSubRelation, nextSubRelation) =
          if (subRelations.nonEmpty) {
            Triplet.slide[MonitorRouteSubRelation](subRelations).find(_.current.relationId == currentSubRelationId) match {
              case None => (None, None)
              case Some(triplet) => (triplet.previous, triplet.next)
            }
          }
          else {
            (None, None)
          }

        routeRepository.routeRelationReference(route._id, currentSubRelationId) match {
          case None =>

            val stateOption = routeRepository.routeState(route._id, currentSubRelationId)
            val bounds = stateOption match {
              case Some(state) => state.bounds
              case None => Bounds()
            }

            MonitorRouteMapPage(
              route._id.oid,
              route.relationId,
              route.name,
              route.description,
              group.name,
              group.description,
              bounds,
              nextSubRelation,
              prevSubRelation,
              stateOption.toSeq.flatMap(_.osmSegments),
              stateOption.flatMap(_.matchesGeometry),
              stateOption.toSeq.flatMap(_.deviations),
              None,
              subRelations
            )

          case Some(reference) =>

            val referenceInfo = MonitorRouteReferenceInfo(
              Time.now, // reference.created,
              "TODO", // reference.user,
              reference.bounds,
              0, // TODO distance
              "", // reference.referenceType,
              Time.now.toDay, // TODO reference.referenceDay,
              reference.segmentCount,
              None, // reference.filename,
              reference.geometry
            )
            val stateOption = routeRepository.routeState(route._id, route.relationId.get)
            val bounds = stateOption match {
              case Some(state) =>
                if (reference.bounds == Bounds()) {
                  state.bounds
                }
                else if (state.bounds == Bounds()) {
                  reference.bounds
                }
                else {
                  Util.mergeBounds(Seq(state.bounds, reference.bounds))
                }

              case None => reference.bounds
            }

            MonitorRouteMapPage(
              route._id.oid,
              route.relationId,
              route.name,
              route.description,
              group.name,
              group.description,
              bounds,
              nextSubRelation,
              prevSubRelation,
              stateOption.toSeq.flatMap(_.osmSegments),
              stateOption.flatMap(_.matchesGeometry),
              stateOption.toSeq.flatMap(_.deviations),
              Some(referenceInfo),
              subRelations
            )
        }
      }
    }
  }
}
