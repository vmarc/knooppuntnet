package kpn.server.api.monitor.route

import kpn.api.common.Bounds
import kpn.api.common.Language
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteReferenceInfo
import kpn.core.util.Util
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteMapPageBuilder(
  groupRepository: MonitorGroupRepository,
  routeRepository: MonitorRouteRepository
) {

  def build(language: Language, groupName: String, routeName: String): Option[MonitorRouteMapPage] = {
    groupRepository.groupByName(groupName).flatMap { group =>
      routeRepository.routeByName(group._id, routeName).map { route =>
        routeRepository.routeReferenceRouteWithId(route._id) match {
          case None =>

            val stateOption = routeRepository.routeState(route._id)
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
              stateOption.toSeq.flatMap(_.osmSegments),
              stateOption.flatMap(_.matchesGeometry),
              stateOption.toSeq.flatMap(_.deviations),
              None
            )

          case Some(reference) =>

            val referenceInfo = MonitorRouteReferenceInfo(
              reference.created,
              reference.user,
              reference.bounds,
              0, // TODO distance
              reference.referenceType,
              reference.referenceDay,
              reference.segmentCount,
              reference.filename,
              reference.geometry
            )
            val stateOption = routeRepository.routeState(route._id)
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
              stateOption.toSeq.flatMap(_.osmSegments),
              stateOption.flatMap(_.matchesGeometry),
              stateOption.toSeq.flatMap(_.deviations),
              Some(referenceInfo)
            )
        }
      }
    }
  }
}
