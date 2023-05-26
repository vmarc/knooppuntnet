package kpn.server.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

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

  // *** reference information ***

  /*
    possible values: "osm", "gpx", "multi-gpx"
   */
  referenceType: String,

  /*
    The "referenceDay" is only filled in when the reference type is "osm" or "gpx".
    The "referenceDay" is not filled in for reference type "multi-gpx", even if a file is
    uploaded for the main relation (the reference details will be in the MonitorRelation in
    "structure").
   */
  referenceDay: Option[Day],

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
    The sum the "osmWayCount" values in all MonitorRouteRelation objects (for "multi-gpx", this
    is the total number of ways in the main super relations and all subrelations).
   */
  osmWayCount: Long,
  /*
    The sum the "osmDistance" values in all MonitorRouteRelation objects (for "multi-gpx", this
    is the total sum of the lengths of all ways in the main super relations and all subrelations).
   */
  osmDistance: Long,

  /*
    Number of osm segments.
    For "multi-gpx" this is not just the sum of the "osmSegmentCount" values in all MonitorRouteRelation
    objects but the result of the analysis in MonitorRouteOsmSegmentBuilder which looks at segments
    across sub relations.
   */
  osmSegmentCount: Long,

  /*
    Summary information of osm segments across subrelations.
    Result of the analysis in MonitorRouteOsmSegmentBuilder.

    One MonitorRouteOsmSegment can be composed of multiple MonitorRouteOsmSegmentElement. The
    MonitorRouteOsmSegmentElement's can point osm segments in multiple subrelations. The actual
    segment geometries are in the MonitorRouteSegment objects in the MonitorRouteState objects
    of the subrelations.
   */
  osmSegments: Seq[MonitorRouteOsmSegment],

  /*
    The structure of the route. Value "None" if the relationId has not been defined yet.
    A single MonitorRouteRelation object when reference type is "osm" or "gpx".
    For reference type "multi-gpx" there is a tree of MonitorRouteRelation objects
    with the super route at the top of the tree.
   */
  relation: Option[MonitorRouteRelation],

  /*
    Overall route status. True if "happy" is true in the entire route structure (all MonitorRouteRelation objects).
   */
  happy: Boolean,
) extends WithObjectId {

  def isSuperRoute: Boolean = relation match {
    case None => throw new RuntimeException("could not determine wether super route yet")
    case Some(monitorRouteRelation) => monitorRouteRelation.relations.nonEmpty
  }
}
