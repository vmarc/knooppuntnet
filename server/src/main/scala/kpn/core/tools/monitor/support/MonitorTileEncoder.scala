package kpn.core.tools.monitor.support

import kpn.api.base.ObjectId
import kpn.core.tools.config.Dirs
import kpn.core.util.CoordinateUtil.coordinatesToLineString
import kpn.core.util.Log
import kpn.server.analyzer.engine.tiles.domain.RouteTiles
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.monitor.domain.MonitorSegment
import kpn.server.monitor.domain.MonitorStateTile
import kpn.server.monitor.domain.MonitorStateTileDeviation
import no.ecc.vectortile.VectorTileEncoder
import org.apache.commons.io.FileUtils

import java.io.File
import java.util

class MonitorTileEncoder(log: Log) {

  def processTile(
    tile: Tile,
    stateTileInfos: Seq[MonitorStateTile],
  ): Unit = {
    val extent = RouteTiles.extent(tile.z)
    val clipBufferSize = RouteTiles.clipBufferSize(tile.z)
    val encoder = new VectorTileEncoder(extent, clipBufferSize, false)
    encodeStateTileInfos(stateTileInfos, encoder)
    val tileBytes = encoder.encode()
    if (tileBytes.nonEmpty) {
      writeTile(tile, tileBytes)
    }
  }

  private def encodeStateTileInfos(stateTileInfos: Seq[MonitorStateTile], encoder: VectorTileEncoder): Unit = {
    stateTileInfos.foreach { tileInfo =>
      tileInfo.matchesLines.foreach { line =>
        encodeStateTileMatches(encoder, tileInfo, line)
      }
      tileInfo.deviations.foreach { deviation =>
        deviation.lines.foreach { line =>
          encodeDeviation(encoder, tileInfo, deviation, line)
        }
      }
      tileInfo.segments.foreach { segment =>
        encodeStateTileSegment(encoder, tileInfo, segment)
      }
    }
  }

  private def encodeDeviation(encoder: VectorTileEncoder, tileInfo: MonitorStateTile, deviation: MonitorStateTileDeviation, line: String): Unit = {
    val lineString = coordinatesToLineString(line)
    val userData = buildUserData(tileInfo.routeId)
    userData.put("relationId", tileInfo.relationId.toString)
    userData.put("deviationId", deviation.id.toString)
    encoder.addFeature("deviation", userData, lineString)
  }

  private def encodeStateTileMatches(encoder: VectorTileEncoder, tileInfo: MonitorStateTile, line: String): Unit = {
    val lineString = coordinatesToLineString(line)
    val userData = buildUserData(tileInfo.routeId)
    userData.put("relationId", tileInfo.relationId.toString)
    encoder.addFeature("match", userData, lineString)
  }

  private def encodeStateTileSegment(encoder: VectorTileEncoder, tileInfo: MonitorStateTile, segment: MonitorSegment): Unit = {
    val lineString = coordinatesToLineString(segment.coordinates)
    val userData = buildUserData(tileInfo.routeId)
    userData.put("relationId", segment.relationId.toString)
    userData.put("segmentId", segment.segmentId.toString)
    encoder.addFeature("route", userData, lineString)
  }

  private def buildUserData(routeId: ObjectId): util.HashMap[String, String] = {
    val userData = new util.HashMap[String, String]()
    userData.put("route", routeId.oid)
    userData
  }

  private def writeTile(tile: Tile, tileBytes: Array[Byte]): Unit = {
    val fileName = s"${Dirs.root}/tiles/monitor/${tile.z}/${tile.x}/${tile.y}.mvt"
    val file = new File(fileName)
    if (file.exists()) {
      val existingTile: Array[Byte] = FileUtils.readFileToByteArray(file: File)
      if (existingTile.sameElements(tileBytes)) {
        log.info(s"no change for tile $fileName")
      }
      else {
        FileUtils.writeByteArrayToFile(file, tileBytes)
        log.info(s"saved updated tile $fileName")
      }
    }
    else {
      FileUtils.writeByteArrayToFile(file, tileBytes)
      log.info(s"saved tile $fileName")
    }
  }
}
