package kpn.core.tools.analysis

import kpn.core.doc.NetworkDoc
import kpn.core.util.Log

class AnalysisStartNetworkAnalyzer(log: Log, config: AnalysisStartConfiguration) {

  def analyze(): Seq[Long] = {
    Log.context("network-analysis") {
      log.infoElapsed {
        val overpassNetworkIds = collectOverpassNetworkIds()
        val databaseNetworkIds = config.networkRepository.allNetworkIds()
        val networkIds = (overpassNetworkIds.toSet -- databaseNetworkIds.toSet).toSeq.sorted
        val analyzedNetworkIds = analyzeNetworks(networkIds)
        (s"analyzed (${analyzedNetworkIds.size} networks", analyzedNetworkIds ++ databaseNetworkIds)
      }
    }
  }

  private def collectOverpassNetworkIds(): Seq[Long] = {
    log.info("Collecting overpass network ids")
    log.infoElapsed {
      val ids = config.overpassRepository.networkIds(config.timestamp)
      (s"Collected ${ids.size} overpass network ids", ids)
    }
  }

  private def analyzeNetworks(networkIds: Seq[Long]): Seq[Long] = {
    val batchSize = 25
    networkIds.sliding(batchSize, batchSize).zipWithIndex.flatMap { case (batchNetworkIds, index) =>
      Log.context(s"${index * batchSize}/${networkIds.size}") {
        analyzeNetworkBatch(batchNetworkIds)
      }
    }.toSeq
  }

  private def analyzeNetworkBatch(networkIds: Seq[Long]): Seq[Long] = {
    log.infoElapsed {
      val networkDocs = config.overpassRepository.relations(config.timestamp, networkIds).map(NetworkDoc.from)
      config.networkRepository.bulkSave(networkDocs)
      val ids = networkDocs.map(_._id)
      (s"analyzed ${ids.size} networks: ${ids.mkString(", ")}", ids)
    }
  }
}
