package kpn.core.tools.export

import kpn.api.common.common.TrackPath
import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkNodeDetail
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.doc.NetworkInfoDoc
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.json.Json
import kpn.server.repository.NetworkInfoRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

import java.io.File

/*
  Exports cycling network nodes and connections between nodes to GeoJson files.
 */
object NodeCyclerExportTool {

  private val log = Log(classOf[NodeCyclerExportTool])

  def main(args: Array[String]): Unit = {

    val exit: Int = try {
      NodeCyclerExportToolOptions.parse(args) match {
        case Some(options) =>

          Mongo.executeIn(options.database) { database =>
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
  private val networkInfoRepository = new NetworkInfoRepositoryImpl(database)
  private val routeRepository = new RouteRepositoryImpl(database)

  def export(): Unit = {
    val networks = cyclingNetworksFromDatabase()
    networks.zipWithIndex.foreach { case (networkAttributes, index) =>
      log.info(s"${index + 1}/${networks.size} ${networkAttributes.name}")
      networkInfoRepository.findById(networkAttributes.id) match {
        case None =>
        case Some(network) =>
          exportNodes(network)
          exportRoutes(network)
      }
    }
  }

  private def cyclingNetworksFromDatabase(): Seq[NetworkAttributes] = {
    val subsets = Subset.all.filter(_.networkType == NetworkType.cycling)
    subsets.flatMap(subset => networkInfoRepository.networks(subset, stale = false))
  }

  private def exportNodes(networkInfoDoc: NetworkInfoDoc): Unit = {
    val nodes = networkInfoDoc.nodes.map(nodeToGeoJson)
    val collection = GeoJson.featureCollection(nodes)
    json.writeValue(new File(s"$exportDir/nodes-${networkInfoDoc._id}.json"), collection)
  }

  private def exportRoutes(networkInfo: NetworkInfoDoc): Unit = {
    val routeIds = networkInfo.routes.map(_.id)
    val paths = routeIds.flatMap { routeId =>
      routeRepository.findById(routeId).toSeq.flatMap(_.analysis.map.paths)
    }
    val collection = GeoJson.featureCollection(paths.map(trackPathToGeoJson))
    json.writeValue(new File(s"$exportDir/routes-${networkInfo._id}.json"), collection)
  }

  private def nodeToGeoJson(networkNodeDetail: NetworkNodeDetail): GeoJsonFeature = {
    GeoJson.feature(
      GeoJson.pointGeometry(networkNodeDetail.latitude, networkNodeDetail.longitude),
      GeoJson.properties(
        id = Some(networkNodeDetail.id.toString),
        name = Some(networkNodeDetail.name)
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
