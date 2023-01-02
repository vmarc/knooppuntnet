package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.core.util.Log
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.repository.MonitorGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorUpdateRouteImpl(
  monitorGroupRepository: MonitorGroupRepository
) extends MonitorUpdateRoute {

  private val log = Log(classOf[MonitorUpdateRouteImpl])

  override def update(
    context: MonitorUpdateContext,
    user: String,
    properties: MonitorRouteProperties
  ): MonitorUpdateContext = {

    val routeId = context.oldRoute match {
      case Some(oldRoute) => oldRoute._id
      case None => ObjectId()
    }

    val groupIdOption = context.group match {
      case None => monitorGroupRepository.groupByName(properties.groupName).map(_._id)
      case Some(group) =>
        if (group.name != properties.groupName) {
          monitorGroupRepository.groupByName(properties.groupName).map(_._id)
        }
        else {
          Some(group._id)
        }
    }

    groupIdOption match {
      case None =>
        val exception = s"""Could not find group with name "${properties.groupName}""""
        log.error(exception)
        context.copy(
          abort = true,
          saveResult = context.saveResult.copy(
            exception = Some(exception)
          )
        )

      case Some(groupId) =>
        val newRoute = MonitorRoute(
          routeId,
          groupId,
          properties.name,
          properties.description,
          properties.comment,
          properties.relationId,
          user,
          referenceType = properties.referenceType,
          referenceDay = properties.referenceDay,
          referenceFilename = properties.referenceFilename,
          referenceDistance = 0L,
          deviationDistance = 0L,
          deviationCount = 0L,
          osmWayCount = 0L,
          osmDistance = 0L,
          osmSegmentCount = 0L,
          happy = false,
          relation = None //  TODO monitorRouteRelationData.map(_.relation)
        )

        context.copy(newRoute = Some(newRoute))
    }
  }
}
