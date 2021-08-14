package kpn.server.analyzer.engine.changes.data

import kpn.server.analyzer.engine.changes.ElementIdMap

case class Watched(
  networks: ElementIdSet = ElementIdSet(),
  routes: ElementIdMap = ElementIdMap(),
  nodes: ElementIdSet = ElementIdSet()
)
