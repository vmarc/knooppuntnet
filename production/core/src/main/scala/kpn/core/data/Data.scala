package kpn.core.data

import kpn.core.engine.analysis.Interpreter
import kpn.shared.NetworkType
import kpn.shared.Timestamp
import kpn.shared.data.Member
import kpn.shared.data.Node
import kpn.shared.data.NodeMember
import kpn.shared.data.Relation
import kpn.shared.data.RelationMember
import kpn.shared.data.Way
import kpn.shared.data.raw.RawData

case class Data(
  networkType: NetworkType,
  raw: RawData,
  nodes: Map[Long, Node],
  ways: Map[Long, Way],
  relations: Map[Long, Relation]
) {

  def timestamp: Option[Timestamp] = raw.timestamp

  def networkNodes: Seq[Node] = nodes.values.filter(node => isNetworkNode(node)).toSeq

  def isNetworkRelation(relation: Relation): Boolean = {
    new Interpreter(networkType).isNetworkRelation(relation.raw)
  }

  private def isNetworkNode(node: Node): Boolean = {
    node.tags.has(networkType.nodeTagKey)
  }

  def isRouteRelation(relation: Relation): Boolean = {
    relation.tags.has("network", networkType.name) /*&& relation.tags.has("route")*/ && relation.tags.has("type", "route")
  }
}
