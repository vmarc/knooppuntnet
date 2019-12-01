package kpn.server.analyzer.engine.tiles

import kpn.core.db.couch.Couch
import kpn.core.poi.PoiInfo
import kpn.core.util.Log
import kpn.server.repository.PoiRepository

class PoiTileAnalyzerImpl(poiRepository: PoiRepository) {

  private val log = Log(classOf[TileAnalyzerImpl])

  def analysis(): Seq[PoiInfo] = {
    poiRepository.allPois(Couch.defaultTimeout)
  }

}
