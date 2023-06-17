package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatus
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReferenceSummary

case class MonitorUpdateContext(
  user: String,
  reporter: MonitorUpdateReporter,
  update: MonitorRouteUpdate,
  status: MonitorRouteUpdateStatus = MonitorRouteUpdateStatus(),
  group: Option[MonitorGroup] = None,
  oldRoute: Option[MonitorRoute] = None,
  newRoute: Option[MonitorRoute] = None,
  newReferenceSummaries: Seq[MonitorRouteReferenceSummary] = Seq.empty,
  stateChanged: Boolean = false
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
          if (update.referenceType == "osm") {
            route.relationId != update.relationId ||
              route.referenceTimestamp != update.referenceTimestamp
          }
          else if (update.referenceType == "gpx") {
            update.referenceGpx.nonEmpty
          }
          else {
            throw new RuntimeException("TODO")
          }
        }
    }
  }
}
