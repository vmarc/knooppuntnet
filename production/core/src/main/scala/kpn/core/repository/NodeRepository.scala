package kpn.core.repository

import akka.util.Timeout
import kpn.shared.NetworkType
import kpn.shared.NodeInfo
import kpn.shared.node.NodeReferences

trait NodeRepository {

  def save(nodes: NodeInfo*): Boolean

  def delete(nodeId: Long): Unit

  def nodeWithId(nodeId: Long, timeout: Timeout): Option[NodeInfo]

  def nodesWithIds(nodeIds: Seq[Long], timeout: Timeout, stale: Boolean = true): Seq[NodeInfo]

  def nodeReferences(nodeId: Long, timeout: Timeout, stale: Boolean = true): NodeReferences

  def networkTypeNodeReferences(networkType: NetworkType, nodeId: Long, timeout: Timeout, stale: Boolean = true): NodeReferences

  def filterKnown(nodeIds: Set[Long]): Set[Long]
}
