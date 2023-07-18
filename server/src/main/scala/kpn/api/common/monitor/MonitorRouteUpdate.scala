package kpn.api.common.monitor

import kpn.api.custom.Timestamp

case class MonitorRouteUpdate(
  action: String, // "add", "update", "gpx-upload", "gpx-delete"
  groupName: String,
  routeName: String,
  referenceType: String, // "osm" | "gpx" | "multi-gpx"
  description: Option[String] = None,
  comment: Option[String] = None,
  relationId: Option[Long] = None,
  referenceTimestamp: Option[Timestamp] = None,
  referenceFilename: Option[String] = None,
  referenceGpx: Option[String] = None,
  migrationGeojson: Option[String] = None,
  newGroupName: Option[String] = None,
  newRouteName: Option[String] = None,
)
