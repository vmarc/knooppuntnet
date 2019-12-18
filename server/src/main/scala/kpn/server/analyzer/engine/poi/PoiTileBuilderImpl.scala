package kpn.server.analyzer.engine.poi

import kpn.core.poi.PoiInfo
import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.TileCalculator
import kpn.server.analyzer.engine.tiles.PoiTileData
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.vector.PoiVectorTileBuilder
import kpn.server.repository.PoiRepository

class PoiTileBuilderImpl(
  poiRepository: PoiRepository,
  tileCalculator: TileCalculator,
  tileFileRepository: TileFileRepository,
  tileBuilder: PoiVectorTileBuilder
) extends PoiTileBuilder {

  private val log = Log(classOf[PoiTileBuilderImpl])

  override def build(tileName: String): Unit = {

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

    val tileBytes = tileBuilder.build(tileData)
    if (tileBytes.length > 0) {
      tileFileRepository.saveOrUpdate("poi", tile, tileBytes)
      log.info(s"tile $tileName (${poiInfos.size} elements)")
    }
  }

}
