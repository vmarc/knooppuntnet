package kpn.server.analyzer.engine.analysis.network.main.analyzers

case class NetworkNodeIntegrity(nodeId: Long, expectedRouteCount: Int, actualRouteCount: Int) {

  def ok: Boolean = expectedRouteCount == actualRouteCount

  def notOk: Boolean = !ok
}
