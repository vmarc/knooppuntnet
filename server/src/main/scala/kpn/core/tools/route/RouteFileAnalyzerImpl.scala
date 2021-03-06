package kpn.core.tools.route

import kpn.api.common.common.Reference
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteMapInfo
import kpn.api.common.route.RouteNameInfo
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationReader
import kpn.server.analyzer.engine.analysis.location.RouteLocatorImpl
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNodeInfoAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.changes.changes.RouteElements
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedRoute
import kpn.server.repository.RouteRepository

import java.io.File
import scala.xml.Source
import scala.xml.XML

class RouteFileAnalyzerImpl extends RouteFileAnalyzer {

  private val log = Log(classOf[RouteFileAnalyzerImpl])
  private val routeAnalyzer = setupRouteAnalyzer()

  private val countryAnalyzer = {
    val analysisContext = new AnalysisContext()
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    new CountryAnalyzerImpl(relationAnalyzer)
  }

  def analyze(routeId: Long): Option[RouteAnalysis] = {
    try {
      readRouteRelation(routeId).flatMap { data =>
        data.relations.get(routeId).map { routeRelation =>
          val scopedNetworkType = RelationAnalyzer.scopedNetworkType(routeRelation.raw).get
          val country = countryAnalyzer.relationCountry(routeRelation)
          val loadedRoute = LoadedRoute(country, scopedNetworkType, data, routeRelation)
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
    val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)
    val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
    val routeRepository = mockRouteRepository()
    val locationConfiguration = {
      log.info("Location configuration loading")
      val config = new LocationConfigurationReader().read()
      log.info("Location configuration loaded")
      config
    }
    val routeLocator = new RouteLocatorImpl(locationConfiguration)
    val routeLocationAnalyzer = new RouteLocationAnalyzerImpl(routeRepository, routeLocator)
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val routeNodeInfoAnalyzer = new RouteNodeInfoAnalyzerImpl(analysisContext, nodeAnalyzer)
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
      rawData.relations.find(rawRelation => rawRelation.id == routeId) match {
        case None =>
          log.error(s"Route relation not found")
          None
        case Some(rawRelation) =>
          if (!rawRelation.tags.has("type", "route")) {
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

  private def mockRouteRepository(): RouteRepository = {
    new RouteRepository() {
      override def allRouteIds(): Seq[Long] = ???

      override def save(routes: RouteInfo): Unit = ???

      override def saveElements(routeElements: RouteElements): Unit = ???

      override def delete(routeId: Long): Unit = ???

      override def routeWithId(routeId: Long): Option[RouteInfo] = None

      override def mapInfo(routeId: Long): Option[RouteMapInfo] = None

      override def nameInfo(routeId: Long): Option[RouteNameInfo] = None

      override def routeElementsWithId(routeId: Long): Option[RouteElements] = ???

      override def networkReferences(routeId: Long, stale: Boolean): Seq[Reference] = ???

      override def filterKnown(routeIds: Set[Long]): Set[Long] = ???
    }
  }
}
