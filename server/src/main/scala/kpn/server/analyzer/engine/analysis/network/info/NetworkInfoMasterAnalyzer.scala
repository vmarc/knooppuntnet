package kpn.server.analyzer.engine.analysis.network.info

import kpn.api.custom.Country
import kpn.api.custom.Timestamp
import kpn.core.doc.NetworkInfoDoc
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkCenterAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkCountryAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoChangeAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoExtraAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoFactAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoIntegrityAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoNodeAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoNodeDocAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoNodeMemberMissingAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoProposedAnalyzer
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
  networkInfoRouteAnalyzer: NetworkInfoRouteAnalyzer,
  networkInfoNodeDocAnalyzer: NetworkInfoNodeDocAnalyzer,
  networkInfoChangeAnalyzer: NetworkInfoChangeAnalyzer,
  networkCountryAnalyzer: NetworkCountryAnalyzer,
  networkInfoExtraAnalyzer: NetworkInfoExtraAnalyzer
) {

  private val log = Log(classOf[NetworkInfoMasterAnalyzer])

  private val analyzers: List[NetworkInfoAnalyzer] = List(
    NetworkTypeAnalyzer,
    NetworkSurveyAnalyzer,
    NetworkNameAnalyzer,
    NetworkInfoTagAnalyzer,
    NetworkInfoProposedAnalyzer,
    networkInfoRouteAnalyzer,
    networkInfoNodeDocAnalyzer,
    NetworkInfoNodeAnalyzer,
    NetworkInfoIntegrityAnalyzer,
    NetworkInfoFactAnalyzer,
    NetworkInfoNodeMemberMissingAnalyzer,
    networkInfoChangeAnalyzer,
    networkCountryAnalyzer,
    networkInfoExtraAnalyzer,
    NetworkCenterAnalyzer,
    NetworkLastUpdatedAnalyzer
  )

  def updateAll(analysisTimestamp: Timestamp): Unit = {
    log.infoElapsed {
      val networkIds = database.networks.ids(log)
      networkIds.zipWithIndex.foreach { case (networkDoc, index) =>
        Log.context(s"${index + 1}/${networkIds.size}") {
          updateNetwork(analysisTimestamp, networkDoc)
        }
      }
      ("done", ())
    }
  }

  def updateNetworks(analysisTimestamp: Timestamp, networkIds: Seq[Long]): Unit = {
    networkIds.zipWithIndex.foreach { case (networkId, index) =>
      Log.context(s"${index + 1}/${networkIds.size}") {
        updateNetwork(analysisTimestamp, networkId)
      }
    }
  }

  def updateNetwork(analysisTimestamp: Timestamp, networkId: Long, previousKnownCountry: Option[Country] = None): Option[NetworkInfoDoc] = {
    Log.context(networkId.toString) {
      log.infoElapsed {
        val networkInfoDoc = database.networks.findById(networkId, log).flatMap { networkDoc =>
          val context = NetworkInfoAnalysisContext(
            analysisTimestamp,
            networkDoc,
            previousKnownCountry = previousKnownCountry
          )
          doAnalyze(analyzers, context) match {
            case Some(doc) =>
              database.networkInfos.save(doc, log)
              Some(doc)
            case None =>
              None
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
