package kpn.server.repository

import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Timestamp
import kpn.core.analysis._
import kpn.core.database.Database
import kpn.core.database.doc.TimestampDoc
import kpn.core.gpx.GpxFile
import kpn.core.gpx.GpxRoute
import kpn.core.gpx.WayPoint
import kpn.core.mongo.doc.NodeDoc
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import org.springframework.stereotype.Component

@Component
class AnalysisRepositoryImpl(
  analysisDatabase: Database,
  networkRepository: NetworkRepository,
  routeRepository: RouteRepository,
  nodeRepository: NodeRepository,
  nodeAnalyzer: NodeAnalyzer
) extends AnalysisRepository {

  private val lastUpdatedDocumentKey = "analysis"

  override def saveNetwork(network: Network): Unit = {
    saveNetworkDoc(network)
    saveRouteDocs(network)
    saveNodeDocs(network)
    saveGpxDoc(network)
  }

  override def saveIgnoredNetwork(networkInfo: NetworkInfo): Unit = {
    networkRepository.oldSaveNetworkInfo(networkInfo)
  }

  override def lastUpdated(): Option[Timestamp] = {
    analysisDatabase.docWithId(lastUpdatedDocumentKey, classOf[TimestampDoc]).map(_.value)
  }

  override def saveLastUpdated(timestamp: Timestamp): Unit = {
    analysisDatabase.save(TimestampDoc(lastUpdatedDocumentKey, timestamp))
  }

  private def saveNetworkDoc(network: Network): Unit = {
    val networkInfo = new OldNetworkInfoBuilder().build(network)
    networkRepository.oldSaveNetworkInfo(networkInfo)
  }

  private def saveRouteDocs(network: Network): Unit = {
    network.routes.foreach { networkMemberRoute =>
      routeRepository.save(networkMemberRoute.routeAnalysis.route)
    }
  }

  private def saveNodeDocs(network: Network): Unit = {
    val nodeDocs: Seq[NodeDoc] = network.nodes.map { node =>
      nodeAnalyzer.analyze(NodeAnalysis(node.networkNode.node.raw)).toNodeDoc
    }
    nodeRepository.bulkSave(nodeDocs: _*)
  }

  private def saveGpxDoc(network: Network): Unit = {

    val wayPoints = network.nodes.map(node =>
      WayPoint(
        node.networkNode.name,
        node.networkNode.node.latitude,
        node.networkNode.node.longitude, ""
      )
    )

    val trackSegments = network.routes.flatMap(networkMemberRoute => new GpxRoute().trackSegments(networkMemberRoute.routeAnalysis.ways))

    val name = "%s-%s.gpx".format(network.id, network.name.replaceAll(" ", ""))
    networkRepository.saveGpxFile(GpxFile(network.id, network.id, name, wayPoints, trackSegments))
  }
}
