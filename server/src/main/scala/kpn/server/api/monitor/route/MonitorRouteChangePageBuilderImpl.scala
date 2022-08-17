package kpn.server.api.monitor.route

import kpn.api.base.MongoId
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.api.common.monitor.MonitorRouteReferenceInfo
import kpn.core.util.Log
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteChangePageBuilderImpl(
  monitorRouteRepository: MonitorRouteRepository,
  monitorGroupRepository: MonitorGroupRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends MonitorRouteChangePageBuilder {

  private val log = Log(classOf[MonitorRouteChangePageBuilderImpl])

  override def build(routeId: Long, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChangePage] = {

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
            monitorGroupRepository.groupById(MongoId("TODO MON") /*routeChange.groupId*/) match {
              case None =>
                log.warn(s"Could not read group TODO {routeChange.groupName}")
                None

              case Some(group) =>
                monitorRouteRepository.routeReference("TODO KEY" + routeId, routeChange.referenceKey) match {
                  case None =>
                    log.warn(s"Could not routeReference routeId=$routeId, referenceKey=${routeChange.referenceKey}")
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
        routeChangeGeometry.newNokSegments,
        routeChangeGeometry.resolvedNokSegments,
        reference,
        routeChange.happy,
        routeChange.investigate
      )
    )
  }
}
