package kpn.api.common.monitor;

import kpn.api.common.monitor.MonitorAction;
import kpn.api.common.monitor.MonitorReferenceType;
import kpn.api.custom.Timestamp;

import java.util.Optional;

public record MonitorRouteUpdate(
  MonitorAction action,
  String groupName,
  String routeName,
  MonitorReferenceType referenceType,
  Optional<String> description,
  Optional<String> comment,
  Optional<Long> relationId,
  Optional<Timestamp> referenceTimestamp,
  Optional<String> referenceFilename,
  Optional<String> referenceGpx,
  Optional<String> migrationGeojson,
  Optional<String> newGroupName,
  Optional<String> newRouteName
) {
}

/*
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

*/
