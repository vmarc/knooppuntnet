package kpn.server.analyzer.engine.changes.data

import kpn.api.base.WithStringId

object Blacklist {

  val id: String = "blacklist"

  def apply(
    networks: Seq[BlacklistEntry] = Seq.empty,
    routes: Seq[BlacklistEntry] = Seq.empty,
    nodes: Seq[BlacklistEntry] = Seq.empty
  ): Blacklist = {
    Blacklist(
      id,
      networks,
      routes,
      nodes
    )
  }
}

case class Blacklist(
  _id: String,
  networks: Seq[BlacklistEntry],
  routes: Seq[BlacklistEntry],
  nodes: Seq[BlacklistEntry]
) extends WithStringId {

  def containsNetwork(networkId: Long): Boolean = networks.exists(_.id == networkId)

  def containsRoute(routeId: Long): Boolean = routes.exists(_.id == routeId)

  def containsNode(nodeId: Long): Boolean = nodes.exists(_.id == nodeId)
}
