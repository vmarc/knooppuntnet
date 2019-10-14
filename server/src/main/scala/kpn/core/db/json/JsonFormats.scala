package kpn.core.db.json

import java.util.NoSuchElementException

import kpn.server.analyzer.engine.changes.changes.ElementIds
import kpn.core.db.BlackListDoc
import kpn.core.db.Change
import kpn.core.db.ChangeRevision
import kpn.core.db.Changes
import kpn.core.db.GpxDoc
import kpn.core.db.NetworkDoc
import kpn.core.db.NodeDoc
import kpn.core.db.ReviewDoc
import kpn.core.db.RouteDoc
import kpn.core.db.StringValueDoc
import kpn.core.db.TileDoc
import kpn.core.db.TimestampDoc
import kpn.core.db.TimestampsDoc
import kpn.core.db.couch.DesignDoc
import kpn.core.db.couch.ViewDoc
import kpn.server.analyzer.engine.analysis.location.LocationTree
import kpn.server.analyzer.engine.changes.ElementIdMap
import kpn.server.analyzer.engine.changes.data.AnalysisData
import kpn.server.analyzer.engine.changes.data.AnalysisDataDetail
import kpn.server.analyzer.engine.changes.data.AnalysisDataOrphanNodes
import kpn.server.analyzer.engine.changes.data.BlackList
import kpn.server.analyzer.engine.changes.data.BlackListEntry
import kpn.server.analyzer.engine.changes.data.OrphanNodesData
import kpn.core.gpx.GpxFile
import kpn.core.gpx.GpxSegment
import kpn.core.gpx.WayPoint
import kpn.core.poi.PoiDoc
import kpn.core.poi.PoiInfo
import kpn.shared.Bounds
import kpn.shared.ChangeSetElementRef
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetNetwork
import kpn.shared.ChangeSetSubsetAnalysis
import kpn.shared.ChangeSetSubsetElementRefs
import kpn.shared.ChangeSetSummary
import kpn.shared.ChangeSetSummaryDoc
import kpn.shared.ChangeSetSummaryInfo
import kpn.shared.ChangesPage
import kpn.shared.Check
import kpn.shared.Fact
import kpn.shared.FactCount
import kpn.shared.FactLevel
import kpn.shared.LatLonImpl
import kpn.shared.NetworkChanges
import kpn.shared.NetworkExtraMemberNode
import kpn.shared.NetworkExtraMemberRelation
import kpn.shared.NetworkExtraMemberWay
import kpn.shared.NetworkFact
import kpn.shared.NetworkFacts
import kpn.shared.NetworkIntegrityCheck
import kpn.shared.NetworkIntegrityCheckFailed
import kpn.shared.NetworkNameMissing
import kpn.shared.Location
import kpn.shared.LocationCandidate
import kpn.shared.RouteLocationAnalysis
import kpn.shared.NodeInfo
import kpn.shared.NodeIntegrityCheck
import kpn.shared.NodeIntegrityCheckChange
import kpn.shared.PageInfo
import kpn.shared.Poi
import kpn.shared.PoiPage
import kpn.shared.LocationPage
import kpn.shared.ReplicationId
import kpn.shared.RouteSummary
import kpn.shared.Subset
import kpn.shared.TimeInfo
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeSetData
import kpn.shared.changes.ChangeSetInfo
import kpn.shared.changes.ChangeSetPage
import kpn.shared.changes.Review
import kpn.shared.changes.details.ChangeKey
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NetworkChangeDoc
import kpn.shared.changes.details.NetworkChangeInfo
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.NodeChangeDoc
import kpn.shared.changes.details.RefBooleanChange
import kpn.shared.changes.details.RefChanges
import kpn.shared.changes.details.RouteChange
import kpn.shared.changes.details.RouteChangeDoc
import kpn.shared.changes.filter.ChangesFilter
import kpn.shared.changes.filter.ChangesFilterPeriod
import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.common.KnownElements
import kpn.shared.common.MapBounds
import kpn.shared.common.NetworkRefs
import kpn.shared.common.Ref
import kpn.shared.common.Reference
import kpn.shared.common.TrackPath
import kpn.shared.common.TrackPoint
import kpn.shared.common.TrackSegment
import kpn.shared.common.TrackSegmentFragment
import kpn.shared.data.MetaData
import kpn.shared.data.Tag
import kpn.shared.data.raw.RawData
import kpn.shared.data.raw.RawMember
import kpn.shared.data.raw.RawNode
import kpn.shared.data.raw.RawRelation
import kpn.shared.data.raw.RawWay
import kpn.shared.diff.IdDiffs
import kpn.shared.diff.NetworkData
import kpn.shared.diff.NetworkDataUpdate
import kpn.shared.diff.NetworkNodeData
import kpn.shared.diff.NetworkNodeUpdate
import kpn.shared.diff.NodeData
import kpn.shared.diff.NodeUpdate
import kpn.shared.diff.RefDiffs
import kpn.shared.diff.RouteData
import kpn.shared.diff.TagDetail
import kpn.shared.diff.TagDetailType
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.WayData
import kpn.shared.diff.WayInfo
import kpn.shared.diff.WayUpdate
import kpn.shared.diff.common.FactDiffs
import kpn.shared.diff.network.NetworkNodeDiff
import kpn.shared.diff.network.NodeIntegrityCheckDiff
import kpn.shared.diff.network.NodeRouteReferenceDiffs
import kpn.shared.diff.node.NodeMoved
import kpn.shared.diff.route.RouteDiff
import kpn.shared.diff.route.RouteNameDiff
import kpn.shared.diff.route.RouteNodeDiff
import kpn.shared.diff.route.RouteRoleDiff
import kpn.shared.network.Integrity
import kpn.shared.network.NetworkAttributes
import kpn.shared.network.NetworkChangesPage
import kpn.shared.network.NetworkDetailsPage
import kpn.shared.network.NetworkFactsPage
import kpn.shared.network.NetworkInfo
import kpn.shared.network.NetworkInfoDetail
import kpn.shared.network.NetworkMapInfo
import kpn.shared.network.NetworkMapPage
import kpn.shared.network.NetworkNodeFact
import kpn.shared.network.NetworkNodeInfo2
import kpn.shared.network.NetworkNodesPage
import kpn.shared.network.NetworkRouteFact
import kpn.shared.network.NetworkRouteInfo
import kpn.shared.network.NetworkRouteRow
import kpn.shared.network.NetworkRoutesPage
import kpn.shared.network.NetworkShape
import kpn.shared.network.NetworkSummary
import kpn.shared.network.OldNetworkFactsPage
import kpn.shared.node.MapDetailNode
import kpn.shared.node.NodeChangeInfo
import kpn.shared.node.NodeChangeInfos
import kpn.shared.node.NodeChangesPage
import kpn.shared.node.NodeDetailsPage
import kpn.shared.node.NodeMapPage
import kpn.shared.node.NodeNetworkIntegrityCheck
import kpn.shared.node.NodeNetworkReference
import kpn.shared.node.NodeNetworkRouteReference
import kpn.shared.node.NodeOrphanRouteReference
import kpn.shared.node.NodeReferences
import kpn.shared.planner.RouteLeg
import kpn.shared.planner.RouteLegFragment
import kpn.shared.planner.RouteLegNode
import kpn.shared.planner.RouteLegRoute
import kpn.shared.planner.RouteLegSegment
import kpn.shared.route.GeometryDiff
import kpn.shared.route.MapDetailRoute
import kpn.shared.route.PointSegment
import kpn.shared.route.RouteChangeInfo
import kpn.shared.route.RouteChangeInfos
import kpn.shared.route.RouteChangesPage
import kpn.shared.route.RouteDetailsPage
import kpn.shared.route.RouteInfo
import kpn.shared.route.RouteInfoAnalysis
import kpn.shared.route.RouteMap
import kpn.shared.route.RouteMapPage
import kpn.shared.route.RouteMemberInfo
import kpn.shared.route.RouteNetworkNodeInfo
import kpn.shared.route.RouteReferences
import kpn.shared.route.WayGeometry
import kpn.shared.statistics.CountryStatistic
import kpn.shared.statistics.Statistic
import kpn.shared.statistics.Statistics
import kpn.shared.subset.NetworkFactRefs
import kpn.shared.subset.SubsetChangesPage
import kpn.shared.subset.SubsetFactDetailsPage
import kpn.shared.subset.SubsetFactsPage
import kpn.shared.subset.SubsetInfo
import kpn.shared.subset.SubsetNetworksPage
import kpn.shared.subset.SubsetOrphanNodesPage
import kpn.shared.subset.SubsetOrphanRoutesPage
import kpn.shared.tiles.ClientPoiConfiguration
import kpn.shared.tiles.ClientPoiDefinition
import kpn.shared.tiles.ClientPoiGroupDefinition
import spray.json.DefaultJsonProtocol
import spray.json.JsString
import spray.json.JsValue
import spray.json.RootJsonFormat
import spray.json._

object JsonFormats extends DefaultJsonProtocol {

  private def jsonEnum[T <: Enumeration](enumeration: T) = new JsonFormat[T#Value] {

    def write(obj: T#Value) = JsString(obj.toString)

    def read(json: JsValue): T#Value = json match {
      case JsString(string) =>
        try {
          enumeration.withName(string)
        }
        catch {
          case e: NoSuchElementException =>
            throw DeserializationException(s"Expected a value from enum $enumeration instead of '$string'")
        }
      case something => throw DeserializationException(s"Expected a value from enum $enumeration instead of $something")
    }
  }

  implicit val wayDirectionFormat = WayDirectionFormat

  implicit val countryOptionFormat = CountryOptionFormat
  implicit val countryFormat = CountryFormat

  implicit val networkTypeFormat = NetworkTypeFormat

  implicit val subsetFormat = SubsetFormat

  implicit val timestampFormat = TimestampFormat

  implicit val routeNetworkNodeInfoFormat: RootJsonFormat[RouteNetworkNodeInfo] = jsonFormat5(RouteNetworkNodeInfo)

  implicit val tagsFormat = TagsFormat
  implicit val routeMemberInfoFormat: RootJsonFormat[RouteMemberInfo] = jsonFormat17(RouteMemberInfo)

  implicit val trackPointFormat: RootJsonFormat[TrackPoint] = jsonFormat2(TrackPoint)

  implicit val trackSegmentFragmentFormat: RootJsonFormat[TrackSegmentFragment] = jsonFormat4(TrackSegmentFragment)
  implicit val trackSegmentFormat: RootJsonFormat[TrackSegment] = jsonFormat3(TrackSegment)
  implicit val trackPathFormat: RootJsonFormat[TrackPath] = jsonFormat4(TrackPath)
  implicit val mapBoundsFormat: RootJsonFormat[MapBounds] = jsonFormat4(MapBounds.apply)

  implicit val routeMapFormat: RootJsonFormat[RouteMap] = jsonFormat14(RouteMap)

  implicit val factFormat = FactFormat

  implicit val latLonImplFormat: RootJsonFormat[LatLonImpl] = jsonFormat2(LatLonImpl.apply)

  implicit val locationFormat: RootJsonFormat[Location] = jsonFormat1(Location)
  implicit val locationCandidateFormat: RootJsonFormat[LocationCandidate] = jsonFormat2(LocationCandidate)
  implicit val routeLocationAnalysisFormat: RootJsonFormat[RouteLocationAnalysis] = jsonFormat2(RouteLocationAnalysis)
  implicit val routeInfoAnalysisFormat: RootJsonFormat[RouteInfoAnalysis] = jsonFormat10(RouteInfoAnalysis)

  implicit val routeSummaryFormat: RootJsonFormat[RouteSummary] = jsonFormat10(RouteSummary)
  implicit val routeInfoFormat: RootJsonFormat[RouteInfo] = jsonFormat9(RouteInfo)

  implicit val routeDocFormat: RootJsonFormat[RouteDoc] = jsonFormat3(RouteDoc)

  implicit val nodeIntegrityCheckFormat: RootJsonFormat[NodeIntegrityCheck] = jsonFormat5(NodeIntegrityCheck)
  implicit val nodeInfoFormat: RootJsonFormat[NodeInfo] = jsonFormat17(NodeInfo)
  implicit val nodeDocFormat: RootJsonFormat[NodeDoc] = jsonFormat3(NodeDoc)

  implicit val integrityFormat: RootJsonFormat[Integrity] = jsonFormat8(Integrity)

  implicit val refFormat: RootJsonFormat[Ref] = jsonFormat2(Ref)
  implicit val networkNodeInfo2Format: RootJsonFormat[NetworkNodeInfo2] = jsonFormat14(NetworkNodeInfo2)
  implicit val networkAttributesFormat: RootJsonFormat[NetworkAttributes] = jsonFormat16(NetworkAttributes)

  implicit val networkNameMissingFormat: RootJsonFormat[NetworkNameMissing] = jsonFormat0(NetworkNameMissing)
  implicit val networkExtraMemberWayFormat: RootJsonFormat[NetworkExtraMemberWay] = jsonFormat1(NetworkExtraMemberWay)
  implicit val networkExtraMemberNodeFormat: RootJsonFormat[NetworkExtraMemberNode] = jsonFormat1(NetworkExtraMemberNode)
  implicit val networkExtraMemberRelationFormat: RootJsonFormat[NetworkExtraMemberRelation] = jsonFormat1(NetworkExtraMemberRelation)
  implicit val networkIntegrityCheckFormat: RootJsonFormat[NetworkIntegrityCheck] = jsonFormat2(NetworkIntegrityCheck)
  implicit val networkIntegrityCheckFailedFormat: RootJsonFormat[NetworkIntegrityCheckFailed] = jsonFormat2(NetworkIntegrityCheckFailed)
  implicit val networkFactsFormat: RootJsonFormat[NetworkFacts] = jsonFormat6(NetworkFacts)
  implicit val networkRouteInfoFormat: RootJsonFormat[NetworkRouteInfo] = jsonFormat8(NetworkRouteInfo)

  implicit val boundsFormat: RootJsonFormat[Bounds] = jsonFormat4(Bounds.apply)
  implicit val networkShapeFormat: RootJsonFormat[NetworkShape] = jsonFormat2(NetworkShape)

  implicit val networkInfoDetailFormat: RootJsonFormat[NetworkInfoDetail] = jsonFormat4(NetworkInfoDetail)
  implicit val networkInfoFormat: RootJsonFormat[NetworkInfo] = jsonFormat8(NetworkInfo)

  implicit val networkDocFormat: RootJsonFormat[NetworkDoc] = jsonFormat3(NetworkDoc)

  implicit val wayPointFormat: RootJsonFormat[WayPoint] = jsonFormat4(WayPoint)
  implicit val gpxSegmentFormat: RootJsonFormat[GpxSegment] = jsonFormat1(GpxSegment)
  implicit val gpxFileFormat: RootJsonFormat[GpxFile] = jsonFormat4(GpxFile)
  implicit val gpxDocFormat: RootJsonFormat[GpxDoc] = jsonFormat3(GpxDoc)

  implicit val timestampDocFormat: RootJsonFormat[TimestampDoc] = jsonFormat3(TimestampDoc)
  implicit val timestampsDocFormat: RootJsonFormat[TimestampsDoc] = jsonFormat3(TimestampsDoc)

  implicit val viewDocFormat: RootJsonFormat[ViewDoc] = jsonFormat2(ViewDoc)
  implicit val designDocFormat: RootJsonFormat[DesignDoc] = jsonFormat4(DesignDoc)

  implicit val stringValueDocFormat: RootJsonFormat[StringValueDoc] = jsonFormat3(StringValueDoc)
  implicit val tileDocFormat: RootJsonFormat[TileDoc] = jsonFormat3(TileDoc)

  implicit val nodeMovedFormat: RootJsonFormat[NodeMoved] = jsonFormat3(NodeMoved)

  implicit val routeFactsDiffsFormat: RootJsonFormat[FactDiffs] = jsonFormat3(FactDiffs)
  implicit val routeNameDiffFormat: RootJsonFormat[RouteNameDiff] = jsonFormat2(RouteNameDiff)

  implicit val tagDetailTypeFormat: RootJsonFormat[TagDetailType] = jsonFormat1(TagDetailType.apply)
  implicit val tagDetailFormat: RootJsonFormat[TagDetail] = jsonFormat4(TagDetail)
  implicit val tagsDiffFormat: RootJsonFormat[TagDiffs] = jsonFormat2(TagDiffs)

  implicit val metaFormat: RootJsonFormat[MetaData] = jsonFormat3(MetaData)
  implicit val rawNodeFormat: RootJsonFormat[RawNode] = jsonFormat7(RawNode)
  implicit val rawWayFormat: RootJsonFormat[RawWay] = jsonFormat6(RawWay)
  implicit val memberFormat: RootJsonFormat[RawMember] = jsonFormat3(RawMember)
  implicit val rawRelationFormat: RootJsonFormat[RawRelation] = jsonFormat6(RawRelation)
  implicit val replicationIdFormat: RootJsonFormat[ReplicationId] = jsonFormat3(ReplicationId.apply)
  implicit val rawDataFormat: RootJsonFormat[RawData] = jsonFormat4(RawData.apply)

  implicit val changeSetElementRefFormat: RootJsonFormat[ChangeSetElementRef] = jsonFormat4(ChangeSetElementRef)
  implicit val changeSetElementRefsFormat: RootJsonFormat[ChangeSetElementRefs] = jsonFormat3(ChangeSetElementRefs.apply)
  implicit val changeSetSubsetElementRefsFormat: RootJsonFormat[ChangeSetSubsetElementRefs] = jsonFormat2(ChangeSetSubsetElementRefs)

  implicit val changeSetNetworkFormat: RootJsonFormat[ChangeSetNetwork] = jsonFormat8(ChangeSetNetwork)
  implicit val networkChangesFormat: RootJsonFormat[NetworkChanges] = jsonFormat3(NetworkChanges)

  implicit val changeKeyFormat: RootJsonFormat[ChangeKey] = jsonFormat4(ChangeKey)

  implicit val changeSetSubsetAnalysisFormat: RootJsonFormat[ChangeSetSubsetAnalysis] = jsonFormat3(ChangeSetSubsetAnalysis.apply)

  // implicit val changeSetSummaryFormat: RootJsonFormat[ChangeSetSummary] = jsonFormat10(ChangeSetSummary.apply)
  // temporary format that can be removed after all ChangeSetSummary instances in the database include 'happy', 'investigate'
  implicit object ChangeSetSummaryFormat extends RootJsonFormat[ChangeSetSummary] {
    override def write(obj: ChangeSetSummary): JsValue = JsObject(
      "key" -> obj.key.toJson,
      "subsets" -> obj.subsets.toJson,
      "timestampFrom" -> obj.timestampFrom.toJson,
      "timestampUntil" -> obj.timestampUntil.toJson,
      "networkChanges" -> obj.networkChanges.toJson,
      "orphanRouteChanges" -> obj.orphanRouteChanges.toJson,
      "orphanNodeChanges" -> obj.orphanNodeChanges.toJson,
      "subsetAnalyses" -> obj.subsetAnalyses.toJson,
      "happy" -> obj.happy.toJson,
      "investigate" -> obj.investigate.toJson
    )

    override def read(json: JsValue): ChangeSetSummary = {
      val emptySubsetAnalysis: Seq[ChangeSetSubsetAnalysis] = Seq()
      val fields = json.asJsObject("Invalid Json Object").fields
      ChangeSetSummary(
        fields("key").convertTo[ChangeKey],
        fields("subsets").convertTo[Seq[Subset]],
        fields("timestampFrom").convertTo[Timestamp],
        fields("timestampUntil").convertTo[Timestamp],
        fields("networkChanges").convertTo[NetworkChanges],
        fields("orphanRouteChanges").convertTo[Seq[ChangeSetSubsetElementRefs]],
        fields("orphanNodeChanges").convertTo[Seq[ChangeSetSubsetElementRefs]],
        fields.get("subsetAnalyses").fold(emptySubsetAnalysis)(_.convertTo[Seq[ChangeSetSubsetAnalysis]]),
        fields("happy").convertTo[Boolean],
        fields("investigate").convertTo[Boolean]
      )
    }
  }

  implicit val reviewFormat: RootJsonFormat[Review] = jsonFormat4(Review)
  implicit val reviewDocFormat: RootJsonFormat[ReviewDoc] = jsonFormat3(ReviewDoc)
  implicit val nodeDataFormat: RootJsonFormat[NodeData] = jsonFormat3(NodeData)
  implicit val nodeUpdateFormat: RootJsonFormat[NodeUpdate] = jsonFormat4(NodeUpdate)
  implicit val wayDataFormat: RootJsonFormat[WayData] = jsonFormat2(WayData)
  implicit val wayUpdateFormat: RootJsonFormat[WayUpdate] = jsonFormat8(WayUpdate)
  implicit val networkDataFormat: RootJsonFormat[NetworkData] = jsonFormat2(NetworkData)
  implicit val networkDataUpdateFormat: RootJsonFormat[NetworkDataUpdate] = jsonFormat2(NetworkDataUpdate)

  implicit val nodeRouteReferencesChanged: RootJsonFormat[NodeRouteReferenceDiffs] = jsonFormat3(NodeRouteReferenceDiffs)
  implicit val nodeIntegrityCheckChanged: RootJsonFormat[NodeIntegrityCheckDiff] = jsonFormat2(NodeIntegrityCheckDiff)
  implicit val networkNodeDiffFormat: RootJsonFormat[NetworkNodeDiff] = jsonFormat6(NetworkNodeDiff)

  implicit val routeDataFormat: RootJsonFormat[RouteData] = jsonFormat9(RouteData)

  implicit val routeNodeDiffFormat: RootJsonFormat[RouteNodeDiff] = jsonFormat3(RouteNodeDiff)
  implicit val routeRoleDiffFormat: RootJsonFormat[RouteRoleDiff] = jsonFormat2(RouteRoleDiff)
  implicit val routeDiffFormat: RootJsonFormat[RouteDiff] = jsonFormat6(RouteDiff)
  implicit val idDiffsFormat: RootJsonFormat[IdDiffs] = jsonFormat3(IdDiffs.apply)
  implicit val refChangesFormat: RootJsonFormat[RefChanges] = jsonFormat2(RefChanges.apply)

  implicit val refDiffsFormat: RootJsonFormat[RefDiffs] = jsonFormat3(RefDiffs.apply)
  implicit val changeTypeFormat: RootJsonFormat[ChangeType] = jsonFormat1(ChangeType.apply)
  implicit val networkChangeFormat: RootJsonFormat[NetworkChange] = jsonFormat16(NetworkChange)

  // implicit val routeChangeFormat: RootJsonFormat[RouteChange] = jsonFormat14(RouteChange)
  // temporary format that can be removed after all RouteChange instances in the database include 'happy' and 'investigate'
  implicit object RouteChangeFormat extends RootJsonFormat[RouteChange] {
    override def write(obj: RouteChange): JsValue = JsObject(
      "key" -> obj.key.toJson,
      "changeType" -> obj.changeType.toJson,
      "name" -> obj.name.toJson,
      "addedToNetwork" -> obj.addedToNetwork.toJson,
      "removedFromNetwork" -> obj.removedFromNetwork.toJson,
      "before" -> obj.before.toJson,
      "after" -> obj.after.toJson,
      "removedWays" -> obj.removedWays.toJson,
      "addedWays" -> obj.addedWays.toJson,
      "updatedWays" -> obj.updatedWays.toJson,
      "diffs" -> obj.diffs.toJson,
      "facts" -> obj.facts.toJson,
      "happy" -> obj.happy.toJson,
      "investigate" -> obj.investigate.toJson
    )

    override def read(json: JsValue): RouteChange = {
      val fields = json.asJsObject("Invalid Json Object").fields
      val routeDataNone: Option[RouteData] = None
      RouteChange(
        fields("key").convertTo[ChangeKey],
        fields("changeType").convertTo[ChangeType],
        fields("name").convertTo[String],
        fields("addedToNetwork").convertTo[Seq[Ref]],
        fields("removedFromNetwork").convertTo[Seq[Ref]],
        fields.get("before").fold(routeDataNone)(_.convertTo[Option[RouteData]]),
        fields.get("after").fold(routeDataNone)(_.convertTo[Option[RouteData]]),
        fields("removedWays").convertTo[Seq[RawWay]],
        fields("addedWays").convertTo[Seq[RawWay]],
        fields("updatedWays").convertTo[Seq[WayUpdate]],
        fields("diffs").convertTo[RouteDiff],
        fields("facts").convertTo[Seq[Fact]],
        fields.get("happy").fold(false)(_.convertTo[Boolean]),
        fields.get("investigate").fold(false)(_.convertTo[Boolean])
      )
    }
  }

  implicit val refBooleanChangeFormat: RootJsonFormat[RefBooleanChange] = jsonFormat2(RefBooleanChange)
  implicit val nodeIntegrityCheckChangeFormat: RootJsonFormat[NodeIntegrityCheckChange] = jsonFormat3(NodeIntegrityCheckChange)

  // implicit val nodeChangeFormat: RootJsonFormat[NodeChange] = jsonFormat19(NodeChange)
  // temporary format that can be removed after all NodeChange instances in the database include 'happy' and 'investigate'
  implicit object NodeChangeFormat extends RootJsonFormat[NodeChange] {
    override def write(obj: NodeChange): JsValue = JsObject(
      "key" -> obj.key.toJson,
      "changeType" -> obj.changeType.toJson,
      "subsets" -> obj.subsets.toJson,
      "name" -> obj.name.toJson,
      "before" -> obj.before.toJson,
      "after" -> obj.after.toJson,
      "connectionChanges" -> obj.connectionChanges.toJson,
      "roleConnectionChanges" -> obj.roleConnectionChanges.toJson,
      "definedInNetworkChanges" -> obj.definedInNetworkChanges.toJson,
      "tagDiffs" -> obj.tagDiffs.toJson,
      "nodeMoved" -> obj.nodeMoved.toJson,
      "addedToRoute" -> obj.addedToRoute.toJson,
      "removedFromRoute" -> obj.removedFromRoute.toJson,
      "addedToNetwork" -> obj.addedToNetwork.toJson,
      "removedFromNetwork" -> obj.removedFromNetwork.toJson,
      "factDiffs" -> obj.factDiffs.toJson,
      "facts" -> obj.facts.toJson,
      "happy" -> obj.happy.toJson,
      "investigate" -> obj.investigate.toJson
    )

    override def read(json: JsValue): NodeChange = {
      val fields = json.asJsObject("Invalid Json Object").fields
      val rawNodeNone: Option[RawNode] = None
      val tagDiffsNone: Option[TagDiffs] = None
      val nodeMovedNone: Option[NodeMoved] = None
      NodeChange(
        fields("key").convertTo[ChangeKey],
        fields("changeType").convertTo[ChangeType],
        fields("subsets").convertTo[Seq[Subset]],
        fields("name").convertTo[String],
        fields.get("before").fold(rawNodeNone)(_.convertTo[Option[RawNode]]),
        fields.get("after").fold(rawNodeNone)(_.convertTo[Option[RawNode]]),
        fields("connectionChanges").convertTo[Seq[RefBooleanChange]],
        fields("roleConnectionChanges").convertTo[Seq[RefBooleanChange]],
        fields("definedInNetworkChanges").convertTo[Seq[RefBooleanChange]],
        fields.get("tagDiffs").fold(tagDiffsNone)(_.convertTo[Option[TagDiffs]]),
        fields.get("nodeMoved").fold(nodeMovedNone)(_.convertTo[Option[NodeMoved]]),
        fields("addedToRoute").convertTo[Seq[Ref]],
        fields("removedFromRoute").convertTo[Seq[Ref]],
        fields("addedToNetwork").convertTo[Seq[Ref]],
        fields("removedFromNetwork").convertTo[Seq[Ref]],
        fields("factDiffs").convertTo[FactDiffs],
        fields("facts").convertTo[Seq[Fact]],
        fields.get("happy").fold(false)(_.convertTo[Boolean]),
        fields.get("investigate").fold(false)(_.convertTo[Boolean])
      )
    }
  }

  implicit val changeSetSummaryDocFormat: RootJsonFormat[ChangeSetSummaryDoc] = jsonFormat3(ChangeSetSummaryDoc)

  implicit val networkChangeDocFormat: RootJsonFormat[NetworkChangeDoc] = jsonFormat3(NetworkChangeDoc)
  implicit val routeChangeDocFormat: RootJsonFormat[RouteChangeDoc] = jsonFormat3(RouteChangeDoc)
  implicit val nodeChangeDocFormat: RootJsonFormat[NodeChangeDoc] = jsonFormat3(NodeChangeDoc)

  implicit val changeRevisionFormat: RootJsonFormat[ChangeRevision] = jsonFormat1(ChangeRevision)
  implicit val changeFormat: RootJsonFormat[Change] = jsonFormat3(Change)
  implicit val changesFormat: RootJsonFormat[Changes] = jsonFormat2(Changes)
  implicit val changeSetInfoFormat: RootJsonFormat[ChangeSetInfo] = jsonFormat6(ChangeSetInfo)

  implicit val elementIdsFormat: RootJsonFormat[ElementIds] = jsonFormat3(ElementIds)
  implicit val elementIdMapFormat = ElementIdMap.ElementIdMapFormat
  implicit val analysisDataDetailFormat: RootJsonFormat[AnalysisDataDetail] = jsonFormat1(AnalysisDataDetail)
  implicit val orphanNodesDataFormat = OrphanNodesData.OrphanNodesDataFormat
  implicit val analysisDataOrphanNodesFormat: RootJsonFormat[AnalysisDataOrphanNodes] = jsonFormat1(AnalysisDataOrphanNodes)
  implicit val analysisDataFormat: RootJsonFormat[AnalysisData] = jsonFormat3(AnalysisData.apply)

  implicit val blackListEntryFormat: RootJsonFormat[BlackListEntry] = jsonFormat3(BlackListEntry)
  implicit val blackListFormat: RootJsonFormat[BlackList] = jsonFormat3(BlackList)
  implicit val blackListDocFormat: RootJsonFormat[BlackListDoc] = jsonFormat3(BlackListDoc)

  implicit val changesFilterPeriodFormat: JsonFormat[ChangesFilterPeriod] = lazyFormat(jsonFormat6(ChangesFilterPeriod))
  implicit val changesFilterFormat: RootJsonFormat[ChangesFilter] = jsonFormat1(ChangesFilter)

  implicit val nodeNetworkIntegrityCheckFormat: RootJsonFormat[NodeNetworkIntegrityCheck] = jsonFormat3(NodeNetworkIntegrityCheck)
  implicit val nodeNetworkRouteReferenceFormat: RootJsonFormat[NodeNetworkRouteReference] = jsonFormat3(NodeNetworkRouteReference)
  implicit val nodeNetworkReferenceFormat: RootJsonFormat[NodeNetworkReference] = jsonFormat9(NodeNetworkReference)
  implicit val nodeOrphanRouteReferenceFormat: RootJsonFormat[NodeOrphanRouteReference] = jsonFormat3(NodeOrphanRouteReference)
  implicit val nodeReferencesFormat: RootJsonFormat[NodeReferences] = jsonFormat2(NodeReferences)

  implicit val referenceFormat: RootJsonFormat[Reference] = jsonFormat4(Reference)
  implicit val nodeChangeInfoFormat: RootJsonFormat[NodeChangeInfo] = jsonFormat20(NodeChangeInfo)
  implicit val nodeChangeInfosFormat: RootJsonFormat[NodeChangeInfos] = jsonFormat2(NodeChangeInfos)
  implicit val nodeDetailsPageFormat: RootJsonFormat[NodeDetailsPage] = jsonFormat3(NodeDetailsPage)
  implicit val nodeMapPageFormat: RootJsonFormat[NodeMapPage] = jsonFormat2(NodeMapPage)
  implicit val nodeChangesPageFormat: RootJsonFormat[NodeChangesPage] = jsonFormat6(NodeChangesPage)

  implicit val knownElementsFormat: RootJsonFormat[KnownElements] = jsonFormat2(KnownElements)
  implicit val tagFormat: RootJsonFormat[Tag] = jsonFormat2(Tag)
  implicit val wayInfoFormat: RootJsonFormat[WayInfo] = jsonFormat5(WayInfo)
  implicit val networkNodeDataFormat: RootJsonFormat[NetworkNodeData] = jsonFormat3(NetworkNodeData)
  implicit val networkNodeUpdateFormat: RootJsonFormat[NetworkNodeUpdate] = jsonFormat3(NetworkNodeUpdate)
  implicit val networkChangeInfoFormat: RootJsonFormat[NetworkChangeInfo] = jsonFormat19(NetworkChangeInfo)

  implicit val factLevelFormat: RootJsonFormat[FactLevel] = jsonFormat1(FactLevel.apply)

  implicit val networkSummaryFormat: RootJsonFormat[NetworkSummary] = jsonFormat7(NetworkSummary)
  implicit val networkNodeFactFormat: RootJsonFormat[NetworkNodeFact] = jsonFormat2(NetworkNodeFact)
  implicit val networkRouteFactFormat: RootJsonFormat[NetworkRouteFact] = jsonFormat2(NetworkRouteFact)
  implicit val networkChangesPageFormat: RootJsonFormat[NetworkChangesPage] = jsonFormat4(NetworkChangesPage)

  implicit val changeSetSummaryInfoFormat: RootJsonFormat[ChangeSetSummaryInfo] = jsonFormat2(ChangeSetSummaryInfo)

  implicit val changesPageFormat: RootJsonFormat[ChangesPage] = jsonFormat3(ChangesPage)

  implicit val subsetInfoFormat: RootJsonFormat[SubsetInfo] = jsonFormat7(SubsetInfo)
  implicit val pageInfoFormat: RootJsonFormat[PageInfo] = jsonFormat2(PageInfo)
  implicit val networkMapInfoFormat: RootJsonFormat[NetworkMapInfo] = jsonFormat3(NetworkMapInfo)
  implicit val factCountFormat: RootJsonFormat[FactCount] = jsonFormat2(FactCount)

  implicit val changeSetDataFormat: RootJsonFormat[ChangeSetData] = jsonFormat4(ChangeSetData)

  implicit val pointSegmentFormat: RootJsonFormat[PointSegment] = jsonFormat2(PointSegment)
  implicit val geometryDiffFormat: RootJsonFormat[GeometryDiff] = jsonFormat3(GeometryDiff)
  implicit val routeChangeInfoFormat: RootJsonFormat[RouteChangeInfo] = jsonFormat16(RouteChangeInfo)
  implicit val routeChangeInfosFormat: RootJsonFormat[RouteChangeInfos] = jsonFormat2(RouteChangeInfos)

  implicit val changeSetPageFormat: RootJsonFormat[ChangeSetPage] = jsonFormat7(ChangeSetPage)

  implicit val timeInfoFormat: RootJsonFormat[TimeInfo] = jsonFormat4(TimeInfo)
  implicit val subsetOrphanRoutesPageFormat: RootJsonFormat[SubsetOrphanRoutesPage] = jsonFormat3(SubsetOrphanRoutesPage)
  implicit val subsetOrphanNodesPageFormat: RootJsonFormat[SubsetOrphanNodesPage] = jsonFormat3(SubsetOrphanNodesPage)
  implicit val subsetNetworksPageFormat: RootJsonFormat[SubsetNetworksPage] = jsonFormat12(SubsetNetworksPage)
  implicit val subsetFactsPageFormat: RootJsonFormat[SubsetFactsPage] = jsonFormat2(SubsetFactsPage)
  implicit val subsetChangesPageFormat: RootJsonFormat[SubsetChangesPage] = jsonFormat4(SubsetChangesPage)
  implicit val networkRouteRowFormat: RootJsonFormat[NetworkRouteRow] = jsonFormat8(NetworkRouteRow)
  implicit val networkRoutesPageFormat: RootJsonFormat[NetworkRoutesPage] = jsonFormat4(NetworkRoutesPage)
  implicit val networkNodesPageFormat: RootJsonFormat[NetworkNodesPage] = jsonFormat5(NetworkNodesPage)
  implicit val networkDetailsPageFormat: RootJsonFormat[NetworkDetailsPage] = jsonFormat5(NetworkDetailsPage)
  implicit val networkMapPageFormat: RootJsonFormat[NetworkMapPage] = jsonFormat4(NetworkMapPage)
  implicit val checkFormat: RootJsonFormat[Check] = jsonFormat4(Check)
  implicit val networkFactFormat: RootJsonFormat[NetworkFact] = jsonFormat5(NetworkFact)
  implicit val networkFactsPageFormat: RootJsonFormat[NetworkFactsPage] = jsonFormat2(NetworkFactsPage)
  implicit val oldNetworkFactsPageFormat: RootJsonFormat[OldNetworkFactsPage] = jsonFormat5(OldNetworkFactsPage)
  implicit val networkRoutesFactsFormat: RootJsonFormat[NetworkFactRefs] = jsonFormat3(NetworkFactRefs)
  implicit val subsetFactDetailsPageFormat: RootJsonFormat[SubsetFactDetailsPage] = jsonFormat3(SubsetFactDetailsPage)

  implicit val networkRefsFormat: RootJsonFormat[NetworkRefs] = jsonFormat5(NetworkRefs)

  implicit val routeReferencesFormat: RootJsonFormat[RouteReferences] = jsonFormat1(RouteReferences)
  implicit val wayGeometryFormat: RootJsonFormat[WayGeometry] = jsonFormat2(WayGeometry.apply)

  implicit val routeDetailsPageFormat: RootJsonFormat[RouteDetailsPage] = jsonFormat3(RouteDetailsPage)
  implicit val routeMapPageFormat: RootJsonFormat[RouteMapPage] = jsonFormat2(RouteMapPage)
  implicit val routeChangesPageFormat: RootJsonFormat[RouteChangesPage] = jsonFormat6(RouteChangesPage)

  implicit val changesParametersFormat: RootJsonFormat[ChangesParameters] = jsonFormat10(ChangesParameters)

  implicit val countryStatisticFormat: RootJsonFormat[CountryStatistic] = jsonFormat6(CountryStatistic)
  implicit val statisticFormat: RootJsonFormat[Statistic] = jsonFormat5(Statistic.apply)
  implicit val statisticsFormat: RootJsonFormat[Statistics] = jsonFormat1(Statistics)

  implicit val mapDetailNodeFormat: RootJsonFormat[MapDetailNode] = jsonFormat2(MapDetailNode)
  implicit val mapDetailRouteFormat: RootJsonFormat[MapDetailRoute] = jsonFormat2(MapDetailRoute)

  implicit val poiFormat: RootJsonFormat[Poi] = jsonFormat6(Poi)
  implicit val poiPageFormat: RootJsonFormat[PoiPage] = jsonFormat14(PoiPage)
  implicit val poiDocFormat: RootJsonFormat[PoiDoc] = jsonFormat3(PoiDoc)
  implicit val poiInfoFormat: RootJsonFormat[PoiInfo] = jsonFormat5(PoiInfo)

  implicit val locationPageFormat: RootJsonFormat[LocationPage] = jsonFormat1(LocationPage)

  implicit val clientPoiDefinitionFormat: RootJsonFormat[ClientPoiDefinition] = jsonFormat4(ClientPoiDefinition)
  implicit val clientPoiGroupFormat: RootJsonFormat[ClientPoiGroupDefinition] = jsonFormat3(ClientPoiGroupDefinition)
  implicit val clientPoiConfigurationFormat: RootJsonFormat[ClientPoiConfiguration] = jsonFormat1(ClientPoiConfiguration)

  implicit val routeLegNodeFormat: RootJsonFormat[RouteLegNode] = jsonFormat4(RouteLegNode)
  implicit val routeLegFragmentFormat: RootJsonFormat[RouteLegFragment] = jsonFormat5(RouteLegFragment)
  implicit val routeLegSegmentFormat: RootJsonFormat[RouteLegSegment] = jsonFormat3(RouteLegSegment)
  implicit val routeLegRouteFormat: RootJsonFormat[RouteLegRoute] = jsonFormat5(RouteLegRoute)
  implicit val routeLegFormat: RootJsonFormat[RouteLeg] = jsonFormat2(RouteLeg)
  implicit val locationTreeFormat: JsonFormat[LocationTree] = lazyFormat(jsonFormat(LocationTree, "name", "children"))

}
