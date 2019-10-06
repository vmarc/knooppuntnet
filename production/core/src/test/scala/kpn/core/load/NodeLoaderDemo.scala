package kpn.core.load

import kpn.core.changes.RelationAnalyzerImpl
import kpn.core.engine.analysis.country.CountryAnalyzerImpl
import kpn.core.load.data.LoadedNode
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.tools.analyzer.AnalysisContext
import kpn.core.util.Log
import kpn.shared.Timestamp

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
