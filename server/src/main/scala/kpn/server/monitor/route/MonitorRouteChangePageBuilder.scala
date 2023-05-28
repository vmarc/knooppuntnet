package kpn.server.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.api.common.monitor.MonitorRouteReferenceInfo
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRouteChange
import kpn.server.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.ChangeSetInfoRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteChangePageBuilder(
  monitorRouteRepository: MonitorRouteRepository,
  monitorGroupRepository: MonitorGroupRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) {

  private val log = Log(classOf[MonitorRouteChangePageBuilder])

  def build(routeId: Long, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChangePage] = {

    val comment = changeSetInfoRepository.get(changeSetId).flatMap(_.tags("comment"))

    monitorRouteRepository.routeChange("TODO KEY" + routeId, changeSetId, replicationNumber) match {
      case None =>
        log.warn(s"Could not read routeChange routeId=$routeId, changeSetId=$changeSetId, replicationNumber=$replicationNumber")
        None

      case Some(routeChange) =>
        monitorRouteRepository.routeChangeGeometry("TODO KEY" + routeId, changeSetId, replicationNumber) match {
          case None =>
            log.warn(s"Could not read routeChangeGeometry routeId=$routeId, changeSetId=$changeSetId, replicationNumber=$replicationNumber")
            None

          case Some(routeChangeGeometry) =>
            monitorGroupRepository.groupById(ObjectId("TODO MON") /*routeChange.groupId*/) match {
              case None =>
                log.warn(s"Could not read group TODO {routeChange.groupName}")
                None

              case Some(group) =>
                monitorRouteRepository.routeReference(ObjectId("TODO MON") /*"TODO KEY" + routeId , routeChange.referenceKey*/) match {
                  case None =>
                    log.warn(s"Could not routeReference routeId=$routeId")
                    None

                  case Some(routeReference) =>
                    buildPage(comment, routeChange, routeChangeGeometry, group, routeReference)
                }
            }
        }
    }
  }

  private def buildPage(comment: Option[String], routeChange: MonitorRouteChange, routeChangeGeometry: MonitorRouteChangeGeometry, group: MonitorGroup, routeReference: MonitorRouteReference) = {
    val reference = MonitorRouteReferenceInfo(
      routeReference.timestamp,
      routeReference.user,
      routeReference.bounds,
      0, // TODO distance
      routeReference.referenceType,
      routeReference.referenceDay,
      routeReference.segmentCount,
      routeReference.filename,
      routeReference.geoJson
    )

    Some(
      MonitorRouteChangePage(
        routeChange.key,
        group.name,
        group.description,
        comment,
        routeChange.wayCount,
        routeChange.waysAdded,
        routeChange.waysRemoved,
        routeChange.waysUpdated,
        routeChange.osmDistance,
        routeReference.bounds,
        routeChange.routeSegmentCount,
        routeChangeGeometry.routeSegments,
        routeChangeGeometry.newDeviations,
        routeChangeGeometry.resolvedDeviations,
        reference,
        routeChange.happy,
        routeChange.investigate
      )
    )
  }
}
