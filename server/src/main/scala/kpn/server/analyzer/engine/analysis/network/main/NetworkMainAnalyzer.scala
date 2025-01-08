package kpn.server.analyzer.engine.analysis.network.main

import kpn.core.common.Time
import kpn.core.doc.BaseNetworkDoc
import kpn.core.doc.NetworkDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkAnalysisContext
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkAnalyzer

import scala.annotation.tailrec

class NetworkMainAnalyzer {

  def analyze(network: BaseNetworkDoc): Option[NetworkDoc] = {
    Log.context(f"network=${network._id}%07d") {
      val context = NetworkAnalysisContext(network)
      val analyzers: List[NetworkAnalyzer] = List(
      )
      doAnalyze(analyzers, context)
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[NetworkAnalyzer], context: NetworkAnalysisContext): Option[NetworkDoc] = {
    if (analyzers.isEmpty) {
      Some(
        NetworkDoc(
          _id = context.network._id,
          active = true,
          version = 0,
          changeSetId = 0,
          relationLastUpdated = Time.now,
          nodeMembers = Seq.empty,
          wayMembers = Seq.empty,
          relationMembers = Seq.empty,
          tags = Seq.empty,
        )
      )
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }
}
