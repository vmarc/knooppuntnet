package kpn.core.tools.next.support

import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.NetworkType
import kpn.api.custom.Relation
import kpn.core.tools.analysis.AnalysisStartConfiguration
import kpn.core.tools.analysis.AnalysisStartToolOptions
import kpn.core.util.Log

object RouteAnalysisTool {
  def main(args: Array[String]): Unit = {
    val configuration = new AnalysisStartConfiguration(AnalysisStartToolOptions("kpn-next"))
    new RouteAnalysisTool(configuration).analyze()
  }
}

class RouteAnalysisTool(config: AnalysisStartConfiguration) {
  private val log = Log(classOf[RouteAnalysisTool])

  def analyze(): Unit = {
    log.info("Start")
    analyzeRoutes(Seq(13844575L))
    buildTiles()
    log.info(s"Done")
  }

  private def analyzeRoutes(routeIds: Seq[Long]): Unit = {
    val relations = config.overpassRepository.fullRelations(config.timestamp, routeIds)
    relations.foreach { relation =>
      analyzeRoute(relation)
    }
  }

  private def analyzeRoute(relation: Relation): Unit = {
    Log.context(s"route=${relation.id}") {
      try {
        config.masterRouteAnalyzer.analyze(relation) match {
          case None =>
          case Some(routeAnalysis) =>
            config.routeRepository.save(routeAnalysis.route)
          // TODO saveRouteChange(routeAnalysis)
        }
      } catch {
        case e: Exception =>
          log.error(s"Error processing route ${relation.id}", e)
          throw e
      }
    }
  }

  private def buildTiles(): Unit = {
    /* NetworkType.all */ Seq(NetworkType.hiking).foreach { networkType =>
      Log.context(networkType.name) {
        log.info("Start tile analysis")
        val tileAnalysis = config.tileAnalyzer.analysis(networkType)
        (ZoomLevel.minZoom to ZoomLevel.vectorTileMaxZoom).foreach { z =>
          Log.context(s"$z") {
            config.tilesBuilder.build(z, tileAnalysis)
          }
        }
      }
    }
  }
}
