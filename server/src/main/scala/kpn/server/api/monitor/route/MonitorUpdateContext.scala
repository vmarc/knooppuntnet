package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.server.api.monitor.domain.{MonitorGroup, MonitorRoute, MonitorRouteReference, MonitorRouteState}

case class MonitorUpdateContext(
  group: MonitorGroup,
  referenceType: String,
  oldRoute: Option[MonitorRoute] = None,
  newRoute: Option[MonitorRoute] = None,
  oldReferences: Seq[MonitorRouteReference] = Seq.empty,
  newReferences: Seq[MonitorRouteReference] = Seq.empty,
  oldStates: Seq[MonitorRouteState] = Seq.empty,
  newStates: Seq[MonitorRouteState] = Seq.empty,
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

  def route: MonitorRoute = {
    newRoute match {
      case Some(route) => route
      case None =>
        oldRoute match {
          case Some(route) => route
          case None => throw new RuntimeException("could not determine route")
        }
    }
  }
}
