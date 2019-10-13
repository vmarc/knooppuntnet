package kpn.server.analyzer.engine.changes.data

case class BlackList(
  networks: Seq[BlackListEntry] = Seq.empty,
  routes: Seq[BlackListEntry] = Seq.empty,
  nodes: Seq[BlackListEntry] = Seq.empty
) {

  def containsNetwork(networkId: Long): Boolean = networks.exists(_.id == networkId)

  def containsRoute(routeId: Long): Boolean = routes.exists(_.id == routeId)

  def containsNode(nodeId: Long): Boolean = nodes.exists(_.id == nodeId)
}
