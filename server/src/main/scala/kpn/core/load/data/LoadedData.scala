package kpn.core.load.data

case class LoadedData(
  networks: Seq[LoadedNetwork],
  orphanRoutes: Seq[LoadedRoute],
  orphanNodes: Seq[LoadedNode]
)
