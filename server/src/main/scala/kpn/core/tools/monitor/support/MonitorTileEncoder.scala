package kpn.core.tools.monitor.support

import kpn.api.base.ObjectId
import kpn.core.tools.config.Dirs
import kpn.core.util.CoordinateUtil.coordinatesToLineString
import kpn.core.util.Log
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.monitor.domain.MonitorReferenceTileInfo
import kpn.server.monitor.domain.MonitorRouteInfo
import kpn.server.monitor.domain.MonitorStateTile
import kpn.server.monitor.domain.MonitorStateTileDeviation
import no.ecc.vectortile.VectorTileEncoder
import org.apache.commons.io.FileUtils

import java.io.File
import java.util

class MonitorTileEncoder(routeMap: Map[String, Seq[MonitorRouteInfo]], log: Log) {

  def processTile(
    tile: Tile,
    referenceTileInfos: Seq[MonitorReferenceTileInfo],
    stateTileInfos: Seq[MonitorStateTile],
  ): Unit = {
    val encoder = new VectorTileEncoder()
    encodeStateTileInfos(routeMap, stateTileInfos, encoder)
    encodeReferenceTileInfos(routeMap, referenceTileInfos, encoder)
    val tileBytes = encoder.encode()
    writeTile(tile, tileBytes)
  }

  private def encodeReferenceTileInfos(routeMap: Map[String, Seq[MonitorRouteInfo]], referenceTileInfos: Seq[MonitorReferenceTileInfo], encoder: VectorTileEncoder): Unit = {
    referenceTileInfos.foreach { tileInfo =>
      tileInfo.lines.foreach { line =>
        encodeReference(routeMap, encoder, tileInfo, line)
      }
    }
  }

  private def encodeReference(routeMap: Map[String, Seq[MonitorRouteInfo]], encoder: VectorTileEncoder, tileInfo: MonitorReferenceTileInfo, line: String): Unit = {
    val lineString = coordinatesToLineString(line)
    val userData = buildUserData(routeMap, tileInfo.routeId)
    userData.put("relationId", tileInfo.relationId.toString)
    encoder.addFeature("reference", userData, lineString)
  }

  private def encodeStateTileInfos(routeMap: Map[String, Seq[MonitorRouteInfo]], stateTileInfos: Seq[MonitorStateTile], encoder: VectorTileEncoder): Unit = {
    stateTileInfos.foreach { tileInfo =>
      tileInfo.matchesLines.foreach { line =>
        encodeStateTileMatches(routeMap, encoder, tileInfo, line)
      }
      tileInfo.deviations.foreach { deviation =>
        deviation.lines.foreach { line =>
          encodeDeviation(routeMap, encoder, tileInfo, deviation, line)
        }
      }
    }
  }

  private def encodeDeviation(routeMap: Map[String, Seq[MonitorRouteInfo]], encoder: VectorTileEncoder, tileInfo: MonitorStateTile, deviation: MonitorStateTileDeviation, line: String): Unit = {
    val lineString = coordinatesToLineString(line)
    val userData = buildUserData(routeMap, tileInfo.routeId)
    userData.put("relationId", tileInfo.relationId.toString)
    userData.put("deviationId", deviation.id.toString)
    encoder.addFeature("deviation", userData, lineString)
  }

  private def encodeStateTileMatches(routeMap: Map[String, Seq[MonitorRouteInfo]], encoder: VectorTileEncoder, tileInfo: MonitorStateTile, line: String): Unit = {
    val lineString = coordinatesToLineString(line)
    val userData = buildUserData(routeMap, tileInfo.routeId)
    userData.put("relationId", tileInfo.relationId.toString)
    encoder.addFeature("match", userData, lineString)
  }

  private def buildUserData(routeMap: Map[String, Seq[MonitorRouteInfo]], routeId: ObjectId): util.HashMap[String, String] = {
    val routeInfo = routeMap.getOrElse(routeId.oid, Seq.empty).headOption
    val groupName = routeInfo.map(_.groupName).get
    val routeName = routeInfo.map(_.routeName).get
    val userData = new util.HashMap[String, String]()
    userData.put("group", "group")
    userData.put("route", "route")
    userData.put("relationId", "relationId")
    userData.put("deviationId", "deviationId")
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
