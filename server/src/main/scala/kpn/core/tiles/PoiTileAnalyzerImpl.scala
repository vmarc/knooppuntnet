package kpn.core.tiles

import kpn.core.db.couch.Couch
import kpn.core.poi.PoiInfo
import kpn.core.poi.PoiRepository
import kpn.core.util.Log

class PoiTileAnalyzerImpl(poiRepository: PoiRepository) {

  private val log = Log(classOf[TileAnalyzerImpl])

  def analysis(): Seq[PoiInfo] = {
    poiRepository.allPois(Couch.defaultTimeout)
  }

}
