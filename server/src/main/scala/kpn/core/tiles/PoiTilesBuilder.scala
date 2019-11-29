package kpn.core.tiles

import kpn.core.poi.PoiConfiguration
import kpn.core.poi.PoiInfo
import kpn.core.tiles.domain.TilePois
import kpn.core.tiles.vector.PoiVectorTileBuilder
import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.NodeTileAnalyzer

class PoiTilesBuilder(
  tileBuilder: PoiVectorTileBuilder,
  tileFileRepository: TileFileRepository,
  nodeTileAnalyzer: NodeTileAnalyzer
) {

  private val log = Log(classOf[PoiTilesBuilder])

  def build(
    z: Int,
    pois: Seq[PoiInfo]
  ): Unit = {

    val existingTileNames = tileFileRepository.existingTileNames("poi", z)

    log.info(s"Processing zoomlevel $z")
    log.info(s"Number of tiles before: " + existingTileNames.size)

    log.info(s"buildTilePoisMap()")
    val tilePoisMap = buildTilePoisMap(z, pois)

    log.info(s"build ${tilePoisMap.size} tiles")

    var progress: Int = 0

    tilePoisMap.values.zipWithIndex.foreach { case (tilePois, index) =>
      val currentProgress = (100d * (index + 1) / tilePoisMap.size).round.toInt
      if (currentProgress != progress) {
        progress = currentProgress
        log.info(s"Build tile ${index + 1}/${tilePoisMap.size} $progress ${tilePois.tile.name}")
      }

      val tileData = PoiTileData(
        tilePois.tile,
        tilePois.pois
      )

      val tileBytes = tileBuilder.build(tileData)
      if (tileBytes.length > 0) {
        tileFileRepository.saveOrUpdate("poi", tilePois.tile, tileBytes)
      }
    }

    val afterTileNames = tilePoisMap.keys.map(tileName => "poi-" + tileName)

    val obsoleteTileNames = (existingTileNames.toSet -- afterTileNames.toSet).toSeq.sorted
    log.info(s"Obsolete: " + obsoleteTileNames)

    log.info(s"Obsolete tile count: " + obsoleteTileNames.size)
    tileFileRepository.delete(obsoleteTileNames)
    log.info(s"Obsolete tiles removed")
  }

  private def buildTilePoisMap(z: Int, pois: Seq[PoiInfo]): Map[String, TilePois] = {
    val map = scala.collection.mutable.Map[String, TilePois]()
    pois.foreach { poi =>
      PoiConfiguration.instance.poiDefinition(poi.layer) match {
        case Some(poiDefinition) =>
          if (z >= poiDefinition.minLevel) {
            val tiles = nodeTileAnalyzer.tiles(z, poi)
            tiles.foreach { tile =>
              map(tile.name) = map.get(tile.name) match {
                case Some(tilePois) => TilePois(tile, tilePois.pois :+ poi)
                case None => TilePois(tile, Seq(poi))
              }
            }
          }
        case _ =>
      }
    }
    map.toMap
  }

}
