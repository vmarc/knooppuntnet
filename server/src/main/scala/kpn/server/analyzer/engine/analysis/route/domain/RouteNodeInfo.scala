package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.data.Node

case class RouteNodeInfo(node: Node, name: String, longName: Option[String])
