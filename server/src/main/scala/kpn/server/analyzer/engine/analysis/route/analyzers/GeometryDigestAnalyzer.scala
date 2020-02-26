package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import org.apache.commons.codec.digest.DigestUtils

object GeometryDigestAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new GeometryDigestAnalyzer(context).analyze
  }
}

class GeometryDigestAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val wayNodes = context.allWayNodes.toSeq.flatten
    val string = wayNodes.flatMap(node => Seq(node.latitude, node.longitude)).mkString
    val digest = DigestUtils.sha1Hex(string)
    context.copy(geometryDigest = Some(digest))
  }
}
