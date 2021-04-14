package kpn.core.tools.support

import kpn.api.common.RouteLocationAnalysis
import kpn.api.custom.Country
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
    Couch.executeIn("localhost", "routes") { database =>
      val tool = new RouteAnalysisTool(database)
      tool.analyze()

      // tool.analyzeSingleFile("/kpn/routes/838/9432838.xml")
      // tool.analyzeSingleFile("/kpn/routes/838/9979838.xml")
      // tool.analyzeSingleFile("/kpn/routes/838/4152838.xml") // route is ok, except one segment too much
      // tool.analyzeSingleFile("/kpn/routes/540/3118540.xml") // problem with junction=roundabout !!!

      // tool.analyzeSubdir("/kpn/routes/838")
    }
  }
}

class RouteAnalysisTool(database: Database) {

  private val log = Log(classOf[RouteAnalysisTool])
  private val routeRepository = new RouteRepositoryImpl(database)

  def analyze(): Unit = {
    val subdirs = listDirs(new File("/kpn/routes"))
    subdirs.foreach { subdir =>
      val files = listFiles(subdir)
      files.foreach(analyzeRouteFile)
    }
    log.info("done")
  }

  def analyzeSingleFile(filename: String): Unit = {
    val file = new File(filename)
    analyzeRouteFile(file)
    log.info("done")
  }

  def analyzeSubdir(dirname: String): Unit = {
    val files = new File(dirname).listFiles()
    log.info(s"start: ${files.size} routes")
    files.foreach(analyzeRouteFile)
    log.info("done")
  }

  private def analyzeRoute(file: File): Option[RouteAnalysis] = {

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

  private def analyzeRouteFile(file: File): Unit = {
    val formattedRouteId = String.format("%8s", file.getName.dropRight(4))
    Log.context(formattedRouteId) {
      analyzeRoute(file) match {
        case None =>
        case Some(routeAnalysis) =>

          // log.info(s"name=${routeAnalysis.name}, facts=${routeAnalysis.route.facts.mkString(",")}")

          val oldPaths = routeAnalysis.structure.oldPaths
          val newPaths = routeAnalysis.structure.paths.get
          // printPaths(newPaths)

          val a = oldPaths.map(_.copy(oneWay = false))
          val b = newPaths.map(_.copy(oneWay = false))
          val d = b.filter(p1 => a.contains(p1))

          if (a.toSet != d.toSet) {
            if (routeAnalysis.route.facts.isEmpty && !oldPaths.exists(_.broken)) {
              log.info("MISMATCH1")
              routeRepository.save(routeAnalysis.route)
            }
            else {
              val bp = if (oldPaths.exists(_.broken)) "broken path" else ""
              log.info(s"MISMATCH2 $bp ${routeAnalysis.route.facts.mkString(", ")}")
            }
          }
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

}
