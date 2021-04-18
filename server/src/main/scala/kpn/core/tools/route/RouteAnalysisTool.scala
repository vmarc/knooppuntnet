package kpn.core.tools.route

import kpn.api.common.route.RouteInfo
import kpn.api.custom.Country
import kpn.api.custom.Fact.RouteWithoutNodes
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.core.tools.`export`.GeoJsonLineStringGeometry
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.segment.Path
import kpn.server.json.Json
import kpn.server.repository.RouteRepositoryImpl

import java.io.File

object RouteAnalysisTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("localhost", "attic-analysis") { analysisDatabase =>
      Couch.executeIn("localhost", "routes") { routeDatabase =>
        val tool = new RouteAnalysisTool(analysisDatabase, routeDatabase)
        tool.analyze()
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

  private val reviewedRouteIds = Seq(

    // NL cycling
    9427213L, // new analysis is better
    9432838L, // new analysis is better
    12444552L, // new analysis is better
    2672912L, // new analysis is better

    7097802L, // OK minor differences in geometry

    // BE cycling
    6938L, // OK minor differences in geometry
  )

  private val ignoredRouteIds = IdsFile.read("/kpn/routes/ignored-route-ids.txt")

  def analyze(): Unit = {
    val allRouteIds = readAllRouteIds()
    val routeIds = (allRouteIds.toSet -- ignoredRouteIds -- reviewedRouteIds).toSeq.sorted
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
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
              if (newRouteAnalysis.route.facts.contains(RouteWithoutNodes)) {
                log.info("IGNORE RouteWithoutNodes")
              }
              else {
                analysisRouteRepository.routeWithId(newRouteAnalysis.route.id) match {
                  case None => log.info("route not found in analysis database")
                  case Some(oldRoute) =>
                    if (newRouteAnalysis.route.lastUpdated != oldRoute.lastUpdated) {
                      log.info("IGNORE updated after snapshot")
                    }
                    else {
                      if (oldRoute.summary.country.contains(Country.be) && oldRoute.summary.networkType == NetworkType.cycling) {
                        if (oldRoute.active) {
                          if (isImprovedRoute(oldRoute, newRouteAnalysis.route)) {
                            log.info("improved")
                          }
                          else {
                            val comparator = new RouteAnalysisComparator()
                            val comparison = comparator.compareRouteInfos(oldRoute, newRouteAnalysis.route)
                          }
                        }
                      }
                      else {
                        log.info("IGNORE not BE cycling")
                      }
                    }
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

  private def listDirs(dir: File): Seq[File] = {
    val files = dir.listFiles().filter(_.isDirectory)
    files.sortBy(_.getAbsolutePath)
  }

  private def listFiles(dir: File): Seq[File] = {
    val files = dir.listFiles().filter(_.getName.endsWith(".xml"))
    files.sortBy(_.getName.dropRight(4).toLong)
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

  private def readAllRouteIds(): Seq[Long] = {
    val subdirs = listDirs(new File("/kpn/routes"))
    subdirs.flatMap { subdir =>
      val files = listFiles(subdir)
      files.map(_.getName.dropRight(4).toLong)
    }
  }

}
