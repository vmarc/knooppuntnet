package kpn.api.common

import kpn.api.base.ObjectId
import kpn.api.common.changes.ChangeSet
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RefBooleanChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.MapBounds
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackPathKey
import kpn.api.common.common.TrackPoint
import kpn.api.common.common.TrackSegment
import kpn.api.common.data.MetaData
import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkData
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
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.common.network.Integrity
import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkDetail
import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkInfoDetail
import kpn.api.common.network.NetworkInfoNode
import kpn.api.common.network.NetworkInfoRoute
import kpn.api.common.network.NetworkShape
import kpn.api.common.network.NetworkSummary
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.planner.LegEndRoute
import kpn.api.common.route.RouteEdge
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.common.route.RouteMap
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.custom.Change
import kpn.api.custom.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.RouteMemberInfo
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.doc.Label
import kpn.core.doc.NetworkDoc
import kpn.core.doc.NetworkInfoDoc
import kpn.core.doc.NetworkInfoNodeDetail
import kpn.core.doc.NetworkInfoRouteDetail
import kpn.core.doc.NetworkNodeMember
import kpn.core.doc.NetworkRelationMember
import kpn.core.doc.NetworkWayMember
import kpn.core.doc.NodeDoc
import kpn.core.doc.OrphanNodeDoc
import kpn.core.doc.OrphanRouteDoc
import kpn.core.doc.RouteDoc
import kpn.database.actions.statistics.ChangeSetCount2
import kpn.server.analyzer.engine.changes.network.NetworkChange
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.api.monitor.domain.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteReference
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
    version: Long = 0,
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
    nodeIds: Vector[Long] = Vector.empty,
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
    version: Long = 0,
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
    Tags.from(
      "network:type" -> "node_network",
      "type" -> "network",
      "network" -> "rwn",
      "name" -> name,
    )
  }

  def newRouteTags(name: String = ""): Tags = {
    Tags.from(
      "network" -> "rwn",
      "type" -> "route",
      "route" -> "foot",
      "note" -> name,
      "network:type" -> "node_network"
    )
  }

  def newNodeTags(name: String = ""): Tags = {
    Tags.from(
      "rwn_ref" -> name,
      "network:type" -> "node_network"
    )
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

  def newMetaData(
    version: Long = 0,
    timestamp: Timestamp = defaultTimestamp,
    changeSetId: Long = 0
  ): MetaData = {
    MetaData(
      version,
      timestamp,
      changeSetId
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
    impactedNodeIds: Seq[Long] = Seq.empty,
    tiles: Seq[String] = Seq.empty,
    happy: Boolean = false,
    investigate: Boolean = false,
    impact: Boolean = false,
    locationHappy: Boolean = false,
    locationInvestigate: Boolean = false,
    locationImpact: Boolean = false
  ): RouteChange = {
    RouteChange(
      key.toId,
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
      impactedNodeIds,
      tiles,
      happy,
      investigate,
      impact,
      locationHappy,
      locationInvestigate,
      locationImpact
    )
  }

  def newRouteData(
    country: Option[Country] = None,
    networkType: NetworkType = NetworkType.hiking,
    networkScope: NetworkScope = NetworkScope.regional,
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
      networkScope,
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
    nodes: Vector[Node] = Vector.empty,
    tags: Tags = Tags.empty,
    length: Int = 0
  ): Way = {
    val nodeIds = nodes.map(_.id)
    val way = RawWay(id, version, timestamp, changeSetId, nodeIds, tags)
    Way(way, nodes, length)
  }

  def newNodeDoc(
    id: Long,
    labels: Seq[String] = Seq(Label.active),
    country: Option[Country] = None,
    name: String = "",
    names: Seq[NodeName] = Seq.empty,
    version: Long = 0,
    changeSetId: Long = 0,
    latitude: String = "0",
    longitude: String = "0",
    lastUpdated: Timestamp = defaultTimestamp,
    lastSurvey: Option[Day] = None,
    tags: Tags = Tags.empty,
    facts: Seq[Fact] = Seq.empty,
    locations: Seq[String] = Seq.empty,
    tiles: Seq[String] = Seq.empty,
    integrity: Option[NodeIntegrity] = None,
    routeReferences: Seq[Reference] = Seq.empty
  ): NodeDoc = {

    NodeDoc(
      id,
      labels,
      country,
      name,
      names,
      version,
      changeSetId,
      latitude,
      longitude,
      lastUpdated,
      lastSurvey,
      tags,
      facts,
      locations,
      tiles,
      integrity,
      routeReferences
    )
  }

  def newRoute(
    id: Long = 0,
    labels: Seq[String] = Seq(Label.active),
    proposed: Boolean = false,
    country: Option[Country] = None,
    networkType: NetworkType = NetworkType.hiking,
    networkScope: NetworkScope = NetworkScope.regional,
    name: String = "",
    meters: Int = 0,
    wayCount: Int = 0,
    lastUpdated: Timestamp = defaultTimestamp,
    relationLastUpdated: Timestamp = defaultTimestamp,
    lastSurvey: Option[Day] = None,
    analysis: RouteInfoAnalysis = newRouteInfoAnalysis(),
    facts: Seq[Fact] = Seq.empty,
    tiles: Seq[String] = Seq.empty,
    elementIds: ElementIds = ElementIds()
  ): RouteDoc = {

    val summary = RouteSummary(
      id,
      country,
      networkType,
      networkScope,
      name,
      meters,
      broken = facts.exists(_.isError),
      inaccessible = facts.contains(Fact.RouteInaccessible),
      wayCount,
      relationLastUpdated,
      nodeNames = Seq.empty,
      tags = Tags.empty
    )

    RouteDoc(
      summary.id,
      labels,
      summary,
      proposed,
      version = 0L,
      changeSetId = 0L,
      lastUpdated,
      lastSurvey,
      Tags.empty,
      facts,
      analysis,
      tiles,
      analysis.map.nodeIds,
      elementIds,
      Seq.empty
    )
  }

  def newNetworkAttributes(
    id: Long,
    country: Option[Country] = None,
    networkType: NetworkType = NetworkType.hiking,
    networkScope: NetworkScope = NetworkScope.regional,
    name: String = "",
    km: Int = 0,
    meters: Int = 0,
    nodeCount: Int = 0,
    routeCount: Int = 0,
    brokenRouteCount: Int = 0,
    brokenRoutePercentage: String = "",
    integrity: Integrity = newIntegrity(),
    inaccessibleRouteCount: Int = 0,
    connectionCount: Int = 0,
    lastUpdated: Timestamp = defaultTimestamp,
    relationLastUpdated: Timestamp = defaultTimestamp,
    center: Option[LatLonImpl] = None
  ): NetworkAttributes = {
    NetworkAttributes(
      id,
      country,
      networkType,
      networkScope,
      name,
      km,
      meters,
      nodeCount,
      routeCount,
      brokenRouteCount,
      brokenRoutePercentage,
      integrity,
      inaccessibleRouteCount,
      connectionCount,
      lastUpdated,
      relationLastUpdated,
      center
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
    name: String,
    longName: Option[String] = None,
    latitude: String = "",
    longitude: String = "",
    connection: Boolean = false,
    roleConnection: Boolean = false,
    definedInRelation: Boolean = false,
    definedInRoute: Boolean = false,
    proposed: Boolean = false,
    timestamp: Timestamp = defaultTimestamp,
    routeReferences: Seq[Ref] = Seq.empty,
    integrityCheck: Option[NodeIntegrityCheck] = None,
    facts: Seq[Fact] = Seq.empty,
    tags: Tags = Tags.empty
  ): NetworkInfoNode = {
    NetworkInfoNode(
      id,
      name,
      longName,
      latitude,
      longitude,
      connection,
      roleConnection,
      definedInRelation,
      definedInRoute,
      proposed,
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
    facts: Seq[Fact] = Seq.empty,
    proposed: Boolean = false
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
      facts,
      proposed
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
    unexpectedRelationIds: Seq[Long] = Seq.empty,
    members: Seq[RouteMemberInfo] = Seq.empty,
    expectedName: String = "",
    map: RouteMap = RouteMap(),
    structureStrings: Seq[String] = Seq.empty,
    geometryDigest: String = "",
    locationAnalysis: RouteLocationAnalysis = newRouteLocationAnalysis()
  ): RouteInfoAnalysis = {
    RouteInfoAnalysis(
      unexpectedNodeIds,
      unexpectedRelationIds,
      members,
      expectedName,
      nameDerivedFromNodes = false,
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
    networkScope: NetworkScope = NetworkScope.regional,
    name: String = "",
    meters: Int = 0,
    broken: Boolean = false,
    inaccessible: Boolean = false,
    wayCount: Int = 0,
    timestamp: Timestamp = defaultTimestamp,
    nodeNames: Seq[String] = Seq.empty,
    tags: Tags = Tags.empty
  ): RouteSummary = {
    RouteSummary(
      id,
      country,
      networkType,
      networkScope,
      name,
      meters,
      broken,
      inaccessible,
      wayCount,
      timestamp,
      nodeNames,
      tags
    )
  }

  def newRouteMap(
    bounds: MapBounds = MapBounds(),
    freePaths: Seq[TrackPath] = Seq.empty,
    forwardPath: Option[TrackPath] = None,
    backwardPath: Option[TrackPath] = None,
    unusedSegments: Seq[TrackSegment] = Seq.empty,
    startTentaclePaths: Seq[TrackPath] = Seq.empty,
    endTentaclePaths: Seq[TrackPath] = Seq.empty,
    forwardBreakPoint: Option[TrackPoint] = None,
    backwardBreakPoint: Option[TrackPoint] = None,
    freeNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
    startNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
    endNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
    startTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
    endTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
    redundantNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
    streets: Seq[String] = Seq.empty
  ): RouteMap = {
    RouteMap(
      bounds,
      freePaths,
      forwardPath,
      backwardPath,
      unusedSegments,
      startTentaclePaths,
      endTentaclePaths,
      forwardBreakPoint,
      backwardBreakPoint,
      freeNodes,
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
    longName: Option[String] = None,
    lat: String = "",
    lon: String = ""
  ): RouteNetworkNodeInfo = {
    RouteNetworkNodeInfo(
      id,
      name,
      alternateName,
      longName,
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
    locations: Seq[String] = Seq.empty,
    name: String = "",
    before: Option[MetaData] = None,
    after: Option[MetaData] = None,
    connectionChanges: Seq[RefBooleanChange] = Seq.empty,
    roleConnectionChanges: Seq[RefBooleanChange] = Seq.empty,
    definedInNetworkChanges: Seq[RefBooleanChange] = Seq.empty,
    tagDiffs: Option[TagDiffs] = None,
    nodeMoved: Option[NodeMoved] = None,
    addedToRoute: Seq[Ref] = Seq.empty,
    removedFromRoute: Seq[Ref] = Seq.empty,
    addedToNetwork: Seq[Ref] = Seq.empty,
    removedFromNetwork: Seq[Ref] = Seq.empty,
    factDiffs: Option[FactDiffs] = None,
    facts: Seq[Fact] = Seq.empty,
    initialTags: Option[Tags] = None,
    initialLatLon: Option[LatLonImpl] = None,
    tiles: Seq[String] = Seq.empty,
    happy: Boolean = false,
    investigate: Boolean = false,
    impact: Boolean = false,
    locationHappy: Boolean = false,
    locationInvestigate: Boolean = false,
    locationImpact: Boolean = false
  ): NodeChange = {
    NodeChange(
      key.toId,
      key,
      changeType,
      subsets,
      locations,
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
      initialTags,
      initialLatLon,
      tiles,
      happy,
      investigate,
      impact,
      locationHappy,
      locationInvestigate,
      locationImpact
    )
  }

  def newNetworkInfoChange(
    key: ChangeKey = newChangeKey(),
    changeType: ChangeType = ChangeType.Update,
    country: Option[Country] = None,
    networkType: NetworkType = NetworkType.hiking,
    networkId: Long = 0,
    networkName: String = "",
    networkDataUpdate: Option[NetworkDataUpdate] = None,
    nodeDiffs: RefDiffs = RefDiffs.empty,
    routeDiffs: RefDiffs = RefDiffs.empty,
    extraNodes: IdDiffs = IdDiffs.empty,
    extraWays: IdDiffs = IdDiffs.empty,
    extraRelations: IdDiffs = IdDiffs.empty,
    happy: Boolean = false,
    investigate: Boolean = false
  ): NetworkInfoChange = {
    NetworkInfoChange(
      key.toId,
      key,
      changeType,
      country,
      networkType,
      networkId,
      networkName,
      networkDataUpdate,
      nodeDiffs,
      routeDiffs,
      extraNodes,
      extraWays,
      extraRelations,
      happy,
      investigate,
      happy || investigate
    )
  }

  def newNetworkChange(
    key: ChangeKey = newChangeKey(),
    networkName: String = "",
    changeType: ChangeType = ChangeType.Update,
    networkDataUpdate: Option[NetworkDataUpdate] = None,
    nodes: IdDiffs = IdDiffs.empty,
    ways: IdDiffs = IdDiffs.empty,
    relations: IdDiffs = IdDiffs.empty,
  ): NetworkChange = {
    NetworkChange(
      key.toId,
      key,
      key.elementId,
      networkName,
      changeType,
      networkDataUpdate,
      nodes,
      ways,
      relations,
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
    locationChanges: Seq[LocationChanges] = Seq.empty,
    locations: Seq[String] = Seq.empty,
    happy: Boolean = false,
    investigate: Boolean = false
  ): ChangeSetSummary = {
    ChangeSetSummary(
      key.toShortId,
      key,
      subsets,
      timestampFrom,
      timestampUntil,
      networkChanges,
      orphanRouteChanges,
      orphanNodeChanges,
      subsetAnalyses,
      locationChanges,
      locations,
      happy,
      investigate,
      happy || investigate
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
      attributes.id,
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

  def newRouteDoc(
    summary: RouteSummary,
    labels: Seq[String] = Seq(Label.active),
    proposed: Boolean = false,
    version: Int = 0,
    changeSetId: Long = 1,
    lastUpdated: Timestamp = defaultTimestamp,
    lastSurvey: Option[Day] = None,
    tags: Tags = Tags.empty,
    facts: Seq[Fact] = Seq.empty,
    analysis: RouteInfoAnalysis = newRouteInfoAnalysis(),
    tiles: Seq[String] = Seq.empty,
    nodeRefs: Seq[Long] = Seq.empty,
    elementIds: ElementIds = ElementIds(),
    edges: Seq[RouteEdge] = Seq.empty
  ): RouteDoc = {
    RouteDoc(
      summary.id,
      labels,
      summary,
      proposed,
      version,
      changeSetId,
      lastUpdated,
      lastSurvey,
      tags,
      facts,
      analysis,
      tiles,
      nodeRefs,
      elementIds,
      edges
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
    location: Location = Location.empty,
    tiles: Seq[String] = Seq.empty
  ): Poi = {
    Poi(
      s"$elementType:$elementId",
      elementType,
      elementId,
      latitude,
      longitude,
      layers,
      tags,
      location,
      tiles,
      None,
      None,
      link = false,
      image = false
    )
  }

  def legEndRoute(routeId: Long, pathId: Long): LegEndRoute = {
    LegEndRoute(List(TrackPathKey(routeId, pathId)), None)
  }

  def newMonitorGroup(
    name: String,
    description: String = ""
  ): MonitorGroup = {
    MonitorGroup(
      ObjectId(),
      name,
      description
    )
  }

  def newMonitorRoute(
    groupId: ObjectId,
    name: String,
    description: String = "",
    comment: Option[String] = None,
    relationId: Option[Long] = None,
    user: String = "",
    referenceType: String = "gpx",
    referenceDay: Option[Day] = None,
    referenceDistance: Long = 0,
    referenceFilename: Option[String] = None,
    deviationDistance: Long = 0,
    deviationCount: Long = 0,
    osmWayCount: Long = 0,
    osmDistance: Long = 0,
    osmSegmentCount: Long = 0,
    happy: Boolean = false,
    relation: Option[MonitorRouteRelation] = None
  ): MonitorRoute = {
    MonitorRoute(
      ObjectId(),
      groupId,
      name,
      description,
      comment,
      relationId,
      user,
      referenceType,
      referenceDay,
      referenceFilename,
      referenceDistance,
      deviationDistance,
      deviationCount,
      osmWayCount,
      osmDistance,
      osmSegmentCount,
      happy,
      relation
    )
  }

  def newMonitorRouteRelation(
    relationId: Long,
    name: String = "",
    role: Option[String] = None,
    survey: Option[Day] = None,
    deviationDistance: Long = 0,
    deviationCount: Long = 0,
    osmWayCount: Long = 0,
    osmDistance: Long = 0,
    osmSegmentCount: Long = 0,
    happy: Boolean = false,
    relations: Seq[MonitorRouteRelation] = Seq.empty
  ): MonitorRouteRelation = {
    MonitorRouteRelation(
      relationId,
      name,
      role,
      survey,
      deviationDistance,
      deviationCount,
      osmWayCount,
      osmDistance,
      osmSegmentCount,
      happy,
      relations
    )
  }

  def newMonitorRouteChange(
    key: ChangeKey,
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
      wayCount,
      waysAdded,
      waysRemoved,
      waysUpdated,
      osmDistance,
      routeSegmentCount,
      newNokSegmentCount,
      resolvedNokSegmentCount,
      happy,
      investigate
    )
  }

  def newMonitorRouteReference(
    routeId: ObjectId,
    relationId: Option[Long] = None,
    created: Timestamp = Time.now,
    user: String = "",
    bounds: Bounds = Bounds(),
    referenceType: String = "", // "osm" | "gpx"
    referenceDay: Day = Time.now.toDay,
    distance: Long = 0,
    segmentCount: Long = 0,
    filename: Option[String] = None,
    geometry: String = ""
  ): MonitorRouteReference = {
    MonitorRouteReference(
      ObjectId(),
      routeId,
      relationId,
      created,
      user,
      bounds,
      referenceType,
      referenceDay,
      distance,
      segmentCount,
      filename,
      geometry
    )
  }

  def newMonitorRouteState(
    routeId: ObjectId,
    relationId: Long,
    timestamp: Timestamp = defaultTimestamp,
    wayCount: Long = 0,
    osmDistance: Long = 0,
    bounds: Bounds = Bounds(),
    osmSegments: Seq[MonitorRouteSegment] = Seq.empty,
    matchesGeometry: Option[String] = None,
    deviations: Seq[MonitorRouteDeviation] = Seq.empty,
    happy: Boolean = false
  ): MonitorRouteState = {
    MonitorRouteState(
      ObjectId(),
      routeId,
      relationId,
      timestamp,
      wayCount,
      osmDistance,
      bounds,
      osmSegments,
      matchesGeometry,
      deviations,
      happy,
    )
  }

  def newNodeName(
    networkType: NetworkType = NetworkType.hiking,
    networkScope: NetworkScope = NetworkScope.regional,
    name: String = "",
    longName: Option[String] = None,
    proposed: Boolean = false
  ): NodeName = {
    NodeName(
      networkType,
      networkScope,
      name,
      longName,
      proposed
    )
  }

  def newNetworkData(
    version: Int = 1,
    timestamp: Timestamp = defaultTimestamp,
    changeSetId: Long = 1,
    name: String
  ): NetworkData = {
    NetworkData(MetaData(version, timestamp, changeSetId), name)
  }

  def newNetwork(
    _id: Long,
    active: Boolean = true,
    version: Long = 0,
    changeSetId: Long = 1,
    relationLastUpdated: Timestamp = defaultTimestamp,
    nodeMembers: Seq[NetworkNodeMember] = Seq.empty,
    wayMembers: Seq[NetworkWayMember] = Seq.empty,
    relationMembers: Seq[NetworkRelationMember] = Seq.empty,
    tags: Tags = Tags.empty
  ): NetworkDoc = {
    NetworkDoc(
      _id,
      active,
      version,
      changeSetId,
      relationLastUpdated,
      nodeMembers,
      wayMembers,
      relationMembers,
      tags
    )
  }

  def newNetworkInfoDoc(
    _id: Long,
    active: Boolean = true,
    country: Option[Country] = Some(Country.nl),
    summary: NetworkSummary = newNetworkSummary(),
    detail: NetworkDetail = newNetworkDetail(),
    facts: Seq[NetworkFact] = Seq.empty,
    nodes: Seq[NetworkInfoNodeDetail] = Seq.empty,
    routes: Seq[NetworkInfoRouteDetail] = Seq.empty,
    extraNodeIds: Seq[Long] = Seq.empty,
    extraWayIds: Seq[Long] = Seq.empty,
    extraRelationIds: Seq[Long] = Seq.empty
  ): NetworkInfoDoc = {
    NetworkInfoDoc(
      _id,
      active,
      country,
      summary,
      detail,
      facts,
      nodes,
      routes,
      extraNodeIds,
      extraWayIds,
      extraRelationIds
    )
  }

  def newNetworkSummary(
    name: String = "",
    networkType: NetworkType = NetworkType.hiking,
    networkScope: NetworkScope = NetworkScope.regional,
    factCount: Long = 0,
    nodeCount: Long = 0,
    routeCount: Long = 0,
    changeCount: Long = 0
  ): NetworkSummary = {
    NetworkSummary(
      name,
      networkType,
      networkScope,
      factCount,
      nodeCount,
      routeCount,
      changeCount
    )
  }

  def newNetworkInfoNodeDetail(
    id: Long,
    name: String = "",
    longName: String = "-",
    latitude: String = "0",
    longitude: String = "0",
    connection: Boolean = false,
    roleConnection: Boolean = false,
    definedInRelation: Boolean = false,
    proposed: Boolean = false,
    timestamp: Timestamp = defaultTimestamp,
    lastSurvey: Option[Day] = None,
    expectedRouteCount: Option[Long] = None,
    facts: Seq[Fact] = Seq.empty
  ): NetworkInfoNodeDetail = {
    NetworkInfoNodeDetail(
      id,
      name,
      longName,
      latitude,
      longitude,
      connection,
      roleConnection,
      definedInRelation,
      proposed,
      timestamp,
      lastSurvey,
      expectedRouteCount,
      facts
    )
  }

  def newNetworkInfoRouteDetail(
    id: Long,
    name: String = "",
    length: Long = 0,
    role: Option[String] = None,
    investigate: Boolean = false,
    accessible: Boolean = true,
    roleConnection: Boolean = false,
    lastUpdated: Timestamp = defaultTimestamp,
    lastSurvey: Option[Day] = None,
    proposed: Boolean = false,
    facts: Seq[Fact] = Seq.empty,
    tags: Tags = Tags.empty,
    nodeRefs: Seq[Long] = Seq.empty
  ): NetworkInfoRouteDetail = {
    NetworkInfoRouteDetail(
      id,
      name,
      length,
      role,
      investigate,
      accessible,
      roleConnection,
      lastUpdated,
      lastSurvey,
      proposed,
      facts,
      tags,
      nodeRefs
    )
  }

  def newNetworkDetail(
    km: Long = 0,
    meters: Long = 0,
    version: Long = 0,
    changeSetId: Long = 1,
    lastUpdated: Timestamp = defaultTimestamp,
    relationLastUpdated: Timestamp = defaultTimestamp,
    lastSurvey: Option[Day] = None,
    tags: Tags = Tags.empty,
    brokenRouteCount: Long = 0,
    brokenRoutePercentage: String = "-",
    integrity: Integrity = Integrity(),
    inaccessibleRouteCount: Long = 0,
    connectionCount: Long = 0,
    center: Option[LatLonImpl] = None
  ): NetworkDetail = {
    NetworkDetail(
      km,
      meters,
      version,
      changeSetId,
      lastUpdated,
      relationLastUpdated,
      lastSurvey,
      tags,
      brokenRouteCount,
      brokenRoutePercentage,
      integrity,
      inaccessibleRouteCount,
      connectionCount,
      center
    )
  }

  def newOrphanNodeDoc(
    country: Country,
    networkType: NetworkType,
    nodeId: Long,
    name: String = "",
    longName: Option[String] = None,
    proposed: Boolean = false,
    lastUpdated: Timestamp = defaultTimestamp,
    lastSurvey: Option[Day] = None,
    facts: Seq[Fact] = Seq.empty
  ): OrphanNodeDoc = {
    val _id = s"${country.domain}:${networkType.name}:$nodeId"
    OrphanNodeDoc(
      _id,
      country,
      networkType,
      nodeId,
      name,
      longName,
      proposed,
      lastUpdated,
      lastSurvey,
      facts
    )
  }

  def newOrphanRouteDoc(
    _id: Long,
    country: Country,
    networkType: NetworkType,
    name: String = "",
    meters: Long = 0L,
    facts: Seq[Fact] = Seq.empty,
    lastSurvey: Option[Day] = None,
    lastUpdated: Timestamp = defaultTimestamp
  ): OrphanRouteDoc = {
    OrphanRouteDoc(
      _id,
      country,
      networkType,
      name,
      meters,
      facts,
      lastSurvey,
      lastUpdated
    )
  }

  def newChangeSetCount(
    year: Long,
    month: Long = 0,
    day: Long = 0
  )(
    impact: Long,
    total: Long
  ): ChangeSetCount2 = {
    ChangeSetCount2(
      year,
      month,
      day,
      impact,
      total
    )
  }
}
