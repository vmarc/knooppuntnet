package kpn.server.api.monitor.route

import kpn.api.base.MongoId
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

  override def build(monitorRouteId: String, language: Language): Option[MonitorRouteMapPage] = {
    monitorRouteRepository.routeById(MongoId("TODO") /*monitorRouteId*/).flatMap { route =>

      monitorGroupRepository.groupById(route.groupId).map { group =>

        val routeStateOption = monitorRouteRepository.routeState(monitorRouteId)

        val reference = routeStateOption match {
          case Some(routeState) =>
            routeState.referenceKey.flatMap { referenceKey =>
              monitorRouteRepository.routeReference(monitorRouteId, referenceKey).map { routeReference =>
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

          case None =>
            monitorRouteRepository.routeReference(monitorRouteId).map { routeReference =>
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
          route._id,
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
