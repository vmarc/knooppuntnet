package kpn.core.tiles

import kpn.shared.NetworkType

trait TileAnalyzer {

  def analysis(networkType: NetworkType): TileAnalysis

}
