package kpn.server.analyzer.load.data

case class LoadedData(
  networks: Seq[LoadedNetwork],
  orphanRoutes: Seq[LoadedRoute],
  orphanNodes: Seq[LoadedNode]
)
