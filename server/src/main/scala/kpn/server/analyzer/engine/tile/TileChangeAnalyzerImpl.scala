package kpn.server.analyzer.engine.tile

import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.tiles.TileDataRouteBuilder
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.repository.TaskRepository
import org.springframework.stereotype.Component

@Component
class TileChangeAnalyzerImpl(
  taskRepository: TaskRepository,
  routeTileCalculator: RouteTileCalculator
) extends TileChangeAnalyzer {

  def analyzeRoute(routeAnalysis: RouteAnalysis): Unit = {
    val tileNames = tileNamesIn(routeAnalysis)
    generateTasks(tileNames)
  }

  def analyzeRouteChange(routeAnalysisBefore: RouteAnalysis, routeAnalysisAfter: RouteAnalysis): Unit = {
    val tileNames = routeChanged(routeAnalysisBefore, routeAnalysisAfter)
    generateTasks(tileNames)
  }

  private def routeChanged(routeAnalysisBefore: RouteAnalysis, routeAnalysisAfter: RouteAnalysis): Seq[String] = {

    val networkTypeBefore = routeAnalysisBefore.route.summary.networkType
    val networkTypeAfter = routeAnalysisAfter.route.summary.networkType

    (ZoomLevel.minZoom to ZoomLevel.maxZoom).flatMap { zoomLevel =>
      val tileDataRouteBuilder = new TileDataRouteBuilder(zoomLevel)
      val tileDataRouteBeforeOption = tileDataRouteBuilder.fromRouteInfo(routeAnalysisBefore.route)
      val tileDataRouteAfterOption = tileDataRouteBuilder.fromRouteInfo(routeAnalysisAfter.route)

      if (tileDataRouteBeforeOption != tileDataRouteAfterOption) {
        tileDataRouteBeforeOption match {
          case Some(tileDataRouteBefore) =>
            tileDataRouteAfterOption match {
              case None => tileNames(networkTypeBefore, zoomLevel, tileDataRouteBefore)
              case Some(tileDataRouteAfter) =>
                val before = tileNames(networkTypeBefore, zoomLevel, tileDataRouteBefore)
                val after = tileNames(networkTypeAfter, zoomLevel, tileDataRouteAfter)
                before ++ after
            }

          case None =>
            tileDataRouteAfterOption match {
              case Some(tileDataRouteAfter) => tileNames(networkTypeAfter, zoomLevel, tileDataRouteAfter)
              case None => Seq.empty
            }
        }
      }
      else {
        Seq.empty
      }
    }
  }

  private def tileNamesIn(analysis: RouteAnalysis): Seq[String] = {
    val networkType = analysis.route.summary.networkType
    (ZoomLevel.minZoom to ZoomLevel.maxZoom).flatMap { zoomLevel =>
      val tileDataRouteBuilder = new TileDataRouteBuilder(zoomLevel)
      tileDataRouteBuilder.fromRouteInfo(analysis.route) match {
        case Some(tileDataRouteBefore) =>
          tileNames(networkType, zoomLevel, tileDataRouteBefore)
        case None =>
          Seq.empty
      }
    }
  }

  private def tileNames(networkType: NetworkType, zoomLevel: Int, tileDataRoute: TileDataRoute): Seq[String] = {
    routeTileCalculator.tiles(zoomLevel, tileDataRoute).map(tile => s"${networkType.name}-${tile.name}")
  }

  private def generateTasks(tileNames: Seq[String]): Unit = {
    tileNames.distinct.foreach { tileName =>
      taskRepository.add(s"${TileTask.prefix}$tileName")
    }
  }
}
