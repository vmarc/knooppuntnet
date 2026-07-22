package kpn.server.monitor.domain

import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.custom.Timestamp
import kpn.api.id.WithObjectId
import org.bson.types.ObjectId

case class MonitorRoute(
  _id: ObjectId,
  groupId: ObjectId,
  name: String,
  description: String,
  comment: Option[String],
  /*
    The "relationId" contains the osm id of the route relation (the main super relation if "multi-gpx").
    The value can be "None" if the relation id was not defined yet by the user.
   */
  relationId: Option[Long],
  user: String,
  timestamp: Timestamp,
  symbol: Option[String],

  analysisTimestamp: Option[Timestamp],
  analysisDuration: Option[Long],

  // *** reference information ***

  referenceType: MonitorReferenceType,

  /*
    The "referenceTimestamp" is only filled in when the reference type is "osm" or "gpx".
    The "referenceTimestamp" is not filled in for reference type "multi-gpx", even if a file is
    uploaded for the main relation (the reference details will be in the MonitorRelation in
    "structure").
   */
  referenceTimestamp: Option[Timestamp],

  /*
    The "referenceFilename" is only filled in when the reference type is "gpx".
    The "referenceFilename" is not filled in for reference type "multi-gpx", even if a file is
    uploaded for the main relation (the reference details will be in the MonitorRelation in
    "structure").
   */
  referenceFilename: Option[String], // filled in when "gpx", not for "multi-gpx"

  /*
    The "referenceDistance" when the reference type is "osm" or "gpx". For reference type
    "muti-gpx", this is the sum of the "referenceDistance" values in all MonitorRelation
    objects in "structure".
   */
  referenceDistance: Long,

  // *** analysis results ***

  /*
    The sum the "distance" values in all MonitorRouteDeviation objects (for "multi-gpx", these
    are all deviations in all sub relations and in the main relation if it has ways).
   */
  deviationDistance: Long,

  /*
    The total number of MonitorRouteDeviation objects (for "multi-gpx", these are all deviations
    in all sub relations and in the main relation if it has ways).
   */
  deviationCount: Long,

  /*
    Number of osm segments.
    For "multi-gpx" this is not just the sum of the "osmSegmentCount" values in all MonitorRouteRelation
    objects but the result of the analysis in MonitorRouteOsmSegmentBuilder which looks at segments
    across sub relations.
   */
  osmSegmentCount: Long,

  osmDistance: Long,

  relationIds: Seq[Long],

  bounds: Option[Bounds],
  /*
    Overall route status. True if "happy" is true in the entire route structure (all MonitorRouteRelation objects).
   */
  happy: Boolean,
) extends WithObjectId
