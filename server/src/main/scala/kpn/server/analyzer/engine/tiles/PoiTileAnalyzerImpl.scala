package kpn.server.analyzer.engine.tiles

import kpn.core.poi.PoiInfo
import kpn.server.repository.PoiRepository

class PoiTileAnalyzerImpl(poiRepository: PoiRepository) {

  def analysis(): Seq[PoiInfo] = {
    poiRepository.allPois()
  }

}
