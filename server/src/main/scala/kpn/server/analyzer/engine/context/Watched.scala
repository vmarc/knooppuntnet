package kpn.server.analyzer.engine.context

case class Watched(
  networks: ElementIdSet = ElementIdSet(),
  routes: ElementIdMap = ElementIdMap(),
  nodes: ElementIdSet = ElementIdSet()
)
