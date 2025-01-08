package kpn.server.analyzer.engine.analysis.node.domain

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NodeName
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.data.MetaData
import kpn.api.common.data.raw.RawNode
import kpn.api.common.node.NodeIntegrity
import kpn.api.custom.Day
import kpn.api.custom.Subset
import kpn.core.doc.NodeDoc

case class NodeAnalysis(
  node: RawNode,
  active: Boolean = true,
  orphan: Boolean = false,
  country: Option[Country] = None,
  lastSurvey: Option[Day] = None,
  facts: Seq[Fact] = Seq.empty,
  name: String = "",
  nodeNames: Seq[NodeName] = Seq.empty,
  locations: Seq[String] = Seq.empty,
  integrity: Option[NodeIntegrity] = None,
  labels: Seq[String] = Seq.empty,
  routeReferences: Seq[Reference] = Seq.empty,
  tiles: Seq[String] = Seq.empty,
  abort: Boolean = false
) {

  def routeTypes: Seq[RouteType] = {
    nodeNames.map(_.routeType).distinct
  }

  def subsets: Seq[Subset] = {
    country match {
      case Some(c) => routeTypes.map(n => Subset(c, n))
      case None => Seq.empty
    }
  }

  def toMeta: MetaData = {
    MetaData(
      node.version,
      node.timestamp,
      node.changeSetId
    )
  }

  def toNodeDoc: NodeDoc = {
    NodeDoc(
      node.id,
      labels,
      country,
      Some(name),
      nodeNames,
      node.version,
      node.changeSetId,
      node.latitude,
      node.longitude,
      node.timestamp,
      lastSurvey,
      node.tags,
      facts,
      locations,
      integrity,
      routeReferences
    )
  }
}
