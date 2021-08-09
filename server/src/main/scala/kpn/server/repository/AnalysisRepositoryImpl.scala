package kpn.server.repository

import kpn.api.custom.Timestamp
import kpn.core.analysis._
import kpn.core.gpx.GpxFile
import kpn.core.gpx.GpxRoute
import kpn.core.gpx.WayPoint
import kpn.core.mongo.Database
import kpn.core.mongo.doc.NodeDoc
import kpn.server.analyzer.engine.analysis.AnalysisStatus
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import org.mongodb.scala.model.Filters.equal
import org.springframework.stereotype.Component

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

@Component
class AnalysisRepositoryImpl(
  database: Database,
  networkRepository: NetworkRepository,
  routeRepository: RouteRepository,
  nodeRepository: NodeRepository,
  nodeAnalyzer: NodeAnalyzer
) extends AnalysisRepository {

  override def saveNetwork(network: Network): Unit = {
    saveNetworkDoc(network)
    saveRouteDocs(network)
    saveNodeDocs(network)
    saveGpxDoc(network)
  }

  override def lastUpdated(): Option[Timestamp] = {
    val future = database.status.native.find[AnalysisStatus](equal("_id", AnalysisStatus.id)).headOption
    Await.result(future, Duration(30, TimeUnit.SECONDS)).map(_.timestamp)
  }

  override def saveLastUpdated(timestamp: Timestamp): Unit = {
    database.status.save(AnalysisStatus(Timestamp.redaction))
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
