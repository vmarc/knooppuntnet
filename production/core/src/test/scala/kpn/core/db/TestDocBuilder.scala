package kpn.core.db

import kpn.core.db.couch.Database
import kpn.core.repository.NetworkRepository
import kpn.core.repository.NetworkRepositoryImpl
import kpn.core.repository.NodeRepository
import kpn.core.repository.NodeRepositoryImpl
import kpn.core.repository.RouteRepository
import kpn.core.repository.RouteRepositoryImpl
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkFacts
import kpn.shared.SharedTestObjects
import kpn.shared.Subset
import kpn.shared.Timestamp
import kpn.shared.data.Tags
import kpn.shared.network.Integrity
import kpn.shared.network.NetworkAttributes
import kpn.shared.network.NetworkInfo
import kpn.shared.network.NetworkInfoDetail
import kpn.shared.network.NetworkNodeInfo2
import kpn.shared.network.NetworkRouteInfo
import kpn.shared.network.NetworkShape
import kpn.shared.route.RouteNetworkNodeInfo

class TestDocBuilder(database: Database) extends SharedTestObjects {

  private val networkRepository: NetworkRepository = new NetworkRepositoryImpl(database)
  private val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)
  private val routeRepository: RouteRepository = new RouteRepositoryImpl(database)

  def networkInfoDetail(
    nodes: Seq[NetworkNodeInfo2] = Seq.empty,
    routes: Seq[NetworkRouteInfo] = Seq.empty,
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

  def networkRouteInfo(id: Long, name: String = "name", facts: Seq[Fact] = Seq.empty): NetworkRouteInfo = {
    NetworkRouteInfo(
      id,
      name = name,
      wayCount = 0,
      length = 0,
      role = None,
      relationLastUpdated = Timestamp(11, 8, 2015),
      lastUpdated = Timestamp(11, 8, 2015),
      facts = facts
    )
  }

  def network(
    id: Long,
    subset: Subset,
    name: String = "name",
    facts: Seq[Fact] = Seq.empty,
    detail: Option[NetworkInfoDetail] = None,
    meters: Int = 0,
    nodeCount: Int = 0,
    routeCount: Int = 0,
    active: Boolean = true,
    ignored: Boolean = false
  ): Unit = {
    val attributes = NetworkAttributes(
      id,
      Some(subset.country),
      subset.networkType,
      name,
      km = 0,
      meters = meters,
      nodeCount = nodeCount,
      routeCount = routeCount,
      brokenRouteCount = 0,
      brokenRoutePercentage = "",
      integrity = Integrity(),
      unaccessibleRouteCount = 0,
      connectionCount = 0,
      Timestamp(2015, 8, 11),
      Timestamp(2015, 8, 11),
      None,
      tagged = false
    )

    val networkInfo = NetworkInfo(
      attributes,
      active,
      ignored,
      Seq(),
      Seq(),
      Seq(),
      facts,
      Tags.empty,
      detail
    )

    networkRepository.save(networkInfo)
  }

  def node(
    id: Long,
    country: Country = Country.nl,
    tags: Tags = Tags.empty,
    active: Boolean = true,
    orphan: Boolean = false,
    ignored: Boolean = false,
    facts: Seq[Fact] = Seq.empty
  ): Unit = {
    nodeRepository.save(
      newNodeInfo(
        id,
        country = Some(country),
        tags = tags,
        active = active,
        orphan = orphan,
        ignored = ignored,
        facts = facts
      )
    )
  }

  def route(
    id: Long,
    subset: Subset,
    name: String = "01-02",
    active: Boolean = true,
    orphan: Boolean = false,
    ignored: Boolean = false,
    facts: Seq[Fact] = Seq.empty
  ): Unit = {
    routeRepository.save(
      newRoute(
        id = id,
        country = Some(subset.country),
        networkType = subset.networkType,
        name = name,
        active = active,
        orphan = orphan,
        ignored = ignored,
        facts = facts
      )
    )
  }

  def route(
    subset: Subset,
    startNodeName: String,
    endNodeName: String,
    routeId: Long,
    startNodeId: Long,
    endNodeId: Long,
    meters: Int
  ): Unit = {

    val routeName = "%s-%s".format(startNodeName, endNodeName)
    val startNode = RouteNetworkNodeInfo(startNodeId, startNodeName, startNodeName)
    val endNode = RouteNetworkNodeInfo(endNodeId, endNodeName, endNodeName)

    val routeAnalysis = newRouteInfoAnalysis(
      startNodes = Seq(startNode),
      endNodes = Seq(endNode)
    )

    routeRepository.save(
      newRoute(
        id = routeId,
        country = Some(subset.country),
        networkType = subset.networkType,
        name = routeName,
        meters = meters,
        analysis = routeAnalysis
      )
    )
  }
}
