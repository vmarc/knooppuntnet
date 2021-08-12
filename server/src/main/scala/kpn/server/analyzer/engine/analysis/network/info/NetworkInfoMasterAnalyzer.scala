package kpn.server.analyzer.engine.analysis.network.info

import kpn.core.mongo.Database
import kpn.core.mongo.doc.NetworkInfoDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkCountryAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoChangeAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoFactAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoNodeAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoRouteAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoTagAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkLastUpdatedAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkNameAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkSurveyAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkTypeAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
class NetworkInfoMasterAnalyzer(
  database: Database,
  networkInfoTagAnalyzer: NetworkInfoTagAnalyzer,
  networkInfoRouteAnalyzer: NetworkInfoRouteAnalyzer,
  networkInfoNodeAnalyzer: NetworkInfoNodeAnalyzer,
  networkInfoFactAnalyzer: NetworkInfoFactAnalyzer,
  networkInfoChangeAnalyzer: NetworkInfoChangeAnalyzer,
  networkCountryAnalyzer: NetworkCountryAnalyzer
) {

  private val log = Log(classOf[NetworkInfoMasterAnalyzer])

  private val analyzers: List[NetworkInfoAnalyzer] = List(
    NetworkTypeAnalyzer,
    NetworkSurveyAnalyzer,
    NetworkNameAnalyzer,
    networkInfoTagAnalyzer,
    networkInfoRouteAnalyzer,
    networkInfoNodeAnalyzer,
    networkInfoFactAnalyzer,
    networkInfoChangeAnalyzer,
    networkCountryAnalyzer,
    NetworkLastUpdatedAnalyzer
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

  def updateNetwork(networkId: Long): Option[NetworkInfoDoc] = {
    Log.context(networkId.toString) {
      log.infoElapsed {
        val networkInfoDoc = database.networks.findById(networkId, log).flatMap { networkDoc =>
          val context = NetworkInfoAnalysisContext(networkDoc)
          doAnalyze(analyzers, context) match {
            case Some(doc) =>
              database.networkInfos.save(doc, log)
              Some(doc)
            case None => None
          }
        }
        ("updated", networkInfoDoc)
      }
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[NetworkInfoAnalyzer], context: NetworkInfoAnalysisContext): Option[NetworkInfoDoc] = {
    if (context.abort) {
      None
    }
    else if (analyzers.isEmpty) {
      Some(new NetworkInfoDocBuilder(context).build())
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }
}
