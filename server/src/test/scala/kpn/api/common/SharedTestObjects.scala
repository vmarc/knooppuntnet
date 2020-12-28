package kpn.api.common

import kpn.api.common.changes.ChangeSet
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.ChangeKeyI
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RefBooleanChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.MapBounds
import kpn.api.common.common.Ref
import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackPathKey
import kpn.api.common.common.TrackPoint
import kpn.api.common.common.TrackSegment
import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.WayUpdate
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.network.NodeRouteReferenceDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.location.Location
import kpn.api.common.location.LocationCandidate
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.monitor.MonitorRouteNokSegment
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.common.network.Integrity
import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkInfoDetail
import kpn.api.common.network.NetworkInfoNode
import kpn.api.common.network.NetworkInfoRoute
import kpn.api.common.network.NetworkShape
import kpn.api.common.planner.LegEndRoute
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.common.route.RouteMap
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.custom.Change
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.RouteMemberInfo
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteState
import org.scalamock.scalatest.MockFactory

trait SharedTestObjects extends MockFactory {

  val defaultTimestamp: Timestamp = Timestamp(2015, 8, 11, 0, 0, 0)

  val timestampBeforeValue: Timestamp = Timestamp(2015, 8, 11, 0, 0, 1)
  val timestampFromValue: Timestamp = Timestamp(2015, 8, 11, 0, 0, 2)
  val timestampUntilValue: Timestamp = Timestamp(2015, 8, 11, 0, 0, 3)
  val timestampAfterValue: Timestamp = Timestamp(2015, 8, 11, 0, 0, 4)

  def newRawNode(
    id: Long = 1,
    latitude: String = "0",
    longitude: String = "0",
    version: Int = 0,
    timestamp: Timestamp = defaultTimestamp,
    changeSetId: Long = 0,
    tags: Tags = Tags.empty
  ): RawNode = {
    RawNode(
      id,
      latitude,
      longitude,
      version,
      timestamp,
      changeSetId,
      tags
    )
  }

  def newRawNodeWithName(nodeId: Long, name: String, extraTags: Tags = Tags.empty): RawNode = {
    newRawNode(nodeId, tags = newNodeTags(name) ++ extraTags)
  }

  def newForeignRawNode(nodeId: Long, name: String): RawNode = {
    newRawNode(nodeId, latitude = "99", longitude = "99", tags = newNodeTags(name))
  }

  def newRawWay(
    id: Long,
    version: Int = 0,
    timestamp: Timestamp = defaultTimestamp,
    changeSetId: Long = 0,
    nodeIds: Seq[Long] = Seq.empty,
    tags: Tags = Tags.empty
  ): RawWay = {
    RawWay(
      id,
      version,
      timestamp,
      changeSetId,
      nodeIds,
      tags
    )
  }

  def newRawRelation(
    id: Long = 0,
    version: Int = 1,
    timestamp: Timestamp = defaultTimestamp,
    changeSetId: Long = 1,
    members: Seq[RawMember] = Seq.empty,
    tags: Tags = Tags.empty
  ): RawRelation = {
    RawRelation(
      id,
      version,
      timestamp,
      changeSetId,
      members,
      tags
    )
  }

  def newMember(memberType: String, ref: Long, role: String = ""): RawMember = {
    RawMember(memberType, ref, if (role.nonEmpty) Some(role) else None)
  }

  def newNetworkTags(name: String = "name"): Tags = {
    Tags.from("network" -> "rwn", "type" -> "network", "name" -> name, "network:type" -> "node_network")
  }

  def newRouteTags(name: String = ""): Tags = {
    Tags.from("network" -> "rwn", "type" -> "route", "route" -> "foot", "note" -> name, "network:type" -> "node_network")
  }

  def newNodeTags(name: String = ""): Tags = {
    Tags.from("rwn_ref" -> name, "network:type" -> "node_network")
  }

  def newChangeKey(
    replicationNumber: Int = 1,
    timestamp: Timestamp = defaultTimestamp,
    changeSetId: Long = 123,
    elementId: Long = 0
  ): ChangeKey = {
    ChangeKey(
      replicationNumber,
      timestamp,
      changeSetId,
      elementId
    )
  }

  def newChangeKeyI(
    replicationNumber: Int = 1,
    timestamp: String = defaultTimestamp.yyyymmddhhmmss,
    changeSetId: Long = 123,
    elementId: Long = 0
  ): ChangeKeyI = {
    ChangeKeyI(
      replicationNumber,
      timestamp,
      changeSetId,
      elementId
    )
  }

  def newRouteChange(
    key: ChangeKey = newChangeKey(),
    changeType: ChangeType = ChangeType.Create,
    name: String = "",
    locationAnalysis: RouteLocationAnalysis = RouteLocationAnalysis(None, Seq.empty, Seq.empty),
    addedToNetwork: Seq[Ref] = Seq.empty,
    removedFromNetwork: Seq[Ref] = Seq.empty,
    before: Option[RouteData] = None,
    after: Option[RouteData] = None,
    removedWays: Seq[RawWay] = Seq.empty,
    addedWays: Seq[RawWay] = Seq.empty,
    updatedWays: Seq[WayUpdate] = Seq.empty,
    diffs: RouteDiff = RouteDiff(),
    facts: Seq[Fact] = Seq.empty,
    happy: Boolean = false,
    investigate: Boolean = false,
    locationHappy: Boolean = false,
    locationInvestigate: Boolean = false
  ): RouteChange = {
    RouteChange(
      key,
      changeType,
      name,
      locationAnalysis,
      addedToNetwork,
      removedFromNetwork,
      before,
      after,
      removedWays,
      addedWays,
      updatedWays,
      diffs,
      facts,
      happy,
      investigate,
      locationHappy,
      locationInvestigate
    )
  }

  def newRouteData(
    country: Option[Country] = None,
    networkType: NetworkType = NetworkType.hiking,
    relation: RawRelation = newRawRelation(),
    name: String = "",
    networkNodes: Seq[RawNode] = Seq.empty,
    nodes: Seq[RawNode] = Seq.empty,
    ways: Seq[RawWay] = Seq.empty,
    relations: Seq[RawRelation] = Seq.empty,
    facts: Seq[Fact] = Seq.empty
  ): RouteData = {
    RouteData(
      country,
      networkType,
      relation,
      name,
      networkNodes,
      nodes,
      ways,
      relations,
      facts
    )
  }

  def newNode(
    id: Long,
    latitude: String = "0",
    longitude: String = "0",
    version: Int = 0,
    timestamp: Timestamp = defaultTimestamp,
    changeSetId: Long = 0,
    tags: Tags = Tags.empty
  ): Node = {
    Node(
      RawNode(
        id,
        latitude,
        longitude,
        version,
        timestamp,
        changeSetId,
        tags
      )
    )
  }

  def newWay(
    id: Long,
    version: Int = 0,
    timestamp: Timestamp = defaultTimestamp,
    changeSetId: Long = 0,
    nodes: Seq[Node] = Seq.empty,
    tags: Tags = Tags.empty,
    length: Int = 0
  ): Way = {
    val nodeIds = nodes.map(_.id)
    val way = RawWay(id, version, timestamp, changeSetId, nodeIds, tags)
    Way(way, nodes, length)
  }

  def newNodeInfo(
    id: Long,
    active: Boolean = true,
    orphan: Boolean = false,
    country: Option[Country] = None,
    latitude: String = "0",
    longitude: String = "0",
    lastUpdated: Timestamp = defaultTimestamp,
    tags: Tags = Tags.empty,
    facts: Seq[Fact] = Seq.empty,
    location: Option[Location] = None,
    tiles: Seq[String] = Seq.empty
  ): NodeInfo = {

    NodeInfo(
      id,
      active,
      orphan,
      country,
      NodeAnalyzer.name(tags),
      NodeAnalyzer.names(tags),
      latitude,
      longitude,
      lastUpdated,
      None,
      tags,
      facts,
      location,
      tiles
    )
  }

  def newRoute(
    id: Long = 0,
    active: Boolean = true,
    orphan: Boolean = false,
    country: Option[Country] = None,
    networkType: NetworkType = NetworkType.hiking,
    name: String = "",
    meters: Int = 0,
    wayCount: Int = 0,
    lastUpdated: Timestamp = defaultTimestamp,
    lastUpdatedBy: String = "",
    relationLastUpdated: Timestamp = defaultTimestamp,
    lastSurvey: Option[Day] = None,
    analysis: RouteInfoAnalysis = newRouteInfoAnalysis(),
    facts: Seq[Fact] = Seq.empty,
    tiles: Seq[String] = Seq.empty
  ): RouteInfo = {

    val summary = RouteSummary(
      id,
      country,
      networkType,
      name,
      meters,
      isBroken = false,
      wayCount,
      relationLastUpdated,
      nodeNames = Seq.empty,
      tags = Tags.empty
    )

    RouteInfo(
      summary,
      active,
      orphan,
      version = 0L,
      changeSetId = 0L,
      lastUpdated,
      lastSurvey,
      Tags.empty,
      facts,
      analysis,
      tiles
    )
  }

  def newNetworkAttributes(
    id: Long,
    country: Option[Country] = None,
    networkType: NetworkType = NetworkType.hiking,
    name: String = "",
    km: Int = 0,
    meters: Int = 0,
    nodeCount: Int = 0,
    routeCount: Int = 0,
    brokenRouteCount: Int = 0,
    brokenRoutePercentage: String = "",
    integrity: Integrity = newIntegrity(),
    unaccessibleRouteCount: Int = 0,
    connectionCount: Int = 0,
    lastUpdated: Timestamp = defaultTimestamp,
    relationLastUpdated: Timestamp = defaultTimestamp
  ): NetworkAttributes = {
    NetworkAttributes(
      id,
      country,
      networkType,
      name,
      km,
      meters,
      nodeCount,
      routeCount,
      brokenRouteCount,
      brokenRoutePercentage,
      integrity,
      unaccessibleRouteCount,
      connectionCount,
      lastUpdated,
      relationLastUpdated,
      None
    )
  }

  def newNetworkInfoDetail(
    nodes: Seq[NetworkInfoNode] = Seq.empty,
    routes: Seq[NetworkInfoRoute] = Seq.empty,
    networkFacts: NetworkFacts = NetworkFacts(),
    shape: Option[NetworkShape] = None
  ): NetworkInfoDetail = {
    NetworkInfoDetail(
      nodes,
      routes,
      networkFacts,
      shape
    )
  }

  def newIntegrity(
    isOk: Boolean = true,
    hasChecks: Boolean = false,
    count: String = "",
    okCount: Int = 0,
    nokCount: Int = 0,
    coverage: String = "",
    okRate: String = "",
    nokRate: String = ""
  ): Integrity = {
    Integrity(
      isOk,
      hasChecks,
      count,
      okCount,
      nokCount,
      coverage,
      okRate,
      nokRate
    )
  }

  def newNetworkInfoNode(
    id: Long,
    title: String,
    number: String = "",
    latitude: String = "",
    longitude: String = "",
    connection: Boolean = false,
    roleConnection: Boolean = false,
    definedInRelation: Boolean = false,
    definedInRoute: Boolean = false,
    timestamp: Timestamp = defaultTimestamp,
    routeReferences: Seq[Ref] = Seq.empty,
    integrityCheck: Option[NodeIntegrityCheck] = None,
    facts: Seq[Fact] = Seq.empty,
    tags: Tags = Tags.empty
  ): NetworkInfoNode = {
    NetworkInfoNode(
      id,
      title,
      number,
      latitude,
      longitude,
      connection,
      roleConnection,
      definedInRelation,
      definedInRoute,
      timestamp,
      None,
      routeReferences,
      integrityCheck,
      facts,
      tags
    )
  }

  def newNetworkInfoRoute(
    id: Long,
    name: String,
    wayCount: Int = 0,
    length: Int = 0, // length in meter
    role: Option[String] = None,
    relationLastUpdated: Timestamp = defaultTimestamp,
    lastUpdated: Timestamp = defaultTimestamp,
    facts: Seq[Fact] = Seq.empty
  ): NetworkInfoRoute = {
    NetworkInfoRoute(
      id,
      name,
      wayCount,
      length,
      role,
      relationLastUpdated,
      lastUpdated: Timestamp,
      None,
      facts
    )
  }

  def newRouteLocationAnalysis(
    location: Option[Location] = None,
    candidates: Seq[LocationCandidate] = Seq.empty,
    locationNames: Seq[String] = Seq.empty
  ): RouteLocationAnalysis = {
    RouteLocationAnalysis(
      location,
      candidates,
      locationNames
    )
  }

  def newRouteInfoAnalysis(
    unexpectedNodeIds: Seq[Long] = Seq.empty,
    members: Seq[RouteMemberInfo] = Seq.empty,
    expectedName: String = "",
    map: RouteMap = RouteMap(),
    structureStrings: Seq[String] = Seq.empty,
    geometryDigest: String = "",
    locationAnalysis: RouteLocationAnalysis = newRouteLocationAnalysis()
  ): RouteInfoAnalysis = {
    RouteInfoAnalysis(
      unexpectedNodeIds,
      members,
      expectedName,
      map,
      structureStrings,
      geometryDigest,
      locationAnalysis
    )
  }

  def newRouteSummary(
    id: Long,
    country: Option[Country] = None,
    networkType: NetworkType = NetworkType.hiking,
    name: String = "",
    meters: Int = 0,
    isBroken: Boolean = false,
    wayCount: Int = 0,
    timestamp: Timestamp = defaultTimestamp,
    nodeNames: Seq[String] = Seq.empty,
    tags: Tags = Tags.empty
  ): RouteSummary = {
    RouteSummary(
      id,
      country,
      networkType,
      name,
      meters,
      isBroken,
      wayCount,
      timestamp,
      nodeNames,
      tags
    )
  }

  def newRouteMap(
    bounds: MapBounds = MapBounds(),
    forwardPath: Option[TrackPath] = None,
    backwardPath: Option[TrackPath] = None,
    unusedSegments: Seq[TrackSegment] = Seq.empty,
    startTentaclePaths: Seq[TrackPath] = Seq.empty,
    endTentaclePaths: Seq[TrackPath] = Seq.empty,
    forwardBreakPoint: Option[TrackPoint] = None,
    backwardBreakPoint: Option[TrackPoint] = None,
    startNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
    endNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
    startTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
    endTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
    redundantNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
    streets: Seq[String] = Seq()
  ): RouteMap = {
    RouteMap(
      bounds,
      forwardPath,
      backwardPath,
      unusedSegments,
      startTentaclePaths,
      endTentaclePaths,
      forwardBreakPoint,
      backwardBreakPoint,
      startNodes,
      endNodes,
      startTentacleNodes,
      endTentacleNodes,
      redundantNodes,
      streets
    )
  }

  def newRouteNetworkNodeInfo(
    id: Long,
    name: String,
    alternateName: String = "",
    lat: String = "",
    lon: String = ""
  ): RouteNetworkNodeInfo = {
    RouteNetworkNodeInfo(
      id,
      name,
      alternateName,
      lat,
      lon
    )
  }

  def newChangeSet(
    id: Long = 123L,
    timestamp: Timestamp = defaultTimestamp,
    timestampFrom: Timestamp = timestampFromValue,
    timestampUntil: Timestamp = timestampUntilValue,
    timestampBefore: Timestamp = timestampBeforeValue,
    timestampAfter: Timestamp = timestampAfterValue,
    changes: Seq[Change] = Seq.empty
  ): ChangeSet = {
    ChangeSet(
      id,
      timestamp,
      timestampFrom,
      timestampUntil,
      timestampBefore,
      timestampAfter,
      changes
    )
  }

  def newNodeChange(
    key: ChangeKey = newChangeKey(),
    changeType: ChangeType = ChangeType.Update,
    subsets: Seq[Subset] = Seq.empty,
    location: Option[Location] = None,
    name: String = "",
    before: Option[RawNode] = None,
    after: Option[RawNode] = None,
    connectionChanges: Seq[RefBooleanChange] = Seq.empty,
    roleConnectionChanges: Seq[RefBooleanChange] = Seq.empty,
    definedInNetworkChanges: Seq[RefBooleanChange] = Seq.empty,
    tagDiffs: Option[TagDiffs] = None,
    nodeMoved: Option[NodeMoved] = None,
    addedToRoute: Seq[Ref] = Seq.empty,
    removedFromRoute: Seq[Ref] = Seq.empty,
    addedToNetwork: Seq[Ref] = Seq.empty,
    removedFromNetwork: Seq[Ref] = Seq.empty,
    factDiffs: FactDiffs = FactDiffs(),
    facts: Seq[Fact] = Seq.empty,
    happy: Boolean = false,
    investigate: Boolean = false,
    locationHappy: Boolean = false,
    locationInvestigate: Boolean = false
  ): NodeChange = {
    NodeChange(
      key,
      changeType,
      subsets,
      location,
      name,
      before,
      after,
      connectionChanges,
      roleConnectionChanges,
      definedInNetworkChanges,
      tagDiffs,
      nodeMoved,
      addedToRoute,
      removedFromRoute,
      addedToNetwork,
      removedFromNetwork,
      factDiffs,
      facts,
      happy,
      investigate,
      locationHappy,
      locationInvestigate
    )
  }

  def newNetworkChange(
    key: ChangeKey = newChangeKey(),
    changeType: ChangeType = ChangeType.Update,
    country: Option[Country] = None,
    networkType: NetworkType = NetworkType.hiking,
    networkId: Long = 0,
    networkName: String = "",
    orphanRoutes: RefChanges = RefChanges.empty,
    orphanNodes: RefChanges = RefChanges.empty,
    networkDataUpdate: Option[NetworkDataUpdate] = None,
    networkNodes: RefDiffs = RefDiffs.empty,
    routes: RefDiffs = RefDiffs.empty,
    nodes: IdDiffs = IdDiffs.empty,
    ways: IdDiffs = IdDiffs.empty,
    relations: IdDiffs = IdDiffs.empty,
    happy: Boolean = false,
    investigate: Boolean = false
  ): NetworkChange = {
    NetworkChange(
      key,
      changeType,
      country,
      networkType,
      networkId,
      networkName,
      orphanRoutes,
      orphanNodes,
      networkDataUpdate,
      networkNodes,
      routes,
      nodes,
      ways,
      relations,
      happy,
      investigate
    )
  }

  def newRefChanges(
    oldRefs: Seq[Ref] = Seq.empty,
    newRefs: Seq[Ref] = Seq.empty
  ): RefChanges = {
    RefChanges(
      oldRefs,
      newRefs
    )
  }

  def newChangeSetSummary(
    key: ChangeKey = newChangeKey(),
    subsets: Seq[Subset] = Seq.empty,
    timestampFrom: Timestamp = timestampFromValue,
    timestampUntil: Timestamp = timestampUntilValue,
    networkChanges: NetworkChanges = NetworkChanges(),
    orphanRouteChanges: Seq[ChangeSetSubsetElementRefs] = Seq.empty,
    orphanNodeChanges: Seq[ChangeSetSubsetElementRefs] = Seq.empty,
    subsetAnalyses: Seq[ChangeSetSubsetAnalysis] = Seq.empty,
    happy: Boolean = false,
    investigate: Boolean = false
  ): ChangeSetSummary = {
    ChangeSetSummary(
      key,
      subsets,
      timestampFrom,
      timestampUntil,
      networkChanges,
      orphanRouteChanges,
      orphanNodeChanges,
      subsetAnalyses,
      happy,
      investigate
    )
  }

  def newChangeSetNetwork(
    country: Option[Country] = None,
    networkType: NetworkType = NetworkType.hiking,
    networkId: Long = 0,
    networkName: String = "",
    routeChanges: ChangeSetElementRefs = ChangeSetElementRefs.empty,
    nodeChanges: ChangeSetElementRefs = ChangeSetElementRefs.empty,
    happy: Boolean = false,
    investigate: Boolean = false
  ): ChangeSetNetwork = {
    ChangeSetNetwork(
      country,
      networkType,
      networkId,
      networkName,
      routeChanges,
      nodeChanges,
      happy,
      investigate
    )
  }

  def newChangeSetElementRef(
    id: Long,
    name: String,
    happy: Boolean = false,
    investigate: Boolean = false
  ): ChangeSetElementRef = {
    ChangeSetElementRef(
      id,
      name,
      happy,
      investigate
    )
  }

  def newNetworkInfo(
    attributes: NetworkAttributes,
    active: Boolean = true,
    nodeRefs: Seq[Long] = Seq.empty,
    routeRefs: Seq[Long] = Seq.empty,
    networkRefs: Seq[Long] = Seq.empty,
    facts: Seq[Fact] = Seq.empty,
    tags: Tags = Tags.empty,
    detail: Option[NetworkInfoDetail] = None
  ): NetworkInfo = {
    NetworkInfo(
      attributes,
      active,
      nodeRefs,
      routeRefs,
      networkRefs,
      facts,
      tags,
      detail
    )
  }

  def newRouteInfo(
    summary: RouteSummary,
    active: Boolean = true,
    orphan: Boolean = false,
    version: Int = 1,
    changeSetId: Long = 1,
    lastUpdated: Timestamp = defaultTimestamp,
    tags: Tags = Tags.empty,
    facts: Seq[Fact] = Seq.empty,
    analysis: RouteInfoAnalysis = newRouteInfoAnalysis()
  ): RouteInfo = {
    RouteInfo(
      summary,
      active,
      orphan,
      version,
      changeSetId,
      lastUpdated,
      None,
      tags,
      facts,
      analysis,
      Seq()
    )
  }

  def newNodeRouteReferenceDiffs(
    removed: Seq[Ref] = Seq.empty,
    added: Seq[Ref] = Seq.empty,
    remaining: Seq[Ref] = Seq.empty
  ): NodeRouteReferenceDiffs = {
    NodeRouteReferenceDiffs(
      removed,
      added,
      remaining
    )
  }

  def newPoi(
    elementType: String,
    elementId: Long,
    latitude: String = "",
    longitude: String = "",
    layers: Seq[String] = Seq.empty,
    tags: Tags = Tags.empty,
    tiles: Seq[String] = Seq.empty
  ): Poi = {
    Poi(
      elementType,
      elementId,
      latitude,
      longitude,
      layers,
      tags,
      tiles
    )
  }

  def legEndRoute(routeId: Long, pathId: Long): LegEndRoute = {
    LegEndRoute(List(TrackPathKey(routeId, pathId)), None)
  }

  def newMonitorGroup(
    name: String,
    description: String
  ): MonitorGroup = {
    MonitorGroup(
      name,
      description
    )
  }

  def newMonitorRoute(
    id: Long,
    groupName: String,
    name: String,
    nameNl: Option[String] = None,
    nameEn: Option[String] = None,
    nameDe: Option[String] = None,
    nameFr: Option[String] = None,
    ref: Option[String] = None,
    description: Option[String] = None,
    operator: Option[String] = None,
    website: Option[String] = None
  ): MonitorRoute = {
    MonitorRoute(
      id: Long,
      groupName: String,
      name: String,
      nameNl: Option[String],
      nameEn: Option[String],
      nameDe: Option[String],
      nameFr: Option[String],
      ref: Option[String],
      description: Option[String],
      operator: Option[String],
      website: Option[String]
    )
  }

  def newMonitorRouteChange(
    key: ChangeKeyI,
    groupName: String,
    wayCount: Long = 0,
    waysAdded: Long = 0,
    waysRemoved: Long = 0,
    waysUpdated: Long = 0,
    osmDistance: Long = 0,
    routeSegmentCount: Long = 0,
    newNokSegmentCount: Long = 0,
    resolvedNokSegmentCount: Long = 0,
    referenceKey: String = "",
    happy: Boolean = false,
    investigate: Boolean = false
  ): MonitorRouteChange = {
    MonitorRouteChange(
      key,
      groupName,
      wayCount,
      waysAdded,
      waysRemoved,
      waysUpdated,
      osmDistance,
      routeSegmentCount,
      newNokSegmentCount,
      resolvedNokSegmentCount,
      referenceKey,
      happy,
      investigate
    )
  }

  def newMonitorRouteState(
    routeId: Long,
    timestamp: Timestamp = defaultTimestamp,
    wayCount: Long = 0,
    osmDistance: Long = 0,
    gpxDistance: Long = 0,
    bounds: BoundsI = BoundsI(),
    referenceKey: Option[String] = None,
    osmSegments: Seq[MonitorRouteSegment] = Seq.empty,
    okGeometry: Option[String] = None,
    nokSegments: Seq[MonitorRouteNokSegment] = Seq.empty
  ): MonitorRouteState = {
    MonitorRouteState(
      routeId,
      timestamp,
      wayCount,
      osmDistance,
      gpxDistance,
      bounds,
      referenceKey,
      osmSegments,
      okGeometry,
      nokSegments
    )
  }

}
