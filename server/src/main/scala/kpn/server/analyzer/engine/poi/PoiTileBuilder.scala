package kpn.server.analyzer.engine.poi

import kpn.core.util.Log
import kpn.server.analyzer.engine.tiles.PoiTileData
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.domain.PoiTiles
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.analyzer.engine.tiles.vector.PoiVectorTileBuilder
import kpn.server.repository.PoiRepository
import org.springframework.stereotype.Component

@Component
class PoiTileBuilder(
  poiRepository: PoiRepository,
  vectorTileRepository: TileFileRepository,
  poiVectorTileBuilder: PoiVectorTileBuilder
) {

  private val log = Log(classOf[PoiTileBuilder])

  def build(tileName: String): Unit = {

    log.infoElapsed {

      val tileId = TileId(tileName)
      val tile = PoiTiles.tile(tileId)
      val poiInfos = poiRepository.tilePoiInfos(tileName)
      val tileData = PoiTileData(
        tile,
        poiInfos
      )

      val tileBytes = poiVectorTileBuilder.build(tileData)
      if (tileBytes.nonEmpty) {
        vectorTileRepository.saveOrUpdate("poi", tile, tileBytes)
      }

      (s"${poiInfos.size} elements", ())
    }
  }
}
