package kpn.server.analyzer.engine.tiles

import kpn.api.common.route.RouteInfo
import kpn.api.custom.NetworkType
import kpn.core.mongo.doc.NodeDoc
import kpn.server.analyzer.engine.tiles.domain.TileDataNode

case class TileAnalysis(
  networkType: NetworkType,
  nodes: Seq[TileDataNode],
  orphanNodes: Seq[NodeDoc],
  routeInfos: Seq[RouteInfo]
)
