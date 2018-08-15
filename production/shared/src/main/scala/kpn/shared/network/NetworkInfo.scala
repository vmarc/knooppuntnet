package kpn.shared.network

import kpn.shared.Fact
import kpn.shared.common.ToStringBuilder
import kpn.shared.data.Tags

case class NetworkInfo(
  attributes: NetworkAttributes,
  active: Boolean,
  ignored: Boolean,
  nodeRefs: Seq[Long],
  routeRefs: Seq[Long],
  networkRefs: Seq[Long],
  facts: Seq[Fact] = Seq(),
  tags: Tags,
  detail: Option[NetworkInfoDetail] = None
) {

  def id: Long = attributes.id

  def hasFacts: Boolean = facts.nonEmpty || detail.get.routes.exists(r => r.facts.nonEmpty)

  def hasRoutesWithFact(fact: Fact): Boolean = detail.get.routes.exists(_.facts.contains(fact))

  def routesWithFact(fact: Fact): Seq[NetworkRouteInfo] = detail.get.routes.filter(_.facts.contains(fact))

  def factCount: Int = {
    val networkFactCount = detail match {
      case Some(networkInfoDetail) => networkInfoDetail.networkFacts.factCount
      case None => 0
    }
    val routeFactCount = detail match {
      case Some(networkInfoDetail) => networkInfoDetail.routes.map(_.facts.count(Fact.reportedFacts.contains)).sum
      case None => 0
    }
    facts.size + networkFactCount + routeFactCount
  }

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("attributes", attributes).
    field("active", active).
    field("ignored", ignored).
    field("nodeRefs", nodeRefs).
    field("routeRefs", routeRefs).
    field("networkRefs", networkRefs).
    field("facts", facts).
    field("tags", tags).
    field("detail", detail).
    build
}
