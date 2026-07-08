package kpn.server.monitor.route

import kpn.api.common.monitor.MonitorReferenceInfo
import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRouteChange
import kpn.server.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.ChangeSetInfoRepository
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class MonitorRouteChangePageBuilder(
  monitorRouteRepository: MonitorRouteRepository,
  monitorGroupRepository: MonitorGroupRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) {

  private val log = Log(classOf[MonitorRouteChangePageBuilder])

  def build(routeId: Long, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChangePage] = {

    val comment = changeSetInfoRepository.get(changeSetId).flatMap(_.tagValue("comment"))

    monitorRouteRepository.routeChange(s"TODO KEY$routeId", changeSetId, replicationNumber) match {
      case None =>
        log.warn(s"Could not read routeChange routeId=$routeId, changeSetId=$changeSetId, replicationNumber=$replicationNumber")
        None

      case Some(routeChange) =>
        monitorRouteRepository.routeChangeGeometry(s"TODO KEY$routeId", changeSetId, replicationNumber) match {
          case None =>
            log.warn(s"Could not read routeChangeGeometry routeId=$routeId, changeSetId=$changeSetId, replicationNumber=$replicationNumber")
            None

          case Some(routeChangeGeometry) =>
            monitorGroupRepository.groupById(new ObjectId("TODO MON") /*routeChange.groupId*/) match {
              case None =>
                log.warn(s"Could not read group TODO {routeChange.groupName}")
                None

              case Some(group) =>
                monitorRouteRepository.reference(new ObjectId("TODO MON") /*"TODO KEY" + routeId , routeChange.referenceKey*/ , None) match {
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

  private def buildPage(comment: Option[String], routeChange: MonitorRouteChange, routeChangeGeometry: MonitorRouteChangeGeometry, group: MonitorGroup, reference: MonitorReference) = {
    val referenceInfo = MonitorReferenceInfo(
      reference.timestamp,
      reference.user,
      reference.referenceBounds,
      reference.referenceDistance,
      reference.referenceType,
      reference.referenceTimestamp,
      reference.referenceSegmentCount,
      reference.referenceFilename,
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
        referenceInfo.referenceBounds,
        routeChange.routeSegmentCount,
        routeChangeGeometry.routeSegments,
        routeChangeGeometry.newDeviations,
        routeChangeGeometry.resolvedDeviations,
        referenceInfo,
        routeChange.happy,
        routeChange.investigate
      )
    )
  }
}
