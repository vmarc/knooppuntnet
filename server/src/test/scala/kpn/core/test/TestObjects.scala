package kpn.core.test

import kpn.api.common.Bounds
import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetNetwork
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.ElementChangeType
import kpn.api.common.Fact
import kpn.api.common.FeatureLayer
import kpn.api.common.LatLonImpl
import kpn.api.common.LocationChanges
import kpn.api.common.NetworkChanges
import kpn.api.common.NetworkFact
import kpn.api.common.NodeName
import kpn.api.common.OrphanNodeInfo
import kpn.api.common.OrphanRouteInfo
import kpn.api.common.Relation
import kpn.api.common.ReplicationId
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteMemberInfo
import kpn.api.common.RouteMemberInfoWay
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.ChangeSet
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RefBooleanChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.common.TrackPathKey
import kpn.api.common.data.Member
import kpn.api.common.data.MemberType
import kpn.api.common.data.MetaData
import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.data.raw.Raw
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
import kpn.api.common.diff.WayDiffsInfo
import kpn.api.common.diff.WayInfo
import kpn.api.common.diff.WayUpdate
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.network.NodeRouteReferenceDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.diff.route.RouteNameDiff
import kpn.api.common.diff.route.RouteNodeDiff
import kpn.api.common.diff.route.RouteRoleDiff
import kpn.api.common.location.Location
import kpn.api.common.location.LocationCandidate
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.network.Integrity
import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkBaseData
import kpn.api.common.network.NetworkDetail
import kpn.api.common.network.NetworkSummary
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.planner.LegEndRoute
import kpn.api.common.poi.Poi
import kpn.api.common.route.BaseRouteSegment
import kpn.api.common.route.GeometryDiff
import kpn.api.common.route.Link
import kpn.api.common.route.LinkDirection
import kpn.api.common.route.ParentRoute
import kpn.api.common.route.RouteEdge
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.common.route.RouteNode
import kpn.api.common.route.RouteNodeChange
import kpn.api.common.route.RouteNodes
import kpn.api.common.route.RoutePath
import kpn.api.common.route.RouteSegment
import kpn.api.common.route.RouteStructureRow
import kpn.api.common.route.WayGeometryUpdate
import kpn.api.common.route.WayLine
import kpn.api.custom.Change
import kpn.api.custom.Day
import kpn.api.custom.Subset
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.doc.BaseNetworkDoc
import kpn.core.doc.BaseNodeDoc
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.BaseRoutePath
import kpn.core.doc.BaseRouteSegmentElement
import kpn.core.doc.NetworkDoc
import kpn.core.doc.NetworkInfoNodeDetail
import kpn.core.doc.NetworkRouteDetail
import kpn.core.doc.NodeBaseData
import kpn.core.doc.NodeDoc
import kpn.core.doc.RouteBaseData
import kpn.core.doc.RouteDoc
import kpn.core.doc.RouteRelation
import kpn.core.doc.SuperSegment
import kpn.core.doc.SuperSubSegment
import kpn.database.actions.statistics.ChangeSetCount2
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileData
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileSegment
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.changes.OsmChange
import kpn.server.analyzer.engine.context.ChangeElementIds
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorReferenceTile
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteChange
import kpn.server.monitor.domain.MonitorSegment
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.domain.MonitorStateTile
import kpn.server.monitor.domain.MonitorStateTileDeviation
import org.bson.types.ObjectId

object TestObjects {

  def newRawNode(
    id: Long = 1,
    latitude: String = "0",
    longitude: String = "0",
    version: Long = 0,
    timestamp: Timestamp = Timestamps.default,
    changeSetId: Long = 1,
    tags: Seq[Tag] = Seq.empty
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

  def newNodeBaseData(
    raw: Raw = newRaw(),
    name: Option[String] = None,
    names: Seq[NodeName] = Seq.empty,
    lastSurvey: Option[Day] = None,
    latitude: String = "0",
    longitude: String = "0",
    country: Option[Country] = None,
    locations: Seq[String] = Seq.empty,
  ): NodeBaseData = {
    NodeBaseData(
      raw,
      name,
      names,
      lastSurvey,
      latitude,
      longitude,
      country,
      locations
    )
  }

  def newBaseNodeDoc(
    _id: Long = 0,
    active: Boolean = true,
    base: NodeBaseData = newNodeBaseData(),
    facts: Seq[Fact] = Seq.empty,
    tiles: Seq[String] = Seq.empty,
  ): BaseNodeDoc = {
    BaseNodeDoc(
      _id,
      active,
      base,
      facts,
      tiles
    )
  }

  def newNodeWithName(nodeId: Long, name: String, extraTags: Seq[Tag] = Seq.empty): Node = {
    newNode(nodeId, tags = newNodeTags(name) ++ extraTags)
  }

  def newRouteNode(nodeId: Long, name: String): RouteNode = {
    RouteNode(
      nodeId = nodeId,
      latitude = "0",
      longitude = "0",
      name = name,
      alternateName = name,
      isInWay = true,
    )
  }

  def newForeignRawNode(nodeId: Long, name: String): RawNode = {
    newRawNode(nodeId, latitude = "99", longitude = "99", tags = newNodeTags(name))
  }

  def newRawWay(
    id: Long,
    version: Int = 0,
    timestamp: Timestamp = Timestamps.default,
    changeSetId: Long = 1,
    nodeIds: Vector[Long] = Vector.empty,
    tags: Seq[Tag] = Seq.empty
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

  def newWayInfo(
    id: Long,
    version: Int = 0,
    changeSetId: Long = 1,
    timestamp: Timestamp = Timestamps.default,
    tags: Seq[Tag] = Seq.empty
  ): WayInfo = {
    WayInfo(
      id,
      version,
      changeSetId,
      timestamp,
      tags
    )
  }

  def newRelation(
    id: Long = 0,
    version: Long = 0,
    timestamp: Timestamp = Timestamps.default,
    changeSetId: Long = 1,
    tags: Seq[Tag] = Seq.empty,
    members: Seq[Member] = Seq.empty,
  ): Relation = {
    Relation(
      id,
      version,
      timestamp,
      changeSetId,
      tags,
      members
    )
  }

  def newRawRelation(
    id: Long = 0,
    version: Long = 0,
    timestamp: Timestamp = Timestamps.default,
    changeSetId: Long = 1,
    members: Seq[RawMember] = Seq.empty,
    tags: Seq[Tag] = Seq.empty
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

  def newMember(memberType: MemberType, ref: Long, role: String = ""): RawMember = {
    RawMember(memberType, ref, if (role.nonEmpty) Some(role) else None)
  }

  def newNetworkTags(name: String = "name"): Seq[Tag] = {
    Tags.from(
      "network:type" -> "node_network",
      "type" -> "network",
      "network" -> "rwn",
      "name" -> name,
    )
  }

  def newRouteTags(name: String = ""): Seq[Tag] = {
    Tags.from(
      "network" -> "rwn",
      "type" -> "route",
      "route" -> "foot",
      "ref" -> name,
      "network:type" -> "node_network"
    )
  }

  def newNodeTags(name: String = ""): Seq[Tag] = {
    Tags.from(
      "rwn_ref" -> name,
      "network:type" -> "node_network"
    )
  }

  def newChangeKey(
    replicationNumber: Int = 1,
    timestamp: Timestamp = Timestamps.default,
    changeSetId: Long = 1,
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
    timestamp: Timestamp = Timestamps.default,
    changeSetId: Long = 1
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
    nodeChanges: Seq[RouteNodeChange] = Seq.empty,
    facts: Seq[Fact] = Seq.empty,
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
      nodeChanges,
      facts,
      happy,
      investigate,
      impact,
      locationHappy,
      locationInvestigate,
      locationImpact
    )
  }

  def newBaseRouteChange(
    _id: String,
    key: ChangeKey = newChangeKey(),
    changeType: ChangeType = ChangeType.Update,
    routeDiff: RouteDiff = RouteDiff.empty,
    wayDiffs: Option[WayDiffsInfo] = None,
    geometryDiff: Option[GeometryDiff] = None
  ): BaseRouteChange = {
    BaseRouteChange(
      _id,
      key,
      changeType,
      routeDiff,
      wayDiffs,
      geometryDiff
    )
  }

  def newRouteDiff(
    nameDiff: Option[RouteNameDiff] = None,
    roleDiff: Option[RouteRoleDiff] = None,
    factDiffs: Option[FactDiffs] = None,
    nodeDiffs: Seq[RouteNodeDiff] = Seq.empty,
    memberOrderChanged: Boolean = false,
    tagDiffs: Option[TagDiffs] = None
  ): RouteDiff = {
    RouteDiff(
      nameDiff,
      roleDiff,
      factDiffs,
      nodeDiffs,
      memberOrderChanged,
      tagDiffs
    )
  }

  def newRouteData(
    relationId: Long = 0,
    raw: Raw = newRaw(),
    countries: Seq[Country] = Seq.empty,
    routeTypes: Seq[RouteType] = Seq.empty,
    name: String = "",
    networkNodes: Seq[RouteNode] = Seq.empty,
    facts: Seq[Fact] = Seq.empty,
    meters: Long = 0,
    locationAnalysis: RouteLocationAnalysis = RouteLocationAnalysis(None, Seq.empty, Seq.empty)
  ): RouteData = {
    RouteData(
      relationId,
      raw,
      countries,
      routeTypes,
      name,
      networkNodes,
      facts,
      meters,
      locationAnalysis
    )
  }

  def newNode(
    id: Long = 1001,
    latitude: String = "0",
    longitude: String = "0",
    version: Int = 0,
    timestamp: Timestamp = Timestamps.default,
    changeSetId: Long = 1,
    tags: Seq[Tag] = Seq.empty
  ): Node = {
    Node(
      id,
      latitude,
      longitude,
      version,
      timestamp,
      changeSetId,
      tags
    )
  }

  def newWay(
    id: Long,
    version: Int = 0,
    timestamp: Timestamp = Timestamps.default,
    changeSetId: Long = 1,
    nodes: Vector[Node] = Vector.empty,
    tags: Seq[Tag] = Seq.empty,
    length: Int = 0
  ): Way = {
    Way(id, version, timestamp, changeSetId, tags, nodes, length)
  }

  def newNodeDoc(
    id: Long,
    active: Boolean = true,
    base: NodeBaseData = newNodeBaseData(),
    labels: Seq[String] = Seq.empty,
    facts: Seq[Fact] = Seq.empty,
    tiles: Seq[String] = Seq.empty,
    integrity: Option[NodeIntegrity] = None,
    routeReferences: Seq[Reference] = Seq.empty,
    networkReferences: Seq[Reference] = Seq.empty,
  ): NodeDoc = {

    NodeDoc(
      id,
      active,
      base,
      labels,
      facts,
      integrity,
      routeReferences,
      networkReferences,
      None
    )
  }

  def newRouteTileInfo(
    _id: String,
    routeId: Long,
  ): RouteTileInfo = {
    RouteTileInfo(
      _id,
      routeId,
      routeName = "",
      routeTypes = Seq.empty,
      z = 0,
      x = 0,
      y = 0,
      layer = FeatureLayer.route,
      scope = None,
      survey = None,
      error = None,
      proposed = false,
      segments = Seq.empty
    )
  }

  def newNetworkBaseData(
    raw: Raw = newRaw(),
    name: Option[String] = None,
    routeType: RouteType = RouteType.hiking,
    routeScope: RouteScope = RouteScope.regional,
    members: Seq[RawMember] = Seq.empty,
  ): NetworkBaseData = {
    NetworkBaseData(
      raw,
      name,
      routeType,
      routeScope,
      members
    )
  }

  def newBaseNetworkDoc(
    _id: Long,
    active: Boolean = true,
    base: NetworkBaseData = newNetworkBaseData(),
    nodeIds: Seq[Long] = Seq.empty,
    relationIds: Seq[Long] = Seq.empty,
  ): BaseNetworkDoc = {
    BaseNetworkDoc(
      _id,
      active,
      base,
      nodeIds,
      relationIds,
    )
  }

  def newNetworkAttributes(
    id: Long,
    country: Option[Country] = None,
    routeType: RouteType = RouteType.hiking,
    routeScope: RouteScope = RouteScope.regional,
    name: Option[String] = None,
    km: Int = 0,
    meters: Int = 0,
    nodeCount: Int = 0,
    routeCount: Int = 0,
    brokenRouteCount: Int = 0,
    brokenRoutePercentage: String = "",
    integrity: Integrity = newIntegrity(),
    inaccessibleRouteCount: Int = 0,
    connectionCount: Int = 0,
    lastUpdated: Timestamp = Timestamps.default,
    relationLastUpdated: Timestamp = Timestamps.default,
    center: Option[LatLonImpl] = None
  ): NetworkAttributes = {
    NetworkAttributes(
      id,
      country,
      routeType,
      routeScope,
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
    expectedName: String = ""
  ): RouteInfoAnalysis = {
    RouteInfoAnalysis(
      expectedName
    )
  }

  def newRaw(
    version: Long = 0,
    changeSetId: Long = 1,
    timestamp: Timestamp = Timestamps.default,
    tags: Seq[Tag] = Seq.empty
  ): Raw = {
    Raw(
      version,
      changeSetId,
      timestamp,
      tags
    )
  }

  def newRouteNetworkNodeInfo(
    id: Long,
    name: String,
    alternateName: String = "",
    longName: Option[String] = None,
    lat: String = "0",
    lon: String = "0"
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
    id: Long = 1,
    timestamp: Timestamp = Timestamps.default,
    timestampFrom: Timestamp = Timestamps.from,
    timestampUntil: Timestamp = Timestamps.until,
    timestampBefore: Timestamp = Timestamps.before,
    timestampAfter: Timestamp = Timestamps.after,
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
    name: Option[String] = None,
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
    initialTags: Option[Seq[Tag]] = None,
    initialLatLon: Option[LatLonImpl] = None,
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
      happy,
      investigate,
      impact,
      locationHappy,
      locationInvestigate,
      locationImpact
    )
  }

  def newRouteNodeChange(
    id: Long,
    latitude: String = "0",
    longitude: String = "0",
    changeType: ElementChangeType = ElementChangeType.Unchanged
  ): RouteNodeChange = {
    RouteNodeChange(
      id,
      latitude,
      longitude,
      changeType
    )
  }

  def newNetworkChange(
    key: ChangeKey = newChangeKey(),
    networkName: Option[String] = None,
    changeType: ChangeType = ChangeType.Update,
    country: Option[Country] = None,
    routeType: RouteType = RouteType.hiking,
    networkDataUpdate: Option[NetworkDataUpdate] = None,
    nodes: IdDiffs = IdDiffs.empty,
    ways: IdDiffs = IdDiffs.empty,
    relations: IdDiffs = IdDiffs.empty,
    nodeDiffs: RefDiffs = RefDiffs.empty,
    routeDiffs: RefDiffs = RefDiffs.empty,
    extraNodeDiffs: IdDiffs = IdDiffs.empty,
    extraWayDiffs: IdDiffs = IdDiffs.empty,
    extraRelationDiffs: IdDiffs = IdDiffs.empty,
    happy: Boolean = false,
    investigate: Boolean = false,
    impact: Boolean = false,
  ): NetworkChange = {
    NetworkChange(
      key.toId,
      key,
      key.elementId,
      networkName,
      changeType,
      country,
      routeType,
      networkDataUpdate,
      nodes,
      ways,
      relations,
      nodeDiffs: RefDiffs,
      routeDiffs: RefDiffs,
      extraNodeDiffs: IdDiffs,
      extraWayDiffs: IdDiffs,
      extraRelationDiffs: IdDiffs,
      happy: Boolean,
      investigate: Boolean,
      impact: Boolean,
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
    locations: Seq[String] = Seq.empty,
    timestampFrom: Timestamp = Timestamps.from,
    timestampUntil: Timestamp = Timestamps.until,
    networkChanges: NetworkChanges = NetworkChanges(),
    orphanRouteChanges: Seq[ChangeSetSubsetElementRefs] = Seq.empty,
    orphanNodeChanges: Seq[ChangeSetSubsetElementRefs] = Seq.empty,
    subsetAnalyses: Seq[ChangeSetSubsetAnalysis] = Seq.empty,
    locationChanges: Seq[LocationChanges] = Seq.empty,
    happy: Boolean = false,
    investigate: Boolean = false
  ): ChangeSetSummary = {
    ChangeSetSummary(
      key.toShortId,
      key,
      subsets,
      locations,
      timestampFrom,
      timestampUntil,
      networkChanges,
      orphanRouteChanges,
      orphanNodeChanges,
      subsetAnalyses,
      locationChanges,
      happy,
      investigate,
      happy || investigate
    )
  }

  def newChangeSetNetwork(
    country: Option[Country] = None,
    routeType: RouteType = RouteType.hiking,
    networkId: Long = 0,
    networkName: Option[String] = None,
    routeChanges: ChangeSetElementRefs = ChangeSetElementRefs.empty,
    nodeChanges: ChangeSetElementRefs = ChangeSetElementRefs.empty,
    happy: Boolean = false,
    investigate: Boolean = false
  ): ChangeSetNetwork = {
    ChangeSetNetwork(
      country,
      routeType,
      networkId,
      networkName,
      routeChanges,
      nodeChanges,
      happy,
      investigate
    )
  }

  def newLocationChanges(
    routeType: RouteType,
    locationNames: Seq[String],
    routeChanges: ChangeSetElementRefs = ChangeSetElementRefs(),
    nodeChanges: ChangeSetElementRefs = ChangeSetElementRefs(),
    happy: Boolean = false,
    investigate: Boolean = false
  ): LocationChanges = {
    LocationChanges(
      routeType,
      locationNames,
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

  def newRouteBaseData(
    raw: Raw = newRaw(),
    countries: Seq[Country] = Seq.empty,
    nodeNetwork: Boolean = true,
    routeTypes: Seq[RouteType] = Seq(RouteType.hiking),
    scopes: Seq[RouteScope] = Seq(RouteScope.regional),
    name: String = "",
    meters: Int = 0,
    wayCount: Int = 0,
    proposed: Boolean = false,
    lastUpdated: Timestamp = Timestamps.default,
    lastSurvey: Option[Day] = None,
    unexpectedNodeIds: Seq[Long] = Seq.empty,
    members: Seq[RouteMemberInfo] = Seq.empty,
    nameDerivedFromNodes: Boolean = false,
    nodes: RouteNodes = RouteNodes(),
    analysis: RouteInfoAnalysis = newRouteInfoAnalysis(),
    locationAnalysis: RouteLocationAnalysis = RouteLocationAnalysis(None, Seq.empty, Seq.empty),
    networkNodeIds: Option[Seq[Long]] = None,
    edges: Seq[RouteEdge] = Seq.empty,
  ): RouteBaseData = {
    RouteBaseData(
      raw,
      countries,
      nodeNetwork,
      routeTypes,
      scopes,
      name,
      meters,
      wayCount,
      proposed,
      lastUpdated,
      lastSurvey,
      unexpectedNodeIds,
      members,
      nameDerivedFromNodes,
      nodes,
      analysis,
      locationAnalysis,
      networkNodeIds,
      edges
    )
  }

  def newRouteDoc(
    _id: Long = 1,
    active: Boolean = true,
    labels: Seq[String] = Seq.empty,
    base: RouteBaseData = newRouteBaseData(),
    facts: Seq[Fact] = Seq.empty,
    unexpectedRelationIds: Seq[Long] = Seq.empty,
    segments: Seq[RouteSegment] = Seq.empty,
    superDistance: Long = 0,
    superSegments: Seq[SuperSegment] = Seq.empty,
    paths: Seq[RoutePath] = Seq.empty,
    routeIds: Seq[Long] = Seq.empty,
    structureRows: Seq[RouteStructureRow] = Seq.empty,
    relationCount: Long = 0,
    relationLevels: Long = 0,
    parentRoutes: Seq[ParentRoute] = Seq.empty,
    networkReferences: Seq[Reference] = Seq.empty,
    bounds: Option[Bounds] = None,
  ): RouteDoc = {
    RouteDoc(
      _id,
      active,
      labels,
      base,
      facts,
      unexpectedRelationIds,
      segments,
      superDistance,
      superSegments,
      paths,
      routeIds,
      structureRows,
      relationCount,
      relationLevels,
      parentRoutes,
      networkReferences,
      bounds,
      None,
    )
  }

  def newBaseRouteDoc(
    _id: Long,
    active: Boolean = true,
    base: RouteBaseData = newRouteBaseData(),
    facts: Seq[Fact] = Seq.empty,
    geometryDigest: String = "",
    elementIds: ElementIds = ElementIds(),
    segments: Seq[BaseRouteSegment] = Seq.empty,
    segmentElements: Seq[BaseRouteSegmentElement] = Seq.empty,
    paths: Seq[BaseRoutePath] = Seq.empty,
    subRelationTree: Option[RouteRelation] = None,
    subRouteIds: Seq[Long] = Seq.empty,
    bounds: Option[Bounds] = None
  ): BaseRouteDoc = {
    BaseRouteDoc(
      _id,
      active,
      base,
      facts,
      geometryDigest,
      elementIds,
      segments,
      segmentElements,
      paths,
      subRelationTree,
      subRouteIds,
      bounds
    )
  }

  def newRouteMemberInfo(
    id: Long = 0,
    memberType: MemberType = MemberType.Node,
    role: Option[String] = None,
    name: Option[String] = None,
    poi: Option[String] = None,
    way: Option[RouteMemberInfoWay] = None,
    segmentIds: Seq[Long] = Seq.empty,
    pathIds: Seq[Long] = Seq.empty,
  ): RouteMemberInfo = {
    RouteMemberInfo(
      id,
      memberType,
      role,
      name,
      poi,
      way,
      segmentIds,
      pathIds,
    )
  }

  def newRouteSegment(
    id: Long = 0,
    startNodeId: Long = 0,
    endNodeId: Long = 0,
    meters: Long = 0,
    bounds: Bounds = Bounds(),
    elementIds: Seq[Long] = Seq.empty
  ): RouteSegment = {
    RouteSegment(
      id,
      startNodeId,
      endNodeId,
      meters,
      bounds,
      elementIds
    )
  }

  def newRoutePath(
    id: Long = 0,
    name: String = "forward",
    elementIds: Seq[Long] = Seq.empty
  ): RoutePath = {
    RoutePath(
      id,
      name,
      elementIds
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
    tags: Seq[Tag] = Seq.empty,
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
      ObjectId.get(),
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
    timestamp: Timestamp = Time.now,
    symbol: Option[String] = None,
    analysisTimestamp: Option[Timestamp] = None,
    analysisDuration: Option[Long] = None,
    referenceType: MonitorReferenceType = MonitorReferenceType.gpx,
    referenceTimestamp: Option[Timestamp] = None,
    referenceDistance: Long = 0,
    referenceFilename: Option[String] = None,
    deviationDistance: Long = 0,
    deviationCount: Long = 0,
    osmDistance: Long = 0,
    osmSegmentCount: Long = 0,
    relationIds: Seq[Long] = Seq.empty,
    bounds: Option[Bounds] = None,
    happy: Boolean = false
  ): MonitorRoute = {
    MonitorRoute(
      ObjectId.get(),
      groupId,
      name,
      description,
      comment,
      relationId,
      user,
      timestamp,
      symbol,
      analysisTimestamp,
      analysisDuration,
      referenceType,
      referenceTimestamp,
      referenceFilename,
      referenceDistance,
      deviationDistance,
      deviationCount,
      osmSegmentCount,
      osmDistance,
      relationIds,
      bounds,
      happy
    )
  }

  def newMonitorRouteRelation(
    relationId: Long,
    name: String = "",
    role: Option[String] = None,
    survey: Option[Day] = None,
    symbol: Option[String] = None,
    referenceTimestamp: Option[Timestamp] = None,
    referenceFileName: Option[String] = None,
    referenceDistance: Long = 0,
    deviationDistance: Long = 0,
    deviationCount: Long = 0,
    happy: Boolean = false,
    relations: Seq[MonitorRouteRelation] = Seq.empty
  ): MonitorRouteRelation = {
    MonitorRouteRelation(
      relationId,
      name,
      role,
      survey,
      symbol,
      referenceTimestamp,
      referenceFileName,
      referenceDistance,
      deviationDistance,
      deviationCount,
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

  def newMonitorReference(
    routeId: ObjectId,
    relationId: Option[Long],
    timestamp: Timestamp = Time.now,
    user: String = "",
    bounds: Bounds = Bounds(),
    referenceType: MonitorReferenceType = MonitorReferenceType.gpx,
    referenceTimestamp: Timestamp = Time.now,
    distance: Long = 0,
    segmentCount: Long = 0,
    filename: Option[String] = None,
    referenceLines: Seq[String] = Seq.empty,
    tiles: Seq[MonitorReferenceTile] = Seq.empty,
  ): MonitorReference = {
    MonitorReference(
      ObjectId.get(),
      routeId,
      relationId,
      timestamp,
      user,
      bounds,
      referenceType,
      referenceTimestamp,
      distance,
      segmentCount,
      filename,
      referenceLines,
      tiles
    )
  }

  def newMonitorState(
    _id: ObjectId = ObjectId.get(),
    routeId: ObjectId,
    relationId: Long,
    timestamp: Timestamp = Timestamps.default,
    deviations: Seq[MonitorRouteDeviation] = Seq.empty,
    matchesDistance: Long = 0,
    matchesLines: Seq[String] = Seq.empty,
    segments: Seq[MonitorSegment] = Seq.empty,
  ): MonitorState = {
    MonitorState(
      _id,
      routeId,
      relationId,
      timestamp,
      deviations,
      matchesDistance,
      matchesLines,
      segments
    )
  }

  def newMonitorStateTile(
    _id: ObjectId = ObjectId.get(),
    routeId: ObjectId = ObjectId.get(),
    relationId: Long = 0,
    z: Long = 0,
    x: Long = 0,
    y: Long = 0,
    deviations: Seq[MonitorStateTileDeviation] = Seq.empty,
    matchesLines: Seq[String] = Seq.empty,
    segments: Seq[MonitorSegment] = Seq.empty,
  ): MonitorStateTile = {
    MonitorStateTile(
      _id: ObjectId,
      routeId: ObjectId,
      relationId: Long,
      z: Long,
      x: Long,
      y: Long,
      deviations: Seq[MonitorStateTileDeviation],
      matchesLines: Seq[String],
      segments: Seq[MonitorSegment],
    )
  }

  def newNodeName(
    routeType: RouteType = RouteType.hiking,
    routeScope: RouteScope = RouteScope.regional,
    name: String = "",
    longName: Option[String] = None,
    proposed: Boolean = false
  ): NodeName = {
    NodeName(
      routeType,
      routeScope,
      name,
      longName,
      proposed
    )
  }

  def newNetworkData(
    version: Int = 1,
    timestamp: Timestamp = Timestamps.default,
    changeSetId: Long = 1,
    name: Option[String] = None
  ): NetworkData = {
    NetworkData(MetaData(version, timestamp, changeSetId), name)
  }

  def newNetworkDoc(
    _id: Long,
    active: Boolean = true,
    base: NetworkBaseData = newNetworkBaseData(),
    country: Option[Country] = Some(Country.nl),
    detail: NetworkDetail = newNetworkDetail(),
    facts: Seq[NetworkFact] = Seq.empty,
    nodes: Seq[NetworkInfoNodeDetail] = Seq.empty,
    routes: Seq[NetworkRouteDetail] = Seq.empty,
    factCount: Long = 0,
    nodeCount: Long = 0,
    routeCount: Long = 0,
    extraNodeIds: Seq[Long] = Seq.empty,
    extraWayIds: Seq[Long] = Seq.empty,
    extraRelationIds: Seq[Long] = Seq.empty,
  ): NetworkDoc = {
    NetworkDoc(
      _id,
      active,
      base,
      country,
      detail,
      facts,
      nodes,
      routes,
      factCount,
      nodeCount,
      routeCount,
      extraNodeIds,
      extraWayIds,
      extraRelationIds,
      None
    )
  }

  def newNetworkSummary(
    name: Option[String] = None,
    routeType: RouteType = RouteType.hiking,
    routeScope: RouteScope = RouteScope.regional,
    factCount: Long = 0,
    nodeCount: Long = 0,
    routeCount: Long = 0,
  ): NetworkSummary = {
    NetworkSummary(
      name,
      routeType,
      routeScope,
      factCount,
      nodeCount,
      routeCount,
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
    timestamp: Timestamp = Timestamps.default,
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

  def newNetworkRouteDetail(
    id: Long,
    name: String = "",
    length: Long = 0,
    role: Option[String] = None,
    investigate: Boolean = false,
    accessible: Boolean = true,
    roleConnection: Boolean = false,
    lastUpdated: Timestamp = Timestamps.default,
    lastSurvey: Option[Day] = None,
    proposed: Boolean = false,
    facts: Seq[Fact] = Seq.empty,
    tags: Seq[Tag] = Seq.empty,
    networkNodeIds: Option[Seq[Long]] = None
  ): NetworkRouteDetail = {
    NetworkRouteDetail(
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
      networkNodeIds
    )
  }

  def newNetworkDetail(
    km: Long = 0,
    meters: Long = 0,
    lastUpdated: Timestamp = Timestamps.default,
    relationLastUpdated: Timestamp = Timestamps.default,
    lastSurvey: Option[Day] = None,
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
      lastUpdated,
      relationLastUpdated,
      lastSurvey,
      brokenRouteCount,
      brokenRoutePercentage,
      integrity,
      inaccessibleRouteCount,
      connectionCount,
      center
    )
  }

  def newOrphanNodeInfo(
    nodeId: Long,
    name: String = "",
    longName: Option[String] = None,
    lastUpdated: Timestamp = Timestamps.default,
    proposed: Boolean = false,
    lastSurvey: Option[String] = None,
    factCount: Long = 0
  ): OrphanNodeInfo = {
    OrphanNodeInfo(
      nodeId,
      name,
      longName,
      lastUpdated,
      proposed,
      lastSurvey,
      factCount
    )
  }

  def newOrphanRouteInfo(
    id: Long,
    name: String,
    meters: Long = 0,
    lastSurvey: Option[String] = None,
    lastUpdated: Timestamp = Timestamps.default,
    facts: Seq[Fact] = Seq.empty,
    investigate: Boolean = false
  ): OrphanRouteInfo = {
    OrphanRouteInfo(
      id,
      name,
      meters,
      lastSurvey,
      lastUpdated,
      facts,
      investigate
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

  def newRouteRelation(
    relationId: Long,
    name: String = "",
    role: Option[String] = None,
    relations: Seq[RouteRelation] = Seq.empty
  ): RouteRelation = {
    RouteRelation(
      relationId,
      name,
      role,
      if (relations.nonEmpty) Some(relations) else None
    )
  }

  def newBaseRouteSegmentElement(
    segmentId: Long,
    segmentElementId: Long,
    surface: String = "",
    memberIndexes: Seq[Long] = Seq.empty,
    meters: Long = 0,
    coordinates: String = ""
  ): BaseRouteSegmentElement = {
    BaseRouteSegmentElement(
      segmentId,
      segmentElementId,
      surface,
      memberIndexes,
      meters,
      coordinates
    )
  }

  def newLink(
    memberIndex: Long,
    direction: LinkDirection = LinkDirection.Unconnected,
    hasPrev: Boolean = false,
    hasNext: Boolean = false,
    isLoop: Boolean = false,
    isOnewayLoopForwardPart: Boolean = false,
    isOnewayLoopBackwardPart: Boolean = false,
    isOnewayHead: Boolean = false,
    isOnewayTail: Boolean = false,
  ): Link = {
    Link(
      memberIndex,
      direction,
      hasPrev,
      hasNext,
      isLoop,
      isOnewayLoopForwardPart,
      isOnewayLoopBackwardPart,
      isOnewayHead,
      isOnewayTail,
    )
  }

  def newChangeSetContext(): ChangeSetContext = {
    ChangeSetContext(
      ReplicationId(1),
      newChangeSet(),
      ChangeElementIds()
    )
  }

  def newRouteTileData(
    z: Long = 0,
    x: Long = 0,
    y: Long = 0,
    layer: FeatureLayer = FeatureLayer.route,
    scope: Option[RouteScope] = None,
    survey: Option[String] = None,
    error: Option[String] = None,
    proposed: Boolean = false,
    segments: Seq[RouteTileSegment] = Seq.empty
  ): RouteTileData = {
    RouteTileData(
      z,
      x,
      y,
      layer,
      scope,
      survey,
      error,
      proposed,
      segments
    )
  }

  def newRouteNodeAnalysis(
    id: Long = 0,
    name: String = "",
  ): RouteNodeAnalysis = {
    RouteNodeAnalysis(
      node = newNode(id),
      name = name,
      alternateName = "",
      isInWay = false,
    )
  }

  def newBaseRouteSegment(
    id: Long,
    startNodeId: Long = 0,
    endNodeId: Long = 0,
    meters: Long = 0,
    bounds: Bounds = Bounds(),
    elementIds: Seq[Long] = Seq.empty,
  ): BaseRouteSegment = {
    BaseRouteSegment(
      id,
      startNodeId,
      endNodeId,
      meters,
      bounds,
      elementIds
    )
  }

  def newSuperSegment(
    segments: SuperSubSegment*
  ): SuperSegment = {
    SuperSegment(segments)
  }

  def newOsmChange(actions: Seq[Change] = Seq.empty): OsmChange = {
    OsmChange(actions)
  }

  def newChange(
    action: ChangeAction,
    nodes: Seq[RawNode] = Seq.empty,
    ways: Seq[RawWay] = Seq.empty,
    relations: Seq[RawRelation] = Seq.empty
  ): Change = {
    Change(
      action,
      nodes,
      ways,
      relations
    )
  }

  def newWayUpdate(
    id: Long,
    before: MetaData,
    after: MetaData,
    removedNodeIds: Seq[Long] = Seq.empty,
    addedNodeIds: Seq[Long] = Seq.empty,
    directionReversed: Boolean = false,
    tagDiffs: Option[TagDiffs] = None
  ): WayUpdate = {
    WayUpdate(
      id,
      before,
      after,
      removedNodeIds,
      addedNodeIds,
      directionReversed,
      tagDiffs
    )
  }

  def newWayGeometryUpdate(
    wayId: Long,
    common: Option[Seq[WayLine]] = None,
    added: Option[Seq[WayLine]] = None,
    removed: Option[Seq[WayLine]] = None
  ): WayGeometryUpdate = {
    WayGeometryUpdate(
      wayId,
      common,
      added,
      removed
    )
  }
}

