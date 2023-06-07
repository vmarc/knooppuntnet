package kpn.server.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.common.Time
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorUpdateRouteImpl(
  monitorGroupRepository: MonitorGroupRepository
) extends MonitorUpdateRoute {

  override def update(
    context: MonitorUpdateContext,
    user: String,
    update: MonitorRouteUpdate
  ): MonitorUpdateContext = {

    val groupId = determineGroupId(context, update)

    context.oldRoute match {
      case Some(oldRoute) =>
        if (isRouteChanged(oldRoute, update, groupId)) {
          context.copy(
            newRoute = Some(
              oldRoute.copy(
                groupId = groupId,
                name = update.newRouteName.getOrElse(oldRoute.name),
                description = update.description.get, // TODO make more safe!!
                comment = update.comment,
                relationId = update.relationId,
                user = user,
                timestamp = Time.now,
                referenceType = update.referenceType,
                referenceTimestamp = update.referenceTimestamp,
                referenceFilename = update.referenceFilename,
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
              update.routeName,
              update.description.get, // TODO make more safe!
              update.comment,
              update.relationId,
              user,
              Time.now,
              referenceType = update.referenceType,
              referenceTimestamp = update.referenceTimestamp,
              referenceFilename = update.referenceFilename,
              referenceDistance = 0L,
              deviationDistance = 0L,
              deviationCount = 0L,
              osmWayCount = 0L,
              osmDistance = 0L,
              osmSegmentCount = 0L,
              happy = false,
              osmSegments = Seq.empty,
              relation = None
            )
          )
        )
    }
  }

  private def isRouteChanged(oldRoute: MonitorRoute, update: MonitorRouteUpdate, groupId: ObjectId): Boolean = {
    update.newRouteName.nonEmpty ||
      oldRoute.groupId != groupId ||
      !update.description.contains(oldRoute.description) ||
      oldRoute.comment != update.comment ||
      oldRoute.relationId != update.relationId ||
      oldRoute.referenceType != update.referenceType ||
      oldRoute.referenceTimestamp != update.referenceTimestamp ||
      oldRoute.referenceFilename != update.referenceFilename
  }

  private def determineGroupId(context: MonitorUpdateContext, update: MonitorRouteUpdate): ObjectId = {
    update.newGroupName match {
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
