package kpn.server.api.monitor.route

import kpn.api.common.Bounds
import kpn.api.common.Language
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteReferenceInfo
import kpn.api.common.monitor.MonitorRouteSubRelation
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

        relationId match {
          case None =>

            val references = routeRepository.routeReferences(route._id) // TODO improve repository method
            val referenceInfo = if (references.size == 1) {
              val reference = references.head
              Some(
                MonitorRouteReferenceInfo(
                  reference.timestamp,
                  reference.user,
                  reference.bounds,
                  reference.distance,
                  reference.referenceType,
                  reference.referenceDay,
                  reference.segmentCount,
                  reference.filename,
                  reference.geometry
                )
              )
            }
            else {
              None
            }


            val stateOption = route.relationId.flatMap(relId => routeRepository.routeState(route._id, relId))
            val bounds = stateOption match {
              case None => referenceInfo.map(_.bounds)
              case Some(state) =>
                referenceInfo match {
                  case None => Some(state.bounds)
                  case Some(ref) =>
                    if (state.bounds == Bounds()) {
                      Some(ref.bounds)
                    }
                    else {
                      Some(
                        Util.mergeBounds(Seq(state.bounds, ref.bounds))
                      )
                    }

                }
            }

            MonitorRouteMapPage(
              route._id.oid,
              route.relationId,
              route.name,
              route.description,
              group.name,
              group.description,
              bounds,
              None,
              None,
              stateOption.toSeq.flatMap(_.osmSegments),
              stateOption.flatMap(_.matchesGeometry),
              stateOption.toSeq.flatMap(_.deviations),
              referenceInfo,
              Seq.empty
            )


          case Some(xxRelationId) =>

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
                val bounds = stateOption.map(_.bounds)

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
                  reference.timestamp,
                  reference.user,
                  reference.bounds,
                  reference.distance,
                  reference.referenceType,
                  reference.referenceDay,
                  reference.segmentCount,
                  reference.filename,
                  reference.geometry
                )
                val stateOption = routeRepository.routeState(route._id, route.relationId.get)
                val bounds = stateOption match {
                  case None => referenceInfo.bounds
                  case Some(state) =>
                    if (state.bounds == Bounds()) { // TODO this is not possible?
                      reference.bounds
                    }
                    else {
                      Util.mergeBounds(Seq(state.bounds, referenceInfo.bounds))
                    }
                }

                MonitorRouteMapPage(
                  route._id.oid,
                  route.relationId,
                  route.name,
                  route.description,
                  group.name,
                  group.description,
                  Some(bounds),
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
}
