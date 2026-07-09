package kpn.api.common.monitor

import kpn.api.custom.Timestamp
import kpn.core.doc.Storable

case class MonitorRouteUpdate(
  action: MonitorAction,
  groupName: String,
  routeName: String,
  referenceType: MonitorReferenceType,
  description: Option[String] = None,
  comment: Option[String] = None,
  relationId: Option[Long] = None,
  referenceTimestamp: Option[Timestamp] = None,
  referenceFilename: Option[String] = None,
  referenceGpx: Option[String] = None,
  migrationGeojson: Option[String] = None,
  newGroupName: Option[String] = None,
  newRouteName: Option[String] = None,
) extends Storable {
  def printable(): MonitorRouteUpdate = {
    referenceGpx match {
      case None => this
      case Some(gpx) =>
        if (gpx.length > 25) {
          copy(referenceGpx = Some(gpx.substring(0, 25)))
        }
        else {
          this
        }
    }
  }
}
