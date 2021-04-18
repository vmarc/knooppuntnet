package kpn.core.tools.route

import kpn.api.common.RouteLocationAnalysis
import kpn.api.custom.Country
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNodeInfoAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedRoute

import java.io.File
import scala.xml.Source
import scala.xml.XML

class RouteFileAnalyzerImpl extends RouteFileAnalyzer {

  private val log = Log(classOf[RouteFileAnalyzerImpl])
  private val routeAnalyzer = setupRouteAnalyzer()

  def analyze(routeId: Long): Option[RouteAnalysis] = {
    try {
      readRouteRelation(routeId).flatMap { data =>
        data.relations.get(routeId).map { routeRelation =>
          val scopedNetworkType = RelationAnalyzer.scopedNetworkType(routeRelation.raw).get
          val loadedRoute = LoadedRoute(Some(Country.nl), scopedNetworkType, data, routeRelation)
          routeAnalyzer.analyze(loadedRoute, orphan = false)
        }
      }
    }
    catch {
      case e: Exception =>
        log.info(e.getMessage)
        None
    }
  }

  private def setupRouteAnalyzer(): MasterRouteAnalyzer = {
    val analysisContext = new AnalysisContext()
    val tileCalculator = new TileCalculatorImpl()
    val routeTileAnalyzer = new RouteTileAnalyzerImpl(tileCalculator)
    val routeLocationAnalyzer = new RouteLocationAnalyzer {
      def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
        context.copy(locationAnalysis = Some(RouteLocationAnalysis(None, Seq.empty, Seq.empty)))
      }
    }
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val routeNodeInfoAnalyzer = new RouteNodeInfoAnalyzerImpl(nodeAnalyzer)
    new MasterRouteAnalyzerImpl(
      analysisContext,
      routeLocationAnalyzer,
      routeTileAnalyzer,
      routeNodeInfoAnalyzer
    )
  }

  private def readRouteRelation(routeId: Long): Option[Data] = {
    val xml = XML.load(Source.fromFile(routeFile(routeId)))
    val rawData = new Parser().parse(xml)
    if (rawData.relations.isEmpty) {
      log.error(s"No route relation found in file of route $routeId")
      None
    }
    else {
      if (rawData.relations.size > 1) {
        log.info(s"Multiple relations found in route $routeId (expected 1 single relation only)")
        None
      }
      else {
        val rawRouteRelation = rawData.relations.head
        if (!rawRouteRelation.tags.has("type", "route")) {
          log.info(s"Relation does not have expected tag type=route in route $routeId")
          None
        }
        else {
          Some(new DataBuilder(rawData).data)
        }
      }
    }
  }

  private def routeFile(routeId: Long): File = {
    val subdir = routeId.toString.takeRight(3)
    val filename = s"/kpn/routes/$subdir/$routeId.xml"
    new File(filename)
  }
}
