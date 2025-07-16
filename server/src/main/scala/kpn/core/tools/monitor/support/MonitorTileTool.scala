package kpn.core.tools.monitor.support

import kpn.core.tools.config.Dirs
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.analyzer.engine.tiles.domain.RouteTiles
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.json.Json
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl
import no.ecc.vectortile.VectorTileEncoder
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.GeometryFactory

import java.io.File

object MonitorTileTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { database =>
      val tool = new MonitorTileTool(database)
      tool.generate()
    }
  }
}

class MonitorTileTool(database: Database) {
  private val log = Log(classOf[MonitorTileTool])

  private val geometryFactory = new GeometryFactory
  private val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)

  def generate(): Unit = {

    val routeMap = monitorRouteRepository.routeInfos().groupBy(_._id.oid)

    val stateTileIds = monitorRouteRepository.stateTileIds()
    val referenceTileIds = monitorRouteRepository.referenceTileIds()
    val start = System.currentTimeMillis()
    val tileIds = (stateTileIds.toSet ++ referenceTileIds.toSet).toSeq.sortBy(t => (t.z, t.x, t.y))
    val tileIdCount = tileIds.size
    tileIds.zipWithIndex.foreach { case (tileId, index) =>
      Log.context(s"${index + 1}/$tileIdCount ${tileId.z}/${tileId.x}/${tileId.y}") {
        val tile = RouteTiles.tile(tileId)

        val stateTileInfos = monitorRouteRepository.stateTiles(tileId)
        val referenceTileInfos = monitorRouteRepository.referenceTiles(tileId)

        val encoder = new VectorTileEncoder()

        stateTileInfos.foreach { tileInfo =>
          tileInfo.matchesLines.foreach { line =>
            val coordinates = Json.value(line, classOf[CoordinateArray]).coordinates
            val lineString = geometryFactory.createLineString(coordinates)
            val routeInfo = routeMap.getOrElse(tileInfo.routeId.oid, Seq.empty).headOption
            val groupName = routeInfo.map(_.groupName).get
            val routeName = routeInfo.map(_.routeName).get
            val userData = new java.util.HashMap[String, String]()
            userData.put("group", groupName)
            userData.put("route", routeName)
            userData.put("relationId", tileInfo.relationId.toString)
            encoder.addFeature("match", userData, lineString)
          }

          tileInfo.deviations.foreach { deviation =>
            deviation.lines.foreach { line =>
              val coordinates = Json.value(line, classOf[CoordinateArray]).coordinates
              val lineString = geometryFactory.createLineString(coordinates)
              val routeInfo = routeMap.getOrElse(tileInfo.routeId.oid, Seq.empty).headOption
              val groupName = routeInfo.map(_.groupName).get
              val routeName = routeInfo.map(_.routeName).get
              val userData = new java.util.HashMap[String, String]()
              userData.put("group", groupName)
              userData.put("route", routeName)
              userData.put("relationId", tileInfo.relationId.toString)
              userData.put("deviationId", deviation.id.toString)
              encoder.addFeature("deviation", userData, lineString)
            }
          }
        }

        referenceTileInfos.foreach { tileInfo =>
          tileInfo.lines.foreach { line =>
            val coordinates = Json.value(line, classOf[CoordinateArray]).coordinates
            val lineString = geometryFactory.createLineString(coordinates)
            val routeInfo = routeMap.getOrElse(tileInfo.routeId.oid, Seq.empty).headOption
            val groupName = routeInfo.map(_.groupName).get
            val routeName = routeInfo.map(_.routeName).get
            val userData = new java.util.HashMap[String, String]()
            userData.put("group", groupName)
            userData.put("route", routeName)
            userData.put("relationId", tileInfo.relationId.toString)
            encoder.addFeature("reference", userData, lineString)
          }
        }

        val tileBytes = encoder.encode()
        writeTile(tile, tileBytes)
      }
    }
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
