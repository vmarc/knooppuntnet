package kpn.core.tools.route

import kpn.api.common.route.RouteInfo
import kpn.api.custom.Fact.RouteWithoutNodes
import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.core.tools.`export`.GeoJsonLineStringGeometry
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.segment.Path
import kpn.server.json.Json
import kpn.server.repository.RouteRepositoryImpl

import scala.collection.parallel.CollectionConverters._

object RouteAnalysisTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("localhost", "attic-analysis") { analysisDatabase =>
      Couch.executeIn("localhost", "routes") { routeDatabase =>
        val tool = new RouteAnalysisTool(analysisDatabase, routeDatabase)
        tool.analyze(IdsFile.read("/kpn/routes/cycling-de-problems.txt"))
        // tool.analyze(Seq(10060284L))

        // tool.analyzeRoute(11906621L) // TODO self-loop TO BE IMPLEMENTED !!!
      }
    }
  }
}

class RouteAnalysisTool(
  analysisDatabase: Database,
  routeDatabase: Database
) {

  private val log = Log(classOf[RouteAnalysisTool])
  private val routeRepository = new RouteRepositoryImpl(routeDatabase)
  private val analysisRouteRepository = new RouteRepositoryImpl(analysisDatabase)
  private val routeFileAnalyzer = new RouteFileAnalyzerImpl()

  private val ignoredRouteIds = IdsFile.read("/kpn/routes/ignored-route-ids.txt")

  private val reviewedRouteIds = Seq(

    // NL cycling
    9427213L, // new analysis is better
    9432838L, // new analysis is better
    12444552L, // new analysis is better
    2672912L, // new analysis is better

    7097802L, // OK minor differences in geometry

    // BE cycling
    6938L, // OK minor differences in geometry

    // FR cycling
    9624135L, // new analysis is better

    // NL hiking
    1609743L, // new analysis is better
    1620675L, // new analysis is better
    3229684L, // new analysis is better
    6787609L, // new analysis is better
    6787678L, // new analysis is better
    7207497L, // new analysis is better
    11858847L, // new analysis is better
    11767168L, // new analysis is better

    // BE hiking
    1791341L, // new analysis is better
    1791342L, // new analysis is better
    1791344L, // new analysis is better
    2148544L, // new analysis is better
    2148545L, // new analysis is better
    2570864L, // new analysis is better
    11353140L, // new analysis is better
  )

  def analyze(allRouteIds: Seq[Long]): Unit = {
    val routeIds = (allRouteIds.toSet -- ignoredRouteIds -- reviewedRouteIds).toSeq.sorted
    routeIds.zipWithIndex.par.foreach { case (routeId, index) =>
      Log.context(s"${index + 1}/${routeIds.size}") {
        analyzeRoute(routeId)
      }
    }
    log.info("done")
  }

  def analyzeRoute(routeId: Long): Unit = {
    val formattedRouteId = String.format("%8d", routeId)
    Log.context(formattedRouteId) {
      try {
        routeFileAnalyzer.analyze(routeId) match {
          case None =>
          case Some(newRouteAnalysis) =>
            Log.context(newRouteAnalysis.route.summary.name) {
              routeRepository.save(newRouteAnalysis.route)
              analysisRouteRepository.routeWithId(newRouteAnalysis.route.id) match {
                case None => log.info("route not found in analysis database")
                case Some(oldRoute) =>
                  if (!oldRoute.active) {
                    log.info("IGNORE no longer active")
                  }
                  else if (newRouteAnalysis.route.lastUpdated != oldRoute.lastUpdated) {
                    log.info("IGNORE updated after snapshot")
                  }
                  else if (newRouteAnalysis.route.facts.contains(RouteWithoutNodes)) {
                    log.info("IGNORE RouteWithoutNodes")
                  }
                  else if (isImprovedRoute(oldRoute, newRouteAnalysis.route)) {
                    log.info("OK improved")
                  }
                  else {
                    val comparator = new RouteAnalysisComparator()
                    val comparison = comparator.compareRouteInfos(oldRoute, newRouteAnalysis.route)
                  }
              }
            }
        }
      }
      catch {
        case e: Exception =>
          log.error("Could not process route", e)
          throw e
      }
    }
  }

  private def printPaths(paths: Seq[Path]): Unit = {
    paths.foreach(printPath)
  }

  private def printPath(path: Path): Unit = {
    val nodes = path.segments.flatMap(_.fragments.flatMap(_.nodes))
    val coordinates = nodes.toArray.map(c => Array(c.lon, c.lat))
    val line = GeoJsonLineStringGeometry(
      "LineString",
      coordinates
    )
    val json = Json.objectMapper.writerWithDefaultPrettyPrinter()
    println("fragments" + path.segments.flatMap(_.fragments.map(_.fragment.id)).mkString("-"))
    println(json.writeValueAsString(line))
  }

  private def isImprovedRoute(oldRoute: RouteInfo, newRoute: RouteInfo): Boolean = {

    // new analysis is better (better start and end node, no tentacle node)

    val oldStartNodes = oldRoute.analysis.map.startNodes
    val oldEndNodes = oldRoute.analysis.map.endNodes
    val oldStartTentacleNodes = oldRoute.analysis.map.startTentacleNodes

    val newStartNodes = newRoute.analysis.map.startNodes
    val newEndNodes = newRoute.analysis.map.endNodes
    val newStartTentacleNodes = newRoute.analysis.map.startTentacleNodes

    if (oldStartNodes.size == 1 && oldEndNodes.size == 1 && oldStartTentacleNodes.size == 1) {
      if (newStartNodes.size == 1 && newEndNodes.size == 1 && newStartTentacleNodes.isEmpty) {
        if (oldStartNodes.head.id == oldEndNodes.head.id) {
          if (newEndNodes.head.id != newStartNodes.head.id) {
            return true
          }
        }
      }
    }
    false
  }
}
