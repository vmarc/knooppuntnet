package kpn.server.analyzer.engine.poi

import kpn.core.util.Log
import kpn.server.analyzer.engine.tiles.PoiTileData
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.vector.PoiVectorTileBuilder
import kpn.server.repository.PoiRepository
import org.springframework.stereotype.Component

@Component
class PoiTileBuilderImpl(
  poiRepository: PoiRepository,
  vectorTileRepository: TileFileRepository,
  poiVectorTileBuilder: PoiVectorTileBuilder
) extends PoiTileBuilder {

  private val log = Log(classOf[PoiTileBuilderImpl])

  override def build(tileName: String): Unit = {

    log.infoElapsed {

      val tile = Tile.poiTileFromName(tileName)
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
