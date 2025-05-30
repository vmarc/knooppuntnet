package kpn.server.analyzer.engine.tiles

import kpn.api.common.RouteType
import kpn.api.common.tiles.ZoomLevel
import kpn.core.util.Log

class TileFileManager(
  bitmapTileFileRepository: TileFileRepository,
  vectorTileFileRepository: TileFileRepository,
) {

  private val log = Log(classOf[TileFileManager])

  def existingFiles(z: Int, data: OldTileData): TileFileSnapshot = {
    if (z < ZoomLevel.vectorTileMinZoom) {
      val snapshot = TileFileSnapshot(
        bitmapTileNames = collectExistingBitmapTileNames(z, data),
        bitmapTileNamesSurface = collectExistingBitmapTileNamesSurface(z, data),
        bitmapTileNamesSurvey = collectExistingBitmapTileNamesSurvey(z, data),
        bitmapTileNamesAnalysis = collectExistingBitmapTileNamesAnalysis(z, data)
      )
      log.info(s"Number of bitmap tiles before: ${snapshot.bitmapTileNames.size}")
      log.info(s"Number of surface tiles before: ${snapshot.bitmapTileNamesSurface.size}")
      log.info(s"Number of survey tiles before: ${snapshot.bitmapTileNamesSurvey.size}")
      log.info(s"Number of analysis tiles before: ${snapshot.bitmapTileNamesAnalysis.size}")
      snapshot
    }
    else {
      val snapshot = TileFileSnapshot(
        vectorTileNames = collectExistingVectorTileNames(z, data)
      )
      log.info(s"Number of vector tiles before: ${snapshot.vectorTileNames.size}")
      snapshot
    }
  }

  def deleteObsoleteFiles(z: Int, routeType: RouteType, existingFilesSnapshot: TileFileSnapshot, afterTileNames: Seq[String]): Unit = {

    if (z <= ZoomLevel.bitmapTileMaxZoom) {

      val obsoleteTileNames = (existingFilesSnapshot.bitmapTileNames.toSet -- afterTileNames.toSet).toSeq.sorted
      log.info(s"Obsolete bitmap tiles: $obsoleteTileNames")
      bitmapTileFileRepository.delete(obsoleteTileNames)
      log.info(s"Obsolete bitmap tiles removed: ${obsoleteTileNames.size}")

      val afterTileNamesSurface = afterTileNames.map(tileName => s"${routeType.entryName}-surface-$tileName")
      val obsoleteTileNamesSurface = (existingFilesSnapshot.bitmapTileNamesSurface.toSet -- afterTileNamesSurface.toSet).toSeq.sorted
      bitmapTileFileRepository.delete(obsoleteTileNamesSurface)
      log.info(s"Obsolete bitmap surface tiles removed: ${obsoleteTileNamesSurface.size}")

      val afterTileNamesSurvey = afterTileNames.map(tileName => s"${routeType.entryName}-survey-$tileName")
      val obsoleteTileNamesSurvey = (existingFilesSnapshot.bitmapTileNamesSurvey.toSet -- afterTileNamesSurvey.toSet).toSeq.sorted
      bitmapTileFileRepository.delete(obsoleteTileNamesSurvey)
      log.info(s"Obsolete bitmap survey tiles removed: ${obsoleteTileNamesSurvey.size}")

      val afterTileNamesAnalysis = afterTileNames.map(tileName => s"${routeType.entryName}-analysis-$tileName")
      val obsoleteTileNamesAnalysis = (existingFilesSnapshot.bitmapTileNamesAnalysis.toSet -- afterTileNamesAnalysis.toSet).toSeq.sorted
      bitmapTileFileRepository.delete(obsoleteTileNamesAnalysis)
      log.info(s"Obsolete bitmap analysis tiles removed: ${obsoleteTileNamesAnalysis.size}")
    }

    if (z >= ZoomLevel.vectorTileMinZoom - 1) {
      val obsoleteTileNames = (existingFilesSnapshot.vectorTileNames.toSet -- afterTileNames.toSet).toSeq.sorted
      log.info(s"Obsolete vector tiles: $obsoleteTileNames")
      vectorTileFileRepository.delete(obsoleteTileNames)
      log.info(s"Obsolete vector tiles removed: ${obsoleteTileNames.size}")
    }
  }

  private def collectExistingVectorTileNames(z: Int, data: OldTileData) = {
    if (z >= ZoomLevel.vectorTileMinZoom - 1) {
      vectorTileFileRepository.existingTileNames(data.routeType.entryName, z)
    }
    else {
      Seq.empty
    }
  }

  private def collectExistingBitmapTileNames(z: Int, data: OldTileData) = {
    if (z < ZoomLevel.vectorTileMinZoom) {
      bitmapTileFileRepository.existingTileNames(data.routeType.entryName, z)
    }
    else {
      Seq.empty
    }
  }

  private def collectExistingBitmapTileNamesSurface(z: Int, data: OldTileData) = {
    if (z < ZoomLevel.vectorTileMinZoom) {
      bitmapTileFileRepository.existingTileNames(s"${data.routeType.entryName}/surface", z)
    }
    else {
      Seq.empty
    }
  }

  private def collectExistingBitmapTileNamesSurvey(z: Int, data: OldTileData) = {
    if (z < ZoomLevel.vectorTileMinZoom) {
      bitmapTileFileRepository.existingTileNames(s"${data.routeType.entryName}/survey", z)
    }
    else {
      Seq.empty
    }
  }

  private def collectExistingBitmapTileNamesAnalysis(z: Int, data: OldTileData) = {
    if (z < ZoomLevel.vectorTileMinZoom) {
      bitmapTileFileRepository.existingTileNames(s"${data.routeType.entryName}/analysis", z)
    }
    else {
      Seq.empty
    }
  }
}
