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

/* TODO migrate

  def nodes: Seq[RouteNode] = startNode.toSeq ++ endNode.toSeq ++ startTentacleNodes ++ endTentacleNodes

  def nodeIds: Seq[Long] = nodes.map(_.nodeId)

  def nodeWithId(id: Long): Option[RouteNode] = {
    nodes.find(_.nodeId == id)
  }

*/
