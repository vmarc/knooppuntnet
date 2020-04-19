package kpn.api.common.network

import kpn.api.common.common.ToStringBuilder
import kpn.api.custom.Fact
import kpn.api.custom.Tags

case class NetworkInfo(
  attributes: NetworkAttributes,
  active: Boolean,
  nodeRefs: Seq[Long],
  routeRefs: Seq[Long],
  networkRefs: Seq[Long],
  facts: Seq[Fact] = Seq.empty,
  tags: Tags,
  detail: Option[NetworkInfoDetail] = None
) {

  def id: Long = attributes.id

  def hasFacts: Boolean = {
    facts.nonEmpty || hasNodeFacts || hasRouteFacts
  }

  def hasNodesWithFact(fact: Fact): Boolean = {
    detail match {
      case Some(d) => d.nodes.exists(_.facts.contains(fact))
      case None => false
    }
  }

  def hasRoutesWithFact(fact: Fact): Boolean = {
    detail match {
      case Some(d) => d.routes.exists(_.facts.contains(fact))
      case None => false
    }
  }

  def nodesWithFact(fact: Fact): Seq[NetworkInfoNode] = {
    detail match {
      case Some(d) => d.nodes.filter(_.facts.contains(fact))
      case None => Seq.empty
    }
  }

  def routesWithFact(fact: Fact): Seq[NetworkInfoRoute] = {
    detail match {
      case Some(d) => d.routes.filter(_.facts.contains(fact))
      case None => Seq.empty
    }
  }

  def factCount: Int = {
    facts.size + networkFactCount + nodeFactCount + routeFactCount
  }

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("attributes", attributes).
    field("active", active).
    field("nodeRefs", nodeRefs).
    field("routeRefs", routeRefs).
    field("networkRefs", networkRefs).
    field("facts", facts).
    field("tags", tags).
    field("detail", detail).
    build

  private def hasNodeFacts: Boolean = {
    detail match {
      case Some(d) => d.nodes.exists(_.facts.nonEmpty)
      case None => false
    }
  }

  private def hasRouteFacts: Boolean = {
    detail match {
      case Some(d) => d.routes.exists(_.facts.nonEmpty)
      case None => false
    }
  }

  private def networkFactCount: Int = {
    detail match {
      case Some(d) => d.networkFacts.factCount
      case None => 0
    }
  }

  private def nodeFactCount: Int = {
    detail match {
      case Some(d) => d.nodes.map(_.facts.count(Fact.reportedFacts.contains)).sum
      case None => 0
    }
  }

  private def routeFactCount: Int = {
    detail match {
      case Some(d) => d.routes.map(_.facts.count(Fact.reportedFacts.contains)).sum
      case None => 0
    }
  }

}
