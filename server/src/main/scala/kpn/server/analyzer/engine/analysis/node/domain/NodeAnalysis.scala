package kpn.server.analyzer.engine.analysis.node.domain

import kpn.api.common.NodeName
import kpn.api.common.common.Reference
import kpn.api.common.data.MetaData
import kpn.api.common.data.raw.RawNode
import kpn.api.common.node.NodeIntegrity
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.mongo.doc.NodeDoc

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

  def networkTypes: Seq[NetworkType] = {
    nodeNames.map(_.networkType).distinct
  }

  def subsets: Seq[Subset] = {
    country match {
      case Some(c) => networkTypes.map(n => Subset(c, n))
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
      name,
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
      tiles,
      integrity,
      routeReferences
    )
  }
}
