package kpn.server.analyzer.engine.poi

import kpn.core.poi.PoiInfo
import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.TileCalculator
import kpn.server.analyzer.engine.tiles.PoiTileData
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.vector.PoiVectorTileBuilder
import kpn.server.repository.PoiRepository
import org.springframework.stereotype.Component

@Component
class PoiTileBuilderImpl(
  poiRepository: PoiRepository,
  tileCalculator: TileCalculator,
  vectorTileRepository: TileFileRepository,
  poiVectorTileBuilder: PoiVectorTileBuilder
) extends PoiTileBuilder {

  private val log = Log(classOf[PoiTileBuilderImpl])

  override def build(tileName: String): Unit = {

    log.elapsed {

      val tile = tileCalculator.tileNamed(tileName)
      val poiRefs = poiRepository.tilePoiRefs(tileName)
      val pois = poiRefs.flatMap(poiRef => poiRepository.poi(poiRef))

      val poiInfos = pois.map { poi =>
        PoiInfo(
          poi.elementType,
          poi.elementId,
          poi.latitude,
          poi.longitude,
          poi.layers.head
        )
      }

      val tileData = PoiTileData(
        tile,
        poiInfos
      )

      val tileBytes = poiVectorTileBuilder.build(tileData)
      if (tileBytes.length > 0) {
        vectorTileRepository.saveOrUpdate("poi", tile, tileBytes)
      }

      (s"${poiInfos.size} elements", ())
    }
  }
}
