package kpn.core.analysis

import kpn.api.common.Country
import kpn.api.common.Fact.RouteInaccessible
import kpn.api.common.NetworkFacts
import kpn.api.common.Relation
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.common.Ref
import kpn.api.common.network.NetworkShape
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.util.Formatter.percentage

case class Network(
  country: Option[Country],
  routeType: RouteType,
  routeScope: RouteScope,
  relation: Relation,
  name: String,
  nodes: Seq[NetworkNodeInfo],
  routes: Seq[NetworkMemberRoute],
  shape: Option[NetworkShape],
  facts: NetworkFacts
) {

  def id: Long = relation.id

  def toRef: Ref = Ref(id, name)

  def subset: Option[Subset] = country.flatMap(c => Subset.of(c, routeType))

  def length: Long = routes.map(_.data.meters).sum

  def nodeCount: Int = nodes.filterNot(n => n.roleConnection && !n.definedInRelation).size

  def routeCount: Int = routes.size

  def brokenRouteCount: Int = brokenRoutes.size

  private def brokenRoutes: Seq[NetworkMemberRoute] = routes.filter(_.data.facts.exists(Facts.isError))

  def brokenRoutePercentage: String = percentage(brokenRouteCount, routeCount)

  def connectionCount: Int = {
    routes.count { routeMember =>
      routeMember.role match {
        case Some(role) => role == "connection"
        case None => false
      }
    }
  }

  def hasConnections: Boolean = {
    routes.exists { routeMember =>
      routeMember.role match {
        case Some(role) => role == "connection"
        case None => false
      }
    }
  }

  def relationLastUpdated: Timestamp = relation.timestamp

  def lastUpdated: Timestamp = {
    val relationUpdates = Seq(relation.timestamp)
    val nodeUpdates = nodes.map(_.networkNode.node.timestamp)
    val routeUpdates = routes.map(_.data.meta.timestamp)
    val timestamp: Seq[Timestamp] = relationUpdates ++ nodeUpdates ++ routeUpdates
    timestamp.max
  }

  def hasIntegrityChecks: Boolean = nodes.exists(_.hasIntegrityCheck)

  def hasPartialIntegrityCheck: Boolean = {
    val checkCount = nodes.count(_.hasIntegrityCheck)
    if (checkCount == 0) {
      false
    }
    else {
      checkCount < nodes.size
    }
  }

  def hasCompleteIntegrityCheck: Boolean = {
    val checkCount = nodes.count(_.hasIntegrityCheck)
    if (checkCount == 0) {
      false
    }
    else {
      checkCount == nodes.size
    }
  }

  def isIntegrityCheckOk: Boolean = !hasFailedIntegrityChecks

  def hasFailedIntegrityChecks: Boolean = nodes.exists(!_.integrityCheckOk)

  def hasIntegrityCheck: Boolean = nodes.exists(_.hasIntegrityCheck)

  def failedIntegrityCheckNodes: Seq[NetworkNodeInfo] = nodes.filter(!_.integrityCheckOk)

  def integrityCheckCoverage: String = percentage(integrityCheckCount, nodes.size)

  def integrityCheckOkRate: String = percentage(integrityCheckOkCount, integrityCheckCount)

  def integrityCheckNokRate: String = percentage(integrityCheckNokCount, integrityCheckCount)

  def integrityCheckCount: Int = nodes.count(_.hasIntegrityCheck)

  def integrityCheckOkCount: Int = nodes.count(n => n.hasIntegrityCheck && n.integrityCheckOk)

  def integrityCheckNokCount: Int = integrityCheckCount - integrityCheckOkCount

  def inaccessibleRouteCount: Int = routes.count(_.data.facts.contains(RouteInaccessible))

  def extraMemberNodeIds: Seq[Long] = {
    facts.networkExtraMemberNode match {
      case None => Seq.empty
      case Some(xs) => xs.map(_.memberId).sorted
    }
  }

  def extraMemberWayIds: Seq[Long] = {
    facts.networkExtraMemberWay match {
      case None => Seq.empty
      case Some(xs) => xs.map(_.memberId).sorted
    }
  }

  def extraMemberRelationIds: Seq[Long] = {
    facts.networkExtraMemberRelation match {
      case None => Seq.empty
      case Some(xs) => xs.map(_.memberId).sorted
    }
  }
}
