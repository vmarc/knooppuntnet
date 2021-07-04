package kpn.server.repository

import kpn.api.common.common.Ref
import kpn.api.common.network.Integrity
import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkInfoDetail
import kpn.api.common.network.NetworkInfoNode
import kpn.api.common.network.NetworkInfoRoute
import kpn.core.analysis.Network

class NetworkInfoBuilder {

  def build(network: Network): NetworkInfo = {

    val nodes = network.nodes.map { info =>

      val routeReferences = info.referencedInRoutes.map(route => Ref(route.id, route.summary.name))

      NetworkInfoNode(
        info.networkNode.node.id,
        info.networkNode.name,
        info.networkNode.longName,
        info.networkNode.node.latitude,
        info.networkNode.node.longitude,
        info.connection,
        info.roleConnection,
        info.definedInRelation,
        info.definedInRoute,
        info.networkNode.tags.has("state", "proposed"),
        info.networkNode.node.timestamp,
        info.lastSurvey,
        routeReferences,
        info.integrityCheck,
        info.facts,
        info.networkNode.tags
      )
    }

    val routes = network.routes.map { member =>
      val route = member.routeAnalysis.route
      NetworkInfoRoute(
        route.id,
        route.summary.name,
        member.routeAnalysis.ways.size,
        route.summary.meters,
        member.role,
        route.summary.timestamp,
        route.lastUpdated,
        route.lastSurvey,
        route.facts,
        route.tags.has("state", "proposed")
      )
    }

    val integrity = Integrity(
      network.isIntegrityCheckOk,
      network.hasIntegrityChecks,
      if (network.integrityCheckCount > 0) network.integrityCheckCount.toString else "",
      network.integrityCheckOkCount,
      network.integrityCheckNokCount,
      network.integrityCheckCoverage,
      network.integrityCheckOkRate,
      network.integrityCheckNokRate
    )

    val center = new NetworkCenterCalculator().calculate(network)

    val attributes = NetworkAttributes(
      id = network.id,
      country = network.country,
      networkType = network.networkType,
      networkScope = network.networkScope,
      name = network.name,
      km = network.length / 1000,
      meters = network.length,
      nodeCount = nodes.size,
      routeCount = routes.size,
      brokenRouteCount = network.brokenRouteCount,
      brokenRoutePercentage = network.brokenRoutePercentage,
      integrity = integrity,
      unaccessibleRouteCount = network.unAccessibleRouteCount,
      connectionCount = network.connectionCount,
      lastUpdated = network.lastUpdated,
      relationLastUpdated = network.relationLastUpdated,
      center = center
    )

    val detail = NetworkInfoDetail(
      nodes = nodes,
      routes = routes,
      networkFacts = network.facts,
      shape = network.shape
    )

    NetworkInfo(
      attributes.id,
      attributes,
      active = true,
      nodeRefs = nodes.map(_.id),
      routeRefs = routes.map(_.id),
      networkRefs = Seq.empty,
      facts = Seq.empty,
      tags = network.relation.tags,
      detail = Some(detail)
    )
  }
}
