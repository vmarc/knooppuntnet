package kpn.server.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatus
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

case class MonitorUpdateContext(
  update: MonitorRouteUpdate,
  user: String,
  reporter: MonitorUpdateReporter,
  referenceType: String,
  referenceFilename: Option[String],
  referenceGpx: Option[String],
  status: MonitorRouteUpdateStatus = MonitorRouteUpdateStatus(),
  group: Option[MonitorGroup] = None,
  oldRoute: Option[MonitorRoute] = None,
  newRoute: Option[MonitorRoute] = None,
  removeOldReferences: Boolean = false,
  singleRoute: Boolean = false,
  oldReferences: Seq[MonitorRouteReference] = Seq.empty,
  newReferences: Seq[MonitorRouteReference] = Seq.empty,
  oldStates: Seq[MonitorRouteState] = Seq.empty,
  newStates: Seq[MonitorRouteState] = Seq.empty,
  saveResult: MonitorRouteSaveResult = MonitorRouteSaveResult(), // TODO should eliminate saveResult in favor of using reporter
) {

  def withStatus(newStatus: MonitorRouteUpdateStatus): MonitorUpdateContext = {
    reporter.report(newStatus)
    copy(status = newStatus)
  }

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

  def relationId: Option[Long] = {
    oldRoute match {
      case Some(route) => route.relationId
      case None =>
        newRoute match {
          case Some(route) => route.relationId
          case None => throw new RuntimeException("could not determine relationId")
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

  def isRouteChanged(): Boolean = {
    oldRoute match {
      case None => false
      case Some(route) =>
        update.newGroupName.nonEmpty ||
          update.newRouteName.nonEmpty ||
          !update.description.contains(route.description) ||
          route.comment != update.comment ||
          route.relationId != update.relationId ||
          route.referenceType != update.referenceType ||
          route.referenceTimestamp != update.referenceTimestamp ||
          route.referenceFilename != update.referenceFilename
    }
  }

  def isReferenceChanged(): Boolean = {
    oldRoute match {
      case None => false
      case Some(route) =>
        if (route.referenceType != update.referenceType) {
          true
        }
        else {
          if (referenceType == "osm") {
            route.relationId != update.relationId ||
              route.referenceTimestamp != update.referenceTimestamp
          }
          else if (referenceType == "gpx") {
            update.referenceGpx.nonEmpty
          }
          else {
            throw new RuntimeException("TODO")
          }
        }
    }
  }
}
