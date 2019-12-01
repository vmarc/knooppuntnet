package kpn.server.analyzer.engine.tiles

import kpn.api.custom.NetworkType

trait TileAnalyzer {

  def analysis(networkType: NetworkType): TileAnalysis

}
