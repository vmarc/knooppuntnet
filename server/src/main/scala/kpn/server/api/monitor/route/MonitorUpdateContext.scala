package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.server.api.monitor.domain.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteRelationState

case class MonitorUpdateContext(
  group: MonitorGroup,
  oldRoute: Option[MonitorRoute] = None,
  newRoute: Option[MonitorRoute] = None,
  oldReferences: Seq[MonitorRouteReference] = Seq.empty,
  newReferences: Seq[MonitorRouteReference] = Seq.empty,
  oldStates: Seq[MonitorRouteRelationState] = Seq.empty,
  newStates: Seq[MonitorRouteRelationState] = Seq.empty,
  saveResult: MonitorRouteSaveResult = MonitorRouteSaveResult()
) {

  def routeId: ObjectId = {
    oldRoute match {
      case Some(route) => route._id
      case None =>
        newRoute match {
          case Some(route) => route._id
          case None => throw new RuntimeException("could not determine routeId")
        }
    }
  }
}
