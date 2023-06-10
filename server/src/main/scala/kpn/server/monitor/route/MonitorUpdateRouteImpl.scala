package kpn.server.monitor.route

import kpn.api.base.ObjectId
import kpn.core.common.Time
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorUpdateRouteImpl(
  monitorGroupRepository: MonitorGroupRepository
) extends MonitorUpdateRoute {

  override def update(context: MonitorUpdateContext): MonitorUpdateContext = {

    val groupId = determineGroupId(context)

    context.oldRoute match {
      case Some(oldRoute) =>
        if (context.isRouteChanged()) {
          context.copy(
            newRoute = Some(
              oldRoute.copy(
                groupId = groupId,
                name = context.update.newRouteName.getOrElse(oldRoute.name),
                description = context.update.description.get, // TODO make more safe!!
                comment = context.update.comment,
                relationId = context.update.relationId,
                user = context.user,
                timestamp = Time.now,
                referenceType = context.update.referenceType,
                referenceTimestamp = context.update.referenceTimestamp,
                referenceFilename = context.update.referenceFilename,
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
              context.update.routeName,
              context.update.description.getOrElse(""),
              context.update.comment,
              context.update.relationId,
              context.user,
              Time.now,
              referenceType = context.update.referenceType,
              referenceTimestamp = context.update.referenceTimestamp,
              referenceFilename = context.update.referenceFilename,
              referenceDistance = 0,
              deviationDistance = 0,
              deviationCount = 0,
              osmWayCount = 0,
              osmDistance = 0,
              osmSegmentCount = 0,
              happy = false,
              osmSegments = Seq.empty,
              relation = None
            )
          )
        )
    }
  }

  private def determineGroupId(context: MonitorUpdateContext): ObjectId = {
    context.update.newGroupName match {
      case None => context.group.get._id
      case Some(newGroupName) =>
        monitorGroupRepository.groupByName(newGroupName).map(_._id) match {
          case Some(id) => id
          case None =>
            throw new IllegalArgumentException(
              s"""Could not find group with name "${newGroupName}""""
            )
        }
    }
  }
}
