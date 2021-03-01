package kpn.server.analyzer.load

import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext

object RouteLoaderDemo {

  def main(args: Array[String]): Unit = {
    new RouteLoaderDemo().run()
  }
}

class RouteLoaderDemo() {

  private val log = Log(classOf[RouteLoaderDemo])

  private val analysisContext = new AnalysisContext()
  private val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
  private val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)
  private val executor = new OverpassQueryExecutorImpl()
  private val routeLoader = new RouteLoaderImpl(executor, countryAnalyzer)

  def run(): Unit = {
    log.unitElapsed {
      routeLoader.loadRoute(Timestamp(2018, 5, 17, 3, 19, 24), 8305522L) match {
        case Some(route) =>
          println(route.id)
        case _ =>
          println("could not load route")
      }
      "routeLoader"
    }
  }
}
