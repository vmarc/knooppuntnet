package kpn.server.api.monitor.route

import kpn.api.common.Language
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteReferenceInfo
import kpn.core.util.Util
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteMapPageBuilderImpl(
  monitorRouteRepository: MonitorRouteRepository,
  monitorGroupRepository: MonitorGroupRepository
) extends MonitorRouteMapPageBuilder {

  override def build(routeId: Long, language: Language): Option[MonitorRouteMapPage] = {
    monitorRouteRepository.route(routeId).flatMap { route =>
      monitorRouteRepository.routeState(routeId).flatMap { routeState =>
        monitorGroupRepository.group(route.groupName).map { group =>
          val reference = routeState.referenceKey.flatMap { referenceKey =>
            monitorRouteRepository.routeReference(routeId, referenceKey).map { routeReference =>
              MonitorRouteReferenceInfo(
                routeReference.key,
                routeReference.created,
                routeReference.user,
                routeReference.bounds,
                0, // TODO distance
                routeReference.referenceType,
                routeReference.referenceTimestamp,
                routeReference.segmentCount,
                routeReference.filename,
                routeReference.geometry
              )
            }
          }

          val bounds = reference.map(_.bounds) match {
            case Some(referenceBounds) => Util.mergeBounds(Seq(routeState.bounds, referenceBounds))
            case None => routeState.bounds
          }

          MonitorRouteMapPage(
            route.routeId,
            route.translatedName(language),
            group.name,
            group.description,
            bounds,
            routeState.osmSegments,
            routeState.okGeometry,
            routeState.nokSegments,
            reference
          )
        }
      }
    }
  }
}
