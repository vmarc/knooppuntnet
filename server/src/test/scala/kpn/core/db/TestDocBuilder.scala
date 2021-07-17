package kpn.core.db

import kpn.api.common.NetworkFacts
import kpn.api.common.NodeName
import kpn.api.common.SharedTestObjects
import kpn.api.common.network.Integrity
import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkInfoDetail
import kpn.api.common.network.NetworkInfoNode
import kpn.api.common.network.NetworkInfoRoute
import kpn.api.common.network.NetworkShape
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.database.Database
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepository
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteRepositoryImpl

class TestDocBuilder(database: Database) extends SharedTestObjects {

  private val networkRepository: NetworkRepository = new NetworkRepositoryImpl(null, database, false)
  private val nodeRepository: NodeRepository = new NodeRepositoryImpl(null, database, false)
  private val routeRepository: RouteRepository = new RouteRepositoryImpl(null, database, false)

  def networkInfoDetail(
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

  def networkRouteInfo(id: Long, name: String = "name", facts: Seq[Fact] = Seq.empty, proposed: Boolean): NetworkInfoRoute = {
    NetworkInfoRoute(
      id,
      name = name,
      wayCount = 0,
      length = 0,
      role = None,
      relationLastUpdated = Timestamp(11, 8, 2015),
      lastUpdated = Timestamp(11, 8, 2015),
      lastSurvey = None,
      facts = facts,
      proposed
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
      NetworkScope.regional,
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
      None
    )

    val networkInfo = NetworkInfo(
      attributes.id,
      attributes,
      active,
      Seq.empty,
      Seq.empty,
      Seq.empty,
      facts,
      Tags.empty,
      detail
    )

    networkRepository.save(networkInfo)
  }

  def node(
    id: Long,
    country: Country = Country.nl,
    names: Seq[NodeName] = Seq.empty,
    tags: Tags = Tags.empty,
    active: Boolean = true,
    orphan: Boolean = false,
    facts: Seq[Fact] = Seq.empty
  ): Unit = {
    nodeRepository.save(
      newNodeInfo(
        id,
        country = Some(country),
        names = names,
        tags = tags,
        active = active,
        orphan = orphan,
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
      map = newRouteMap(
        startNodes = Seq(startNode),
        endNodes = Seq(endNode)
      )
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
