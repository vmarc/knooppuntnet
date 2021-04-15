package kpn.core.tools.route

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.route.RouteInfo
import kpn.api.custom.Country
import kpn.api.custom.Fact.RouteWithoutNodes
import kpn.api.custom.NetworkType
import kpn.core.data.DataBuilder
import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.core.loadOld.Parser
import kpn.core.tools.`export`.GeoJsonLineStringGeometry
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNodeInfoAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.segment.Path
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedRoute
import kpn.server.json.Json
import kpn.server.repository.RouteRepositoryImpl

import java.io.File
import scala.xml.Source
import scala.xml.XML

object RouteAnalysisTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("localhost", "attic-analysis") { analysisDatabase =>
      Couch.executeIn("localhost", "routes") { routeDatabase =>
        val tool = new RouteAnalysisTool(analysisDatabase, routeDatabase)
        // tool.analyze()

        tool.analyzeRoute(2672912L)

        // tool.analyzeSingleFile("/kpn/routes/146/8473146.xml")
        // tool.analyzeSingleFile("/kpn/routes/838/9432838.xml")
        // tool.analyzeSingleFile("/kpn/routes/838/9979838.xml")
        // tool.analyzeSingleFile("/kpn/routes/838/4152838.xml") // route is ok, except one segment too much
        // tool.analyzeSingleFile("/kpn/routes/540/3118540.xml") // problem with junction=roundabout !!!

        // tool.analyzeSubdir("/kpn/routes/838")
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

  private val reviewedRouteIds = Seq(
    17004L, // to be further investigated: start node and tentacle node switched
    1465004L, // to be further investigated: start node and tentacle node switched
    125302L, // OK difference caused by rounding errors ???
    545378L, // OK improved route node analysis result
    1031697L, // OK improved route node analysis result --> further investigate, start and end node determination could be better
    1609468L, // OK improved route node analysis result --> further investigate, start and end node determination could be better
    12533280L, // OK improved route node analysis result
    12533271L, // OK improved route node analysis result --> LOOP: further investigate, can do better? no end-nodes?
    12533195L, // OK improved route node analysis result --> LOOP: further investigate, can do better? no end-nodes?
  )

  private val ignoredRouteIds = {
    val source = scala.io.Source.fromFile("/kpn/routes/ignored-route-ids.txt")
    val ids = source.getLines().toSeq.map(_.toLong)
    source.close()
    ids
  }


  def analyze(): Unit = {
    val subdirs = listDirs(new File("/kpn/routes"))
    val allRouteIds = subdirs.flatMap { subdir =>
      val files = listFiles(subdir)
      files.map(_.getName.dropRight(4).toLong)
    }
    val routeIds = (allRouteIds.toSet -- ignoredRouteIds).toSeq.sorted
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      Log.context(s"${index + 1}/${routeIds.size}") {
        if (!reviewedRouteIds.contains(routeId)) {
          analyzeRoute(routeId)
        }
      }
    }
    log.info("done")
  }

  def analyzeRoute(routeId: Long): Unit = {
    val formattedRouteId = String.format("%8d", routeId)
    Log.context(formattedRouteId) {

      try {
        val subdir = routeId.toString.takeRight(3)
        val filename = s"/kpn/routes/$subdir/$routeId.xml"
        val file = new File(filename)
        analyzeRouteFile(file) match {
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
                      if (oldRoute.summary.country.contains(Country.nl) && oldRoute.summary.networkType == NetworkType.cycling) {
                        if (oldRoute.active) {
                          if (isImprovedRoute(oldRoute, newRouteAnalysis.route)) {
                            log.info("improved")
                          }
                          else {
                            val comparator = new RouteAnalysisComparator()
                            val comparison = comparator.compareRouteInfos(oldRoute, newRouteAnalysis.route)
                            if (comparison.factDiff.isDefined) {
                              log.info("facts: " + comparison.factDiff.get)
                            }
                          }
                        }
                      }
                      else {
                        log.info("IGNORE not NL cycling")
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

  // log.info(s"name=${routeAnalysis.name}, facts=${routeAnalysis.route.facts.mkString(",")}")

  //            val oldPaths = routeAnalysis.structure.oldPaths
  //            val newPaths = routeAnalysis.structure.paths.get
  //            // printPaths(newPaths)
  //
  //            val a = oldPaths.map(_.copy(oneWay = false))
  //            val b = newPaths.map(_.copy(oneWay = false))
  //            val d = b.filter(p1 => a.contains(p1))
  //
  //            if (a.toSet != d.toSet) {
  //              if (routeAnalysis.route.facts.isEmpty && !oldPaths.exists(_.broken)) {
  //                log.info("MISMATCH1")
  //                routeRepository.save(routeAnalysis.route)
  //              }
  //              else {
  //                val bp = if (oldPaths.exists(_.broken)) "broken path" else ""
  //                log.info(s"MISMATCH2 $bp ${routeAnalysis.route.facts.mkString(", ")}")
  //              }
  //            }

  private def analyzeRouteFile(file: File): Option[RouteAnalysis] = {

    try {
      val xml = XML.load(Source.fromFile(file))

      val rawData = new Parser().parse(xml)
      if (rawData.relations.isEmpty) {
        // log.info(s"No route relation found in file ${file.getAbsolutePath} (no longer active?)")
        None
      }
      else {
        if (rawData.relations.size > 1) {
          log.info(s"Multiple relations found in file ${file.getAbsolutePath} (expected 1 single relation only)")
          None
        }
        else {
          val rawRouteRelation = rawData.relations.head

          if (!rawRouteRelation.tags.has("type", "route")) {
            log.info(s"Relation does not have expected tag type=route in file ${file.getAbsolutePath}")
            None
          }
          else {
            val scopedNetworkType = RelationAnalyzer.scopedNetworkType(rawRouteRelation).get

            val data = new DataBuilder(rawData).data
            val routeRelation = data.relations(rawRouteRelation.id)
            val analysisContext = new AnalysisContext(oldTagging = true)
            val tileCalculator = new TileCalculatorImpl()
            val routeTileAnalyzer = new RouteTileAnalyzerImpl(tileCalculator)
            val routeLocationAnalyzer = new RouteLocationAnalyzer {
              def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
                context.copy(locationAnalysis = Some(RouteLocationAnalysis(None, Seq.empty, Seq.empty)))
              }
            }
            val nodeAnalyzer = new NodeAnalyzerImpl()
            val routeNodeInfoAnalyzer = new RouteNodeInfoAnalyzerImpl(nodeAnalyzer)
            val routeAnalyzer = new MasterRouteAnalyzerImpl(
              analysisContext,
              routeLocationAnalyzer,
              routeTileAnalyzer,
              routeNodeInfoAnalyzer
            )
            val loadedRoute = LoadedRoute(Some(Country.nl), scopedNetworkType, data, routeRelation)
            Some(routeAnalyzer.analyze(loadedRoute, orphan = false))
          }
        }
      }
    }
    catch {
      case e: Exception =>
        log.info(e.getMessage)
        None
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

}
