package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.server.api.monitor.domain.MonitorRoute
import org.springframework.stereotype.Component

@Component
class MonitorUpdateRouteImpl extends MonitorUpdateRoute {

  override def update(
    context: MonitorUpdateContext,
    routeId: ObjectId,
    user: String,
    properties: MonitorRouteProperties
  ): MonitorUpdateContext = {

    val newRoute = MonitorRoute(
      routeId,
      context.group._id,
      properties.name,
      properties.description,
      properties.comment,
      properties.relationId,
      user,
      referenceType = properties.referenceType,
      referenceDay = properties.referenceDay,
      referenceFilename = properties.referenceFilename,
      referenceDistance = 0,
      deviationDistance = 0,
      deviationCount = 0,
      osmWayCount = 0,
      osmDistance = 0,
      osmSegmentCount = 0,
      happy = false,
      relation = None //  TODO monitorRouteRelationData.map(_.relation)
    )

    context.copy(newRoute = Some(newRoute))
  }
}
