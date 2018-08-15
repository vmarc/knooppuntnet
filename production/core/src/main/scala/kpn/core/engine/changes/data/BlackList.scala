package kpn.core.engine.changes.data

case class BlackList(
  networks: Seq[BlackListEntry] = Seq(),
  routes: Seq[BlackListEntry] = Seq(),
  nodes: Seq[BlackListEntry] = Seq()
) {

  def containsNetwork(networkId: Long): Boolean = networks.exists(_.id == networkId)

  def containsRoute(routeId: Long): Boolean = routes.exists(_.id == routeId)

  def containsNode(nodeId: Long): Boolean = nodes.exists(_.id == nodeId)
}
