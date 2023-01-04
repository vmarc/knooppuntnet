package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.core.common.Time
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.repository.MonitorGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorUpdateRouteImpl(
  monitorGroupRepository: MonitorGroupRepository
) extends MonitorUpdateRoute {

  override def update(
    context: MonitorUpdateContext,
    user: String,
    properties: MonitorRouteProperties
  ): MonitorUpdateContext = {

    val groupId = determineGroupId(context, properties)

    context.oldRoute match {
      case Some(oldRoute) =>
        if (isRouteChanged(oldRoute, properties, groupId)) {
          context.copy(
            newRoute = Some(
              oldRoute.copy(
                groupId = groupId,
                name = properties.name,
                description = properties.description,
                comment = properties.comment,
                relationId = properties.relationId,
                user = user,
                timestamp = Time.now,
                referenceType = properties.referenceType,
                referenceDay = properties.referenceDay,
                referenceFilename = properties.referenceFilename,
              )
            )
          )
        }
        else {
          context
        }

      case None =>

        context.copy(
          newRoute = Some(
            MonitorRoute(
              ObjectId(),
              groupId,
              properties.name,
              properties.description,
              properties.comment,
              properties.relationId,
              user,
              Time.now,
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
              relation = None
            )
          )
        )
    }
  }

  private def isRouteChanged(oldRoute: MonitorRoute, properties: MonitorRouteProperties, groupId: ObjectId): Boolean = {
    oldRoute.groupId != groupId ||
      oldRoute.name != properties.name ||
      oldRoute.description != properties.description ||
      oldRoute.comment != properties.comment ||
      oldRoute.relationId != properties.relationId ||
      oldRoute.referenceType != properties.referenceType ||
      oldRoute.referenceDay != properties.referenceDay ||
      oldRoute.referenceFilename != properties.referenceFilename
  }

  private def determineGroupId(context: MonitorUpdateContext, properties: MonitorRouteProperties): ObjectId = {
    if (context.group.name != properties.groupName) {
      monitorGroupRepository.groupByName(properties.groupName).map(_._id) match {
        case Some(id) => id
        case None =>
          throw new IllegalArgumentException(
            s"""Could not find group with name "${properties.groupName}""""
          )
      }
    }
    else {
      context.group._id
    }
  }
}
