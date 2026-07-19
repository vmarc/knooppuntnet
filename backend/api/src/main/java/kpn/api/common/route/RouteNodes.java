package kpn.api.common.route;

import kpn.api.common.route.RouteNode;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record RouteNodes(
  Optional<RouteNode> startNode,
  Optional<RouteNode> endNode,
  ImmutableList<RouteNode> startTentacleNodes,
  ImmutableList<RouteNode> endTentacleNodes,
  ImmutableList<RouteNode> redundantNodes
) {
}

/*
package kpn.api.common.route

case class RouteNodes(
  startNode: Option[RouteNode] = None,
  endNode: Option[RouteNode] = None,
  startTentacleNodes: Seq[RouteNode] = Seq.empty,
  endTentacleNodes: Seq[RouteNode] = Seq.empty,
  redundantNodes: Seq[RouteNode] = Seq.empty
) {
  def nodes: Seq[RouteNode] = startNode.toSeq ++ endNode.toSeq ++ startTentacleNodes ++ endTentacleNodes

  def nodeIds: Seq[Long] = nodes.map(_.nodeId)

  def nodeWithId(id: Long): Option[RouteNode] = {
    nodes.find(_.nodeId == id)
  }
}

*/
