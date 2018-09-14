package kpn.shared

import kpn.shared.changes.Change
import kpn.shared.changes.ChangeSet
import kpn.shared.changes.details.ChangeKey
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RefBooleanChange
import kpn.shared.changes.details.RefChanges
import kpn.shared.changes.details.RouteChange
import kpn.shared.common.Ref
import kpn.shared.data.Node
import kpn.shared.data.Tags
import kpn.shared.data.Way
import kpn.shared.data.raw.RawMember
import kpn.shared.data.raw.RawNode
import kpn.shared.data.raw.RawRelation
import kpn.shared.data.raw.RawWay
import kpn.shared.diff.IdDiffs
import kpn.shared.diff.NetworkDataUpdate
import kpn.shared.diff.RefDiffs
import kpn.shared.diff.RouteData
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.WayUpdate
import kpn.shared.diff.common.FactDiffs
import kpn.shared.diff.network.NodeRouteReferenceDiffs
import kpn.shared.diff.node.NodeMoved
import kpn.shared.diff.route.RouteDiff
import kpn.shared.network.Integrity
import kpn.shared.network.NetworkAttributes
import kpn.shared.network.NetworkInfo
import kpn.shared.network.NetworkInfoDetail
import kpn.shared.network.NetworkNodeInfo2
import kpn.shared.network.NetworkRouteInfo
import kpn.shared.node.NodeNameAnalyzer.name
import kpn.shared.node.NodeNameAnalyzer.rcnName
import kpn.shared.node.NodeNameAnalyzer.rwnName
import kpn.shared.node.NodeNameAnalyzer.rhnName
import kpn.shared.node.NodeNameAnalyzer.rmnName
import kpn.shared.node.NodeNameAnalyzer.rpnName
import kpn.shared.node.NodeNameAnalyzer.rinName
import kpn.shared.route.RouteInfo
import kpn.shared.route.RouteInfoAnalysis
import kpn.shared.route.RouteMap
import kpn.shared.route.RouteMemberInfo
import kpn.shared.route.RouteNetworkNodeInfo

trait SharedTestObjects {

  val defaultTimestamp: Timestamp = Timestamp(2015, 8, 11, 0, 0, 0)

  val timestampBeforeValue = Timestamp(2015, 8, 11, 0, 0, 1)
  val timestampFromValue = Timestamp(2015, 8, 11, 0, 0, 2)
  val timestampUntilValue = Timestamp(2015, 8, 11, 0, 0, 3)
  val timestampAfterValue = Timestamp(2015, 8, 11, 0, 0, 4)

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
    nodeIds: Seq[Long] = Seq(),
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
    members: Seq[RawMember] = Seq(),
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
    Tags.from("network" -> "rwn", "type" -> "network", "name" -> name)
  }

  def newRouteTags(name: String = ""): Tags = {
    Tags.from("network" -> "rwn", "type" -> "route", "route" -> "foot", "note" -> name)
  }

  def newNodeTags(name: String = ""): Tags = {
    Tags.from("rwn_ref" -> name)
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

  def newRouteChange(
    key: ChangeKey = newChangeKey(),
    changeType: ChangeType = ChangeType.Create,
    name: String = "",
    addedToNetwork: Seq[Ref] = Seq(),
    removedFromNetwork: Seq[Ref] = Seq(),
    before: Option[RouteData] = None,
    after: Option[RouteData] = None,
    removedWays: Seq[RawWay] = Seq(),
    addedWays: Seq[RawWay] = Seq(),
    updatedWays: Seq[WayUpdate] = Seq(),
    diffs: RouteDiff = RouteDiff(),
    facts: Seq[Fact] = Seq()
  ): RouteChange = {
    RouteChange(
      key,
      changeType,
      name,
      addedToNetwork,
      removedFromNetwork,
      before,
      after,
      removedWays,
      addedWays,
      updatedWays,
      diffs,
      facts
    )
  }

  def newRouteData(
    country: Option[Country] = None,
    networkType: NetworkType = NetworkType.hiking,
    relation: RawRelation = newRawRelation(),
    name: String = "",
    networkNodes: Seq[RawNode] = Seq(),
    nodes: Seq[RawNode] = Seq(),
    ways: Seq[RawWay] = Seq(),
    relations: Seq[RawRelation] = Seq(),
    facts: Seq[Fact] = Seq()
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
    nodes: Seq[Node] = Seq(),
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
    display: Boolean = true,
    ignored: Boolean = false,
    orphan: Boolean = false,
    country: Option[Country] = None,
    latitude: String = "0",
    longitude: String = "0",
    lastUpdated: Timestamp = defaultTimestamp,
    tags: Tags = Tags.empty,
    facts: Seq[Fact] = Seq()
  ): NodeInfo = {
    NodeInfo(
      id,
      active,
      display,
      ignored,
      orphan,
      country,
      name(tags),
      rcnName(tags),
      rwnName(tags),
      rhnName(tags),
      rmnName(tags),
      rpnName(tags),
      rinName(tags),
      latitude,
      longitude,
      lastUpdated,
      tags,
      facts
    )
  }

  def newRoute(
    id: Long = 0,
    active: Boolean = true,
    display: Boolean = true,
    ignored: Boolean = false,
    orphan: Boolean = false,
    country: Option[Country] = None,
    networkType: NetworkType = NetworkType.hiking,
    name: String = "",
    meters: Int = 0,
    wayCount: Int = 0,
    lastUpdated: Timestamp = defaultTimestamp,
    lastUpdatedBy: String = "",
    relationLastUpdated: Timestamp = defaultTimestamp,
    analysis: RouteInfoAnalysis = newRouteInfoAnalysis()
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
      nodeNames = Seq(),
      tags = Tags.empty
    )

    RouteInfo(
      summary,
      active,
      display,
      ignored,
      orphan,
      version = 0,
      changeSetId = 0,
      lastUpdated,
      Tags.empty,
      Seq(),
      Some(analysis)
    )
  }

  def newNetwork(
    id: Long,
    country: Option[Country] = None,
    networkType: NetworkType = NetworkType.hiking,
    name: String = "",
    active: Boolean = true,
    facts: Seq[Fact] = Seq(),
    networkFacts: NetworkFacts = NetworkFacts(),
    detail: Option[NetworkInfoDetail] = None,
    nodes: Seq[NetworkNodeInfo2] = Seq(),
    routes: Seq[NetworkRouteInfo] = Seq(),
    tags: Tags = Tags.empty
  ): NetworkInfo = {

    val attributes = NetworkAttributes(
      id,
      country,
      networkType,
      name,
      km = 0,
      meters = 0,
      nodeCount = nodes.size,
      routeCount = routes.size,
      brokenRouteCount = 0,
      brokenRoutePercentage = "",
      integrity = newIntegrity(),
      unaccessibleRouteCount = 0,
      connectionCount = 0,
      Timestamp(2015, 8, 11),
      Timestamp(2015, 8, 11),
      center = None
    )

    NetworkInfo(
      attributes,
      active = active,
      ignored = false,
      Seq(),
      Seq(),
      Seq(),
      facts,
      tags,
      Some(
        NetworkInfoDetail(
          nodes = nodes,
          routes = routes,
          networkFacts = networkFacts,
          shape = None
        )
      )
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

  def newNetworkNodeInfo2(
    id: Long,
    title: String,
    number: String = "",
    latitude: String = "",
    longitude: String = "",
    connection: Boolean = false,
    definedInRelation: Boolean = false,
    definedInRoute: Boolean = false,
    timestamp: Timestamp = defaultTimestamp,
    routeReferences: Seq[Ref] = Seq(),
    integrityCheck: Option[NodeIntegrityCheck] = None,
    tags: Tags = Tags.empty
  ): NetworkNodeInfo2 = {
    NetworkNodeInfo2(
      id,
      title,
      number,
      latitude,
      longitude,
      connection,
      definedInRelation,
      definedInRoute,
      timestamp,
      routeReferences,
      integrityCheck,
      tags
    )
  }

  def newNetworkRouteInfo(
    id: Long,
    name: String,
    wayCount: Int = 0,
    length: Int = 0, // length in meter
    role: Option[String] = None,
    relationLastUpdated: Timestamp = defaultTimestamp,
    lastUpdated: Timestamp = defaultTimestamp,
    facts: Seq[Fact] = Seq()
  ): NetworkRouteInfo = {
    NetworkRouteInfo(
      id,
      name,
      wayCount,
      length,
      role,
      relationLastUpdated,
      lastUpdated: Timestamp,
      facts
    )
  }

  def newRouteInfoAnalysis(
    startNodes: Seq[RouteNetworkNodeInfo] = Seq(),
    endNodes: Seq[RouteNetworkNodeInfo] = Seq(),
    startTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq(),
    endTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq(),
    unexpectedNodeIds: Seq[Long] = Seq(),
    members: Seq[RouteMemberInfo] = Seq(),
    tags: Tags = Tags.empty,
    expectedName: String = "",
    facts: Seq[String] = Seq(),
    map: RouteMap = RouteMap(),
    structureStrings: Seq[String] = Seq()
  ): RouteInfoAnalysis = {
    RouteInfoAnalysis(
      startNodes,
      endNodes,
      startTentacleNodes,
      endTentacleNodes,
      unexpectedNodeIds,
      members,
      expectedName,
      map,
      structureStrings
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
    nodeNames: Seq[String] = Seq(),
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
    changes: Seq[Change] = Seq()
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
    subsets: Seq[Subset] = Seq(),
    name: String = "",
    before: Option[RawNode] = None,
    after: Option[RawNode] = None,
    connectionChanges: Seq[RefBooleanChange] = Seq(),
    definedInNetworkChanges: Seq[RefBooleanChange] = Seq(),
    tagDiffs: Option[TagDiffs] = None,
    nodeMoved: Option[NodeMoved] = None,
    addedToRoute: Seq[Ref] = Seq(),
    removedFromRoute: Seq[Ref] = Seq(),
    addedToNetwork: Seq[Ref] = Seq(),
    removedFromNetwork: Seq[Ref] = Seq(),
    factDiffs: FactDiffs = FactDiffs(),
    facts: Seq[Fact] = Seq()
  ): NodeChange = {
    NodeChange(
      key,
      changeType,
      subsets,
      name,
      before,
      after,
      connectionChanges,
      definedInNetworkChanges,
      tagDiffs,
      nodeMoved,
      addedToRoute,
      removedFromRoute,
      addedToNetwork,
      removedFromNetwork,
      factDiffs,
      facts
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
    ignoredRoutes: RefChanges = RefChanges.empty,
    orphanNodes: RefChanges = RefChanges.empty,
    ignoredNodes: RefChanges = RefChanges.empty,
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
      ignoredRoutes,
      orphanNodes,
      ignoredNodes,
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
    oldRefs: Seq[Ref] = Seq(),
    newRefs: Seq[Ref] = Seq()
  ): RefChanges = {
    RefChanges(
      oldRefs,
      newRefs
    )
  }

  def newChangeSetSummary(
    key: ChangeKey = newChangeKey(),
    subsets: Seq[Subset] = Seq(),
    timestampFrom: Timestamp = timestampFromValue,
    timestampUntil: Timestamp = timestampUntilValue,
    networkChanges: NetworkChanges = NetworkChanges(),
    orphanRouteChanges: Seq[ChangeSetSubsetElementRefs] = Seq(),
    orphanNodeChanges: Seq[ChangeSetSubsetElementRefs] = Seq(),
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
    ignored: Boolean = false,
    nodeRefs: Seq[Long] = Seq(),
    routeRefs: Seq[Long] = Seq(),
    networkRefs: Seq[Long] = Seq(),
    facts: Seq[Fact] = Seq(),
    tags: Tags = Tags.empty,
    detail: Option[NetworkInfoDetail] = None
  ): NetworkInfo = {
    NetworkInfo(
      attributes,
      active,
      ignored,
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
    display: Boolean = true,
    ignored: Boolean = false,
    orphan: Boolean = false,
    version: Int = 1,
    changeSetId: Long = 1,
    lastUpdated: Timestamp = defaultTimestamp,
    tags: Tags = Tags.empty,
    facts: Seq[Fact] = Seq(),
    analysis: Option[RouteInfoAnalysis] = None
  ): RouteInfo = {
    RouteInfo(
      summary,
      active,
      display,
      ignored,
      orphan,
      version,
      changeSetId,
      lastUpdated,
      tags,
      facts,
      analysis
    )
  }

  def newNodeRouteReferenceDiffs(
    removed: Seq[Ref] = Seq(),
    added: Seq[Ref] = Seq(),
    remaining: Seq[Ref] = Seq()
  ): NodeRouteReferenceDiffs = {
    NodeRouteReferenceDiffs(
      removed,
      added,
      remaining
    )
  }
}
