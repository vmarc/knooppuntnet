package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.data.Node

case class RouteNodeData(
  node: Node,
  name: String,
  isInWay: Boolean,
)
