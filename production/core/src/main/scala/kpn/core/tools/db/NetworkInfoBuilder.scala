package kpn.core.tools.db

import kpn.core.analysis.Network
import kpn.shared.common.Ref
import kpn.shared.network.Integrity
import kpn.shared.network.NetworkAttributes
import kpn.shared.network.NetworkInfo
import kpn.shared.network.NetworkInfoDetail
import kpn.shared.network.NetworkNodeInfo2
import kpn.shared.network.NetworkRouteInfo

class NetworkInfoBuilder {

  def build(network: Network): NetworkInfo = {

    var number = 0

    val nodes = network.nodes.map { info =>

      if (info.roleConnection && !info.definedInRelation) {
        // do not increment number
      }
      else {
        number = number + 1
      }

      val numberString = if (info.roleConnection) "" else number.toString

      val routeReferences = info.referencedInRoutes.map(route => Ref(route.id, route.summary.name))

      NetworkNodeInfo2(
        info.networkNode.node.id,
        info.networkNode.name,
        numberString,
        info.networkNode.node.latitude,
        info.networkNode.node.longitude,
        info.connection,
        info.roleConnection,
        info.definedInRelation,
        info.definedInRoute,
        info.networkNode.node.timestamp,
        routeReferences,
        info.integrityCheck,
        info.facts,
        info.networkNode.tags
      )
    }

    val routes = network.routes.map { member =>
      val route = member.routeAnalysis.route
      NetworkRouteInfo(
        route.id,
        route.summary.name,
        member.routeAnalysis.ways.size,
        route.summary.meters,
        member.role,
        route.summary.timestamp,
        route.lastUpdated,
        route.facts
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
