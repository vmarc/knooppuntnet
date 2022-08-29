package kpn.server.api.monitor.route

import kpn.api.common.Language
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteReferenceInfo
import kpn.core.util.Util
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteMapPageBuilder(
  monitorRouteRepository: MonitorRouteRepository,
  monitorGroupRepository: MonitorGroupRepository
) {

  def build(language: Language, groupName: String, routeName: String): Option[MonitorRouteMapPage] = {
    monitorGroupRepository.groupByName(groupName).flatMap { group =>
      monitorRouteRepository.routeByName(group._id, routeName).map { route =>
        val routeStateOption = monitorRouteRepository.routeState(route._id)
        val reference = routeStateOption match {
          case Some(routeState) =>
            routeState.referenceId.flatMap { referenceId =>
              monitorRouteRepository.routeReference(referenceId).map { routeReference =>
                MonitorRouteReferenceInfo(
                  routeReference.created,
                  routeReference.user,
                  routeReference.bounds,
                  0, // TODO distance
                  routeReference.referenceType,
                  routeReference.osmReferenceDay,
                  routeReference.segmentCount,
                  routeReference.filename,
                  routeReference.geometry
                )
              }
            }

          case None =>
            throw new RuntimeException("TODO MON should not come here?")
        }

        val bounds = routeStateOption match {
          case None =>
            reference.map(_.bounds).get

          case Some(routeState) =>
            reference.map(_.bounds) match {
              case Some(referenceBounds) => Util.mergeBounds(Seq(routeState.bounds, referenceBounds))
              case None => routeState.bounds
            }
        }

        MonitorRouteMapPage(
          route._id.oid,
          route.relationId,
          route.name,
          group.name,
          group.description,
          bounds,
          routeStateOption.toSeq.flatMap(_.osmSegments),
          routeStateOption.flatMap(_.okGeometry),
          routeStateOption.toSeq.flatMap(_.nokSegments),
          reference
        )
      }
    }
  }
}
