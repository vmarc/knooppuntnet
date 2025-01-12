package kpn.core.tools.support

import kpn.api.common.common.TrackSegment
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.core.gpx.GpxFile
import kpn.core.gpx.GpxSegment
import kpn.core.gpx.GpxWriter
import kpn.core.gpx.WayPoint
import kpn.core.tools.config.Dirs
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
        val routeIds = network.routes.map(_.id)
        val routeIdsSize = routeIds.size
        val routeDocs = routeIds.zipWithIndex.flatMap { case (routeId, index) =>
          if ((index + 1) % 500 == 0) {
            println(s"${index + 1}/$routeIdsSize")
          }
          routeRepository.findRouteById(routeId)
        }

        val trackSegments: Seq[TrackSegment] = routeDocs.flatMap { routeDoc =>
          // TODO redesign
          //   val map = routeDoc.analysis.map
          //   val trackPaths = map.freePaths ++
          //     map.forwardPath.toSeq ++
          //     map.backwardPath.toSeq ++
          //     map.startTentaclePaths ++
          //     map.endTentaclePaths
          //   trackPaths.flatMap(_.segments)
          Seq.empty
        }

        val gpxSegments = trackSegments.map { trackSegment =>
          GpxSegment(trackSegment.trackPoints)
        }

        val routeNetworkNodeInfos: Seq[RouteNetworkNodeInfo] = routeDocs.flatMap { routeDoc =>
          // TODO redesign
          //   val map = routeDoc.analysis.map
          //   map.freeNodes ++
          //     map.startNodes ++
          //     map.endNodes ++
          //     map.startTentacleNodes ++
          //     map.endTentacleNodes
          Seq.empty
        }

        val wayPoints = routeNetworkNodeInfos.map { routeNetworkNodeInfo =>
          WayPoint(routeNetworkNodeInfo.name, routeNetworkNodeInfo.lat, routeNetworkNodeInfo.lon, "")
        }.distinct

        val file = GpxFile(14346399L, 14346399L, "Vallées d'Aigueblanche", wayPoints, gpxSegments)
        FileUtils.writeStringToFile(new File(Dirs.root, "example.gpx"), new GpxWriter(file).string, "UTF-8")
    }
    println("done")
  }
}
