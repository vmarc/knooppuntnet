package kpn.server.analyzer.load

import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.load.data.LoadedNode
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.util.Log
import kpn.server.analyzer.engine.context.AnalysisContext

object NodeLoaderDemo {

  def main(args: Array[String]): Unit = {

    val executor = new OverpassQueryExecutorImpl()

    val countryAnalyzer = {
      val analysisContext = new AnalysisContext()
      val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
      new CountryAnalyzerImpl(relationAnalyzer)
    }

    val ids = Seq(
      2867993766L,
      2867993767L,
      2867993768L
    )

    val log = Log(classOf[NodeLoaderDemo])

    val loadedNodes: Seq[LoadedNode] = log.elapsed {
      ("done", new NodeLoaderImpl(executor, executor, countryAnalyzer).loadNodes(Timestamp(2015, 1, 1, 0, 0, 0), ids))
    }

    loadedNodes.foreach(println)
  }
}

class NodeLoaderDemo()
