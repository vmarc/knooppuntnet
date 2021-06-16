package kpn.core.tools.export

import java.io.File

import kpn.api.common.common.TrackPath
import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkInfoNode
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.core.util.Log
import kpn.server.json.Json
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

/*
  Exports cycling network nodes and connections between nodes to GeoJson files.
 */
object NodeCyclerExportTool {

  private val log = Log(classOf[NodeCyclerExportTool])

  def main(args: Array[String]): Unit = {

    val exit: Int = try {
      NodeCyclerExportToolOptions.parse(args) match {
        case Some(options) =>

          Couch.executeIn(options.host, options.database) { database =>
            new NodeCyclerExportTool(database, options.exportDir).export()
          }

          log.info("Done")

          0

        case None =>
          // arguments are bad, error message will have been displayed
          -1
      }
    }
    catch {
      case e: Throwable =>
        log.error(e.getMessage)
        -1
    }

    System.exit(exit)
  }
}

class NodeCyclerExportTool(database: Database, exportDir: String) {

  import NodeCyclerExportTool.log

  private val json = Json.objectMapper.writerWithDefaultPrettyPrinter()
  private val networkRepository = new NetworkRepositoryImpl(null, database, false)
  private val routeRepository = new RouteRepositoryImpl(null, database, false)

  def export(): Unit = {
    val networks = cyclingNetworksFromDatabase()
    networks.zipWithIndex.foreach { case (networkAttributes, index) =>
      log.info(s"${index + 1}/${networks.size} ${networkAttributes.name}")
      networkRepository.network(networkAttributes.id) match {
        case None =>
        case Some(network) =>
          exportNodes(network)
          exportRoutes(network)
      }
    }
  }

  private def cyclingNetworksFromDatabase(): Seq[NetworkAttributes] = {
    val subsets = Subset.all.filter(_.networkType == NetworkType.cycling)
    subsets.flatMap(subset => networkRepository.networks(subset, stale = false))
  }

  private def exportNodes(networkInfo: NetworkInfo): Unit = {
    val nodes = networkInfo.detail.toSeq.flatMap(_.nodes).map(nodeToGeoJson)
    val collection = GeoJson.featureCollection(nodes)
    json.writeValue(new File(s"$exportDir/nodes-${networkInfo.id}.json"), collection)
  }

  private def exportRoutes(networkInfo: NetworkInfo): Unit = {
    val routeIds = networkInfo.detail.toSeq.flatMap(_.routes).map(_.id)
    val paths = routeIds.flatMap { routeId =>
      routeRepository.routeWithId(routeId).toSeq.flatMap(_.analysis.map.paths)
    }
    val collection = GeoJson.featureCollection(paths.map(trackPathToGeoJson))
    json.writeValue(new File(s"$exportDir/routes-${networkInfo.id}.json"), collection)
  }

  private def nodeToGeoJson(networkInfoNode: NetworkInfoNode): GeoJsonFeature = {
    GeoJson.feature(
      GeoJson.pointGeometry(networkInfoNode.latitude, networkInfoNode.longitude),
      GeoJson.properties(
        id = Some(networkInfoNode.id.toString),
        name = Some(networkInfoNode.name)
      )
    )
  }

  private def trackPathToGeoJson(path: TrackPath): GeoJsonFeature = {

    val trackPoints = Seq(path.segments.head.source) ++ path.segments.flatMap(_.fragments.map(_.trackPoint))
    val coordinates = trackPoints.map(trackPoint => Seq(trackPoint.lat, trackPoint.lon))

    GeoJson.feature(
      GeoJson.lineStringGeometry(
        coordinates
      ),
      GeoJson.properties(
        begin_geoid = Some(path.startNodeId.toString),
        end_geoid = Some(path.endNodeId.toString),
        distance = Some(path.meters.toString)
      )
    )
  }
}
