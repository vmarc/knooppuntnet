package kpn.core.tools.support

import kpn.core.gpx.GpxFile
import kpn.core.gpx.GpxSegment
import kpn.core.gpx.GpxWriter
import kpn.core.gpx.WayPoint
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl
import org.apache.commons.io.FileUtils

import java.io.File

object NetworkGpxTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new NetworkGpxTool(database).produce()
    }
  }
}

class NetworkGpxTool(database: Database) {

  private val routeRepository = new RouteRepositoryImpl(database)
  private val networkRepository = new NetworkRepositoryImpl(database)


  def produce(): Unit = {

    networkRepository.findById(14346399L) match {
      case None => println("network not found")
      case Some(network) =>
        val routeIds = network.relationMembers.map(_.relationId)
        val routeDocs = routeIds.zipWithIndex.flatMap { case (routeId, index) =>
          if ((index + 1) % 500 == 0) {
            println(s"${index + 1}/${routeIds.size}")
          }
          routeRepository.findById(routeId)
        }


        val trackSegments = routeDocs.flatMap { routeDoc =>
          val map = routeDoc.analysis.map
          val trackPaths = map.freePaths ++
            map.forwardPath.toSeq ++
            map.backwardPath.toSeq ++
            map.startTentaclePaths ++
            map.endTentaclePaths
          trackPaths.flatMap(_.segments)
        }

        val gpxSegments = trackSegments.map { trackSegment =>
          GpxSegment(trackSegment.trackPoints)
        }

        val routeNetworkNodeInfos = routeDocs.flatMap { routeDoc =>
          val map = routeDoc.analysis.map
          map.freeNodes ++
            map.startNodes ++
            map.endNodes ++
            map.startTentacleNodes ++
            map.endTentacleNodes
        }

        val wayPoints = routeNetworkNodeInfos.map { routeNetworkNodeInfo =>
          WayPoint(routeNetworkNodeInfo.name, routeNetworkNodeInfo.lat, routeNetworkNodeInfo.lon, "")
        }.distinct

        val file = GpxFile(14346399L, 14346399L, "Vallées d'Aigueblanche", wayPoints, gpxSegments)
        FileUtils.writeStringToFile(new File("/kpn/example.gpx"), new GpxWriter(file).string, "UTF-8")
    }
    println("done")
  }
}
