package kpn.server.analyzer.full.analyzers

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.main.NetworkMainAnalyzer
import kpn.server.repository.NetworkRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class FullNetworkAnalyzer(
  networkRepository: NetworkRepository,
  networkMainAnalyzer: NetworkMainAnalyzer,
  initialNetworkChangeBuilder: InitialNetworkChangeBuilder
) extends FullAnalyzer {

  private val log = Log(classOf[FullNetworkAnalyzer])

  private case class AnalysisResult(
    analyzedIds: Seq[Long],
    obsoleteIds: Seq[Long]
  )

  def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("network") {
      log.infoElapsed {
        val result = analyzeAll(context)
        (message(result), context)
      }
    }
  }

  private def analyzeAll(context: FullAnalysisContext): AnalysisResult = {
    val activeNetworkIds = collectActiveNetworkIds()
    val baseNetworkIds = collectBaseNetworkIds()
    val analyzedNetworkIds = analyzeNetworks(context, baseNetworkIds)
    val obsoleteNetworkIds = findObsoleteNetworks(activeNetworkIds, analyzedNetworkIds)
    deactivateObsoleteNetworks(obsoleteNetworkIds)
    AnalysisResult(
      analyzedNetworkIds,
      obsoleteNetworkIds
    )
  }

  private def collectActiveNetworkIds(): Seq[Long] = {
    log.infoElapsed {
      val ids = networkRepository.activeNetworkIds()
      (s"Collected ${ids.size} active network ids", ids)
    }
  }

  private def collectBaseNetworkIds(): Seq[Long] = {
    log.info("Collecting base network ids")
    log.infoElapsed {
      val ids = networkRepository.baseNetworkIds()
      (s"Collected ${ids.size} base network ids", ids)
    }
  }

  private def analyzeNetworks(context: FullAnalysisContext, baseNetworkIds: Seq[Long]): Seq[Long] = {
    val batchSize = 25
    val networkCount = baseNetworkIds.size
    log.infoElapsed {
      val ids = baseNetworkIds.zipWithIndex.flatMap { case (networkId, index) =>
        Log.context(s"$index/$networkCount $networkId") {
          log.infoElapsed {
            log.info(s"analyzing network $networkId")
            val id = networkRepository.findBaseNetworkById(networkId).flatMap { baseNetworkDoc =>
              networkMainAnalyzer.analyze(baseNetworkDoc, context.timestamp).map { networkDoc =>
                networkRepository.save(networkDoc)
                context.initialAnalysisChangeSetContext.foreach { changeSetContext =>
                  initialNetworkChangeBuilder.saveNetworkChange(changeSetContext, networkDoc)
                }
                networkId
              }
            }
            (s"Analyzed network $networkId", id)
          }
        }
      }
      (s"Analyzed $networkCount networks", ids)
    }
  }

  private def findObsoleteNetworks(activeNetworkIds: Seq[Long], analyzedNetworkIds: Seq[Long]): Seq[Long] = {
    (activeNetworkIds.toSet -- analyzedNetworkIds).toSeq.sorted
  }

  private def deactivateObsoleteNetworks(networkIds: Seq[Long]): Unit = {
    networkIds.foreach { networkId =>
      networkRepository.findById(networkId).map { networkDoc =>
        log.warn(s"de-activating network ${networkDoc._id}")
        networkRepository.save(networkDoc.copy(active = false))
      }
    }
  }

  private def message(result: AnalysisResult): String = {
    s"completed (${result.analyzedIds.size} networks, ${result.obsoleteIds.size} obsolete networks)"
  }
}
