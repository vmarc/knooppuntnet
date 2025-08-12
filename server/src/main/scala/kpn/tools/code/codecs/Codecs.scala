package kpn.tools.code.codecs

import kpn.core.util.Log
import kpn.tools.code.ClassId

object Codecs {
  def main(args: Array[String]): Unit = {
    extraCodecs
      .filterNot(_.packageName.startsWith("kpn.api"))
      .map(_.className)
      .sorted
      .foreach(println)
  }

  val log: Log = Log(classOf[Codecs])
  val OutputDir = "src/main/scala/kpn/tools/code/codecs/generated"

  val customCodecs: Seq[ClassId] = Seq(
    ClassId("Day", "kpn.api.custom"),
    ClassId("Tag", "kpn.api.custom"),
    ClassId("Timestamp", "kpn.api.custom"),
  )

  val extraCodecs: Seq[ClassId] = Seq(
    ClassId("Distance", "kpn.server.repository"),
    ClassId("Member", "kpn.api.common.data"),
    ClassId("CountryResult", "kpn.database.base"),
    ClassId("RouteInfo", "kpn.api.common.route"),
    ClassId("ChangeSetSummary", "kpn.api.common"),
    ClassId("LocationChangeSet", "kpn.api.common"),
    ClassId("BaseRouteChange", "kpn.api.common.changes.details"),
    ClassId("NetworkChange", "kpn.api.common.changes.details"),
    ClassId("NodeChange", "kpn.api.common.changes.details"),
    ClassId("RouteChange", "kpn.api.common.changes.details"),
    ClassId("Ref", "kpn.api.common.common"),
    ClassId("Reference", "kpn.api.common.common"),
    ClassId("Ids", "kpn.api.common.location"),
    ClassId("LocationFact", "kpn.api.common.location"),
    ClassId("MonitorRouteDetail", "kpn.api.common.monitor"),
    ClassId("MonitorRouteDeviationInfo", "kpn.api.common.monitor"),
    ClassId("MonitorRouteRelationInfo", "kpn.api.common.monitor"),
    ClassId("LocationPoiInfo", "kpn.api.common.poi"),
    ClassId("LocationPoiLayerCount", "kpn.api.common.poi"),
    ClassId("Poi", "kpn.api.common.poi"),
    ClassId("RouteSearchResult", "kpn.api.common.search"),
    ClassId("StatisticValue", "kpn.api.common.statistics"),
    ClassId("NetworkFactRefs", "kpn.api.common.subset"),
    ClassId("SubsetMapNetwork", "kpn.api.common.subset"),
    ClassId("LocationNodeCount", "kpn.core.doc"),
    ClassId("NetworkDoc", "kpn.core.doc"),
    ClassId("NetworkRouteDetail", "kpn.core.doc"),
    ClassId("NodeDoc", "kpn.core.doc"),
    ClassId("NodeRouteRef", "kpn.core.doc"),
    ClassId("OrphanNodeDoc", "kpn.core.doc"),
    ClassId("OrphanRouteDoc", "kpn.core.doc"),
    ClassId("ParentRouteData", "kpn.core.doc"),
    ClassId("RawNetworkDoc", "kpn.core.doc"),
    ClassId("RawNodeDoc", "kpn.core.doc"),
    ClassId("SuperSubSegmentInfo", "kpn.core.doc"),
    ClassId("ApiActionDoc", "kpn.core.metrics"),
    ClassId("PoiInfo", "kpn.core.poi"),
    ClassId("OsmSegments", "kpn.core.tools.monitor.support"),
    ClassId("MembersDoc", "kpn.core.tools.next.support"),
    ClassId("TagCount", "kpn.core.tools.next.support"),
    ClassId("NodeImageTag", "kpn.core.tools.support"),
    ClassId("SpecialNode", "kpn.core.tools.support"),
    ClassId("RouteWithoutLocation", "kpn.core.tools.support.location"),
    ClassId("RouteGraphEdge", "kpn.database.actions.graph"),
    ClassId("LocationNodeInfoDoc", "kpn.database.actions.locations"),
    ClassId("LocationQueryResult", "kpn.database.actions.locations"),
    ClassId("LocationRouteInfoData", "kpn.database.actions.locations"),
    ClassId("NodeFilterOptionQueryResult", "kpn.database.actions.locations"),
    ClassId("RouteFilterOptionQueryResult", "kpn.database.actions.locations"),
    ClassId("MonitorGroupRouteInfoData", "kpn.database.actions.monitor"),
    ClassId("SearchQueryResult", "kpn.database.actions.routes"),
    ClassId("ChangeSetCount", "kpn.database.actions.statistics"),
    ClassId("ChangeSetCounts", "kpn.database.actions.statistics"),
    ClassId("ChangeSetRef", "kpn.database.actions.statistics"),
    ClassId("StatisticLongValues", "kpn.database.actions.statistics"),
    ClassId("CountResult", "kpn.database.base"),
    ClassId("Id", "kpn.database.base"),
    ClassId("NameRow", "kpn.database.base"),
    ClassId("ObjectIdId", "kpn.database.base"),
    ClassId("StringId", "kpn.database.base"),
    ClassId("NodeWithLongName", "kpn.database.tools"),
    ClassId("Period", "kpn.database.tools"),
    ClassId("RouteColourTagValue", "kpn.database.tools"),
    ClassId("RouteTileInfo", "kpn.server.analyzer.engine.analysis.route.domain"),
    ClassId("ReferencedElementIds", "kpn.server.analyzer.engine.changes.changes"),
    ClassId("NodeTileInfo", "kpn.server.analyzer.engine.tiles.domain"),
    ClassId("TileId", "kpn.server.analyzer.engine.tiles.domain"),
    ClassId("MonitorGroupRouteCount", "kpn.server.monitor.domain"),
    ClassId("MonitorReference", "kpn.server.monitor.domain"),
    ClassId("MonitorReferenceTileInfo", "kpn.server.monitor.domain"),
    ClassId("MonitorRouteChange", "kpn.server.monitor.domain"),
    ClassId("MonitorSegment", "kpn.server.monitor.domain"),
    ClassId("MonitorState", "kpn.server.monitor.domain"),
    ClassId("MonitorStateTile", "kpn.server.monitor.domain"),
    ClassId("OldMonitorReference", "kpn.server.monitor.domain"),
    ClassId("MonitorReferenceId", "kpn.server.monitor.repository"),
    ClassId("MonitorRouteCount", "kpn.server.monitor.repository"),
    ClassId("MonitorStateDeviationInfo", "kpn.server.monitor.repository"),
    ClassId("MonitorStateId", "kpn.server.monitor.repository"),
    ClassId("MonitorStateSummary", "kpn.server.monitor.repository"),
    ClassId("MonitorTileData", "kpn.server.monitor.repository"),
    ClassId("NetworkElement", "kpn.server.repository"),
    ClassId("NetworkFactElementIds", "kpn.server.repository"),
    ClassId("StampDoc", "kpn.server.sync"),
    ClassId("ClassId", "kpn.tools.code"),
    ClassId("BoundsResult", "kpn.database.actions.routes"),
  )
}

class Codecs
