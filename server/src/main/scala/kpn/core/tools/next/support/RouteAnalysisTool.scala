package kpn.core.tools.next.support

import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.Relation
import kpn.core.tools.analysis.AnalysisStartConfiguration
import kpn.core.tools.analysis.AnalysisStartToolOptions
import kpn.core.tools.next.domain.RouteRelation
import kpn.core.util.Log
import kpn.core.util.Redesign

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
    analyzeRoutes(Seq(8618)) // ok route with start tenticle
    analyzeRoutes(Seq(5491)) // ok route with 2 start tenticles
    analyzeRoutes(Seq(13844575)) // ok route
    analyzeRoutes(Seq(8831649)) // LAW-9 deel 1 - 01
    analyzeRoutes(Seq(3952592)) // broken route
    analyzeRoutes(Seq(7973533)) // LAW-9 super route containing other super routes
    analyzeRoutes(Seq(3963819)) // route with roundabout
    //    buildTiles()
    log.info(s"Done")
  }

  private def analyzeRoutes(routeIds: Seq[Long]): Unit = {
    routeIds.foreach { routeId =>
      config.nextRepository.nextRouteRelation(routeId) match {
        case None => log.error(s"route $routeId not found in route-relations")
        case Some(nextRouteRelation) =>
          analyzeRoute(nextRouteRelation.relation, nextRouteRelation.structure)
      }
    }
  }

  private def analyzeRoute(relation: Relation, hierarchy: Option[RouteRelation]): Unit = {
    Log.context(s"route=${relation.id}") {
      try {
        config.routeDetailMainAnalyzer.analyze(relation, hierarchy) match {
          case None =>
          case Some(routeAnalysis) =>
            config.routeRepository.saveRouteDetail(routeAnalysis.routeDetail)
            // next analysis should go in second pass
            config.routeMainAnalyzer.analyze(routeAnalysis.routeDetail) match {
              case Some(routeDoc) => config.routeRepository.saveRoute(routeDoc)
              case None =>
            }
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
    Redesign.tileGenerationNetworkTypes.foreach { networkType =>
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
