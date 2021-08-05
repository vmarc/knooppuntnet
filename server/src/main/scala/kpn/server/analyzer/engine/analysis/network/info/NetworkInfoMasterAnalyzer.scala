package kpn.server.analyzer.engine.analysis.network.info

import kpn.core.mongo.Database
import kpn.core.mongo.doc.NetworkInfoDoc
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoChangeAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoFactAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoNodeAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoRouteAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoTagAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext
import org.springframework.stereotype.Component

import scala.annotation.tailrec

object NetworkInfoMasterAnalyzer {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>

      val analyzer = {
        val analysisContext = new AnalysisContext()
        val networkInfoTagAnalyzer = new NetworkInfoTagAnalyzer(analysisContext)
        val networkInfoRouteAnalyzer = new NetworkInfoRouteAnalyzer(database)
        val networkInfoNodeAnalyzer = new NetworkInfoNodeAnalyzer(database)
        val networkInfoFactAnalyzer = new NetworkInfoFactAnalyzer()
        val networkInfoChangeAnalyzer = new NetworkInfoChangeAnalyzer(database)
        new NetworkInfoMasterAnalyzer(
          database,
          networkInfoTagAnalyzer,
          networkInfoRouteAnalyzer,
          networkInfoNodeAnalyzer,
          networkInfoFactAnalyzer,
          networkInfoChangeAnalyzer
        )
      }

      analyzer.updateAll()
    }
  }
}

@Component
class NetworkInfoMasterAnalyzer(
  database: Database,
  networkInfoTagAnalyzer: NetworkInfoTagAnalyzer,
  networkInfoRouteAnalyzer: NetworkInfoRouteAnalyzer,
  networkInfoNodeAnalyzer: NetworkInfoNodeAnalyzer,
  networkInfoFactAnalyzer: NetworkInfoFactAnalyzer,
  networkInfoChangeAnalyzer: NetworkInfoChangeAnalyzer
) {

  private val log = Log(classOf[NetworkInfoMasterAnalyzer])

  private val analyzers: List[NetworkInfoAnalyzer] = List(
    networkInfoTagAnalyzer,
    networkInfoRouteAnalyzer,
    networkInfoNodeAnalyzer,
    networkInfoFactAnalyzer,
    networkInfoChangeAnalyzer,

    // TODO MONGO country analyzer --> remove country from NetworkDoc (node and route details are not available when NetworkDoc is created)

    // TODO MONGO create network shape
    // database.networkShapes.save(doc, log)
    // TODO MONGO create network gpx
    // database.networkGpxs.save(doc, log)
  )

  def updateAll(): Unit = {
    log.infoElapsed {
      val networkIds = database.networks.ids(log)
      networkIds.zipWithIndex.foreach { case (networkDoc, index) =>
        Log.context(s"${index + 1}/${networkIds.size}") {
          updateNetwork(networkDoc)
        }
      }
      ("done", ())
    }
  }

  def updateNetworks(networkIds: Seq[Long]): Unit = {
    networkIds.zipWithIndex.foreach { case (networkId, index) =>
      Log.context(s"${index + 1}/${networkIds.size}") {
        updateNetwork(networkId)
      }
    }
  }

  def updateNetwork(networkId: Long): Unit = {
    Log.context(networkId.toString) {
      log.infoElapsed {
        database.networks.findById(networkId, log).foreach { networkDoc =>
          val context = NetworkInfoAnalysisContext(networkDoc)
          val doc = doAnalyze(analyzers, context)
          database.networkInfos.save(doc, log)
        }
        ("update", ())
      }
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[NetworkInfoAnalyzer], context: NetworkInfoAnalysisContext): NetworkInfoDoc = {
    if (analyzers.isEmpty) {
      new NetworkInfoDocBuilder(context).build()
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }
}
