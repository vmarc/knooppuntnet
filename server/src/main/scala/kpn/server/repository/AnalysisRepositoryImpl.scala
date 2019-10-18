package kpn.server.repository

import kpn.core.analysis._
import kpn.core.db.TimestampDoc
import kpn.core.db.couch.Couch
import kpn.core.db.couch.OldDatabase
import kpn.core.db.json.JsonFormats.timestampDocFormat
import kpn.core.gpx.GpxFile
import kpn.core.gpx.GpxRoute
import kpn.core.gpx.WayPoint
import kpn.core.tools.db.NetworkInfoBuilder
import kpn.core.util.Log
import kpn.shared.Fact
import kpn.shared.NodeInfo
import kpn.shared.Timestamp
import kpn.shared.network.NetworkInfo
import kpn.shared.route.RouteInfo
import org.springframework.stereotype.Component

@Component
class AnalysisRepositoryImpl(
  oldAnalysisDatabase: OldDatabase,
  networkRepository: NetworkRepository,
  routeRepository: RouteRepository,
  nodeRepository: NodeRepository
) extends AnalysisRepository {

  private val log = Log(classOf[AnalysisRepositoryImpl])

  private val lastUpdatedDocumentKey = "analysis"

  override def saveNetwork(network: Network): Unit = {
    buildNetworkDoc(network)
    buildRouteDocs(network)
    buildNodeDocs(network)
    buildGpxDoc(network)
  }

  override def saveIgnoredNetwork(network: NetworkInfo): Unit = {
    networkRepository.save(network)
  }

  override def saveRoute(route: RouteInfo): Unit = {
    routeRepository.save(route)
  }

  override def saveNode(node: NodeInfo): Unit = {
    nodeRepository.save(node)
  }

  override def lastUpdated(): Option[Timestamp] = {
    oldAnalysisDatabase.optionGet(lastUpdatedDocumentKey).map(timestampDocFormat.read).map(_.value)
  }

  override def saveLastUpdated(timestamp: Timestamp): Unit = {
    val rev = oldAnalysisDatabase.optionGet(lastUpdatedDocumentKey, Couch.batchTimeout).map(timestampDocFormat.read).flatMap(_._rev)
    oldAnalysisDatabase.save(lastUpdatedDocumentKey, timestampDocFormat.write(TimestampDoc(lastUpdatedDocumentKey, timestamp, rev)))
  }

  private def buildNetworkDoc(network: Network): Unit = {
    val networkInfo = new NetworkInfoBuilder().build(network)
    networkRepository.save(networkInfo)
  }

  private def buildRouteDocs(network: Network): Unit = {
    val routes: Seq[RouteInfo] = network.routes.map(_.routeAnalysis.route)
    routeRepository.save(routes: _*)
  }

  private def buildNodeDocs(network: Network): Unit = {

    val nodeInfos: Seq[NodeInfo] = network.nodes.map { node =>

      val facts: Seq[Fact] = node.integrityCheck match {
        case None => Seq()
        case Some(nodeIntegrityCheck) =>
          if (nodeIntegrityCheck.failed) {
            Seq(Fact.IntegrityCheckFailed)
          }
          else {
            Seq()
          }
      }

      NodeInfoBuilder.build(
        node.networkNode.node.id,
        active = true,
        orphan = false,
        node.networkNode.country,
        node.networkNode.node.latitude.toString,
        node.networkNode.node.longitude.toString,
        node.networkNode.node.timestamp,
        node.networkNode.node.tags,
        facts
      )
    }
    nodeRepository.save(nodeInfos: _*)
  }

  private def buildGpxDoc(network: Network): Unit = {

    val wayPoints = network.nodes.map(node =>
      WayPoint(
        node.networkNode.name,
        node.networkNode.node.latitude.toString,
        node.networkNode.node.longitude.toString, ""
      )
    )

    val trackSegments = network.routes.flatMap(networkMemberRoute => new GpxRoute().trackSegments(networkMemberRoute.routeAnalysis.ways))

    val name = "%s-%s.gpx".format(network.id, network.name.replaceAll(" ", ""))
    networkRepository.saveGpxFile(GpxFile(network.id, name, wayPoints, trackSegments))
  }
}
