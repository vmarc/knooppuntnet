package kpn.shared

import kpn.platform.PlatformUtil
import kpn.shared.changes.ChangeSetData
import kpn.shared.changes.ChangeSetInfo
import kpn.shared.changes.ChangeSetPage
import kpn.shared.changes.Review
import kpn.shared.changes.details.ChangeKey
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NetworkChangeInfo
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RefBooleanChange
import kpn.shared.changes.details.RefChanges
import kpn.shared.changes.details.RouteChange
import kpn.shared.changes.filter.ChangesFilter
import kpn.shared.changes.filter.ChangesFilterPeriod
import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.common.KnownElements
import kpn.shared.common.MapBounds
import kpn.shared.common.NetworkRefs
import kpn.shared.common.Ref
import kpn.shared.common.Reference
import kpn.shared.common.TrackPoint
import kpn.shared.common.TrackSegment
import kpn.shared.data.MetaData
import kpn.shared.data.Tag
import kpn.shared.data.Tags
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
import kpn.shared.network.NetworkRouteFact
import kpn.shared.network.NetworkFactsPage
import kpn.shared.network.NetworkInfo
import kpn.shared.network.NetworkInfoDetail
import kpn.shared.network.NetworkMapInfo
import kpn.shared.network.NetworkMapPage
import kpn.shared.network.NetworkNodeInfo2
import kpn.shared.network.NetworkNodesPage
import kpn.shared.network.NetworkRouteInfo
import kpn.shared.network.NetworkRoutesPage
import kpn.shared.network.NetworkShape
import kpn.shared.network.NetworkSummary
import kpn.shared.node.MapDetailNode
import kpn.shared.node.NodeChangeInfo
import kpn.shared.node.NodeChangeInfos
import kpn.shared.node.NodePage
import kpn.shared.node.NodeReferences
import kpn.shared.route.Backward
import kpn.shared.route.Both
import kpn.shared.route.Forward
import kpn.shared.route.GeometryDiff
import kpn.shared.route.MapDetailRoute
import kpn.shared.route.PointSegment
import kpn.shared.route.RouteChangeInfo
import kpn.shared.route.RouteChangeInfos
import kpn.shared.route.RouteInfo
import kpn.shared.route.RouteInfoAnalysis
import kpn.shared.route.RouteMap
import kpn.shared.route.RouteMemberInfo
import kpn.shared.route.RouteNetworkNodeInfo
import kpn.shared.route.RoutePage
import kpn.shared.route.RouteReferences
import kpn.shared.route.WayDirection
import kpn.shared.route.WayGeometry
import kpn.shared.statistics.CountryStatistic
import kpn.shared.statistics.Statistic
import kpn.shared.statistics.Statistics
import kpn.shared.subset.NetworkRoutesFacts
import kpn.shared.subset.SubsetFactDetailsPage
import kpn.shared.subset.SubsetChangesPage
import kpn.shared.subset.SubsetFactsPage
import kpn.shared.subset.SubsetInfo
import kpn.shared.subset.SubsetNetworksPage
import kpn.shared.subset.SubsetOrphanNodesPage
import kpn.shared.subset.SubsetOrphanRoutesPage

object KpnPicklers {

  // ignore Intellij error on next line (PlatformUtil is implemented in sharedJS and sharedJVM directories)
  private val platformUtil: PlatformUtil = new kpn.platform.PlatformUtilImpl()

  import boopickle.DefaultBasic._

  implicit object timestampPickler extends Pickler[Timestamp] {
    override def pickle(obj: Timestamp)(implicit state: PickleState): Unit = {

      val local = platformUtil.toLocal(obj)

      state.identityRefFor(local) match {
        case Some(idx) =>
          println("Timestamp.pickle idx=" + idx)
          state.enc.writeInt(-idx)
        case None =>
          state.enc.writeInt(local.year)
          state.enc.writeInt(local.month)
          state.enc.writeInt(local.day)
          state.enc.writeInt(local.hour)
          state.enc.writeInt(local.minute)
          state.enc.writeInt(local.second)
          state.addIdentityRef(local)
      }
    }

    override def unpickle(implicit state: UnpickleState): Timestamp = {
      val c = new Timestamp(
        state.dec.readInt,
        state.dec.readInt,
        state.dec.readInt,
        state.dec.readInt,
        state.dec.readInt,
        state.dec.readInt
      )
      state.addIdentityRef(c)
      c
    }
  }

  implicit val apiResponseNetworkDetailsPage: Pickler[ApiResponse[NetworkDetailsPage]] = PicklerGenerator.generatePickler[ApiResponse[NetworkDetailsPage]]
  implicit val apiResponseNetworkMapPage: Pickler[ApiResponse[NetworkMapPage]] = PicklerGenerator.generatePickler[ApiResponse[NetworkMapPage]]
  implicit val apiResponseNetworkFactsPage: Pickler[ApiResponse[NetworkFactsPage]] = PicklerGenerator.generatePickler[ApiResponse[NetworkFactsPage]]
  implicit val apiResponseNetworkNodesPage: Pickler[ApiResponse[NetworkNodesPage]] = PicklerGenerator.generatePickler[ApiResponse[NetworkNodesPage]]
  implicit val apiResponseNetworkRoutesPage: Pickler[ApiResponse[NetworkRoutesPage]] = PicklerGenerator.generatePickler[ApiResponse[NetworkRoutesPage]]
  implicit val apiResponseNetworkChangesPage: Pickler[ApiResponse[NetworkChangesPage]] = PicklerGenerator.generatePickler[ApiResponse[NetworkChangesPage]]
  implicit val apiResponseStatistics: Pickler[ApiResponse[Statistics]] = PicklerGenerator.generatePickler[ApiResponse[Statistics]]

  implicit val apiResponseNodePage: Pickler[ApiResponse[NodePage]] = PicklerGenerator.generatePickler[ApiResponse[NodePage]]
  implicit val apiResponseRoutePage: Pickler[ApiResponse[RoutePage]] = PicklerGenerator.generatePickler[ApiResponse[RoutePage]]
  implicit val apiResponseSubsetNetworksPage: Pickler[ApiResponse[SubsetNetworksPage]] = PicklerGenerator.generatePickler[ApiResponse[SubsetNetworksPage]]
  implicit val apiResponseSubsetFactsPage: Pickler[ApiResponse[SubsetFactsPage]] = PicklerGenerator.generatePickler[ApiResponse[SubsetFactsPage]]
  implicit val apiResponseSubsetOrphanNodesPage: Pickler[ApiResponse[SubsetOrphanNodesPage]] = PicklerGenerator.generatePickler[ApiResponse[SubsetOrphanNodesPage]]
  implicit val apiResponseSubsetOrphanRoutesPage: Pickler[ApiResponse[SubsetOrphanRoutesPage]] = PicklerGenerator.generatePickler[ApiResponse[SubsetOrphanRoutesPage]]
  implicit val apiResponseSubsetChangesPage: Pickler[ApiResponse[SubsetChangesPage]] = PicklerGenerator.generatePickler[ApiResponse[SubsetChangesPage]]
  implicit val apiResponseRoutesFactPage: Pickler[ApiResponse[SubsetFactDetailsPage]] = PicklerGenerator.generatePickler[ApiResponse[SubsetFactDetailsPage]]
  implicit val apiResponseChangeSetPage: Pickler[ApiResponse[ChangeSetPage]] = PicklerGenerator.generatePickler[ApiResponse[ChangeSetPage]]
  implicit val apiResponseChangesPage: Pickler[ApiResponse[ChangesPage]] = PicklerGenerator.generatePickler[ApiResponse[ChangesPage]]
  implicit val apiResponseMapDetailNode: Pickler[ApiResponse[MapDetailNode]] = PicklerGenerator.generatePickler[ApiResponse[MapDetailNode]]
  implicit val apiResponseMapDetailRoute: Pickler[ApiResponse[MapDetailRoute]] = PicklerGenerator.generatePickler[ApiResponse[MapDetailRoute]]

  implicit val knownElementsPickler: Pickler[KnownElements] = PicklerGenerator.generatePickler[KnownElements]
  implicit val tagPickler: Pickler[Tag] = PicklerGenerator.generatePickler[Tag]
  implicit val tagsPickler: Pickler[Tags] = PicklerGenerator.generatePickler[Tags]
  implicit val tagDetailTypePickler: Pickler[TagDetailType] = PicklerGenerator.generatePickler[TagDetailType]
  implicit val tagDetailPickler: Pickler[TagDetail] = PicklerGenerator.generatePickler[TagDetail]
  implicit val tagDiffsPickler: Pickler[TagDiffs] = PicklerGenerator.generatePickler[TagDiffs]
  implicit val routeNodeDiffPickler: Pickler[RouteNodeDiff] = PicklerGenerator.generatePickler[RouteNodeDiff]
  implicit val factDiffsPickler: Pickler[FactDiffs] = PicklerGenerator.generatePickler[FactDiffs]
  implicit val routeRoleDiffPickler: Pickler[RouteRoleDiff] = PicklerGenerator.generatePickler[RouteRoleDiff]
  implicit val routeNameDiffPickler: Pickler[RouteNameDiff] = PicklerGenerator.generatePickler[RouteNameDiff]
  implicit val routeDiffPickler: Pickler[RouteDiff] = PicklerGenerator.generatePickler[RouteDiff]
  implicit val nodeMovedPickler: Pickler[NodeMoved] = PicklerGenerator.generatePickler[NodeMoved]
  implicit val metaDataPickler: Pickler[MetaData] = PicklerGenerator.generatePickler[MetaData]
  implicit val rawMemberPickler: Pickler[RawMember] = PicklerGenerator.generatePickler[RawMember]
  implicit val rawRelationPickler: Pickler[RawRelation] = PicklerGenerator.generatePickler[RawRelation]
  implicit val rawNodePickler: Pickler[RawNode] = PicklerGenerator.generatePickler[RawNode]
  implicit val rawWayPickler: Pickler[RawWay] = PicklerGenerator.generatePickler[RawWay]
  implicit val nodeDataPickler: Pickler[NodeData] = PicklerGenerator.generatePickler[NodeData]
  implicit val nodeUpdatePickler: Pickler[NodeUpdate] = PicklerGenerator.generatePickler[NodeUpdate]
  implicit val wayUpdatePickler: Pickler[WayUpdate] = PicklerGenerator.generatePickler[WayUpdate]
  implicit val wayInfoPickler: Pickler[WayInfo] = PicklerGenerator.generatePickler[WayInfo]
  implicit val routeDataPickler: Pickler[RouteData] = PicklerGenerator.generatePickler[RouteData]

  implicit val nodeIntegrityCheckDiffPickler: Pickler[NodeIntegrityCheckDiff] = PicklerGenerator.generatePickler[NodeIntegrityCheckDiff]
  implicit val replicationIdPickler: Pickler[ReplicationId] = PicklerGenerator.generatePickler[ReplicationId]
  implicit val refPickler: Pickler[Ref] = PicklerGenerator.generatePickler[Ref]
  implicit val nodeIntegrityCheckPickler: Pickler[NodeIntegrityCheck] = PicklerGenerator.generatePickler[NodeIntegrityCheck]
  implicit val networkIntegrityCheckFailedPickler: Pickler[NetworkIntegrityCheckFailed] = PicklerGenerator.generatePickler[NetworkIntegrityCheckFailed]
  implicit val networkNodeInfo2Pickler: Pickler[NetworkNodeInfo2] = PicklerGenerator.generatePickler[NetworkNodeInfo2]
  implicit val integrityPickler: Pickler[Integrity] = PicklerGenerator.generatePickler[Integrity]

  implicit val networkNodeDiffPickler: Pickler[NetworkNodeDiff] = PicklerGenerator.generatePickler[NetworkNodeDiff]
  implicit val networkNodeUpdatePickler: Pickler[NetworkNodeUpdate] = PicklerGenerator.generatePickler[NetworkNodeUpdate]
  implicit val networkNodeDataPickler: Pickler[NetworkNodeData] = PicklerGenerator.generatePickler[NetworkNodeData]
  implicit val networkDataPickler: Pickler[NetworkData] = PicklerGenerator.generatePickler[NetworkData]
  implicit val idDiffsPickler: Pickler[IdDiffs] = PicklerGenerator.generatePickler[IdDiffs]
  implicit val networkDataUpdatePickler: Pickler[NetworkDataUpdate] = PicklerGenerator.generatePickler[NetworkDataUpdate]

  implicit val refChangesPickler: Pickler[RefChanges] = PicklerGenerator.generatePickler[RefChanges]
  implicit val refDiffsPickler: Pickler[RefDiffs] = PicklerGenerator.generatePickler[RefDiffs]
  implicit val changeTypePickler: Pickler[ChangeType] = PicklerGenerator.generatePickler[ChangeType]
  implicit val networkChangePickler: Pickler[NetworkChange] = PicklerGenerator.generatePickler[NetworkChange]
  implicit val networkChangeInfoPickler: Pickler[NetworkChangeInfo] = PicklerGenerator.generatePickler[NetworkChangeInfo]

  implicit val factLevelPickler: Pickler[FactLevel] = PicklerGenerator.generatePickler[FactLevel]
  implicit val factPickler: Pickler[Fact] = PicklerGenerator.generatePickler[Fact]

  implicit val networkExtraMemberNodePickler: Pickler[NetworkExtraMemberNode] = PicklerGenerator.generatePickler[NetworkExtraMemberNode]
  implicit val networkExtraMemberWayPickler: Pickler[NetworkExtraMemberWay] = PicklerGenerator.generatePickler[NetworkExtraMemberWay]
  implicit val networkExtraMemberRelationPickler: Pickler[NetworkExtraMemberRelation] = PicklerGenerator.generatePickler[NetworkExtraMemberRelation]
  implicit val networkIntegrityCheckPickler: Pickler[NetworkIntegrityCheck] = PicklerGenerator.generatePickler[NetworkIntegrityCheck]
  implicit val networkNameMissingPickler: Pickler[NetworkNameMissing] = PicklerGenerator.generatePickler[NetworkNameMissing]
  implicit val networkFactsPickler: Pickler[NetworkFacts] = PicklerGenerator.generatePickler[NetworkFacts]
  implicit val networkRouteInfoPickler: Pickler[NetworkRouteInfo] = PicklerGenerator.generatePickler[NetworkRouteInfo]
  implicit val networkAttributesPickler: Pickler[NetworkAttributes] = PicklerGenerator.generatePickler[NetworkAttributes]
  implicit val boundsPickler: Pickler[Bounds] = PicklerGenerator.generatePickler[Bounds]
  implicit val networkShapePickler: Pickler[NetworkShape] = PicklerGenerator.generatePickler[NetworkShape]
  implicit val networkInfoDetailPickler: Pickler[NetworkInfoDetail] = PicklerGenerator.generatePickler[NetworkInfoDetail]
  implicit val networkInfoPickler: Pickler[NetworkInfo] = PicklerGenerator.generatePickler[NetworkInfo]
  implicit val networkSummaryPickler: Pickler[NetworkSummary] = PicklerGenerator.generatePickler[NetworkSummary]
  implicit val networkFactPickler: Pickler[NetworkRouteFact] = PicklerGenerator.generatePickler[NetworkRouteFact]
  implicit val networkChangesPagePickler: Pickler[NetworkChangesPage] = PicklerGenerator.generatePickler[NetworkChangesPage]

  implicit val changesPagePickler: Pickler[ChangesPage] = PicklerGenerator.generatePickler[ChangesPage]

  implicit val countryPickler: Pickler[Country] = PicklerGenerator.generatePickler[Country]
  implicit val networkTypePickler: Pickler[NetworkType] = PicklerGenerator.generatePickler[NetworkType]
  implicit val changeSetNetworkPickler: Pickler[ChangeSetNetwork] = PicklerGenerator.generatePickler[ChangeSetNetwork]

  implicit val subsetInfoPickler: Pickler[SubsetInfo] = PicklerGenerator.generatePickler[SubsetInfo]
  implicit val pageInfoPickler: Pickler[PageInfo] = PicklerGenerator.generatePickler[PageInfo]
  implicit val routeSummaryPickler: Pickler[RouteSummary] = PicklerGenerator.generatePickler[RouteSummary]
  implicit val nodeInfoPickler: Pickler[NodeInfo] = PicklerGenerator.generatePickler[NodeInfo]
  implicit val networkMapInfoPickler: Pickler[NetworkMapInfo] = PicklerGenerator.generatePickler[NetworkMapInfo]
  implicit val factCountPickler: Pickler[FactCount] = PicklerGenerator.generatePickler[FactCount]

  implicit val changesFilterPeriodPickler: Pickler[ChangesFilterPeriod] = PicklerGenerator.generatePickler[ChangesFilterPeriod]
  implicit val changesFilterPickler: Pickler[ChangesFilter] = PicklerGenerator.generatePickler[ChangesFilter]

  implicit val changeSetInfoPickler: Pickler[ChangeSetInfo] = PicklerGenerator.generatePickler[ChangeSetInfo]

  implicit val networkChangesPickler: Pickler[NetworkChanges] = PicklerGenerator.generatePickler[NetworkChanges]

  implicit val changeSetElementRefPickler: Pickler[ChangeSetElementRef] = PicklerGenerator.generatePickler[ChangeSetElementRef]
  implicit val changeSetElementRefsPickler: Pickler[ChangeSetElementRefs] = PicklerGenerator.generatePickler[ChangeSetElementRefs]
  implicit val changeSetSubsetElementRefsPickler: Pickler[ChangeSetSubsetElementRefs] = PicklerGenerator.generatePickler[ChangeSetSubsetElementRefs]
  implicit val changeSetSummaryPickler: Pickler[ChangeSetSummary] = PicklerGenerator.generatePickler[ChangeSetSummary]

  implicit val changeKeyPickler: Pickler[ChangeKey] = PicklerGenerator.generatePickler[ChangeKey]
  implicit val changeSetSummaryInfoPickler: Pickler[ChangeSetSummaryInfo] = PicklerGenerator.generatePickler[ChangeSetSummaryInfo]
  implicit val routeChangePickler: Pickler[RouteChange] = PicklerGenerator.generatePickler[RouteChange]

  implicit val refBooleanChangePickler: Pickler[RefBooleanChange] = PicklerGenerator.generatePickler[RefBooleanChange]
  implicit val nodeRouteReferenceDiffsPickler: Pickler[NodeRouteReferenceDiffs] = PicklerGenerator.generatePickler[NodeRouteReferenceDiffs]
  implicit val nodeIntegrityCheckChangePickler: Pickler[NodeIntegrityCheckChange] = PicklerGenerator.generatePickler[NodeIntegrityCheckChange]
  implicit val nodeChangePickler: Pickler[NodeChange] = PicklerGenerator.generatePickler[NodeChange]
  implicit val changeSetDataPickler: Pickler[ChangeSetData] = PicklerGenerator.generatePickler[ChangeSetData]
  implicit val reviewPickler: Pickler[Review] = PicklerGenerator.generatePickler[Review]
  implicit val changeSetPagePickler: Pickler[ChangeSetPage] = PicklerGenerator.generatePickler[ChangeSetPage]

  implicit val timeInfoPickler: Pickler[TimeInfo] = PicklerGenerator.generatePickler[TimeInfo]
  implicit val subsetOrphanRoutesPagePickler: Pickler[SubsetOrphanRoutesPage] = PicklerGenerator.generatePickler[SubsetOrphanRoutesPage]
  implicit val subsetOrphanNodesPagePickler: Pickler[SubsetOrphanNodesPage] = PicklerGenerator.generatePickler[SubsetOrphanNodesPage]
  implicit val subsetNetworksPagePickler: Pickler[SubsetNetworksPage] = PicklerGenerator.generatePickler[SubsetNetworksPage]
  implicit val subsetFactsPagePickler: Pickler[SubsetFactsPage] = PicklerGenerator.generatePickler[SubsetFactsPage]
  implicit val subsetChangesPagePickler: Pickler[SubsetChangesPage] = PicklerGenerator.generatePickler[SubsetChangesPage]
  implicit val networkRoutesPagePickler: Pickler[NetworkRoutesPage] = PicklerGenerator.generatePickler[NetworkRoutesPage]
  implicit val networkNodesPagePickler: Pickler[NetworkNodesPage] = PicklerGenerator.generatePickler[NetworkNodesPage]
  implicit val networkDetailsPagePickler: Pickler[NetworkDetailsPage] = PicklerGenerator.generatePickler[NetworkDetailsPage]
  implicit val networkMapPagePickler: Pickler[NetworkMapPage] = PicklerGenerator.generatePickler[NetworkMapPage]
  implicit val networkFactsPagePickler: Pickler[NetworkFactsPage] = PicklerGenerator.generatePickler[NetworkFactsPage]
  implicit val routesFactsPickler: Pickler[RoutesFact] = PicklerGenerator.generatePickler[RoutesFact]
  implicit val networkRoutesFactsPickler: Pickler[NetworkRoutesFacts] = PicklerGenerator.generatePickler[NetworkRoutesFacts]
  implicit val routesFactPagePickler: Pickler[SubsetFactDetailsPage] = PicklerGenerator.generatePickler[SubsetFactDetailsPage]

  implicit val bothPickler: Pickler[Both.type] = PicklerGenerator.generatePickler[Both.type]
  implicit val forwardPickler: Pickler[Forward.type] = PicklerGenerator.generatePickler[Forward.type]
  implicit val backwardPickler: Pickler[Backward.type] = PicklerGenerator.generatePickler[Backward.type]

  implicit val wayDirectionPickler: Pickler[WayDirection] = compositePickler[WayDirection].
    addConcreteType[Both.type].
    addConcreteType[Forward.type].
    addConcreteType[Backward.type]

  implicit val trackPointPickler: Pickler[TrackPoint] = PicklerGenerator.generatePickler[TrackPoint]
  implicit val trackSegmentPickler: Pickler[TrackSegment] = PicklerGenerator.generatePickler[TrackSegment]
  implicit val mapBoundsPickler: Pickler[MapBounds] = PicklerGenerator.generatePickler[MapBounds]
  implicit val routeMapPickler: Pickler[RouteMap] = PicklerGenerator.generatePickler[RouteMap]
  implicit val routeMemberInfoPickler: Pickler[RouteMemberInfo] = PicklerGenerator.generatePickler[RouteMemberInfo]
  implicit val routeNetworkNodeInfoPickler: Pickler[RouteNetworkNodeInfo] = PicklerGenerator.generatePickler[RouteNetworkNodeInfo]
  implicit val routeInfoAnalysisPickler: Pickler[RouteInfoAnalysis] = PicklerGenerator.generatePickler[RouteInfoAnalysis]
  implicit val routePickler: Pickler[RouteInfo] = PicklerGenerator.generatePickler[RouteInfo]

  implicit val networkRefsPickler: Pickler[NetworkRefs] = PicklerGenerator.generatePickler[NetworkRefs]

  implicit val subsetPickler: Pickler[Subset] = PicklerGenerator.generatePickler[Subset]

  implicit val referencePickler: Pickler[Reference] = PicklerGenerator.generatePickler[Reference]

  implicit val nodeReferencesPickler: Pickler[NodeReferences] = PicklerGenerator.generatePickler[NodeReferences]
  implicit val nodeChangeInfosPickler: Pickler[NodeChangeInfos] = PicklerGenerator.generatePickler[NodeChangeInfos]
  implicit val nodeChangeInfoPickler: Pickler[NodeChangeInfo] = PicklerGenerator.generatePickler[NodeChangeInfo]
  implicit val nodePagePickler: Pickler[NodePage] = PicklerGenerator.generatePickler[NodePage]

  implicit val routeReferencesPickler: Pickler[RouteReferences] = PicklerGenerator.generatePickler[RouteReferences]
  implicit val latLonImplPickler: Pickler[LatLonImpl] = PicklerGenerator.generatePickler[LatLonImpl]
  implicit val wayGeometryPickler: Pickler[WayGeometry] = PicklerGenerator.generatePickler[WayGeometry]

  implicit val routeChangeInfosPickler: Pickler[RouteChangeInfos] = PicklerGenerator.generatePickler[RouteChangeInfos]
  implicit val routeChangeInfoPickler: Pickler[RouteChangeInfo] = PicklerGenerator.generatePickler[RouteChangeInfo]

  implicit val geometryDiffPickler: Pickler[GeometryDiff] = PicklerGenerator.generatePickler[GeometryDiff]
  implicit val pointSegmentPickler: Pickler[PointSegment] = PicklerGenerator.generatePickler[PointSegment]

  implicit val routePagePickler: Pickler[RoutePage] = PicklerGenerator.generatePickler[RoutePage]

  implicit val changesParametersPickler: Pickler[ChangesParameters] = PicklerGenerator.generatePickler[ChangesParameters]

  implicit val statisticPickler: Pickler[Statistic] = PicklerGenerator.generatePickler[Statistic]
  implicit val countryStatisticPickler: Pickler[CountryStatistic] = PicklerGenerator.generatePickler[CountryStatistic]
  implicit val statisticsPickler: Pickler[Statistics] = PicklerGenerator.generatePickler[Statistics]

  implicit val mapDetailNodePickler: Pickler[MapDetailNode] = PicklerGenerator.generatePickler[MapDetailNode]
  implicit val mapDetailRoutePickler: Pickler[MapDetailRoute] = PicklerGenerator.generatePickler[MapDetailRoute]
}
