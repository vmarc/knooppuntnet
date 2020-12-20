package kpn.server.api.monitor.route

import kpn.api.common.Language
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteReferenceInfo
import kpn.core.util.Util
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteMapPageBuilderImpl(
  monitorRouteRepository: MonitorRouteRepository
) extends MonitorRouteMapPageBuilder {

  override def build(routeId: Long, language: Language): Option[MonitorRouteMapPage] = {
    monitorRouteRepository.route(routeId).flatMap { route =>
      monitorRouteRepository.routeState(routeId).map { routeState =>
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
          route.id,
          route.ref,
          route.translatedName(language),
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
